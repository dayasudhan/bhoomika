package khaanavali.customer.utils;

/**
 * Created by dganeshappa on 6/8/2016.
 */
public class Constants {
    public static final String RELEASE_URL = "http://oota.herokuapp.com";
    public static final String DEBUG_URL = "http://kuruva.herokuapp.com";
    public static final String MAIN_URL = DEBUG_URL;
    public static final String ORDER_URL = MAIN_URL + "/v1/vendor/order";
    public static final String GET_HOTEL_BY_DELIVERY_AREAS =  MAIN_URL +"/v1/vendor/delieveryareas?areaName=";
    public static final String GET_COVERAGE_AREAS = MAIN_URL +"/v1/admin/coverageArea";
    public static final String GET_STATUS_URL= MAIN_URL + "/v1/vendor/order_by_id/";
}
