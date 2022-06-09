package com.nttdata.som.serviceorder.helper;

import com.nttdata.model.tmf.ServiceOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ServiceValidatorTest {

    @Test
    public void validatePostRequestTest() {
        ServiceOrder serviceOrder = new ServiceOrder();
        Boolean test = ServiceValidator.validatePostRequest(serviceOrder);
        assertNotNull(test);
    }

    @Test
    public void validateRetrieveRequestTest() {
        Boolean test = ServiceValidator.validateRetrieveRequest(1L);
        assertNotNull(test);
    }
}
