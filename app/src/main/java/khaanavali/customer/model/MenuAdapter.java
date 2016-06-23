package khaanavali.customer.model;

/**
 * Created by dganeshappa on 5/19/2016.
 */
public class MenuAdapter {


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    String name;
    int no_of_order;
    int price;
    String id;
    boolean available;
    public MenuAdapter()
    {
        no_of_order = 0;
        price = 0;
        name = new String();
        id = new String();
        available = true;
    }
    public  MenuAdapter(MenuItem item)
    {
        name = item.getName();
        id = item.getId();
        price = item.getPrice();
       // available = item.isAvailable();
        no_of_order = 0;
    }
    public  MenuAdapter(Menu item)
    {
        name = item.getName();
        no_of_order = item.getNo_of_order();
        price =item.getPrice();
        available = true;
    }
    public Menu getMenuOrder()
    {
        Menu item = new Menu();
        item.no_of_order = this.no_of_order;
        item.name = this.name;
        item.price = this.price;
        return item;
    }
}
