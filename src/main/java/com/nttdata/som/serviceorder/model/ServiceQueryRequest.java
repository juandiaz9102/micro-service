package com.nttdata.som.serviceorder.model;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperation;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperationRequest;
import com.nttdata.model.tmf638.Service;

import java.util.*;

public class ServiceQueryRequest implements GraphQLOperationRequest {

    public static final String OPERATION_NAME = "queryServiceSOM";
    public static final GraphQLOperation OPERATION_TYPE = GraphQLOperation.QUERY;

    private String alias;
    private final Map<String, Object> input = new LinkedHashMap<>();
    private final Set<String> useObjectMapperForInputSerialization = new HashSet<>();

    public ServiceQueryRequest() {
    }

    public ServiceQueryRequest(String alias) {
        this.alias = alias;
    }

    public void setInput(Object episode) {
        this.input.put("filter", episode);
    }

    @Override
    public GraphQLOperation getOperationType() {
        return OPERATION_TYPE;
    }

    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

    @Override
    public String getAlias() {
        return alias != null ? alias : OPERATION_NAME;
    }

    @Override
    public Map<String, Object> getInput() {
        return input;
    }

    @Override
    public Set<String> getUseObjectMapperForInputSerialization() {
        return useObjectMapperForInputSerialization;
    }

    @Override
    public String toString() {
        return Objects.toString(input);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String $alias;
        private Service episode;

        public Builder() {
        }

        public Builder alias(String alias) {
            this.$alias = alias;
            return this;
        }

        public Builder setEpisode(Service episode) {
            this.episode = episode;
            return this;
        }

        public ServiceQueryRequest build() {
            ServiceQueryRequest obj = new ServiceQueryRequest($alias);
            obj.setInput(episode);
            return obj;
        }

    }
}
