package com.nttdata.som.serviceorder.service.impl;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.model.dgraph.*;
import com.nttdata.model.tmf.*;
import com.nttdata.som.serviceorder.ifaces.ServiceOrderUpdate;
import com.nttdata.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Service
@Slf4j
public class ServiceOrderUpdateImpl implements ServiceOrderUpdate {

    private PatchMutationRequest patchMutationRequest = new PatchMutationRequest();
    @Autowired
    ServiceUpdate serviceUpdate = new ServiceUpdate();

    @Override
    public void updateExternalReference(StringBuilder sb, ServiceOrder serviceOrder, String nameId) {
        if (serviceOrder.getExternalReference() != null) {
            for (ExternalReference externalReference :
                    serviceOrder.getExternalReference()) {
                if (externalReference.getId() != null) {
                    String idExternalReference = externalReference.getId();
                    externalReference.setId(null);
                    sb.append(patchMutationRequest.genericUpdate(externalReference, true, nameId, idExternalReference, "externalReference", "updateExternalReference", new ExternalReferenceResponseProjection().id().name().href().externalReferenceType()));
                }
            }
        }
    }

    @Override
    public void updateNote(StringBuilder sb, ServiceOrder serviceOrder, String nameId) {
        if (serviceOrder.getNote() != null) {
            sb.append(" ");
            for (Note note : serviceOrder.getNote()
            ) {
                if (note.getId() != null) {
                    String noteId = note.getId();
                    note.setId(null);
                    sb.append(patchMutationRequest.genericUpdate(note, false, nameId, noteId, "note", "updateNote", new NoteResponseProjection().id().author().date().text()));
                }
            }
        }
    }

    @Override
    public void updateServiceOrderRelationship(StringBuilder sb, ServiceOrder serviceOrder, String nameId) {
        if (serviceOrder.getOrderRelationship() != null) {
            sb.append(" ");
            for (ServiceOrderRelationship serviceOrderRelationship : serviceOrder.getOrderRelationship()
            ) {
                if (serviceOrderRelationship.getId() != null) {
                    String idServiceOrderRelationship = serviceOrderRelationship.getId();
                    serviceOrderRelationship.setId(null);
                    sb.append(patchMutationRequest.genericUpdate(serviceOrderRelationship, true, nameId, idServiceOrderRelationship, "serviceOrderRelationship", "updateServiceOrderRelationship", new ServiceOrderRelationshipResponseProjection().id().href().relationshipType()));
                }

            }
        }
    }

    @Override
    public void updatePartyList(StringBuilder sb, ServiceOrder serviceOrder, String nameId) {
        if (serviceOrder.getRelatedParty() != null) {
            sb.append(" ");
            for (Party party : serviceOrder.getRelatedParty()) {
                String idObject = party.getId();
                party.setId(null);
                sb.append(patchMutationRequest.genericUpdate(party, true, nameId, idObject, "party", "updateParty", new PartyResponseProjection().id()));
            }
        }
    }

    @Override
    public void updateServiceOrderItem(StringBuilder sb, ServiceOrder serviceOrder, String nameId) throws ApplicationException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, BusinessException {
        if (serviceOrder.getServiceOrderItem() != null) {
            sb.append(" ");
            for (ServiceOrderItem serviceOrderItem : serviceOrder.getServiceOrderItem()
            ) {
                if (serviceOrderItem.getServiceOrderItemId() != null) {
                    String serviceOrderItemId = serviceOrderItem.getServiceOrderItemId();
                    serviceOrderItem.setServiceOrderItemId(null);
                    sb.append(patchMutationRequest.genericUpdate(serviceOrderItem, false, "serviceOrderItemId", serviceOrderItemId, "serviceOrderItem", "updateServiceOrderItem", new ServiceOrderItemResponseProjection().serviceOrderItemId().id().action().type().state()));
                    if (serviceOrderItem.getService() != null && serviceOrderItem.getService().getId() != null) {
                        String updateServiceSom = serviceUpdate.patchService(serviceOrderItem.getService().getId(), serviceOrderItem.getService());
                        sb.append(updateServiceSom);
                    }
                }
            }
        }
    }

    @Override
    public void updateCancelRequest(StringBuilder sb, ServiceOrder serviceOrder, String nameId) {
        if (serviceOrder.getCancelRequest() != null && serviceOrder.getCancelRequest().getId() != null) {
            sb.append(" ");
            String cancelRequestId = serviceOrder.getCancelRequest().getId();
            serviceOrder.getCancelRequest().setId(null);
            sb.append(patchMutationRequest.genericUpdate(serviceOrder.getCancelRequest(), false, nameId, cancelRequestId, "cancellationRequest", "updateCancellationRequest", new CancellationRequestResponseProjection().id().externalId().href().cancellationReason().state()));
        }
    }

    @Override
    public void updateModificationRequest(StringBuilder sb, ServiceOrder serviceOrder, String nameId) {
        if (serviceOrder.getModifyRequest() != null) {
            sb.append(" ");
            for (ModificationRequest modificationRequest : serviceOrder.getModifyRequest()
            ) {
                if (modificationRequest.getId() != null) {
                    String modificationRequestId = modificationRequest.getId();
                    modificationRequest.setId(null);
                    sb.append(patchMutationRequest.genericUpdate(modificationRequest, false, nameId, modificationRequestId, "modificationRequest", "updateModificationRequest", new ModificationRequestResponseProjection().id().externalId()));
                }
            }
        }
    }

    @Override
    public void updateFlowExecutionRef(StringBuilder sb, ServiceOrder serviceOrder, String nameId) {
        if (serviceOrder.getFlowExecutionRef() != null && serviceOrder.getFlowExecutionRef().getId() != null) {
            sb.append(" ");
            String idFlowExecutionRef = serviceOrder.getFlowExecutionRef().getId();
            serviceOrder.getFlowExecutionRef().setId(null);
            sb.append(patchMutationRequest.genericUpdate(serviceOrder.getFlowExecutionRef(), false, nameId, idFlowExecutionRef, "flowExecutionRef", "updateFlowExecutionRef", new FlowExecutionRefResponseProjection().id().domainId()));
        }
    }

    @Override
    public String strPatch(ServiceOrder service, String nameId) {
        String strResponse = null;
        try {
            FilterRequest filterTO = new FilterRequest();
            filterTO.setId(service.getId());
            service.setId(null);
            filterTO.setRequest(service.toString());
            filterTO.setNameId(nameId);
            ServiceMutationRequest patchRequest = new ServiceMutationRequest();
            patchRequest.setOperationName("updateServiceOrder");
            patchRequest.setAlias("serviceOrder");
            patchRequest.setInput(filterTO);
            ServiceOrderResponseProjectionWrapper serviceOrderResponseProjectionWrapper = new ServiceOrderResponseProjectionWrapper().serviceOrder("serviceOrder", new ServiceOrderResponseProjection().id());
            GraphQLRequest graphQLRequest = new GraphQLRequest(patchRequest, serviceOrderResponseProjectionWrapper);
            strResponse = Utils.buildQuery(graphQLRequest);
        } catch (Exception e) {
            log.error("Response Error ", e.getMessage());
        }
        return strResponse;
    }
}
