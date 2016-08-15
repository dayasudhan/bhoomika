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

public class FinishActivity extends AppCompatActivity {

    Order order;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_finish);
        Intent intent = getIntent();

        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        HotelDetail hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);
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
        txtViewdeliveryTime.setText(Integer.toString(hotelDetail.getDeliveryTime()) + " mins");
        txtViewBillValue.setText(String.valueOf(order.getTotalCost()));
        ArrayList<Menu> items = order.getMenuItems();
        String MenuItemStr = "";
        for(int j = 0 ; j < items.size() ; j++)
        {
            MenuItemStr += items.get(j).getName() + " (" + items.get(j).getNo_of_order() + ")" + '\n';
        }
        txtViewMenu.setText(MenuItemStr);
        String CustomerAddress = order.getCustomer().getAddress().toString();
        session = new SessionManager(getApplicationContext());
        session.setCurrentOrderId(order.getId());
//        if(!session.isLoggedIn())
//            alertMessage();
//        else if(session.isLoggedIn())
//        {
//            saveAddress();
//        }
        saveAddress();
        txtViewAddress.setText(CustomerAddress);
        setToolBar();
    }

    private void saveAddress() {
        session.setKeyPhone(order.getCustomer().getPhone());
        session.setAddress(order.getCustomer().getAddress().getAreaName(),
                order.getCustomer().getAddress().getLandMark(),
                order.getCustomer().getAddress().getAddressLine1(),
                order.getCustomer().getAddress().getAddressLine2(),
                order.getCustomer().getAddress().getCity());
        session.setName(order.getCustomer().getName());
    }

//    public void alertMessage()
//    {
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface dialog, int which)
//            {
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
//                    {
//                        session.createLoginSession();
//                        saveAddress();
//                    }
//                    break;
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        //Toast.makeText(getApplicationContext(), "Correct the Information", Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        };
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Do you want to save  Address, Phone Number for Future transaction? " ) .setPositiveButton("Yes", dialogClickListener)
//                .setNegativeButton("No", dialogClickListener).show();
//    }
    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Order Summary");
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBackPressed() {

        Intent start = new Intent(FinishActivity.this,MainActivity.class);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(start);
        finish();
    }
}
