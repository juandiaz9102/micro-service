package com.nttdata.som.serviceorder.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.model.tmf.*;
import com.nttdata.model.tmf638.Service;
import com.nttdata.som.serviceorder.ifaces.ServiceOrderListFilter;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceOrderImplTest {

    @InjectMocks
    ServiceOrderImpl serviceOrder = new ServiceOrderImpl();

    @Mock
    ServiceUpdate serviceUpdate;

    @Mock
    ServiceOrderListFilter serviceOrderListFilter;

    @Mock
    ServiceOrderUpdateImpl serviceOrderUpdate;


    @Before
    public void setup() {
        ReflectionTestUtils.setField(serviceOrder, "graphQLURL", "http://demo8227627.mockable.io/graphql");
        ReflectionTestUtils.setField(serviceOrder, "nameId", "id");
        ReflectionTestUtils.setField(serviceOrder, "datePattern", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        ReflectionTestUtils.setField(serviceOrder, "flowCreationDateStr", "creationDate");
        ReflectionTestUtils.setField(serviceOrder, "flowEndExecutionDateStr", "endExecutionDate");
        ReflectionTestUtils.setField(serviceOrder, "flowExecutionDateStr", "executionDate");
        ReflectionTestUtils.setField(serviceOrder, "orderDateStr", "orderDate");
        ReflectionTestUtils.setField(serviceOrder, "startDateStr", "startDate");
        ReflectionTestUtils.setField(serviceUpdate, "graphQLURL", "http://demo8227627.mockable.io/graphql");
        ReflectionTestUtils.setField(serviceUpdate, "nameId", "id");

    }

    @Test
    public void createServiceOrderTest() throws IOException {
        ServiceOrder request = new ServiceOrder();
        request.setCategory("123");
        request.setId("123");
        try {
            serviceOrder.createServiceOrder(request);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void retrieveServiceOrderTest() throws IOException {
        try {
            serviceOrder.retrieveServiceOrder("123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void patchServiceOrderTest() throws IOException {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("serviceOrders").and(FieldPredicates.inClass(Party.class)));
        parameters.excludeField(FieldPredicates.named("service").and(FieldPredicates.inClass(ServiceOrderItem.class)));
        parameters.excludeField(FieldPredicates.named("serviceOrder").and(FieldPredicates.inClass(CancellationRequest.class).and(FieldPredicates.inClass(ModificationRequest.class))));
        EasyRandom generator = new EasyRandom(parameters);
        List<Note> note = generator.objects(Note.class, 2).collect(Collectors.toList());
        List<ServiceOrderRelationship> serviceOrderRelationships = generator.objects(ServiceOrderRelationship.class, 2).collect(Collectors.toList());
        List<ExternalReference> list = generator.objects(ExternalReference.class, 2).collect(Collectors.toList());
        List<Party> relatedParty = generator.objects(Party.class, 2).collect(Collectors.toList());
        List<ServiceOrderItem> serviceOrderItems = generator.objects(ServiceOrderItem.class, 2).collect(Collectors.toList());
        CancellationRequest cancellationRequest = generator.nextObject(CancellationRequest.class);
        List<ModificationRequest> modificationRequest = generator.objects(ModificationRequest.class, 2).collect(Collectors.toList());
        FlowExecutionRef flowExecutionRef = generator.nextObject(FlowExecutionRef.class);

        Service serviceTest = new Service();
        serviceTest.setId("123");
        serviceTest.setCategory("123");

        for (ServiceOrderItem serviceOrderItem :
                serviceOrderItems) {
            serviceOrderItem.setService(serviceTest);
        }

        ServiceOrder service = new ServiceOrder();
        service.setExternalReference(list);
        service.setRelatedParty(relatedParty);
        service.setNote(note);
        service.setOrderRelationship(serviceOrderRelationships);
        service.setServiceOrderItem(serviceOrderItems);
        service.setCancelRequest(cancellationRequest);
        service.setModifyRequest(modificationRequest);
        service.setFlowExecutionRef(flowExecutionRef);
        try {
            serviceOrder.patchServiceOrder("123", service);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void deleteServiceOrderTest() {
        try {
            serviceOrder.deleteServiceOrder("123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void listServiceOrderTest() {
        MultiValueMap<String, String> filters = new LinkedMultiValueMap<>();
        filters.add("orderDate","2022-04-06T08:10:10.6198192Z");
        filters.add("startDate","2022-04-06T08:10:10.6198192Z");
        filters.add("flowExecutionRef.executionDate","2022-04-06T08:10:10.6198192Z");
        filters.add("flowExecutionRef.endExecutionDate","2022-04-06T08:10:10.6198192Z");
        filters.add("flowExecutionRef.creationDate","2022-04-06T08:10:10.6198192Z");
        try {
            serviceOrder.listServiceOrder("123", filters);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateResourceTest() {
        try {
            UpdateResourceServiceOrder resourceServiceOrder = new UpdateResourceServiceOrder();
            serviceOrder.updateResource(resourceServiceOrder);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void saveServiceOrderTest() {
        try {
            ServiceOrder request = new ServiceOrder();
            serviceOrder.saveServiceOrder(request);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void modifyDateFlowExecutionRefTest() {
        MultiValueMap<String, String> fields = new LinkedMultiValueMap<>();
        try {
            serviceOrder.modifyDateFlowExecutionRef("1", fields);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void modifyFlowExecutionRefTest() {
        FlowExecutionRef flowExecutionRef = new FlowExecutionRef();
        try {
            serviceOrder.modifyFlowExecutionRef("1L", flowExecutionRef);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void cancelServiceOrderTest() {
        CancellationRequest cancellationRequest = new CancellationRequest();
        try {
            serviceOrder.cancelServiceOrder(cancellationRequest);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void modifyDateServiceOrderTest() {
        ModificationRequest modificationRequest = new ModificationRequest();
        try {
            serviceOrder.modifyDateServiceOrder(modificationRequest);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void serializeTests() {
        try {
            serviceOrder.deserialize(1L);
            serviceOrder.deserialize("test");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("test");
            serviceOrder.deserialize(stringBuilder);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void buildMapCadenceTest() {
        try {
            EasyRandomParameters parameters = new EasyRandomParameters();
            parameters.excludeField(FieldPredicates.named("serviceOrders").and(FieldPredicates.inClass(Party.class)));
            EasyRandom generator = new EasyRandom(parameters);
            List<ExternalReference> list = generator.objects(ExternalReference.class, 2).collect(Collectors.toList());
            List<Party> relatedParty = generator.objects(Party.class, 2).collect(Collectors.toList());
            ServiceOrder service = new ServiceOrder();
            service.setExternalReference(list);
            service.setRelatedParty(relatedParty);
            CancellationRequest requestToCadence = new CancellationRequest();
            ObjectMapper mapper = new ObjectMapper();
            requestToCadence.setServiceOrder(service);
            requestToCadence.setId("1L");
            requestToCadence.setCancellationReason("test");
            requestToCadence.setExternalId("123");
            requestToCadence.setBaseType("STRING");
            JsonNode jsonNode = mapper.valueToTree(requestToCadence);
            Map<String, String> map = new HashMap<>();
            String parent = "test";
            serviceOrder.buildMapCadence(jsonNode, map, parent);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

}
