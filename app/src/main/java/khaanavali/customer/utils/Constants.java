package khaanavali.customer.utils;


public class Constants {

    public static final String LOCALHOST = "http://10.239.54.38:3000";
    public static final String RELEASE_URL = "http://oota.herokuapp.com";
    public static final String DEBUG_URL = "http://kuruva.herokuapp.com";
    public static final String MAIN_URL = DEBUG_URL;
    public static final String ORDER_URL = MAIN_URL + "/v1/vendor/order";
    public static final String GET_HOTEL_BY_DELIVERY_AREAS =  MAIN_URL +"/v1/vendor/delieveryareas?areaName=";
    public static final String GET_COVERAGE_AREAS = MAIN_URL +"/v1/admin/coverageArea";
    public static final String GET_STATUS_URL= MAIN_URL + "/v1/vendor/order_by_id/";
    public static final String REVIEW_URL = MAIN_URL + "/v1/vendor/review/";
    public static final String OTP_REGISTER_URL = "http://kuruva.herokuapp.com/v1/vendor/otp/register";
    public static final String OTP_CONFIRM_URL = "http://kuruva.herokuapp.com/v1/vendor/otp/confirm";
    public static final String FIREBASE_APP = "https://project-8598805513533999178.firebaseio.com";
    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";
    public static final String INVITE_TEXT = "Karnataka's local food speciality at your door step\n" +
            " \n" +
            " Joladha Rotti, Akki rotti, Jawari rotti , Benne dose and Native Kannada food\n" +
            " \n" +
            " To order online visit: http://Khaanavali.com\n" +
            "\t\n" +
            " Download Android App: https://play.google.com/store/apps/details?id=khaanavali.customer";
    public static final String INVITE_SUBJECT = "Khaanavali ( ಖಾನಾವಳಿ) the real taste of Karnataka";

}
