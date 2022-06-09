package com.nttdata.som.serviceorder.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.model.dgraph.FilterRequest;
import com.nttdata.model.dgraph.GeometryResponseProjection;
import com.nttdata.model.dgraph.PlaceResponseProjection;
import com.nttdata.model.dgraph.ServiceQueryRequest;
import com.nttdata.model.tmf.Geometry;
import com.nttdata.model.tmf.Place;
import com.nttdata.model.tmf.ServiceOrder;
import com.nttdata.model.tmf.ServiceOrderItem;
import com.nttdata.som.serviceorder.client.ClientUpdate;
import com.nttdata.som.serviceorder.ifaces.ServiceOrderValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
public class ServiceOrderValidationsImpl implements ServiceOrderValidations {
  @Autowired private ClientUpdate clientUpdate = new ClientUpdate();

  @Value("${com.nttdata.nameId.value}")
  private String nameId;

  @Value("${graphQL.url}")
  private String graphQLURL;

  private static final String PLACE = "place";

  @Override
  public ServiceOrder validatePlace(ServiceOrder request)
      throws ApplicationException, IOException, BusinessException {
    if (request.getServiceOrderItem() != null) {
      for (ServiceOrderItem serviceOrderItem : request.getServiceOrderItem()) {
        validateServiceOrderItem(serviceOrderItem);
      }
    }
    return request;
  }

  @Override
  public com.nttdata.model.tmf638.Service validateService(com.nttdata.model.tmf638.Service service)
      throws ApplicationException, IOException, BusinessException {
    List<Place> placeList = service.getPlace();
    if ((placeList != null) && (!placeList.isEmpty())) {
      for (Place place : placeList) {
        validatePlace(place);
      }
    }
    return service;
  }

  private void validateServiceOrderItem(ServiceOrderItem serviceOrderItem)
      throws ApplicationException, IOException, BusinessException {
    if (serviceOrderItem.getService() != null && serviceOrderItem.getService().getPlace() != null) {
      for (Place place : serviceOrderItem.getService().getPlace()) {
        validatePlace(place);
      }
    }
  }

  private void validatePlace(Place place)
      throws IOException, BusinessException, ApplicationException {
    if (place.getId() != null && !place.getId().isEmpty()) {
      processIdPlace(place);
    } else {
      if (place.getGeometry() != null && !place.getGeometry().isEmpty()) {
        if (place.getSpatialRef() != null && !place.getSpatialRef().isEmpty()) {
          validateGeometry(place);
        } else {
          throw new BusinessException("spatialRef field is null");
        }
      } else {
        throw new BusinessException("geometry field is null");
      }
    }
  }

  private void processIdPlace(Place place)
      throws ApplicationException, IOException, BusinessException {
    if (validatePlaceById(place)) {
      Place searchPlaceById = retrievePlaceById(place.getId());
      if (searchPlaceById != null) {
        place.setPlaceId(searchPlaceById.getPlaceId());
      }
    } else {
      throw new BusinessException(
          "Geometry data can´t be send when id is present in the place attribute");
    }
  }

  private boolean validatePlaceById(Place place) {
    return isGeometryEmpty(place)
        && place.getSpatialRef() == null
        && place.getGeometryType() == null;
  }

  private boolean isGeometryEmpty(Place place) {
    boolean flag = false;
    if (place.getGeometry() != null) {
      if (place.getGeometry().isEmpty()) {
        flag = true;
      }
    } else {
      flag = true;
    }
    return flag;
  }

  private boolean validatePlaceByGeometry(Place place) {
    return place.getId() == null && place.getRole() == null;
  }

  private void validateGeometry(Place place)
      throws ApplicationException, IOException, BusinessException {
    if (validatePlaceByGeometry(place)) {
      processGeometry(place);
    } else {
      throw new BusinessException(
          "Id and role data can´t be send when geometry is present in the place attribute");
    }
  }

  private void processGeometry(Place place) throws ApplicationException, IOException {

    List<Place> listPlaces = retrieveListPlaceBySpatialRef(place.getSpatialRef());
    if (listPlaces != null && !listPlaces.isEmpty()) {
      place.getGeometry().sort(Comparator.comparing(Geometry::getX));
      for (Place placeListed : listPlaces) {
        if (placeListed.getGeometry() != null && !placeListed.getGeometry().isEmpty()) {
          placeListed.getGeometry().sort(Comparator.comparing(Geometry::getX));
          if (place.getGeometry().equals(placeListed.getGeometry())) {
            place.setPlaceId(placeListed.getPlaceId());
          }
        }
      }
    }
  }

  private List<Place> retrieveListPlaceBySpatialRef(String spatialRef)
      throws ApplicationException, IOException {
    FilterRequest filterRequest = new FilterRequest();
    filterRequest.setDelete(true);
    filterRequest.setNameId("spatialRef");
    filterRequest.setEquals(true);
    filterRequest.setId(spatialRef);
    ServiceQueryRequest serviceQueryRequest = new ServiceQueryRequest();
    serviceQueryRequest.setOperationName("queryPlace");
    serviceQueryRequest.setAlias(PLACE);
    serviceQueryRequest.setInput(filterRequest);
    PlaceResponseProjection placeResponseProjection =
        new PlaceResponseProjection()
            .placeId()
            .id()
            .href()
            .name()
            .role()
            .type()
            .geometryType()
            .accuracy()
            .spatialRef()
            .geometry(new GeometryResponseProjection().x().y());
    GraphQLRequest graphQLRequest =
        new GraphQLRequest(serviceQueryRequest, placeResponseProjection);
    String request = graphQLRequest.toHttpJsonBody().replace("(:", "(");
    JsonNode jsonNode = clientUpdate.ejecutar(request, graphQLURL);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.convertValue(
        objectMapper.treeToValue(jsonNode.get("data").get(PLACE), List.class),
        new TypeReference<>() {});
  }

  private Place retrievePlaceById(String id) throws ApplicationException, IOException {
    FilterRequest filterRequest = new FilterRequest();
    filterRequest.setDelete(true);
    filterRequest.setNameId(nameId);
    filterRequest.setId(id);
    filterRequest.setEquals(true);
    ServiceQueryRequest serviceQueryRequest = new ServiceQueryRequest();
    serviceQueryRequest.setOperationName("queryPlace");
    serviceQueryRequest.setAlias(PLACE);
    serviceQueryRequest.setInput(filterRequest);
    PlaceResponseProjection placeResponseProjection =
        new PlaceResponseProjection()
            .placeId()
            .id()
            .href()
            .name()
            .role()
            .type()
            .geometryType()
            .accuracy()
            .spatialRef()
            .geometry(new GeometryResponseProjection().x().y());
    GraphQLRequest graphQLRequest =
        new GraphQLRequest(serviceQueryRequest, placeResponseProjection);
    String request = graphQLRequest.toHttpJsonBody().replace("(:", "(");
    JsonNode jsonNode = clientUpdate.ejecutar(request, graphQLURL);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.treeToValue(jsonNode.get("data").get(PLACE).get(0), Place.class);
  }
}
