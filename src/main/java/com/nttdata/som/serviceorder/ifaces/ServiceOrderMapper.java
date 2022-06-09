package com.nttdata.som.serviceorder.ifaces;

import com.nttdata.model.tmf.ServiceOrder;

public interface ServiceOrderMapper {
    ServiceOrder mapServiceOrder(String id, ServiceOrder request);
}
