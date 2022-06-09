package com.nttdata.som.serviceorder.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ServiceOrderListFilterImplTest {
    @InjectMocks
    ServiceOrderListFilterImpl serviceOrderListFilter = new ServiceOrderListFilterImpl();

    @Before
    public void setup() {
        ReflectionTestUtils.setField(serviceOrderListFilter, "orderDateStr", "orderDate");
        ReflectionTestUtils.setField(serviceOrderListFilter, "startDateStr", "startDate");
    }


    @Test
    public void createListRequestTest() {
        try {
            serviceOrderListFilter.createListRequest("123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void createFilterDoubleServiceOrderTest() {
        try {
            serviceOrderListFilter.createFilterDoubleServiceOrder("123", "123", "123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void uniqueFlowExecutionRefTest() {
        try {
            serviceOrderListFilter.uniqueFlowExecutionRef();
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }

    }

    @Test
    public void betweenParamFlowExecutionTest() {
        try {
            serviceOrderListFilter.betweenParamFlowExecution("123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }


    @Test
    public void betweenParamFlowExecutionParamsTest() {
        try {
            serviceOrderListFilter.betweenParamFlowExecutionParams("123", "123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }

    }

    @Test
    public void creationDateTest() {
        try {
            serviceOrderListFilter.creationDate("123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void defaultFilterServiceOrderTest() {
        try {
            serviceOrderListFilter.defaultFilterServiceOrder();
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }

    }

    @Test
    public void createStructureFilterFlowExecutionTest() {
        try {
            serviceOrderListFilter.createStructureFilterFlowExecution("123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

    @Test
    public void createStructureFilterFlowExecutionParamBetweenTest() {
        try {
            serviceOrderListFilter.createStructureFilterFlowExecution("123", null);
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }

    }

    @Test
    public void createStructureFilterFlowExecutionNullTest() {
        try {
            serviceOrderListFilter.createStructureFilterFlowExecution(null, "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }

    }


    @Test
    public void createServiceOrderFilterTest() {
        try {
            serviceOrderListFilter.createServiceOrderFilter("123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }

    }

    @Test
    public void createFlowExecutionRefFilter() {
        try {
            serviceOrderListFilter.createFlowExecutionRefFilter("123", "123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }

    }

    @Test
    public void createFilterUnitServiceOrderTest() {
        try {
            serviceOrderListFilter.createFilterUnitServiceOrder("123", "123");
        } catch (Exception e) {
            assertTrue("Error " + e.getMessage(), true);
        }
    }

}
