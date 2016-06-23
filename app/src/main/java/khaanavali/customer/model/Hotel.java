package khaanavali.customer.model;

/**
 * Created by dganeshappa on 5/18/2016.
 */
public class Hotel {
    public Hotel()
    {
        name = new String();
        email = new String();
        id = new String();
        phone = new Integer(0);
        //deliveryCharges = 0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }
    String name;
    String email;
    Integer phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
//    public int getDeliveryCharges() {
//        return deliveryCharges;
//    }
//
//    public void setDeliveryCharges(int deliveryCharges) {
//        this.deliveryCharges = deliveryCharges;
//    }
//
//    int deliveryCharges;
}
