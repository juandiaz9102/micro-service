package com.nttdata.som.serviceorder.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperation;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.exceptions.NotFoundException;
import com.nttdata.model.dgraph.FilterRequest;
import com.nttdata.model.dgraph.*;
import com.nttdata.model.tmf.*;
import com.nttdata.som.serviceorder.client.CadenceClient;
import com.nttdata.som.serviceorder.client.ClientUpdate;
import com.nttdata.som.serviceorder.helper.*;
import com.nttdata.som.serviceorder.ifaces.*;
import com.nttdata.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@org.springframework.stereotype.Service
public class ServiceOrderImpl implements ServiceOrderService {

    @Value("${graphQL.url}")
    private String graphQLURL;

    @Autowired
    private CadenceClient cadenceClient = new CadenceClient();

    @Autowired
    private ServiceOrderMapper serviceOrderMapper = new ServiceOrderMapperImpl();

    @Autowired
    private ServiceOrderValidations serviceOrderValidations = new ServiceOrderValidationsImpl();

    @Autowired
    private ServiceOrderListFilter serviceOrderListFilter = new ServiceOrderListFilterImpl();

    @Autowired
    private ClientUpdate clientUpdate = new ClientUpdate();

    @Autowired
    private ServiceUpdate serviceUpdate = new ServiceUpdate();


    @Value("${com.nttdata.nameId.value}")
    private String nameId;

    @Value("${com.nttdata.datePattern.value}")
    private String datePattern;
    @Value("${com.nttdata.filter.flowExecutionRef.creationDate}")
    private String flowCreationDateStr = "flowExecutionRef.creationDate";
    @Value("${com.nttdata.filter.flowExecutionRef.endExecutionDate}")
    private String flowEndExecutionDateStr = "flowExecutionRef.endExecutionDate";
    @Value("${com.nttdata.filter.flowExecutionRef.executionDate}")
    private String flowExecutionDateStr = "flowExecutionRef.executionDate";
    @Value("${com.nttdata.filter.serviceOrder.orderDate}")
    private String orderDateStr = "orderDate";
    @Value("${com.nttdata.filter.serviceOrder.startDate}")
    private String startDateStr = "startDate";


    @Autowired
    ServiceOrderUpdate serviceOrderUpdate = new ServiceOrderUpdateImpl();


    @Override
    public ServiceOrder createServiceOrder(ServiceOrder request) throws IOException, ApplicationException, BusinessException {

        ServiceOrder responseService = null;
        request = serviceOrderValidations.validatePlace(request);

        AddServiceMutationRequest addServiceMutationRequest = new AddServiceMutationRequest();
        addServiceMutationRequest.setInput(request);

        ServiceOrderResponseProjectionWrapper serviceOrderResponseProjection = new ServiceOrderResponseProjectionWrapper()
                .serviceOrder("serviceOrder", buildResponse());
        GraphQLRequest graphQLRequest = new GraphQLRequest(addServiceMutationRequest,
                serviceOrderResponseProjection);

        JsonNode jsonNode = clientUpdate.ejecutar(graphQLRequest.toHttpJsonBody(), graphQLURL);
        ObjectMapper objMapper = new ObjectMapper();

        String responseGraphql = jsonNode.get("data").get("serviceOrder").get("serviceOrder").get(0)
                .toString();

        responseService = objMapper.treeToValue(objMapper.readTree(responseGraphql),
                ServiceOrder.class);

        cadenceClient.loadFlowCall(responseService, Operation.SERVICEORDER.val());

        return responseService;
    }


    public ServiceOrderResponseProjection buildResponse() {
        return new ServiceOrderResponseProjection().id()._atType().category().description().externalId()
                .priority().requestedCompletionDate().requestedStartDate().state().orderDate().startDate()
                .requestedStartDate()
                .externalReference(new ExternalReferenceResponseProjection().id().href().name().externalReferenceType())
                .note(new NoteResponseProjection().id().author().date().text())
                .orderRelationship(new ServiceOrderRelationshipResponseProjection().id().href().relationshipType())
                .relatedParty(new PartyResponseProjection().id().href().name().role().baseType().type().schemaLocation().referredType())
                .serviceOrderItem(new ServiceOrderItemResponseProjection().serviceOrderItemId().action().id().state().quantity().type()
                        .appointment(new AppointmentRefResponseProjection().id().href().description())
                        .service(serviceUpdate.buildResponse()))
                .cancelRequest(new CancellationRequestResponseProjection().id().externalId().href().cancellationReason().state())
                .modifyRequest(new ModificationRequestResponseProjection().id().externalId().modifyReason())
                .flowExecutionRef(new FlowExecutionRefResponseProjection().id().domainId().workflowId().projectId().runId().creationDate().executionDate().endExecutionDate())
                ;
    }


    @Override
    public ServiceOrder retrieveServiceOrder(String id) throws IOException, ApplicationException {

        ServiceOrder responseService = null;

        ServiceOrder request = new ServiceOrder();
        request.setId(id);

        ServiceOrderQueryRequest queryRequest = new ServiceOrderQueryRequest();
        queryRequest.setInput(request);

        ServiceOrderResponseProjection serviceOrderResponseProjection = buildResponse();


        GraphQLRequest graphQLRequest = new GraphQLRequest(queryRequest,
                serviceOrderResponseProjection);

        JsonNode jsonNode = clientUpdate.ejecutar(graphQLRequest.toHttpJsonBody(), graphQLURL);
        ObjectMapper objMapper = new ObjectMapper();

        responseService = objMapper.treeToValue(
                jsonNode.get("data").get("queryServiceOrder").get(0), ServiceOrder.class);
        if (responseService != null) {
            return responseService;
        } else {
            throw new NotFoundException();
        }

    }

    public ServiceOrder patchServiceOrder(String id, ServiceOrder request) throws ApplicationException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, BusinessException {

        ServiceOrder validateOrder = retrieveServiceOrder(id);
        if (validateOrder != null) {

            StringBuilder queryRequest = new StringBuilder();
            ServiceOrder serviceOrder = serviceOrderMapper.mapServiceOrder(id, request);

            queryRequest.append(serviceOrderUpdate.strPatch(serviceOrder, nameId));

            /**
             * Update ExternalReference
             * */
            serviceOrderUpdate.updateExternalReference(queryRequest, request, nameId);

            /**
             * Update Note
             * */
            serviceOrderUpdate.updateNote(queryRequest, request, nameId);

            /**
             * Update ServiceOrderRelationship
             * */
            serviceOrderUpdate.updateServiceOrderRelationship(queryRequest, request, nameId);


            /**
             * Update party List
             * */
            serviceOrderUpdate.updatePartyList(queryRequest, request, nameId);

            /**
             * Update ServiceOrderItem
             * */
            serviceOrderUpdate.updateServiceOrderItem(queryRequest, request, nameId);

            /**
             * Update Cancel Request
             * */
            serviceOrderUpdate.updateCancelRequest(queryRequest, request, nameId);

            /**
             * Update Modification Request
             * */
            serviceOrderUpdate.updateModificationRequest(queryRequest, request, nameId);

            /**
             * Update Flow Execution Ref
             * */
            serviceOrderUpdate.updateFlowExecutionRef(queryRequest, request, nameId);

            String strGraphQLRequest = Utils.operationWrapper(GraphQLOperation.MUTATION, "updateServiceOrder", queryRequest.toString());


            strGraphQLRequest = Utils.jsonQuery(strGraphQLRequest);
            clientUpdate.ejecutar(strGraphQLRequest, graphQLURL);
            return retrieveServiceOrder(id);

        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public String deleteServiceOrder(String id) throws IOException, ApplicationException {
        String responseDelete = null;

        ServiceOrderDTO request = new ServiceOrderDTO();
        request.setId(id);

        DeleteServiceMutationRequest deleteRequest = new DeleteServiceMutationRequest();
        deleteRequest.setInput(request);

        /*
         * ServiceOrderResponseProjectionWrapper serviceOrderResponseProjection = new
         * ServiceOrderResponseProjectionWrapper().serviceOrder("deleteServiceOrder",
         * new ServiceOrderResponseProjection().id().category().externalId());
         */
        ServiceOrderResponseProjection serviceOrderResponseProjection = new ServiceOrderResponseProjection()
                .msg();

        GraphQLRequest graphQLRequest = new GraphQLRequest(deleteRequest,
                serviceOrderResponseProjection);
        // GraphQLRequest graphQLRequest = new GraphQLRequest("deleteServiceOrder",
        // deleteRequest);

        JsonNode jsonNode = clientUpdate.ejecutar(graphQLRequest.toHttpJsonBody(), graphQLURL);
        ObjectMapper objMapper = new ObjectMapper();


        responseDelete = objMapper.treeToValue(
                jsonNode.get("data").get("deleteServiceOrder").get("msg"), String.class);


        return responseDelete;
    }

    @Override
    public List<ServiceOrder> listServiceOrder(String fields,
                                               MultiValueMap<String, String> filters) throws IOException, ApplicationException, ParseException {

        List<ServiceOrder> serviceOrderList = null;

        log.info("### " + fields);


        StringBuilder sbOrderDate = new StringBuilder();
        StringBuilder sbStartDate = new StringBuilder();
        StringBuilder sbFlowCreationDate = new StringBuilder();
        StringBuilder sbFlowExecutionDate = new StringBuilder();
        StringBuilder sbFlowEndExecutionDate = new StringBuilder();


        filters.keySet().forEach(key -> {
            if (key.contains(orderDateStr))
                sbOrderDate.append(OffsetDateTime.parse(filters.getFirst(key)).format(DateTimeFormatter.ofPattern(datePattern)));
            else if (key.contains(startDateStr))
                sbStartDate.append(OffsetDateTime.parse(filters.getFirst(key)).format(DateTimeFormatter.ofPattern(datePattern)));
            else if (key.contains(flowCreationDateStr))
                sbFlowCreationDate.append(OffsetDateTime.parse(filters.getFirst(key)).format(DateTimeFormatter.ofPattern(datePattern)));
            else if (key.contains(flowExecutionDateStr))
                sbFlowExecutionDate.append(OffsetDateTime.parse(filters.getFirst(key)).format(DateTimeFormatter.ofPattern(datePattern)));
            else if (key.contains(flowEndExecutionDateStr))
                sbFlowEndExecutionDate.append(OffsetDateTime.parse(filters.getFirst(key)).format(DateTimeFormatter.ofPattern(datePattern)));
        });

        String serviceOrderFilter = serviceOrderListFilter.createServiceOrderFilter(sbOrderDate.toString(), sbStartDate.toString());
        String flowExecutionRefFilter = serviceOrderListFilter.createFlowExecutionRefFilter(sbFlowCreationDate.toString(), sbFlowExecutionDate.toString(), sbFlowEndExecutionDate.toString());

        String request = serviceOrderListFilter.createListRequest(serviceOrderFilter, flowExecutionRefFilter);
        JsonNode jsonNode = clientUpdate.ejecutar(Utils.jsonQuery(request), graphQLURL);
        ObjectMapper objectMapper = new ObjectMapper();


        serviceOrderList = objectMapper.treeToValue(jsonNode.get("data").get("queryServiceOrder"),
                List.class);


        return serviceOrderList;
    }

    /**
     * Este funciona bien por si lo daño
     */

    /*
     * public List<ServiceOrder> listServiceOrder(String fields,
     * MultiValueMap<String, String> filters) {
     *
     * List<ServiceOrder> serviceOrderList = null;
     *
     * try { ServiceOrderQueryRequest queryRequest = new ServiceOrderQueryRequest();
     * queryRequest.setInput(null);
     * System.out.println(queryRequest.getInput().toString());
     *
     * ServiceOrderResponseProjection serviceOrderResponseProjection = new
     * ServiceOrderResponseProjection().id().category().externalId().priority();
     *
     * GraphQLRequest graphQLRequest = new GraphQLRequest(queryRequest,
     * serviceOrderResponseProjection);
     *
     * Response response = client.ejecutar(graphQLRequest, graphQLURL); String
     * responseBody = response.body().string(); log.debug("Response Body=>" +
     * responseBody);
     *
     * ObjectMapper objMaper = new ObjectMapper(); JsonNode jsonNode =
     * objMaper.readTree(responseBody);
     *
     * serviceOrderList = objMaper.treeToValue(
     * jsonNode.get("data").get("queryServiceOrder"), List.class);
     *
     * } catch (Exception e) { // } return serviceOrderList; }
     */

    /*
     * @Override public ServiceOrder retrieveServiceOrder(Long id) {
     *
     * /* log.debug("Iniciando guardado de la service order con id {}",id);
     *
     * // 1. Parse the states coma string list List<String> statesList = new
     * ArrayList<String>(); if (states != null) { statesList =
     * Stream.of(states.split(",")) .map(String::trim)
     * .collect(Collectors.toList()); }
     *
     *
     * List<ServiceOrder> retrieve =
     * serviceOrderRepository.findServiceOrderList(id); ServiceOrder res = null; if
     * (retrieve == null || retrieve.isEmpty()) throw new
     * NotFoundException("No se encontraron datos del id : " + id.toString()); for
     * (ServiceOrder serviceOrder : retrieve) {
     *
     *
     * // 2. Filter order by state if (serviceOrder.getState() != null) { String
     * serviceOrderState = serviceOrder.getState().getValue(); if
     * (!statesList.isEmpty()) { if (!statesList.contains(serviceOrderState)) {
     * log.info("Order: {} is not in the states list", id); throw new
     * NotFoundException("No se encontraron datos del id : " + id.toString()); } }
     * }else if(serviceOrder.getState() == null && !statesList.isEmpty()) {
     * LOGGER.info("Order:"+id+" is not in the states list" ); throw new
     * NotFoundException("No se encontraron datos del id : " + id.toString());
     *
     * }
     *
     *
     *
     * try { serviceOrder.setServiceOrderItem(serviceOrderItemRepository.
     * findByServiceOrder(id)); } catch (MappingException e) {
     * LOGGER.error("MappingException [ServiceOrderItem]: " + e.getMessage()); } for
     * (ServiceOrderItem item : serviceOrder.getServiceOrderItem()) {
     * List<com.nttdata.model.tmf.Service> srv = null; try { srv =
     * serviceRepository.findByServiceOrderItem(item.getServiceOrderItemId()); }
     * catch (MappingException e) { LOGGER.error("MappingException [Service]: " +
     * e.getMessage()); } if (srv != null && !srv.isEmpty()) { for
     * (com.nttdata.model.tmf.Service service : srv) { List<Characteristic>
     * characteristics = null; try { characteristics =
     * characteristicRepository.findByService(service.getId()); } catch
     * (MappingException e) { LOGGER.error("MappingException [Characteristic]: " +
     * e.getMessage()); } if (characteristics != null && !characteristics.isEmpty())
     * { service.setServiceCharacteristic(characteristics); }
     * Optional<ServiceSpecificationRef> serviceSpec = null; try { serviceSpec =
     * serviceSpecificationRefRepository.findByService(service.getId()); } catch
     * (MappingException e) {
     * LOGGER.error("MappingException [ServiceSpecificationRef]: " +
     * e.getMessage()); } if (serviceSpec != null && !serviceSpec.isEmpty()) {
     * service.setServiceSpecification(serviceSpec.get()); } else {
     * service.setServiceSpecification(null); } List<Place> places = null; try {
     * places = placeRepository.findByService(service.getId()); } catch
     * (MappingException e) { LOGGER.error("MappingException [Place]: " +
     * e.getMessage()); } if (places != null && !places.isEmpty())
     * service.setPlace(places); List<Party> partysByService = null; try {
     * partysByService = partyRepository.findByService(service.getId()); } catch
     * (MappingException e) { LOGGER.error("MappingException [Party]: " +
     * e.getMessage()); } service.setRelatedParty(partysByService);
     * List<com.nttdata.model.tmf.Service> subSrv = null; try { subSrv =
     * serviceRepository.findByService(service.getId()); } catch (MappingException
     * e) { LOGGER.error("MappingException [Service]: " + e.getMessage()); } if
     * (subSrv != null && !subSrv.isEmpty()) { service.setSupportingService(new
     * ArrayList<>()); List<Long> ids = new ArrayList<>(); for
     * (com.nttdata.model.tmf.Service subService : subSrv) { boolean found = false;
     * for (Long idx: ids) { if (subService.getId().equals(idx)){ found = true;
     * break; } } if (!found) { ids.add(subService.getId()); List<Characteristic>
     * subCharacteristics = null; try { subCharacteristics =
     * characteristicRepository.findByService(subService.getId()); } catch
     * (MappingException e) { LOGGER.error("MappingException [Characteristic]: " +
     * e.getMessage()); } if (subCharacteristics != null &&
     * !subCharacteristics.isEmpty()) {
     * subService.setServiceCharacteristic(subCharacteristics); }
     * Optional<ServiceSpecificationRef> subServiceSpec = null; try { subServiceSpec
     * = serviceSpecificationRefRepository.findByService(subService.getId()); }
     * catch (MappingException e) {
     * LOGGER.error("MappingException [ServiceSpecificationRef]: " +
     * e.getMessage()); } if (subServiceSpec.isPresent()) {
     * subService.setServiceSpecification(subServiceSpec.get()); } else {
     * subService.setServiceSpecification(null); } List<Party> partysBySubService =
     * null; try { partysBySubService =
     * partyRepository.findByService(service.getId()); } catch (MappingException e)
     * { LOGGER.error("MappingException [Party]: " + e.getMessage()); }
     * subService.setRelatedParty(partysBySubService); List<Place> subPlaces = null;
     * try { subPlaces = placeRepository.findByService(subService.getId()); } catch
     * (MappingException e) { LOGGER.error("MappingException [Place]: " +
     * e.getMessage()); } if (places != null && !subPlaces.isEmpty())
     * subService.setPlace(subPlaces);
     * service.getSupportingService().add(subService); } } } else {
     * service.setSupportingService(null); } item.setService(service); } } } try {
     * serviceOrder.setRelatedParty(partyRepository.findByServiceOrder(id)); } catch
     * (MappingException e) { LOGGER.error("MappingException [Party]: " +
     * e.getMessage()); } try {
     * serviceOrder.setNote(noteRepository.findByServiceOrder(id)); } catch
     * (MappingException e) { LOGGER.error("MappingException [Note]: " +
     * e.getMessage()); } try {
     * serviceOrder.setCancelRequest(cancelRepository.findByServiceOrder(id)); }
     * catch (MappingException e) {
     * LOGGER.error("MappingException [CancellationRequest]: " + e.getMessage()); }
     * try { serviceOrder.setModifyRequest(modifyRepository.findByServiceOrder(id));
     * } catch (MappingException e) {
     * LOGGER.error("MappingException [ModificationRequest]: " + e.getMessage()); }
     * try {
     * serviceOrder.setUpdateRequest(updateResourceRepository.findByServiceOrder(id)
     * ); } catch (MappingException e) {
     * LOGGER.error("MappingException [UpdateResourceServiceOrder]: " +
     * e.getMessage()); } try {
     * serviceOrder.setFlowExecutionRef(flowExecutionRefRepository.
     * findByServiceOrder(id)); } catch (MappingException e) {
     * LOGGER.error("MappingException [FlowExecutionRef]: " + e.getMessage()); } res
     * = serviceOrder; break; } return res;
     *
     *
     * return new ServiceOrder(); }
     *
     */

    /*
     * Método antiguo
     *
     * @Override //@Transactional public ServiceOrderDTO
     * createServiceOrder(ServiceOrderDTO request) {
     *
     * /* ObjectMapper mapper1 = new ObjectMapper(); String operation =
     * "ServiceOrder"; JsonNode actualObj1 = mapper1.valueToTree(request);
     * FlowExecutionRef f = new FlowExecutionRef(); String req = ""; try { req =
     * mapper1.writeValueAsString(request); } catch (JsonProcessingException e1) {
     * // TODO Auto-generated catch block e1.printStackTrace(); }
     *
     * ServiceOrder create = new ServiceOrder(); ServiceOrder findSO =
     * serviceOrderRepository.findServiceOrderByExternalId(request.getExternalId());
     *
     * if (externalIdTest.equals(request.getExternalId()) ) { findSO = null; }
     *
     *
     * if (findSO == null) { request.setNote(null); request.setRelatedParty(null);
     * request.setServiceOrderItem(null);
     *
     * create = serviceOrderRepository.save(request); } else {
     *
     * create = retrieveServiceOrder(findSO.getId(), null); return create; }
     *
     *
     *
     *
     * String json = null; try { ObjectMapper mapper = new ObjectMapper();
     * ServiceOrder so = new ServiceOrder(); so = mapper.treeToValue(actualObj1,
     * ServiceOrder.class); so.setId(create.getId());
     *
     * request.setId(create.getId()); Gson gson = new
     * GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create(); json =
     * gson.toJson(so); mapper.setDateFormat(new
     * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
     * mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
     *
     * JsonNode actualObj = mapper.readTree(json);
     *
     * for (int i = 0; i < so.getServiceOrderItem().size(); i++) {
     * ((ObjectNode)actualObj).put("serviceOrderItem[" + i
     * +"].service.serviceSpecification.name",
     * so.getServiceOrderItem().get(i).getService().getServiceSpecification().
     * getName()); ((ObjectNode)actualObj).put("serviceOrderItem[" + i +"].action",
     * so.getServiceOrderItem().get(i).getAction().toString()); }
     * ((ObjectNode)actualObj).put("serviceOrderItem.size",
     * so.getServiceOrderItem().size());
     * ((ObjectNode)actualObj).put("serviceOrderjson", req);
     *
     * Map<String, String> cadenceMap = new HashMap<>(); buildMapCadence(actualObj,
     * cadenceMap, "");
     *
     * try {
     *
     *
     * WorkflowClient workflowClient = WorkflowClient.newInstance(host, port,
     * domain); LoadFlow catalogDriverClient =
     * workflowClient.newWorkflowStub(LoadFlow.class, new WorkflowOptions.Builder()
     * .setExecutionStartToCloseTimeout(Duration.ofDays(this.
     * executionStartToCloseTimeout)) .build()); //
     * catalogDriverClient.execute(template, cadenceMap);// Synchronous start
     * WorkflowExecution workflowExecution =
     * WorkflowClient.start(catalogDriverClient::loadFlow, operation, cadenceMap);
     *
     *
     * } catch (WorkflowException e) { jaegerLogger.logException(1L,
     * cadenceMap.toString(), "createServiceOrder", e.getMessage()); }
     *
     *
     * } catch (JsonProcessingException e) { jaegerLogger.logException(1L, json,
     * "createServiceOrder", e.getMessage()); }
     *
     * try {
     *
     * DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
     * f.setCreationDate(df.format(new Date(System.currentTimeMillis()))); }catch
     * (Exception e){ jaegerLogger.logException(1L,
     * "Error parsing flow execution ref creation date", "createServiceOrder",
     * e.getMessage()); }
     *
     *
     * create.setFlowExecutionRef(f);
     *
     * ServiceOrder updatedSO = serviceOrderRepository.save(create);
     * //jaegerLogger.logCreate(updatedSO);
     *
     * return updatedSO; return new ServiceOrderDTO(); }
     */

    /*
     * @Override public ServiceOrder patchServiceOrder(Long id, ServiceOrder
     * request) { /* ServiceOrder retrieve = retrieveServiceOrder(id, null);
     *
     *
     *
     * if (request.getHref() != null) retrieve.setHref(request.getHref()); if
     * (request.getCancellationDate() != null)
     * retrieve.setCancellationDate(request.getCancellationDate()); if
     * (request.getCancellationReason() != null)
     * retrieve.setCancellationReason(request.getCancellationReason()); if
     * (request.getCategory() != null) retrieve.setCategory(request.getCategory());
     * if (request.getCompletionDate() != null)
     * retrieve.setCompletionDate(request.getCompletionDate()); if
     * (request.getDescription() != null)
     * retrieve.setDescription(request.getDescription()); if
     * (request.getExpectedCompletionDate() != null)
     * retrieve.setExpectedCompletionDate(request.getExpectedCompletionDate()); if
     * (request.getExternalId() != null)
     * retrieve.setExternalId(request.getExternalId()); if
     * (request.getNotificationContact() != null)
     * retrieve.setNotificationContact(request.getNotificationContact()); if
     * (request.getOrderDate() != null)
     * retrieve.setOrderDate(request.getOrderDate()); if (request.getPriority() !=
     * null) retrieve.setPriority(request.getPriority()); if
     * (request.getRequestedCompletionDate() != null)
     * retrieve.setRequestedCompletionDate(request.getRequestedCompletionDate()); if
     * (request.getRequestedStartDate() != null)
     * retrieve.setRequestedStartDate(request.getRequestedStartDate()); if
     * (request.getStartDate() != null)
     * retrieve.setStartDate(request.getStartDate()); if (request.getState() !=
     * null) retrieve.setState( request.getState()); if (request.get_atBaseType() !=
     * null) retrieve.set_atBaseType(request.get_atBaseType()); if
     * (request.get_atSchemaLocation() != null)
     * retrieve.set_atSchemaLocation(request.get_atSchemaLocation()); if
     * (request.get_atType() != null) retrieve.set_atType(request.get_atType());
     *
     *
     *
     * ServiceOrder patch = serviceOrderRepository.save(retrieve);
     * jaegerLogger.logPatch(patch, id); return patch;
     *
     * return new ServiceOrder(); }
     */
    @Override
    public ServiceOrder modifyDateServiceOrder(ModificationRequest request) {

        /*
         * String operation = "Postponement"; ModificationRequest requestToCadence =
         * request; Long id = request.getServiceOrder().getId();
         * request.setServiceOrder(null); ServiceOrder modify = new ServiceOrder();
         * ServiceOrder findSO =
         * serviceOrderRepository.findServiceOrderByModificationRequestExternalId(
         * request.getExternalId()); modify = retrieveServiceOrder(id, null);
         *
         *
         * if (request.getExternalId().equals(externalIdInternal)) { findSO = null; }
         *
         *
         * if (findSO != null) { modify = retrieveServiceOrder(findSO.getId(), null);
         * return modify;
         *
         * }
         *
         * if (modify != null) { if (modify.getModifyRequest() == null) {
         * List<ModificationRequest> listMod = new ArrayList<>();
         * modify.setModifyRequest(listMod); }
         *
         * modify.getModifyRequest().add(request); serviceOrderRepository.save(modify);
         *
         *
         * jaegerLogger.logModify(request, id);
         *
         * // Cadence
         *
         *
         * Map<String, String> cadenceMap = new HashMap<>();
         *
         * try {
         *
         * requestToCadence.setServiceOrder(null); ServiceOrder req = new
         * ServiceOrder(); req.setId(id); requestToCadence.setServiceOrder(req);
         *
         * ObjectMapper mapper = new ObjectMapper(); DateFormat df = new
         * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); mapper.setDateFormat(df); JsonNode
         * actualObj = mapper.valueToTree(requestToCadence); buildMapCadence(actualObj,
         * cadenceMap, "");
         *
         * WorkflowClient workflowClient = WorkflowClient.newInstance(host, port,
         * domain); LoadFlow catalogDriverClient =
         * workflowClient.newWorkflowStub(LoadFlow.class, new WorkflowOptions.Builder()
         * .setExecutionStartToCloseTimeout(Duration.ofDays(this.
         * executionStartToCloseTimeout)) .build());
         * //catalogDriverClient.execute(template, cadenceMap);//Synchronous start
         * WorkflowExecution workflowExecution =
         * WorkflowClient.start(catalogDriverClient::loadFlow, operation, cadenceMap);
         *
         * } catch (WorkflowException e) { jaegerLogger.logException(1L,
         * cadenceMap.toString(), "modifyServiceOrder", e.getMessage());
         *
         * }
         *
         * // Fin Cadence
         *
         *
         * } else { throw new NotFoundException("No se encontraron datos del id : " +
         * id.toString()); }
         *
         *
         *
         * return this.retrieveServiceOrder(id, null);
         */
        return new ServiceOrder();
    }

    @Override
    public ServiceOrder cancelServiceOrder(CancellationRequest request) {
        /*
         * String operation = "Cancel"; CancellationRequest requestToCadence = request;
         *
         * Long id = request.getServiceOrder().getId();
         * requestToCadence.setServiceOrder(null); ServiceOrder req = new
         * ServiceOrder(); req.setId(id); requestToCadence.setServiceOrder(req);
         *
         * FlowExecutionRef f = new FlowExecutionRef(); ServiceOrder cancel = new
         * ServiceOrder(); ServiceOrder findSO =
         * serviceOrderRepository.findServiceOrderByCancellationRequestExternalId(
         * request.getExternalId()); cancel = retrieveServiceOrder(id, null);
         *
         * if (request.getExternalId().equals(externalIdInternal)) { findSO = null; }
         *
         *
         * if (findSO == null) { if (cancel != null) {
         * //cancel.setState(ServiceOrderStateType.ASSESSINGCANCELLATION);
         *
         * Map<String, String> cadenceMap = new HashMap<>();
         *
         * try { ObjectMapper mapper = new ObjectMapper(); DateFormat df = new
         * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); mapper.setDateFormat(df); JsonNode
         * actualObj = mapper.valueToTree(requestToCadence); buildMapCadence(actualObj,
         * cadenceMap, "");
         *
         * WorkflowClient workflowClient = WorkflowClient.newInstance(host, port,
         * domain); LoadFlow catalogDriverClient =
         * workflowClient.newWorkflowStub(LoadFlow.class, new WorkflowOptions.Builder()
         * .setExecutionStartToCloseTimeout(Duration.ofDays(this.
         * executionStartToCloseTimeout)) .build());
         * //catalogDriverClient.execute(template, cadenceMap);//Synchronous start
         * WorkflowExecution workflowExecution =
         * WorkflowClient.start(catalogDriverClient::loadFlow, operation, cadenceMap);
         * //Asynchronous start
         *
         * } catch (WorkflowException e) { jaegerLogger.logException(1L,
         * cadenceMap.toString(), "cancelServiceOrder", e.getMessage());
         *
         * }
         *
         *
         * } else { throw new NotFoundException("No se encontraron datos del id : " +
         * id.toString()); }
         *
         * } else { cancel = retrieveServiceOrder(findSO.getId(), null); return cancel;
         * }
         *
         *
         *
         *
         *
         * //f.setFlowExecutionRef(flowExecutionRefRepository.findByServiceOrder(id));
         * request.setServiceOrder(null); //cancel.setFlowExecutionRef(f);
         * cancel.setCancelRequest(request); jaegerLogger.logCancel(request, id);
         *
         * return serviceOrderRepository.save(cancel);
         */
        return new ServiceOrder();
    }

    @Override
    public FlowExecutionRef modifyFlowExecutionRef(String id, FlowExecutionRef request) throws ApplicationException {


        UpdateServiceOrderMutationRequest mutationRequest = new UpdateServiceOrderMutationRequest();
        UpdateServiceOrderInput input = new UpdateServiceOrderInput();
        ServiceOrderFilter filter = new ServiceOrderFilter();

        List<String> ids = new ArrayList<String>();
        ids.add(id);

        filter.setId(ids);
        ServiceOrderPatch patch = new ServiceOrderPatch();

        patch.setFlowExecutionRef(request);
        input.setFilter(filter);
        input.setSet(patch);

        mutationRequest.setInput(input);

//		ServiceOrderResponseProjectionWrapper serviceOrderResponseProjection = new ServiceOrderResponseProjectionWrapper()
//				.serviceOrder("serviceOrder", buildResponse());
        GraphQLRequest graphQLRequest = new GraphQLRequest(mutationRequest, new UpdateServiceOrderPayloadResponseProjection().serviceOrder(buildResponse()));

        Response response;
        try {
            log.info("Request Body=>" + graphQLRequest.toHttpJsonBody());
            clientUpdate.ejecutar(graphQLRequest.toHttpJsonBody(), graphQLURL);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        /*
         * ServiceOrder so = serviceOrderRepository.findServiceOrder(id); if (so ==
         * null) throw new NotFoundException();
         *
         * FlowExecutionRef exists =
         * flowExecutionRefRepository.findByServiceOrder(so.getId()); if (exists ==
         * null) { throw new NotFoundException(); } else { if (request.getRunId() !=
         * null) exists.setRunId(request.getRunId()); if (request.getDomainId() != null)
         * exists.setDomainId(request.getDomainId()); if (request.getWorkflowId() !=
         * null) exists.setWorkflowId(request.getWorkflowId()); if
         * (request.getProjectId() != null) exists.setProjectId(request.getProjectId());
         * if (request.getBaseFlow() != null) exists.setBaseFlow(request.getBaseFlow());
         * if (request.get_atBaseType() != null)
         * exists.set_atBaseType(request.get_atBaseType()); if
         * (request.get_atSchemaLocation() != null)
         * exists.set_atSchemaLocation(request.get_atSchemaLocation()); if
         * (request.get_atType() != null) exists.set_atType(request.get_atType()); if
         * (request.getCreationDate() != null)
         * exists.setCreationDate(request.getCreationDate()); if
         * (request.getExecutionDate() != null)
         * exists.setExecutionDate(request.getExecutionDate()); if
         * (request.getEndExecutionDate() != null)
         * exists.setEndExecutionDate(request.getEndExecutionDate()); }
         * flowExecutionRefRepository.save(exists); return exists;
         */
        return new FlowExecutionRef();
    }

    @Override
    public FlowExecutionRef modifyDateFlowExecutionRef(String id,
                                                       MultiValueMap<String, String> fields) {

        /*
         * FlowExecutionRef f = flowExecutionRefRepository.findByServiceOrder(id); if (f
         * == null) { throw new NotFoundException(); } else { if
         * (fields.get("creationDate") != null) {
         * f.setCreationDate(deserialize(fields.get("creationDate").toString())); } else
         * if (fields.get("executionDate") != null) {
         * f.setExecutionDate(deserialize(fields.get("executionDate").toString())); }
         * else if (fields.get("endExecutionDate") != null) {
         * f.setEndExecutionDate(deserialize(fields.get("endExecutionDate").toString()))
         * ; } } flowExecutionRefRepository.save(f); return f;
         */
        return new FlowExecutionRef();
    }

    public String deserialize(StringBuilder sb) {
        return sb.toString().replace("[", "").replace("]", "");
    }

    public String deserialize(String s) {
        return s.replace("[", "").replace("]", "");
    }

    public String deserialize(Long l) {
        return l.toString().replace("[", "").replace("]", "");
    }

    public void buildMapCadence(JsonNode actualObj, Map<String, String> map, String parent) {
        Iterator<String> iter = actualObj.fieldNames();

        while (iter.hasNext()) {
            String tmp = iter.next();

            switch (actualObj.get(tmp).getNodeType().toString()) {
                case "STRING":
                    map.put(parent == null ? "" : parent + tmp.replace("_at", "@"),
                            actualObj.get(tmp).asText());
                    break;
                case "NUMBER":
                    map.put(parent == null ? "" : parent + tmp.replace("_at", "@"),
                            actualObj.get(tmp).asText());
                    break;
                case "ARRAY":
                    JsonNode child = actualObj.get(tmp);
                    buildMapCadenceList(child, map, parent.concat(tmp));
                    break;
                case "OBJECT":
                    buildMapCadence(actualObj.get(tmp), map, parent.concat(tmp).concat("."));
                    break;
                case "BOOLEAN":
                    buildMapCadence(actualObj.get(tmp), map, parent.concat(tmp).concat("."));
                    break;
                default:
                    break;
            }
        }
    }

    private void buildMapCadenceList(JsonNode child, Map<String, String> map, String parent) {
        Iterator<JsonNode> iter = child.elements();
        int index = 0;
        while (iter.hasNext()) {
            JsonNode innerIterator = iter.next();
            if (innerIterator.findValue("id") != null
                    && innerIterator.findValue("serviceCharacteristic") == null
                    && innerIterator.findValue("role") == null
                    && innerIterator.findValue("service") == null) {
                buildMapCadence(innerIterator, map,
                        parent.concat("[").concat(innerIterator.get("id").asText().concat("].")));
            } else if (parent.equals("relatedParty")) {
                buildMapCadence(innerIterator, map,
                        parent.concat("[").concat(innerIterator.get("role").asText().concat("].")));
            } else if (parent.equals("serviceOrderItem")) {
                buildMapCadence(innerIterator, map,
                        parent.concat("[").concat(innerIterator.get("service")
                                .get("serviceSpecification").get("name").asText().concat("].")));
            } else {
                buildMapCadence(innerIterator, map,
                        parent.concat("[").concat(String.valueOf(index).concat("].")));
            }
            index++;
        }
    }

    @Override
    public ServiceOrder updateResource(UpdateResourceServiceOrder request) {

        /*
         * String operation = "UpdateResource"; UpdateResourceServiceOrder
         * requestToCadence = request; Long id = request.getServiceOrder().getId();
         * request.setServiceOrder(null); ServiceOrder findService =
         * retrieveServiceOrder(id, null);
         *
         *
         * List<UpdateResourceServiceOrder> listUpdate = findService.getUpdateRequest();
         * listUpdate.add(request);
         *
         *
         * findService.setUpdateRequest(listUpdate);
         * //findService.getUpdateRequest().add(request);
         *
         * serviceOrderRepository.save(findService);
         * jaegerLogger.logUpdateResource(request, id);
         *
         * // Cadence
         *
         *
         * Map<String, String> cadenceMap = new HashMap<>();
         *
         * try {
         *
         * requestToCadence.setServiceOrder(null); ServiceOrder req = new
         * ServiceOrder(); req.setId(id); requestToCadence.setServiceOrder(req);
         *
         * ObjectMapper mapper = new ObjectMapper(); DateFormat df = new
         * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); mapper.setDateFormat(df); JsonNode
         * actualObj = mapper.valueToTree(requestToCadence); buildMapCadence(actualObj,
         * cadenceMap, ""); JsonNode actualObj1 =
         * mapper.readTree(requestToCadence.getResourceData());
         * buildMapCadence(actualObj1, cadenceMap, "");
         *
         * WorkflowClient workflowClient = WorkflowClient.newInstance(host, port,
         * domain); LoadFlow catalogDriverClient =
         * workflowClient.newWorkflowStub(LoadFlow.class, new WorkflowOptions.Builder()
         * .setExecutionStartToCloseTimeout(Duration.ofDays(this.
         * executionStartToCloseTimeout)) .build());
         * //catalogDriverClient.execute(template, cadenceMap);//Synchronous start
         * WorkflowExecution workflowExecution =
         * WorkflowClient.start(catalogDriverClient::loadFlow, operation, cadenceMap);
         *
         * } catch (WorkflowException e) { log.error(1L, cadenceMap.toString(),
         * "UpdateResourceServiceOrder", e.getMessage());
         *
         * } catch (IOException e) { log.error(1L, cadenceMap.toString(),
         * "Reading JSON Template", e.getMessage()); }
         *
         * // Fin Cadence
         *
         *
         *
         * return this.retrieveServiceOrder(id, null);
         */
        return new ServiceOrder();
    }

    @Override
    public ServiceOrder saveServiceOrder(ServiceOrder request) {

        /*
         * request.setState(ServiceOrderStateType.ACKNOWLEDGED); if
         * (request.getServiceOrderItem() != null &&
         * !request.getServiceOrderItem().isEmpty()) { for (ServiceOrderItem soi :
         * request.getServiceOrderItem()) { com.nttdata.model.tmf.Service s =
         * soi.getService(); if (s != null && s.getServiceCharacteristic() != null &&
         * !s.getServiceCharacteristic().isEmpty()) { for (Characteristic c :
         * s.getServiceCharacteristic()) { if (c.get_atType() == null)
         * c.set_atType("SERVICE_BASE"); } } } }
         *
         * // FlowExecutionRef f = new FlowExecutionRef(); //
         * request.setFlowExecutionRef(f); ServiceOrder create =
         * serviceOrderRepository.save(request);
         *
         * return create;
         */
        return new ServiceOrder();
    }
}
