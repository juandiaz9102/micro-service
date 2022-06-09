package com.nttdata.som.serviceorder.ifaces;

public interface ServiceOrderListFilter {

    String createListRequest(String serviceOrderFilter, String serviceFlowExecutionFilter);
    String createFilterDoubleServiceOrder(String param1, String param2, String valueParam1, String valueParam2);
    String createFilterUnitServiceOrder(String param, String valueParam);
    String uniqueFlowExecutionRef();
    String betweenParamFlowExecution(String param, String valueParam);
    String betweenParamFlowExecutionParams(String param1, String valueParam1, String valueParam2);
    String creationDate(String valueParam);
    String defaultFilterServiceOrder();
    String createStructureFilterFlowExecution(String paramCreateDate, String paramBetween);
    String createServiceOrderFilter(String sOrderDate, String sStartDate);
    String createFlowExecutionRefFilter(String sCreationDate, String sExecutionDate, String sEndExecutionDate);
}
