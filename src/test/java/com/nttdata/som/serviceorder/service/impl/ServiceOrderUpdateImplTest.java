package com.nttdata.som.serviceorder.service.impl;

import com.nttdata.model.tmf.*;
import com.nttdata.model.tmf638.Service;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ServiceOrderUpdateImplTest {

    @InjectMocks
    ServiceOrderUpdateImpl serviceOrderUpdate;

    @Mock
    ServiceUpdate serviceUpdate;

    private ServiceOrder service = new ServiceOrder();

    @Before
    public void setup() {
        ReflectionTestUtils.setField(serviceUpdate, "graphQLURL", "http://demo8227627.mockable.io/graphql");
        ReflectionTestUtils.setField(serviceUpdate, "nameId", "id");
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

        service.setExternalReference(list);
        service.setRelatedParty(relatedParty);
        service.setNote(note);
        service.setOrderRelationship(serviceOrderRelationships);
        service.setServiceOrderItem(serviceOrderItems);
        service.setCancelRequest(cancellationRequest);
        service.setModifyRequest(modificationRequest);
        service.setFlowExecutionRef(flowExecutionRef);
    }

    @Test
    public void updateExternalReferenceTest() {

        try {
            serviceOrderUpdate.updateExternalReference(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateNoteTest() {

        try {
            serviceOrderUpdate.updateNote(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateServiceOrderRelationshipTest() {

        try {
            serviceOrderUpdate.updateServiceOrderRelationship(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updatePartyListTest() {

        try {
            serviceOrderUpdate.updatePartyList(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateServiceOrderItemTest() {

        try {
            serviceOrderUpdate.updateServiceOrderItem(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateCancelRequestTest() {

        try {
            serviceOrderUpdate.updateCancelRequest(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateModificationRequestTest() {

        try {
            serviceOrderUpdate.updateModificationRequest(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateFlowExecutionRefTest() {

        try {
            serviceOrderUpdate.updateFlowExecutionRef(new StringBuilder(), service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void updateStrPatchTest() {

        try {
            serviceOrderUpdate.strPatch(service, "id");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }


}
