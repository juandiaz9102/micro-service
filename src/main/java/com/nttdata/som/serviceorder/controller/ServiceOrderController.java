package com.nttdata.som.serviceorder.controller;

import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.exceptions.NotFoundException;
import com.nttdata.model.tmf.CancellationRequest;
import com.nttdata.model.tmf.FlowExecutionRef;
import com.nttdata.model.tmf.ModificationRequest;
import com.nttdata.model.tmf.ServiceOrder;
import com.nttdata.som.serviceorder.api.ServiceOrderApiOperations;
import com.nttdata.som.serviceorder.helper.ServiceValidator;
import com.nttdata.som.serviceorder.service.impl.ServiceOrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

@RestController
public class ServiceOrderController implements ServiceOrderApiOperations {

    @Autowired
    private ServiceOrderImpl serviceOrderImpl;

    @Override
    public ServiceOrder createServiceOrder(@RequestBody ServiceOrder request) throws IOException, ApplicationException, BusinessException {
        ServiceOrder result = new ServiceOrder();

//        try{
        boolean requestCorrect = ServiceValidator.validatePostRequest(request);
        if (!requestCorrect) {
            // jaegerLogger.logException(request.getId().toString(), request.toString(),
            // "createService", PARAMNOTVALID);
        } else {
            result = serviceOrderImpl.createServiceOrder(request);
        }

//        } catch (Exception e){
//            // Misma excepción de arriba
//        }

        if (result == null || result.getId() == null) {
            throw new NotFoundException();
        }
        return result;
    }

    @Override
    public List<ServiceOrder> listServiceOrder(@RequestParam(required = false, value = "fields") String fields,
                                               @RequestParam(required = false) MultiValueMap<String, String> filters)
            throws ParseException, IOException, ApplicationException {
        for (String key : filters.keySet()) {
            System.out.println("Key: " + key + " Value: " + filters.get(key));
        }

        return serviceOrderImpl.listServiceOrder(fields, filters);
    }

    public ServiceOrder retrieveServiceOrder(@PathVariable String id, @RequestParam(required = false, value = "states") String states) throws IOException, ApplicationException {
        return serviceOrderImpl.retrieveServiceOrder(id);
    }

    @Override
    public ServiceOrder patchServiceOrder(@PathVariable String id,
                                          @RequestBody ServiceOrder request) throws ApplicationException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, BusinessException {
        return serviceOrderImpl.patchServiceOrder(id, request);
    }

    @Override
    public String deleteServiceOrder(@PathVariable String id) throws IOException, ApplicationException {
        return serviceOrderImpl.deleteServiceOrder(id);
    }

    @Override
    public ServiceOrder modifyDateServiceOrder(@RequestBody ModificationRequest request) {
        return serviceOrderImpl.modifyDateServiceOrder(request);
    }

    @Override
    public ServiceOrder cancelServiceOrder(@RequestBody CancellationRequest request) {
        return serviceOrderImpl.cancelServiceOrder(request);
    }

    @Override
    public FlowExecutionRef modifyFlowExecutionRef(@PathVariable String id,
                                                   @RequestBody FlowExecutionRef request) throws ApplicationException {
        return serviceOrderImpl.modifyFlowExecutionRef(id, request);
    }

    /*
    @PostMapping(value = "/resourceUpdate")
    @ApiOperation(
            value = "update",
            notes = "This operation Update  a resource"
    )
    public ServiceOrder updateResource( @RequestBody UpdateResourceServiceOrder request) {
        return serviceOrderImpl.updateResource(request);
    }
     */

    @Override
    public FlowExecutionRef modifyDateFlowExecutionRef(@PathVariable String id,
                                                       @RequestParam MultiValueMap<String, String> fields) {
        return serviceOrderImpl.modifyDateFlowExecutionRef(id, fields);
    }
}
