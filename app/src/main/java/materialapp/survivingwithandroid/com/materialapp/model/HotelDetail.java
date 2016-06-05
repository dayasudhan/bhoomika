package materialapp.survivingwithandroid.com.materialapp.model;

import java.util.ArrayList;

/**
 * Created by dganeshappa on 5/18/2016.
 */
public class HotelDetail {


    Integer phone;
    int deliveryRange;
    String id;
    ArrayList<String> deliveryAreas;
    Address address;
    ArrayList<MenuItem> menuItem;
    Hotel hotel;
    String speciality;
    int rating;
    int delivery_time;

    public HotelDetail()
    {
        hotel = new Hotel();

        id = new String();

        address = new Address();
        menuItem = new ArrayList<MenuItem>();
        speciality = new String();
        deliveryAreas = new ArrayList<String>();
        deliveryRange = 3;
        phone = new Integer(0);
        rating = 5;
        delivery_time = 60;
    }

    public int getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public ArrayList<MenuItem> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(ArrayList<MenuItem> menuItem) {
        this.menuItem = menuItem;
    }
    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }
    public ArrayList<String> getDeliveryAreas() {
        return deliveryAreas;
    }

    public void setDeliveryAreas(ArrayList<String> deliveryAreas) {
        this.deliveryAreas = deliveryAreas;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getDeliveryRange() {
        return deliveryRange;
    }

    public void setDeliveryRange(int deliveryRange) {
        this.deliveryRange = deliveryRange;
    }

}
