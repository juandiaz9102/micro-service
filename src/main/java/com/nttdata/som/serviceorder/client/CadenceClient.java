package com.nttdata.som.serviceorder.client;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.config.CadenceProperties;
import com.nttdata.model.tmf.ServiceOrder;
import com.uber.cadence.WorkflowExecution;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.client.WorkflowStub;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CadenceClient {


	@Autowired
	private CadenceProperties config;


	public void loadFlowCall(ServiceOrder serviceOrder, String operation) {
		WorkflowClient workflowClient = WorkflowClient.newInstance(config.getHost(), config.getPort(), config.getDomain());
		WorkflowStub catalogDriverClient = workflowClient.newUntypedWorkflowStub("LoadFlow::loadFlow",
				new WorkflowOptions.Builder()
						.setExecutionStartToCloseTimeout(Duration.ofDays(config.getWorkflowTimeout()))
						.setTaskList(config.getTaskList())
						.build());

		Map<String, String> cadenceMap = new HashMap<>();
		cadenceMap.put("id", serviceOrder.getId());
		cadenceMap.put("Category", serviceOrder.getCategory());
		cadenceMap.put("Operation", operation);


		//TODO: not hardcode params

//		cadenceMap.put("Operation", "ServiceOrder");
//		
//		//TODO: Revisar de donde salen estos parametros
//		cadenceMap.put("CFS", "CFS: CPE;CFS: Access");
//		cadenceMap.put("Action", "add;add");
//		cadenceMap.put("BaseFlow", "1234");
//		cadenceMap.put("serviceOrderItem.size","2");
		WorkflowExecution execution =  catalogDriverClient.start(cadenceMap);// Synchronous start

		log.info("Cadence Workflow started, workflowId: {} , runId: {}", execution.getRunId(), execution.getRunId());
	}

}
