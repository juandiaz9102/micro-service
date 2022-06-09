package com.nttdata.som.serviceorder.service.impl;

import com.nttdata.model.tmf.ExternalReference;
import com.nttdata.model.tmf.Note;
import com.nttdata.model.tmf.ServiceOrder;
import com.nttdata.model.tmf.ServiceOrderRelationship;
import com.nttdata.som.serviceorder.ifaces.ServiceOrderMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderMapperImpl implements ServiceOrderMapper {
    @Override
    public ServiceOrder mapServiceOrder(String id, ServiceOrder request) {
        ServiceOrder serviceOrder = mainAttributes(id, request);
        if (request.getExternalReference() != null && !request.getExternalReference().isEmpty()) {
            serviceOrder.setExternalReference(getExternalReference(request.getExternalReference()));
        }
        if (request.getNote() != null && !request.getNote().isEmpty()) {
            serviceOrder.setNote(getNote(request.getNote()));
        }
        if (request.getOrderRelationship() != null && !request.getOrderRelationship().isEmpty()) {
            serviceOrder.setOrderRelationship(getServiceOrderRelationShip(request.getOrderRelationship()));
        }

        if (request.getRelatedParty() != null && !request.getRelatedParty().isEmpty()) {
            serviceOrder.setRelatedParty(request.getRelatedParty());
        }

        if (request.getServiceOrderItem() != null && !request.getServiceOrderItem().isEmpty()) {
            serviceOrder.setServiceOrderItem(request.getServiceOrderItem());
        }
        if (request.getCancelRequest() != null) {
            serviceOrder.setCancelRequest(request.getCancelRequest());
        }
        if (request.getModifyRequest() != null && !request.getModifyRequest().isEmpty()) {
            serviceOrder.setModifyRequest(request.getModifyRequest());
        }
        if (request.getUpdateRequest() != null && !request.getUpdateRequest().isEmpty()) {
            serviceOrder.setUpdateRequest(request.getUpdateRequest());
        }
        if (request.getState() != null) {
            serviceOrder.setState(request.getState());
        }

        if (request.getFlowExecutionRef() != null) {
            serviceOrder.setFlowExecutionRef(request.getFlowExecutionRef());
        }

        return serviceOrder;
    }

    private ServiceOrder mainAttributes(String id, ServiceOrder request) {
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(id);
        if (validateParameter(request.getHref())) {
            serviceOrder.setHref(request.getHref());
        }
        if (request.getCancellationDate() != null) {
            serviceOrder.setCancellationDate(request.getCancellationDate());
        }
        if (validateParameter(request.getCancellationReason())) {
            serviceOrder.setCancellationReason(request.getCancellationReason());
        }
        if (validateParameter(request.getCategory())) {
            serviceOrder.setCategory(request.getCategory());
        }
        if (request.getCompletionDate() != null) {
            serviceOrder.setCompletionDate(request.getCompletionDate());
        }
        if (validateParameter(request.getDescription())) {
            serviceOrder.setDescription(request.getDescription());
        }
        if (request.getExpectedCompletionDate() != null) {
            serviceOrder.setExpectedCompletionDate(request.getExpectedCompletionDate());
        }
        if (validateParameter(request.getExternalId())) {
            serviceOrder.setExternalId(request.getExternalId());
        }
        if (validateParameter((request.getNotificationContact()))) {
            serviceOrder.setNotificationContact(request.getNotificationContact());
        }
        if (request.getOrderDate() != null) {
            serviceOrder.setOrderDate(request.getOrderDate());
        }
        if (validateParameter(request.getPriority())) {
            serviceOrder.setPriority(request.getPriority());
        }
        if (request.getRequestedCompletionDate() != null) {
            serviceOrder.setRequestedCompletionDate(request.getRequestedCompletionDate());
        }
        if (request.getRequestedStartDate() != null) {
            serviceOrder.setRequestedStartDate(request.getRequestedStartDate());
        }
        if (request.getStartDate() != null) {
            serviceOrder.setStartDate(request.getStartDate());
        }
        return serviceOrder;
    }

    private List<ExternalReference> getExternalReference(List<ExternalReference> externalReferenceRequest) {
        List<ExternalReference> externalReferenceResponse = new ArrayList<>();
        for (ExternalReference reference : externalReferenceRequest) {
            ExternalReference externalReference = new ExternalReference();
            if (validateParameter(reference.getId())) {
                externalReference.setId(reference.getId());
            }
            if (validateParameter(reference.getName())) {
                externalReference.setName(reference.getName());
            }
            if (validateParameter(reference.getHref())) {
                externalReference.setHref(reference.getHref());
            }
            if (validateParameter(reference.getExternalReferenceType())) {
                externalReference.setExternalReferenceType(reference.getExternalReferenceType());
            }
            if (validateParameter(reference.getSchemaLocation())) {
                externalReference.setSchemaLocation(reference.getSchemaLocation());
            }
            if (validateParameter(reference.getBaseType())) {
                externalReference.setBaseType(reference.getBaseType());
            }
            if (validateParameter(reference.getType())) {
                externalReference.setType(reference.getType());
            }
            externalReferenceResponse.add(externalReference);
        }
        return externalReferenceResponse;
    }

    private List<Note> getNote(List<Note> noteListRequest) {
        List<Note> noteListResponse = new ArrayList<>();
        for (Note noteRequest : noteListRequest) {
            Note note = new Note();
            if (validateParameter(noteRequest.getId())) {
                note.setId(noteRequest.getId());
            }
            if (validateParameter(noteRequest.getAuthor())) {
                note.setAuthor(noteRequest.getAuthor());
            }
            if (noteRequest.getDate() != null) {
                note.setDate(noteRequest.getDate());
            }
            if (validateParameter(noteRequest.getText())) {
                note.setText(noteRequest.getText());
            }
            noteListResponse.add(note);
        }
        return noteListResponse;
    }

    private List<ServiceOrderRelationship> getServiceOrderRelationShip(List<ServiceOrderRelationship> serviceOrderRelationshipRequest) {
        List<ServiceOrderRelationship> serviceOrderRelationshipResponse = new ArrayList<>();
        for (ServiceOrderRelationship serviceOrderRelationShipList :
                serviceOrderRelationshipRequest) {
            ServiceOrderRelationship serviceOrderRelationship = new ServiceOrderRelationship();
            if (validateParameter(serviceOrderRelationShipList.getId())) {
                serviceOrderRelationship.setId(serviceOrderRelationShipList.getId());
            }
            if (validateParameter(serviceOrderRelationShipList.getHref())) {
                serviceOrderRelationship.setHref(serviceOrderRelationShipList.getHref());
            }
            if (validateParameter(serviceOrderRelationShipList.getRelationshipType())) {
                serviceOrderRelationship.setRelationshipType(serviceOrderRelationShipList.getRelationshipType());
            }
            serviceOrderRelationshipResponse.add(serviceOrderRelationship);
        }
        return serviceOrderRelationshipResponse;
    }


    private boolean validateParameter(String parameter) {
        return parameter != null && !parameter.isEmpty() && !parameter.equalsIgnoreCase("null");
    }
}
