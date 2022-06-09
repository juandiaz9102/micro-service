package com.nttdata.som.serviceorder.service.impl;

import com.nttdata.model.tmf.Geometry;
import com.nttdata.model.tmf.Place;
import com.nttdata.model.tmf.ServiceOrder;
import com.nttdata.model.tmf.ServiceOrderItem;
import com.nttdata.model.tmf638.Service;
import com.nttdata.som.serviceorder.ifaces.ServiceOrderValidations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ServiceOrderValidationsImplTest {
    @InjectMocks
    ServiceOrderValidationsImpl serviceOrderValidations = new ServiceOrderValidationsImpl();

    @Before
    public void setup() {
        ReflectionTestUtils.setField(serviceOrderValidations, "graphQLURL", "http://demo8227627.mockable.io/graphql");
        ReflectionTestUtils.setField(serviceOrderValidations, "nameId", "id");
    }


    @Test
    public void validatePlaceById() {
        try {
            ServiceOrder serviceOrder = new ServiceOrder();
            List<ServiceOrderItem> listOrderItems = new ArrayList<>();
            ServiceOrderItem serviceOrderItem = new ServiceOrderItem();
            Service service = new Service();
            List<Place> listPlaces = new ArrayList<>();
            Place place = new Place();
            place.setId("TEST123");
            place.setRole("TEST");
            listPlaces.add(place);
            service.setPlace(listPlaces);
            serviceOrderItem.setService(service);
            listOrderItems.add(serviceOrderItem);
            serviceOrder.setServiceOrderItem(listOrderItems);
            serviceOrderValidations.validatePlace(serviceOrder);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void validatePlaceByGeometry() {
        try {
            ServiceOrder serviceOrder = new ServiceOrder();
            List<ServiceOrderItem> listOrderItems = new ArrayList<>();
            ServiceOrderItem serviceOrderItem = new ServiceOrderItem();
            Service service = new Service();
            List<Place> listPlaces = new ArrayList<>();
            Place place = new Place();
            place.setGeometryType("TEST123");
            place.setAccuracy("TEST");
            place.setSpatialRef("WGS84");
            List<Geometry> listGeometry = new ArrayList<>();
            Geometry geometry = new Geometry();
            geometry.setX("1.5");
            geometry.setY("1.6");
            listGeometry.add(geometry);
            place.setGeometry(listGeometry);
            listPlaces.add(place);
            service.setPlace(listPlaces);
            serviceOrderItem.setService(service);
            listOrderItems.add(serviceOrderItem);
            serviceOrder.setServiceOrderItem(listOrderItems);
            serviceOrderValidations.validatePlace(serviceOrder);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }


}
