package com.nttdata.som.serviceorder.helper;

import com.nttdata.model.tmf.*;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;


@RunWith(MockitoJUnitRunner.class)
public class UpdateServiceOrderInputTest {

    @InjectMocks
    UpdateServiceOrderInput updateServiceOrderInput;


    @Test
    public void serviceOrderFilter() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("serviceOrder").and(FieldPredicates.inClass(CancellationRequest.class)));
        EasyRandom generator = new EasyRandom(parameters);
        List<ServiceOrderItemRef> serviceOrderItemRefList = generator.objects(ServiceOrderItemRef.class, 2).collect(Collectors.toList());
        List<Note> note = generator.objects(Note.class, 2).collect(Collectors.toList());
        List<Party> party = generator.objects(Party.class, 2).collect(Collectors.toList());
        FlowExecutionRef flowExecutionRef = generator.nextObject(FlowExecutionRef.class);
        ModificationRequest modificationRequest = generator.nextObject(ModificationRequest.class);
        ServiceOrderPatch serviceOrderPatch = new ServiceOrderPatch();
        serviceOrderPatch.setServiceOrderItem(serviceOrderItemRefList);
        serviceOrderPatch.setCategory("123");
        serviceOrderPatch.setDescription("123");
        serviceOrderPatch.setPriority("123");
        serviceOrderPatch.setRequestedCompletionDate("123");
        serviceOrderPatch.setRequestedStartDate("123");
        serviceOrderPatch.setState("123");
        serviceOrderPatch.setRelatedParty(party);
        serviceOrderPatch.setFlowExecutionRef(flowExecutionRef);
        serviceOrderPatch.setModifyRequest(modificationRequest);
        serviceOrderPatch.setNote(note);
        ServiceOrderFilter serviceOrderFilter = new ServiceOrderFilter();
        updateServiceOrderInput.setSet(serviceOrderPatch);
        updateServiceOrderInput.setRemove(serviceOrderPatch);
        updateServiceOrderInput.setFilter(serviceOrderFilter);
        updateServiceOrderInput.toString();
    }
}
