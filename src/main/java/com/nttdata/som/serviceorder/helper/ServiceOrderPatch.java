package com.nttdata.som.serviceorder.helper;


import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequestSerializer;
import com.nttdata.model.tmf.*;
import lombok.Data;

import java.util.StringJoiner;

@javax.annotation.processing.Generated(
        value = "com.kobylynskyi.graphql.codegen.GraphQLCodegen",
        date = "2022-04-16T17:08:46-0500"
)
@Data
public class ServiceOrderPatch implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String _atType;
    private String category;
    private String description;
    private String externalId;
    private String priority;
    private String requestedCompletionDate;
    private String requestedStartDate;
    private String state;
    private java.util.List<Party> relatedParty;
    private FlowExecutionRef flowExecutionRef;
    private CancellationRequest cancelRequest;
    private ModificationRequest modifyRequest;
    private java.util.List<ServiceOrderItemRef> serviceOrderItem;
    private java.util.List<Note> note;


    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
        if (_atType != null) {
            joiner.add("_atType: " + GraphQLRequestSerializer.getEntry(_atType));
        }
        if (category != null) {
            joiner.add("category: " + GraphQLRequestSerializer.getEntry(category));
        }
        if (description != null) {
            joiner.add("description: " + GraphQLRequestSerializer.getEntry(description));
        }
        if (externalId != null) {
            joiner.add("externalId: " + GraphQLRequestSerializer.getEntry(externalId));
        }
        if (priority != null) {
            joiner.add("priority: " + GraphQLRequestSerializer.getEntry(priority));
        }
        if (requestedCompletionDate != null) {
            joiner.add("requestedCompletionDate: " + GraphQLRequestSerializer.getEntry(requestedCompletionDate));
        }
        if (requestedStartDate != null) {
            joiner.add("requestedStartDate: " + GraphQLRequestSerializer.getEntry(requestedStartDate));
        }
        if (state != null) {
            joiner.add("state: " + GraphQLRequestSerializer.getEntry(state));
        }
        if (relatedParty != null) {
            joiner.add("relatedParty: " + GraphQLRequestSerializer.getEntry(relatedParty));
        }
        if (flowExecutionRef != null) {
            joiner.add("flowExecutionRef: " + GraphQLRequestSerializer.getEntry(flowExecutionRef));
        }
        if (cancelRequest != null) {
            joiner.add("cancelRequest: " + GraphQLRequestSerializer.getEntry(cancelRequest));
        }
        if (modifyRequest != null) {
            joiner.add("modifyRequest: " + GraphQLRequestSerializer.getEntry(modifyRequest));
        }
        if (serviceOrderItem != null) {
            joiner.add("serviceOrderItem: " + GraphQLRequestSerializer.getEntry(serviceOrderItem));
        }
        if (note != null) {
            joiner.add("note: " + GraphQLRequestSerializer.getEntry(note));
        }
        return joiner.toString();
    }


}
