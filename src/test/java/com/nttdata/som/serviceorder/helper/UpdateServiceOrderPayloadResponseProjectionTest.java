package com.nttdata.som.serviceorder.helper;

import com.nttdata.model.dgraph.ServiceOrderResponseProjection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateServiceOrderPayloadResponseProjectionTest {
    @InjectMocks
    UpdateServiceOrderPayloadResponseProjection updateServiceOrderPayloadResponseProjection;


    @Test
    public void payloadTest(){
        updateServiceOrderPayloadResponseProjection.all$(2);
        updateServiceOrderPayloadResponseProjection.all$();
        updateServiceOrderPayloadResponseProjection.serviceOrder("123",new ServiceOrderResponseProjection());
        updateServiceOrderPayloadResponseProjection.serviceOrder(new ServiceOrderResponseProjection());
    }


}
