package com.nttdata.som.serviceorder.helper;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequestSerializer;

import java.util.StringJoiner;

public class FilterRequest {

  private String id;
  private String request;

  public FilterRequest() {}

  public String getId() {
    StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
    if (id != null) {
      joiner.add("id: " + GraphQLRequestSerializer.getEntry(id));
    }
    return joiner.toString();
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(", ", "{ ", " }");

    if (id != null ) {
      joiner.add("filter: " + getId());
      if(request!= null){
        joiner.add("set: " + request);
      }
    }

    return joiner.toString();
  }

}
