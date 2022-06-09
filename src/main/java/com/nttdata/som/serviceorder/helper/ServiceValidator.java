package com.nttdata.som.serviceorder.helper;

import com.nttdata.model.tmf.ServiceOrder;
import com.nttdata.model.tmf638.Service;

public class ServiceValidator {

    private ServiceValidator(){
    }
    public static boolean validatePostRequest(ServiceOrder request){
        boolean correct = true;
        /*state
        if (request.getState() == null){
            correct = false;
        }

        //serviceSpecification
        if (request.getServiceSpecification() != null && request.getServiceSpecification().getId() == null){
            correct = false;
        }

        // Me parece mejor así en un solo if las dos validaciones
        if (request.getState() == null || (request.getServiceSpecification() != null && request.getServiceSpecification().getId() == null)){
         }*/
        return correct;
    }

    public static boolean validateRetrieveRequest (long id){
        boolean correct = false;
        if (id != 0L){
            correct =  true;
        }
        return correct;
    }
}
