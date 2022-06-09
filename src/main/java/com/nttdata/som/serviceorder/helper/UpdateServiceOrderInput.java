package com.nttdata.som.serviceorder.helper;


import java.util.StringJoiner;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequestSerializer;

import lombok.Data;

@javax.annotation.processing.Generated(
        value = "com.kobylynskyi.graphql.codegen.GraphQLCodegen",
        date = "2022-04-16T17:08:46-0500"
)
@Data
public class UpdateServiceOrderInput implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //    @javax.validation.constraints.NotNull
    private ServiceOrderFilter filter;
    private ServiceOrderPatch set;
    private ServiceOrderPatch remove;


    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
        if (filter != null) {
            joiner.add("filter: " + GraphQLRequestSerializer.getEntry(filter));
        }
        if (set != null) {
            joiner.add("set: " + GraphQLRequestSerializer.getEntry(set));
        }
        if (remove != null) {
            joiner.add("remove: " + GraphQLRequestSerializer.getEntry(remove));
        }
        return joiner.toString();
    }


}
