package materialapp.survivingwithandroid.com.materialapp.model;

/**
 * Created by dganeshappa on 5/19/2016.
 */
public class Menu {
    String name;
    int  no_of_order;
    int  price;
    public Menu()
    {
        name = new String();
        no_of_order = 0;
        price  = 0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo_of_order() {
        return no_of_order;
    }

    public void setNo_of_order(int no_of_order) {
        this.no_of_order = no_of_order;
    }
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
