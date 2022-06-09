package com.nttdata.som.serviceorder.api;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.websocket.server.PathParam;

import com.nttdata.exceptions.BusinessException;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;

import com.nttdata.exceptions.ApplicationException;
import com.nttdata.model.dgraph.ServiceOrderDTO;
import com.nttdata.model.tmf.CancellationRequest;
import com.nttdata.model.tmf.FlowExecutionRef;
import com.nttdata.model.tmf.ModificationRequest;
import com.nttdata.model.tmf.ServiceOrder;

@RequestMapping("/tmf-api/serviceOrdering/v4")
public interface ServiceOrderApiOperations {


    @PostMapping(value = "/serviceOrder")
    @ApiOperation(
            value = "Create Service Order",
            notes = "This operation creates a service order entity"
    )
    public ServiceOrder createServiceOrder(@RequestBody ServiceOrder request) throws IOException, ApplicationException, BusinessException;


    @GetMapping(value = "/serviceOrder")
    @ApiOperation(
            value = "List Service Order",
            notes = "This operation list service order entities"
    )
    public List<ServiceOrder> listServiceOrder(@RequestParam(required = false, value = "fields") String fields,
                                               @RequestParam(required = false) MultiValueMap<String, String> filters) throws ParseException, IOException, ApplicationException;

    @GetMapping(value = "/serviceOrder/{id}")
    @ApiOperation(
            value = "Retrieve Service Order",
            notes = "This operation retrieves a service order entity"
    )
    public ServiceOrder retrieveServiceOrder(@PathParam(value = "id") String request, @RequestParam(required = false, value = "states") String states) throws IOException, ApplicationException;


    @PatchMapping(value = "/serviceOrder/{id}")
    @ApiOperation(
            value = "Patch Service Order",
            notes = "This operation allows partial updates of a service order entity"
    )
    public ServiceOrder patchServiceOrder(@PathVariable String id,
                                          @RequestBody ServiceOrder request) throws ApplicationException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, BusinessException;

    @DeleteMapping(value = "/serviceOrder/{id}")
    @ApiOperation(
            value = "Delete Service Order",
            notes = "This operation deletes a service order entity"
    )
    public String deleteServiceOrder(@PathVariable String id) throws IOException, ApplicationException;


    @PostMapping(value = "/serviceOrder/date")
    @ApiOperation(
            value = "Modify Date Service Order",
            notes = "This operation modifies the date of a service order entity"
    )
    public ServiceOrder modifyDateServiceOrder(@RequestBody ModificationRequest request);


    @PostMapping(value = "/serviceOrder/cancel")
    @ApiOperation(
            value = "Cancel Service Order",
            notes = "This operation allows the cancellation of a Service Order"
    )
    public ServiceOrder cancelServiceOrder(@RequestBody CancellationRequest request);

    @PostMapping(value = "/flowExecutionRef/modify/{id}")
    @ApiOperation(
            value = "Modify Flow Execution Ref",
            notes = "This operation modifies a Flow Execution Reference"
    )
    public FlowExecutionRef modifyFlowExecutionRef(@PathVariable String id,
                                                   @RequestBody FlowExecutionRef request) throws ApplicationException;

    @PostMapping(value = "/flowExecutionRef/modifyDate/{id}")
    @ApiOperation(
            value = "Modify Date Flow Execution Ref",
            notes = "This operation modifies a FlowExecutionRef's dates"
    )
    public FlowExecutionRef modifyDateFlowExecutionRef(@PathVariable String id,
                                                       @RequestParam MultiValueMap<String, String> fields);

}
