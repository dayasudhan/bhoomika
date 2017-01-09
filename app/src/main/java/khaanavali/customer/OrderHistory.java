package khaanavali.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

import java.util.ArrayList;

import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.model.Menu;
import khaanavali.customer.model.Order;
import khaanavali.customer.utils.SessionManager;

public class OrderHistory extends AppCompatActivity {

    Order order;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_order_history);
        Intent intent = getIntent();

        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
       // HotelDetail hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);
        TextView txtViewName = (TextView) findViewById(R.id.vendor_name_value);
       // TextView txtViewPhone = (TextView) findViewById(R.id.vendor_contact_value);
        TextView txtViewAddress = (TextView) findViewById(R.id.address_value);
        TextView txtViewMenu = (TextView) findViewById(R.id.items_value);
        TextView txtViewOrderId = (TextView) findViewById(R.id.order_id_value);
        TextView txtViewBillValue = (TextView) findViewById(R.id.bill_value_value);
        TextView txtViewdeliveryTime = (TextView) findViewById(R.id.vendor_delivery_time_value);
      // TextView txtVieworderTime = (TextView) findViewById(R.id.order_time_value);

        txtViewName.setText(order.getHotel().getName());
        //txtViewPhone.setText(String.valueOf(order.getHotel().getPhone()));
        txtViewOrderId.setText(order.getId());
     //   txtViewdeliveryTime.setText(Integer.toString(hotelDetail.getDeliveryTime()) + " mins");
        txtViewBillValue.setText(String.valueOf(order.getTotalCost()));
        ArrayList<Menu> items = order.getMenuItems();
        String MenuItemStr = "";
        for(int j = 0 ; j < items.size() ; j++)
        {
            MenuItemStr += items.get(j).getName() + " (" + items.get(j).getNo_of_order() + ")" + '\n';
        }
        txtViewMenu.setText(MenuItemStr);
        String CustomerAddress = order.getCustomer().getAddress().toString();
        txtViewAddress.setText(CustomerAddress);
        setToolBar();
    }




    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
      //  ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
      //  ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Order Summary");
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    public void onBackPressed() {
//
//        Intent start = new Intent(OrderHistory.this,MainActivity.class);
//        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(start);
//        finish();
//    }
}
