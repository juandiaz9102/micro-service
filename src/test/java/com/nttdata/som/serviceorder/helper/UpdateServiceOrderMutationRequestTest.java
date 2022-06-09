package com.nttdata.som.serviceorder.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateServiceOrderMutationRequestTest {

    @InjectMocks
    UpdateServiceOrderMutationRequest updateServiceOrderMutationRequest;

    @Test
    public void testBean() {
        UpdateServiceOrderInput updateServiceOrderInput = new UpdateServiceOrderInput();
        UpdateServiceOrderMutationRequest.builder();
        UpdateServiceOrderMutationRequest.builder().build();
        UpdateServiceOrderMutationRequest.builder().alias("123");
        UpdateServiceOrderMutationRequest.builder().setInput(updateServiceOrderInput);
        updateServiceOrderMutationRequest.setInput(updateServiceOrderInput);
        updateServiceOrderMutationRequest.getInput();
        updateServiceOrderMutationRequest.getAlias();
        updateServiceOrderMutationRequest.getOperationName();
        updateServiceOrderMutationRequest.getOperationType();
        updateServiceOrderMutationRequest.getUseObjectMapperForInputSerialization();
        updateServiceOrderMutationRequest.toString();
    }
}
