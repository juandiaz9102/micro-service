package com.nttdata.som.serviceorder.helper;


import java.util.StringJoiner;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequestSerializer;

import lombok.Data;

@javax.annotation.processing.Generated(
        value = "com.kobylynskyi.graphql.codegen.GraphQLCodegen",
        date = "2022-04-16T17:08:46-0500"
)
@Data
public class ServiceOrderFilter implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //    @javax.validation.constraints.NotNull
    private java.util.List<String> id;
    //    private StringFullTextFilter description;
//    private StringHashFilter state;
//    private java.util.List<ServiceOrderHasFilter> has;
    private java.util.List<ServiceOrderFilter> and;
    private java.util.List<ServiceOrderFilter> or;
    private ServiceOrderFilter not;



    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
        if (id != null) {
            joiner.add("id: " + GraphQLRequestSerializer.getEntry(id));
        }
//        if (description != null) {
//            joiner.add("description: " + GraphQLRequestSerializer.getEntry(description));
//        }
//        if (state != null) {
//            joiner.add("state: " + GraphQLRequestSerializer.getEntry(state));
//        }
//        if (has != null) {
//            joiner.add("has: " + GraphQLRequestSerializer.getEntry(has));
//        }
        if (and != null) {
            joiner.add("and: " + GraphQLRequestSerializer.getEntry(and));
        }
        if (or != null) {
            joiner.add("or: " + GraphQLRequestSerializer.getEntry(or));
        }
        if (not != null) {
            joiner.add("not: " + GraphQLRequestSerializer.getEntry(not));
        }
        return joiner.toString();
    }

//    public static ServiceOrderFilter.Builder builder() {
//        return new ServiceOrderFilter.Builder();
//    }

//    public static class Builder {
//
//        private java.util.List<String> id;
//        private StringFullTextFilter description;
//        private StringHashFilter state;
//        private java.util.List<ServiceOrderHasFilter> has;
//        private java.util.List<ServiceOrderFilter> and;
//        private java.util.List<ServiceOrderFilter> or;
//        private ServiceOrderFilter not;
//
//        public Builder() {
//        }
//
//        public Builder setId(java.util.List<String> id) {
//            this.id = id;
//            return this;
//        }
//
//        public Builder setDescription(StringFullTextFilter description) {
//            this.description = description;
//            return this;
//        }
//
//        public Builder setState(StringHashFilter state) {
//            this.state = state;
//            return this;
//        }
//
//        public Builder setHas(java.util.List<ServiceOrderHasFilter> has) {
//            this.has = has;
//            return this;
//        }
//
//        public Builder setAnd(java.util.List<ServiceOrderFilter> and) {
//            this.and = and;
//            return this;
//        }
//
//        public Builder setOr(java.util.List<ServiceOrderFilter> or) {
//            this.or = or;
//            return this;
//        }
//
//        public Builder setNot(ServiceOrderFilter not) {
//            this.not = not;
//            return this;
//        }
//
//
//        public ServiceOrderFilter build() {
//            return new ServiceOrderFilter(id, description, state, has, and, or, not);
//        }
//
//    }
}
