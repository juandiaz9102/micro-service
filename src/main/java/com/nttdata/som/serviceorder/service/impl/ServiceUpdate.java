package com.nttdata.som.serviceorder.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.model.dgraph.*;
import com.nttdata.model.tmf.*;
import com.nttdata.model.tmf638.Service;
import com.nttdata.model.tmf638.ServiceSpecificationRef;
import com.nttdata.som.serviceorder.client.ClientUpdate;
import com.nttdata.som.serviceorder.ifaces.ServiceOrderValidations;
import com.nttdata.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceUpdate {
    @Autowired
    private ClientUpdate cliente = new ClientUpdate();

    @Autowired
    private ServiceOrderValidations serviceOrderValidations = new ServiceOrderValidationsImpl();
    @Value("${graphQL.url}")
    private String graphQLURL;
    @Value("${com.nttdata.nameId.value}")
    private String nameId;

    private PatchMutationRequest patchMutationRequest = new PatchMutationRequest();

    private List<Characteristic> creationList;

    public String patchService(String id, Service request) throws IOException, ApplicationException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, BusinessException {
        StringBuilder queryRequest = new StringBuilder();
        request = serviceOrderValidations.validateService(request);
        Service serviceRequest = validateFields(id, request);
        String updateRequest = buildRequest(serviceRequest, id).toString();
        serviceRequest.setServiceCharacteristic(creationList);
        queryRequest.append(strPatch(serviceRequest));
        queryRequest.append(" ");
        queryRequest.append(updateRequest);
        return queryRequest.toString();

    }

    public ServiceSOMResponseProjection buildResponse() {
        return new ServiceSOMResponseProjection()
                .id()
                .category()
                .description()
                .endDate()
                .hasStarted()
                .isBundle()
                .isServiceEnabled()
                .isStateful()
                .name()
                .serviceDate()
                .serviceType()
                .startDate()
                .startMode()
                .state()
                .externalId()
                //.type()
                .feature(new FeatureResponseProjection().id().isBundle().isEnabled().name())
                .note(new NoteResponseProjection().id().author().date().text())
                .place(new PlaceResponseProjection().placeId().id().href().name().role().type().geometryType().accuracy().spatialRef().geometry(new GeometryResponseProjection().x().y()))
                .relatedParty(new PartyResponseProjection().id().href().role().name().baseType().type().schemaLocation().referredType())
                //.relatedEntity(new RelatedEntityResponseProjection().id().href().role().name())
                .serviceCharacteristic(new CharacteristicResponseProjection().characteristicId().id().name().valueType().valueUnits().value())
                //.serviceorderitem(new ServiceOrderItemResponseProjection().id())
                .serviceRelationship(new ServiceRelationshipResponseProjection().id().relationshipType()
                        .service(buildResponseServiceRelationship())
                        .serviceRelationshipCharacteristic(new CharacteristicResponseProjection().characteristicId().id().name().valueType().valueUnits().value()
                                .characteristicRelationship(new CharacteristicRelationshipResponseProjection().id().relationshipType())
                        )
                )
                .serviceSpecification(new ServiceSpecificationRefResponseProjection().id().href().name().version())
                //.supportingResource(new ResourceRefResponseProjection().id().href().name())
                .supportingService(buildResponseSupportingSerice())
                ;
    }

    public ServiceSOMResponseProjection buildResponseServiceRelationship() {
        return new ServiceSOMResponseProjection()
                .id()
                .category()
                .description()
                .name()
                .state()
                //.type() falta la implementación
                ;
    }

    public ServiceSOMResponseProjection buildResponseSupportingSerice() {
        return new ServiceSOMResponseProjection()
                .id()
                .category()
                .description()
                .name()
                .state()
                //.type() falta la implementación
                .relatedParty(new PartyResponseProjection().id().href().role().name().baseType().type().schemaLocation().referredType())
                .place(new PlaceResponseProjection().placeId().id().href().name().role().type().geometryType().accuracy().spatialRef().geometry(new GeometryResponseProjection().x().y()))
                ;
    }

    private Service validateFields(String id, Service request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Service serviceValidated = new Service();
        serviceValidated.setId(id);

        Method metodos[] = request.getClass().getMethods();

        for (int i = 0; i < metodos.length; i++) {
            Method metodo = metodos[i];
            if ((metodo.getName().substring(0, 3).equalsIgnoreCase("get")
                    || metodo.getName().substring(0, 2).equalsIgnoreCase("is"))
                    && !metodo.getName().equals("getClass")) {
                String methodNameSet = "";
                if (metodo.getName().substring(0, 3).equalsIgnoreCase("get")) {
                    methodNameSet = metodo.getName().replaceFirst("get", "set");
                } else {
                    methodNameSet = methodNameSet.replaceFirst("is", "set");
                }

                Method metodoSet = request.getClass().getMethod(methodNameSet, metodo.getReturnType());

                if (metodoSet != null) {
                    //if is String
                    if (metodo.getReturnType().equals(java.lang.String.class)) {
                        String valor = (String) metodo.invoke(request, new Object[0]);
                        if (valor != null) {
                            metodoSet.invoke(serviceValidated, metodo.invoke(request));
                        }
                    }

                    //if is Boolean
                    if (metodo.getReturnType().equals(java.lang.Boolean.class)) {
                        Boolean valor = (Boolean) metodo.invoke(request, new Object[0]);
                        if (valor != null) {
                            metodoSet.invoke(serviceValidated, metodo.invoke(request));

                        }
                    }

                    //if is date
                    if (metodo.getReturnType().equals(java.util.Date.class)) {
                        Date valor = (Date) metodo.invoke(request, new Object[0]);
                        if (valor != null) {
                            metodoSet.invoke(serviceValidated, metodo.invoke(request));
                        }
                    }

                    //if is a list
                    if (metodo.getReturnType().equals(java.util.List.class)) {
                        List objetosList = (List) metodo.invoke(request, new Object[0]);
                        if (!(objetosList == null || objetosList.isEmpty())) {
                            metodoSet.invoke(serviceValidated, metodo.invoke(request));
                        }
                    }

                    // if is ServiceSpecificationRef
                    if (metodo.getReturnType().equals(ServiceSpecificationRef.class)) {
                        if (metodo.invoke(request, new Object[0]) != null) {
                            serviceValidated.setServiceSpecification(request.getServiceSpecification());
                        }
                    }
                }
            }
        }
        return serviceValidated;
    }

    public StringBuilder buildRequest(Service request, String id) throws ApplicationException, IOException {
        StringBuilder queryRequest = new StringBuilder();

        //For Feature
        if (request.getFeature() != null) {
            for (Feature feature : request.getFeature()) {
                String idObject = feature.getId();
                if (idObject != null) {
                    feature.setId(null);
                    queryRequest.append(patchMutationRequest.genericUpdate(
                            feature, false, nameId, idObject, "feature", "updateFeature",
                            new FeatureResponseProjection().id().isBundle().isEnabled().name()));
                    feature.setId(idObject);
                }
            }
        }

        // For Note
        if (request.getNote() != null) {
            for (Note note : request.getNote()) {
                if (note.getId() != null) {
                    String idObject = note.getId();
                    note.setId(null);
                    queryRequest.append(patchMutationRequest.genericUpdate(
                            note, false, nameId, idObject, "note", "updateNote",
                            new NoteResponseProjection().id().author().text().date()));
                    note.setId(idObject);
                }
            }
        }

        // For Place
        if (request.getPlace() != null) {
            for (Place place : request.getPlace()) {
                if (place.getPlaceId() != null) {
                    String idObject = place.getPlaceId();
                    place.setPlaceId(null);
                    queryRequest.append(patchMutationRequest.genericUpdate(
                            place, false, "placeId", idObject, "place", "updatePlace",
                            new PlaceResponseProjection().id().href().name().role()));
                    place.setPlaceId(idObject);
                }
            }
        }

        //For RelatedEntity
        if (request.getRelatedEntity() != null) {
            for (RelatedEntity relatedEntity : request.getRelatedEntity()) {
                if (relatedEntity.getId() != null) {
                    String idObject = relatedEntity.getId();
                    relatedEntity.setId(null);
                    queryRequest.append(patchMutationRequest.genericUpdate(
                            relatedEntity, true, nameId, idObject, "relatedEntity", "updateRelatedEntity",
                            new RelatedEntityResponseProjection().id().href().role().name()));
                    relatedEntity.setId(idObject);
                }
            }
        }

        // For relatedParty
        if (request.getRelatedParty() != null) {
            for (Party relatedParty : request.getRelatedParty()) {
                if (relatedParty.getId() != null) {
                    String idObject = relatedParty.getId();
                    relatedParty.setId(null);
                    queryRequest.append(patchMutationRequest.genericUpdate(
                            relatedParty, true, nameId, idObject, "party", "updateParty",
                            new PartyResponseProjection().id().href().role().name()));
                    relatedParty.setId(idObject);
                }
            }
        }

        // For serviceCharacteristic
        if (!(request.getServiceCharacteristic() == null || request.getServiceCharacteristic().isEmpty())) {

            creationList = new ArrayList<>();
            Service originalService = retrieveService(id);
            List<Characteristic> originalList = originalService.getServiceCharacteristic();

            if (!(originalList.isEmpty() || originalList == null)) {

                for (Characteristic newItem : request.getServiceCharacteristic()) {
                    boolean flag = false;
                    String originalItemId = null;
                    String newItemId = newItem.getId();

                    for (Characteristic originalItem : originalList) {
                        if (newItemId != null) {
                            if (originalItem.getId().equals(newItemId) && originalItem.getName().equals(newItem.getName())) {
                                originalItemId = originalItem.getCharacteristicId();
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag && originalItemId != null) {
                        //exists it is necessary to update
                        queryRequest.append(patchMutationRequest.genericUpdate(
                                newItem, false, "characteristicId", originalItemId, "characteristic", "updateCharacteristic",
                                new CharacteristicResponseProjection().id().name().valueType().valueUnits().value()));
                    } else {
                        // It does not exist, it is necessary to create
                        creationList.add(newItem);
                    }
                }
            } else {
                // It does not exist, it is necessary to create all
                creationList.addAll(request.getServiceCharacteristic());
            }
        }

        // For serviceSpecificationRef
        if (request.getServiceSpecification() != null) {
            ServiceSpecificationRef serviceSpecification = request.getServiceSpecification();
            String idObject = serviceSpecification.getId();

            if (idObject != null) {
                serviceSpecification.setId(null);
                queryRequest.append(patchMutationRequest.genericUpdate(
                        serviceSpecification, true, nameId, idObject, "serviceSpecificationRef", "updateServiceSpecificationRef",
                        new ServiceSpecificationRefResponseProjection().id().href().name().version()));
                serviceSpecification.setId(idObject);
            }
        }

        // For SupportingService
        if (request.getSupportingService() != null) {
            for (Service suppontingService : request.getSupportingService()) {
                if (suppontingService.getId() != null) {
                    String idObject = suppontingService.getId();
                    suppontingService.setId(null);
                    queryRequest.append(patchMutationRequest.genericUpdate(
                            suppontingService, true, nameId, idObject, "serviceSOM", "updateServiceSOM",
                            new PartyResponseProjection().id().href().role().name()));
                    suppontingService.setId(idObject);
                }
            }
        }
        return queryRequest;
    }

    public Service retrieveService(String id) throws JsonProcessingException, IOException, ApplicationException {
        Service responseService = null;

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setDelete(true);
        filterRequest.setNameId(nameId);
        filterRequest.setId(id);

        com.nttdata.model.dgraph.ServiceQueryRequest serviceQueryRequest = new ServiceQueryRequest();
        serviceQueryRequest.setOperationName("queryServiceSOM");
        serviceQueryRequest.setAlias("serviceSOM");
        serviceQueryRequest.setInput(filterRequest);

        ServiceSOMResponseProjection serviceSOMResponseProjection = buildResponse();

        GraphQLRequest graphQLRequest = new GraphQLRequest(serviceQueryRequest, serviceSOMResponseProjection);
        String request = graphQLRequest.toHttpJsonBody().replace("(:", "(");
        JsonNode jsonNode = cliente.ejecutar(request, graphQLURL);
        ObjectMapper objMaper = new ObjectMapper();
        responseService = objMaper.treeToValue(jsonNode.get("data").get("serviceSOM").get(0), Service.class);

        return responseService;
    }


    public String strPatch(Service service) {
        String strResponse = null;

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setNameId(nameId);
        filterRequest.setId(service.getId());
        service.setId(null);
        filterRequest.setRequest(service.toString());

        ServiceMutationRequest patchRequest = new ServiceMutationRequest();
        patchRequest.setOperationName("updateServiceSOM");
        patchRequest.setAlias("serviceSOM");
        patchRequest.setInput(filterRequest);

        ResponseProjectionWrapper serviceSOMResponseProjectionWrapper =
                (ResponseProjectionWrapper) new ResponseProjectionWrapper().responseProjection("serviceSOM", "serviceSOM", new ServiceSOMResponseProjection().id());

        GraphQLRequest graphQLRequest = new GraphQLRequest(patchRequest, serviceSOMResponseProjectionWrapper);
        strResponse = Utils.buildQuery(graphQLRequest);

        return strResponse;
    }
}
