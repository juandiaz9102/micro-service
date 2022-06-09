package com.nttdata.som.serviceorder.helper;


import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperation;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperationRequest;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@javax.annotation.processing.Generated(
        value = "com.kobylynskyi.graphql.codegen.GraphQLCodegen",
        date = "2022-04-16T17:08:46-0500"
)
public class UpdateServiceOrderMutationRequest implements GraphQLOperationRequest {

    public static final String OPERATION_NAME = "updateServiceOrder";
    public static final GraphQLOperation OPERATION_TYPE = GraphQLOperation.MUTATION;

    private String alias;
    private final Map<String, Object> input = new LinkedHashMap<>();
    private final Set<String> useObjectMapperForInputSerialization = new HashSet<>();

    public UpdateServiceOrderMutationRequest() {
    }

    public UpdateServiceOrderMutationRequest(String alias) {
        this.alias = alias;
    }

    public void setInput(UpdateServiceOrderInput input) {
        this.input.put("input", input);
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

    public static UpdateServiceOrderMutationRequest.Builder builder() {
        return new UpdateServiceOrderMutationRequest.Builder();
    }

    public static class Builder {

        private String $alias;
        private UpdateServiceOrderInput input;

        public Builder() {
        }

        public Builder alias(String alias) {
            this.$alias = alias;
            return this;
        }

        public Builder setInput(UpdateServiceOrderInput input) {
            this.input = input;
            return this;
        }


        public UpdateServiceOrderMutationRequest build() {
            UpdateServiceOrderMutationRequest obj = new UpdateServiceOrderMutationRequest($alias);
            obj.setInput(input);
            return obj;
        }

    }
}
