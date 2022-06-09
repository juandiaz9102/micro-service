package com.nttdata.som.serviceorder.ifaces;

import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.model.tmf.ServiceOrder;
import com.nttdata.model.tmf638.Service;

import java.io.IOException;

public interface ServiceOrderValidations {
    ServiceOrder validatePlace(ServiceOrder request) throws ApplicationException, IOException, BusinessException;
    Service validateService(Service service) throws ApplicationException, IOException, BusinessException;
}
