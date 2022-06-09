package com.nttdata.som.serviceorder.helper;


import java.util.StringJoiner;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLParametrizedInput;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequestSerializer;

/**
 * Parametrized input for field serviceOrder in type UpdateServiceOrderPayload
 */
@javax.annotation.processing.Generated(
        value = "com.kobylynskyi.graphql.codegen.GraphQLCodegen",
        date = "2022-04-17T09:22:04-0500"
)
public class UpdateServiceOrderPayloadServiceOrderParametrizedInput implements GraphQLParametrizedInput {

    private ServiceOrderFilter filter;
    //    private ServiceOrderOrder order;
    private Integer first;
    private Integer offset;

    public UpdateServiceOrderPayloadServiceOrderParametrizedInput() {
    }

//    public UpdateServiceOrderPayloadServiceOrderParametrizedInput(ServiceOrderFilter filter, ServiceOrderOrder order, Integer first, Integer offset) {
//        this.filter = filter;
////        this.order = order;
//        this.first = first;
//        this.offset = offset;
//    }

    public UpdateServiceOrderPayloadServiceOrderParametrizedInput filter(ServiceOrderFilter filter) {
        this.filter = filter;
        return this;
    }

//    public UpdateServiceOrderPayloadServiceOrderParametrizedInput order(ServiceOrderOrder order) {
//        this.order = order;
//        return this;
//    }

    public UpdateServiceOrderPayloadServiceOrderParametrizedInput first(Integer first) {
        this.first = first;
        return this;
    }

    public UpdateServiceOrderPayloadServiceOrderParametrizedInput offset(Integer offset) {
        this.offset = offset;
        return this;
    }


    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "(", ")");
        if (filter != null) {
            joiner.add("filter: " + GraphQLRequestSerializer.getEntry(filter));
        }
//        if (order != null) {
//            joiner.add("order: " + GraphQLRequestSerializer.getEntry(order));
//        }
        if (first != null) {
            joiner.add("first: " + GraphQLRequestSerializer.getEntry(first));
        }
        if (offset != null) {
            joiner.add("offset: " + GraphQLRequestSerializer.getEntry(offset));
        }
        return joiner.toString();
    }

}
