package com.nttdata.som.serviceorder.service.impl;

import com.nttdata.model.dgraph.*;
import com.nttdata.som.serviceorder.ifaces.ServiceOrderListFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class ServiceOrderListFilterImpl implements ServiceOrderListFilter {
    @Autowired
    private ServiceUpdate serviceUpdate = new ServiceUpdate();

    @Value("${com.nttdata.filter.serviceOrder.orderDate}")
    private String orderDateStr = "orderDate";
    @Value("${com.nttdata.filter.serviceOrder.startDate}")
    private String startDateStr = "startDate";

    private String initFlowExecutionFilter = "flowExecutionRef( filter: { ";
    private String executionDate = "executionDate";

    private ServiceOrderResponseProjection buildListResponse() {
        return new ServiceOrderResponseProjection().id()._atType().category().description().externalId()
                .priority().requestedCompletionDate().requestedStartDate().state().orderDate().startDate()
                .requestedStartDate()
                .externalReference(new ExternalReferenceResponseProjection().id().href().name().externalReferenceType())
                .note(new NoteResponseProjection().id().author().date().text())
                .orderRelationship(new ServiceOrderRelationshipResponseProjection().id().href().relationshipType())
                .relatedParty(new PartyResponseProjection().id().href().name().role())
                .serviceOrderItem(new ServiceOrderItemResponseProjection().serviceOrderItemId().action().id().state().quantity()
                        .appointment(new AppointmentRefResponseProjection().id().href().description())
                        .service(serviceUpdate.buildResponse()))
                .cancelRequest(new CancellationRequestResponseProjection().id().externalId().href().cancellationReason().state())
                .modifyRequest(new ModificationRequestResponseProjection().id().externalId().modifyReason())
                ;
    }


    private FlowExecutionRefResponseProjection buildFlowResponse() {
        return new FlowExecutionRefResponseProjection().id().domainId().workflowId().projectId().runId().creationDate().executionDate().endExecutionDate();
    }

    @Override
    public String createListRequest(String serviceOrderFilter, String serviceFlowExecutionFilter) {
        StringBuilder sbRequestList = new StringBuilder();
        String queryName = "queryServiceOrder";
        String responseServiceOrder = buildListResponse().toString();
        responseServiceOrder = responseServiceOrder.substring(0, responseServiceOrder.length() - 1);
        sbRequestList.append("query " + queryName + " { " + queryName + ": " + queryName + "(" + serviceOrderFilter + ")" + responseServiceOrder + serviceFlowExecutionFilter +
                " } ");

        return sbRequestList.toString();
    }

    @Override
    public String createFilterDoubleServiceOrder(String param1, String param2, String valueParam1, String valueParam2) {
        StringBuilder sbFilterDoubleServiceOrder = new StringBuilder();
        sbFilterDoubleServiceOrder.append("filter: {");
        sbFilterDoubleServiceOrder.append(param1 + ": { ge: \"" + valueParam1 + "\" } ");
        sbFilterDoubleServiceOrder.append("and: { " + param2 + ": { ge: \"" + valueParam2 + "\" } } }");
        return sbFilterDoubleServiceOrder.toString();
    }

    @Override
    public String createFilterUnitServiceOrder(String param, String valueParam) {
        StringBuilder sbFilterUnitServiceOrder = new StringBuilder();
        sbFilterUnitServiceOrder.append("filter: {");
        sbFilterUnitServiceOrder.append(param + ": { ge: \"" + valueParam + "\" } }");
        return sbFilterUnitServiceOrder.toString();
    }

    @Override
    public String uniqueFlowExecutionRef() {
        return "flowExecutionRef"+buildFlowResponse()+"}";
    }

    @Override
    public String betweenParamFlowExecution(String param, String valueParam) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = new Date();
        String currentDateTime = dateFormat.format(date);
        StringBuilder sbBetweenParamFlowExecution = new StringBuilder();
        sbBetweenParamFlowExecution.append(param + ": { between: { min: \"" + valueParam + "\" max: \"" + currentDateTime + "\" } }");
        return sbBetweenParamFlowExecution.toString();
    }

    @Override
    public String betweenParamFlowExecutionParams(String param1, String valueParam1, String valueParam2) {
        StringBuilder sbBetweenParamFlowExecutionParams = new StringBuilder();
        sbBetweenParamFlowExecutionParams.append(param1 + ": { between: { min: \"" + valueParam1 + "\" max: \"" + valueParam2 + "\" } }");
        return sbBetweenParamFlowExecutionParams.toString();
    }

    @Override
    public String creationDate(String valueParam) {
        return "creationDate: { ge: \"" + valueParam + "\" }";
    }

    @Override
    public String defaultFilterServiceOrder() {
        return " order: { asc:externalId }";
    }

    @Override
    public String createStructureFilterFlowExecution(String paramCreateDate, String paramBetween) {
        StringBuilder sbCreateFilterFlowExecutionRef = new StringBuilder();
        if (paramCreateDate != null && paramBetween != null) {
            sbCreateFilterFlowExecutionRef.append(initFlowExecutionFilter + paramCreateDate + " and: { " + paramBetween + " } } ) " + buildFlowResponse() + "}");
            sbCreateFilterFlowExecutionRef.toString();
        }
        if (paramCreateDate != null && paramBetween == null) {
            sbCreateFilterFlowExecutionRef.append(initFlowExecutionFilter + paramCreateDate + " } ) " + buildFlowResponse() + "}");
            sbCreateFilterFlowExecutionRef.toString();
        }
        if (paramCreateDate == null && paramBetween != null) {
            sbCreateFilterFlowExecutionRef.append(initFlowExecutionFilter + paramBetween + " } ) " + buildFlowResponse() + "}");
            sbCreateFilterFlowExecutionRef.toString();
        }
        return sbCreateFilterFlowExecutionRef.toString();
    }

    @Override
    public String createServiceOrderFilter(String sOrderDate, String sStartDate) {
        if (sOrderDate.isEmpty() && sStartDate.isEmpty()) {
            return defaultFilterServiceOrder();
        } else if (!sOrderDate.isEmpty() && sStartDate.isEmpty()) {
            return createFilterUnitServiceOrder(orderDateStr, sOrderDate);
        } else if (sOrderDate.isEmpty() && !sStartDate.isEmpty()) {
            return createFilterUnitServiceOrder(startDateStr, sStartDate);
        } else {
            return createFilterDoubleServiceOrder(orderDateStr, startDateStr, sOrderDate, sStartDate);
        }
    }

    @Override
    public String createFlowExecutionRefFilter(String sCreationDate, String sExecutionDate, String sEndExecutionDate) {
        if (sCreationDate.isEmpty() && sExecutionDate.isEmpty() && sEndExecutionDate.isEmpty()) {
            return uniqueFlowExecutionRef();
        } else if (!sCreationDate.isEmpty() && !sExecutionDate.isEmpty() && !sEndExecutionDate.isEmpty()) {
            return createStructureFilterFlowExecution(creationDate(sCreationDate), betweenParamFlowExecutionParams(executionDate, sExecutionDate, sEndExecutionDate));
        } else if (!sCreationDate.isEmpty() && sExecutionDate.isEmpty() && sEndExecutionDate.isEmpty()) {
            return createStructureFilterFlowExecution(creationDate(sCreationDate), null);
        } else if (!sCreationDate.isEmpty() && !sExecutionDate.isEmpty() && sEndExecutionDate.isEmpty()) {
            return createStructureFilterFlowExecution(creationDate(sCreationDate), betweenParamFlowExecution(executionDate, sExecutionDate));
        } else if (!sCreationDate.isEmpty() && sExecutionDate.isEmpty() && !sEndExecutionDate.isEmpty()) {
            return createStructureFilterFlowExecution(creationDate(sCreationDate), betweenParamFlowExecution("endExecutionDate", sEndExecutionDate));
        } else if (sCreationDate.isEmpty() && !sExecutionDate.isEmpty() && sEndExecutionDate.isEmpty()) {
            return createStructureFilterFlowExecution(null, betweenParamFlowExecution(executionDate, sExecutionDate));
        } else {
            return createStructureFilterFlowExecution(null, betweenParamFlowExecution("endExecutionDate", sEndExecutionDate));
        }
    }
}
