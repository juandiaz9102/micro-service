package com.nttdata.som.serviceorder.ifaces;

import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.model.tmf.*;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

public interface ServiceOrderService {

    List<ServiceOrder> listServiceOrder(String fields, MultiValueMap<String, String> filters) throws ParseException, IOException, ApplicationException;

    ServiceOrder retrieveServiceOrder(String id) throws IOException, ApplicationException;

    ServiceOrder createServiceOrder(ServiceOrder request) throws IOException, ApplicationException, BusinessException;

    ServiceOrder patchServiceOrder(String id, ServiceOrder request) throws ApplicationException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, BusinessException;

    String deleteServiceOrder(String id) throws IOException, ApplicationException;

    ServiceOrder modifyDateServiceOrder(ModificationRequest request);

    ServiceOrder cancelServiceOrder(CancellationRequest request);

    FlowExecutionRef modifyFlowExecutionRef(String id, FlowExecutionRef request) throws ApplicationException;

    ServiceOrder updateResource(UpdateResourceServiceOrder request);

    ServiceOrder saveServiceOrder(ServiceOrder request);

    FlowExecutionRef modifyDateFlowExecutionRef(String id, MultiValueMap<String, String> fields);


}
