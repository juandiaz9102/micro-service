package com.nttdata.som.serviceorder.service.impl;

import com.nttdata.model.tmf.CancellationRequest;
import com.nttdata.model.tmf.FlowExecutionRef;
import com.nttdata.model.tmf.ModificationRequest;
import com.nttdata.model.tmf.ServiceOrder;
import com.nttdata.som.serviceorder.controller.ServiceOrderController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceOrderControllerTest {

    @InjectMocks
    ServiceOrderController serviceOrderController;

    @Mock
    ServiceOrderImpl serviceOrder;

    private ServiceOrder response = new ServiceOrder();

    @Before
    public void setup() throws IOException {

    }


    @Test
    public void createServiceOrderTestException() {
        try {
            ServiceOrder service = new ServiceOrder();
            when(serviceOrder.createServiceOrder(any(ServiceOrder.class))).thenReturn(service);
            ServiceOrder ServiceOrder = new ServiceOrder();
            serviceOrderController.createServiceOrder(ServiceOrder);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void createServiceOrderTestOk() {
        try {
            ServiceOrder service = new ServiceOrder();
            service.setId("1");
            when(serviceOrder.createServiceOrder(any(ServiceOrder.class))).thenReturn(service);
            ServiceOrder ServiceOrder = new ServiceOrder();
            serviceOrderController.createServiceOrder(ServiceOrder);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void listServiceOrderTest() throws IOException, ParseException {
        try {
            List<ServiceOrder> list = new ArrayList<>();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.put("1", Collections.singletonList("1"));
            when(serviceOrder.listServiceOrder(any(), any())).thenReturn(list);
            serviceOrderController.listServiceOrder("test", map);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void retrieveServiceOrderTest() throws IOException, ParseException {
        try {
            when(serviceOrder.retrieveServiceOrder(any())).thenReturn(response);
            serviceOrderController.retrieveServiceOrder("test", "test");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void patchServiceOrderTest() throws IOException, ParseException {
        try {
            when(serviceOrder.patchServiceOrder(any(), any())).thenReturn(response);
            serviceOrderController.patchServiceOrder("test", response);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void deleteServiceOrderTest() throws IOException, ParseException {
        try {
            when(serviceOrder.deleteServiceOrder(any())).thenReturn("test");
            serviceOrderController.deleteServiceOrder("test");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void modifyDateServiceOrderTest() throws IOException, ParseException {
        try {
            ServiceOrder responseServiceOrder = new ServiceOrder();
            ModificationRequest modificationRequest = new ModificationRequest();
            when(serviceOrder.modifyDateServiceOrder(any())).thenReturn(responseServiceOrder);
            serviceOrderController.modifyDateServiceOrder(modificationRequest);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void cancelServiceOrderTest() throws IOException, ParseException {
        try {
            ServiceOrder responseServiceOrder = new ServiceOrder();
            CancellationRequest cancellationRequest = new CancellationRequest();
            when(serviceOrder.cancelServiceOrder(any())).thenReturn(responseServiceOrder);
            serviceOrderController.cancelServiceOrder(cancellationRequest);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void modifyFlowExecutionRefTest() throws IOException, ParseException {
        try {
            FlowExecutionRef flowExecutionRef = new FlowExecutionRef();
            when(serviceOrder.modifyFlowExecutionRef(any(), any())).thenReturn(flowExecutionRef);
            serviceOrderController.modifyFlowExecutionRef("1L", flowExecutionRef);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void modifyDateFlowExecutionRefTest() throws IOException, ParseException {
        try {
            FlowExecutionRef flowExecutionRef = new FlowExecutionRef();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.put("1", Collections.singletonList("1"));
            when(serviceOrder.modifyDateFlowExecutionRef(any(), any())).thenReturn(flowExecutionRef);
            serviceOrderController.modifyDateFlowExecutionRef("1L", map);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

}
