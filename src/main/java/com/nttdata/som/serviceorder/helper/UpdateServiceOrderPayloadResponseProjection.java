package com.nttdata.som.serviceorder.helper;


import java.util.HashMap;
import java.util.Map;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResponseField;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResponseProjection;
import com.nttdata.model.dgraph.ServiceOrderResponseProjection;

/**
 * Response projection for UpdateServiceOrderPayload
 */
@javax.annotation.processing.Generated(
        value = "com.kobylynskyi.graphql.codegen.GraphQLCodegen",
        date = "2022-04-17T09:22:04-0500"
)
public class UpdateServiceOrderPayloadResponseProjection extends GraphQLResponseProjection {

    private final Map<String, Integer> projectionDepthOnFields = new HashMap<>();

    public UpdateServiceOrderPayloadResponseProjection() {
    }

    public UpdateServiceOrderPayloadResponseProjection all$() {
        return all$(3);
    }

    public UpdateServiceOrderPayloadResponseProjection all$(int maxDepth) {
        if (projectionDepthOnFields.getOrDefault("UpdateServiceOrderPayloadResponseProjection.ServiceOrderResponseProjection.serviceOrder", 0) <= maxDepth) {
            projectionDepthOnFields.put("UpdateServiceOrderPayloadResponseProjection.ServiceOrderResponseProjection.serviceOrder", projectionDepthOnFields.getOrDefault("UpdateServiceOrderPayloadResponseProjection.ServiceOrderResponseProjection.serviceOrder", 0) + 1);
            this.serviceOrder(new ServiceOrderResponseProjection().all$(maxDepth - projectionDepthOnFields.getOrDefault("UpdateServiceOrderPayloadResponseProjection.ServiceOrderResponseProjection.serviceOrder", 0)));
        }
        this.numUids();
        this.typename();
        return this;
    }

    public UpdateServiceOrderPayloadResponseProjection serviceOrder(ServiceOrderResponseProjection subProjection) {
        return serviceOrder((String)null, subProjection);
    }

    public UpdateServiceOrderPayloadResponseProjection serviceOrder(String alias, ServiceOrderResponseProjection subProjection) {
        fields.add(new GraphQLResponseField("serviceOrder").alias(alias).projection(subProjection));
        return this;
    }

    public UpdateServiceOrderPayloadResponseProjection serviceOrder(UpdateServiceOrderPayloadServiceOrderParametrizedInput input, ServiceOrderResponseProjection subProjection) {
        return serviceOrder(null, input, subProjection);
    }

    public UpdateServiceOrderPayloadResponseProjection serviceOrder(String alias, UpdateServiceOrderPayloadServiceOrderParametrizedInput input, ServiceOrderResponseProjection subProjection) {
        fields.add(new GraphQLResponseField("serviceOrder").alias(alias).parameters(input).projection(subProjection));
        return this;
    }

    public UpdateServiceOrderPayloadResponseProjection numUids() {
        return numUids(null);
    }

    public UpdateServiceOrderPayloadResponseProjection numUids(String alias) {
        fields.add(new GraphQLResponseField("numUids").alias(alias));
        return this;
    }

    public UpdateServiceOrderPayloadResponseProjection typename() {
        return typename(null);
    }

    public UpdateServiceOrderPayloadResponseProjection typename(String alias) {
        fields.add(new GraphQLResponseField("__typename").alias(alias));
        return this;
    }


}
