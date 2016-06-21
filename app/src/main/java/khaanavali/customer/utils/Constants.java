package khaanavali.customer.utils;


public class Constants {
    public static final String RELEASE_URL = "http://oota.herokuapp.com";
    public static final String DEBUG_URL = "http://kuruva.herokuapp.com";
    public static final String MAIN_URL = DEBUG_URL;
    public static final String ORDER_URL = MAIN_URL + "/v1/vendor/order";
    public static final String GET_HOTEL_BY_DELIVERY_AREAS =  MAIN_URL +"/v1/vendor/delieveryareas?areaName=";
    public static final String GET_COVERAGE_AREAS = MAIN_URL +"/v1/admin/coverageArea";
    public static final String GET_STATUS_URL= MAIN_URL + "/v1/vendor/order_by_id/";
    public static final String FIREBASE_APP = "https://project-8598805513533999178.firebaseio.com";
    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";
}
