package materialapp.survivingwithandroid.com.materialapp.model;

/**
 * Created by dganeshappa on 5/18/2016.
 */
public class Address {
    String addressLine1;
    String addressLine2;
    String street;
    String LandMark;
    String areaName;
    String city;
    String zip;
    String latitude;
    String longitude;

    public Address(){
        addressLine1 = new String();
        addressLine1 = new String();
        addressLine2 = new String();
        street = new String();
        LandMark = new String();
        areaName = new String();
        city = new String();
        zip = new String();
        latitude = new String();
        longitude = new String();
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandMark() {
        return LandMark;
    }

    public void setLandMark(String landMark) {
        LandMark = landMark;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
