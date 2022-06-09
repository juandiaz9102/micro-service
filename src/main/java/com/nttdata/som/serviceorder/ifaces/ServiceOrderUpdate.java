package com.nttdata.som.serviceorder.ifaces;

import com.nttdata.exceptions.ApplicationException;
import com.nttdata.exceptions.BusinessException;
import com.nttdata.model.tmf.ServiceOrder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface ServiceOrderUpdate {
    void updateExternalReference(StringBuilder sb, ServiceOrder serviceOrder, String nameId);
    void updateNote(StringBuilder sb, ServiceOrder serviceOrder, String nameId);
    void updateServiceOrderRelationship(StringBuilder sb, ServiceOrder serviceOrder, String nameId);
    void updatePartyList(StringBuilder sb, ServiceOrder serviceOrder, String nameId);
    void updateServiceOrderItem(StringBuilder sb, ServiceOrder serviceOrder, String nameId) throws ApplicationException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, BusinessException;
    void updateCancelRequest(StringBuilder sb, ServiceOrder serviceOrder, String nameId);
    void updateModificationRequest(StringBuilder sb, ServiceOrder serviceOrder, String nameId);
    void updateFlowExecutionRef(StringBuilder sb, ServiceOrder serviceOrder, String nameId);

    String strPatch(ServiceOrder service, String nameId);
}
