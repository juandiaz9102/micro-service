package com.nttdata.som.serviceorder.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.meanbean.test.BeanVerifier;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ServiceOrderFilterTest {

    @InjectMocks
    ServiceOrderFilter serviceOrderFilter;


    @Test
    public void serviceOrderFilter(){
        BeanVerifier.verifyBean(ServiceOrderFilter.class);
    }
}
