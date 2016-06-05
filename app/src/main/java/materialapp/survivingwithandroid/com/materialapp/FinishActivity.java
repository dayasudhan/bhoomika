package materialapp.survivingwithandroid.com.materialapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import materialapp.survivingwithandroid.com.materialapp.model.Menu;
import materialapp.survivingwithandroid.com.materialapp.model.Order;

import java.util.ArrayList;

public class FinishActivity extends AppCompatActivity {

    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Intent intent = getIntent();
        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);

        TextView txtViewName = (TextView) findViewById(R.id.vendor_name_value);
        TextView txtViewPhone = (TextView) findViewById(R.id.vendor_contact_value);
        TextView txtViewAddress = (TextView) findViewById(R.id.address_value);
        TextView txtViewMenu = (TextView) findViewById(R.id.items_value);
        TextView txtViewOrderId = (TextView) findViewById(R.id.order_id_value);
        TextView txtViewBillValue = (TextView) findViewById(R.id.bill_value_value);
      // TextView txtVieworderTime = (TextView) findViewById(R.id.order_time_value);

        txtViewName.setText(order.getHotel().getName());
        txtViewPhone.setText(String.valueOf(order.getHotel().getPhone()));
        txtViewOrderId.setText(order.getId());

        txtViewBillValue.setText(String.valueOf(order.getBill_value()));
        ArrayList<Menu> items = order.getMenuItems();
        String MenuItemStr = "";
        for(int j = 0 ; j < items.size() ; j++)
        {
            MenuItemStr += items.get(j).getName() + " (" + items.get(j).getNo_of_order() + ")" + '\n';
        }
        txtViewMenu.setText(MenuItemStr);
        String CustomerAddress = new String();
        if(!order.getCustomer().getAddress().getAddressLine1().isEmpty())
            CustomerAddress =  CustomerAddress.concat(order.getCustomer().getAddress().getAddressLine1()).concat("\n");
        if(!order.getCustomer().getAddress().getAddressLine2().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getAddressLine2()).concat("\n");
        if(!order.getCustomer().getAddress().getAreaName().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getAreaName()).concat("\n");
        if(!order.getCustomer().getAddress().getLandMark().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getLandMark().concat("\n"));
        if(!order.getCustomer().getAddress().getStreet().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getStreet()).concat("\n");
        if(!order.getCustomer().getAddress().getCity().isEmpty())
            CustomerAddress =   CustomerAddress.concat(order.getCustomer().getAddress().getCity());

        txtViewAddress.setText(CustomerAddress);
    }
    public void onBackPressed() {

        Intent start = new Intent(FinishActivity.this,MainActivity.class);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(start);
        finish();
    }
}
