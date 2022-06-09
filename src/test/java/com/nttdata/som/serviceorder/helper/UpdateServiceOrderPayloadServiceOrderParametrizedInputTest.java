package com.nttdata.som.serviceorder.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateServiceOrderPayloadServiceOrderParametrizedInputTest {

    @InjectMocks
    UpdateServiceOrderPayloadServiceOrderParametrizedInput updateServiceOrderPayloadServiceOrderParametrizedInput;

    @Test
    public void testPayloadService() {
        updateServiceOrderPayloadServiceOrderParametrizedInput.filter(new ServiceOrderFilter());
        updateServiceOrderPayloadServiceOrderParametrizedInput.first(1);
        updateServiceOrderPayloadServiceOrderParametrizedInput.offset(1);
        updateServiceOrderPayloadServiceOrderParametrizedInput.toString();
    }
}
