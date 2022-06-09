package com.nttdata.som.serviceorder.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class SpringFoxConfigTest {

    @InjectMocks
    SpringFoxConfig springFoxConfig;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(springFoxConfig, "controller", "test");
        ReflectionTestUtils.setField(springFoxConfig, "title", "test");
        ReflectionTestUtils.setField(springFoxConfig, "description", "test");
        ReflectionTestUtils.setField(springFoxConfig, "version", "test");
        ReflectionTestUtils.setField(springFoxConfig, "contDescrip", "test");
        ReflectionTestUtils.setField(springFoxConfig, "packageName", "test");
    }

    @Test
    public void testApi(){
        springFoxConfig.api();
    }

}
