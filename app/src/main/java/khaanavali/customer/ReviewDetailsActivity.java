package khaanavali.customer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import khaanavali.customer.adapter.AddressListAdapater;
import khaanavali.customer.model.Address;
import khaanavali.customer.model.FavouriteAddress;
import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.model.Order;
import khaanavali.customer.utils.Constants;
import khaanavali.customer.utils.GPSTracker;
import khaanavali.customer.utils.LocationAddress;
import khaanavali.customer.utils.SessionManager;

public class ReviewDetailsActivity extends AppCompatActivity {

    Order order;
    String responseOrder;

    Button btnAddNewAddress;
    EditText editName,editPhone,editAddress;

    HotelDetail hotelDetail;
    SessionManager session;
    ListView listView ;
    ArrayList<FavouriteAddress> mFavouriteAddressArrayList;
    AddressListAdapater addressListAdapater;
    Address deliveryAddress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);
        Intent intent = getIntent();
        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);
        responseOrder = new String();
//        deliveryAddress = new Address();
        session = new SessionManager(getApplicationContext());
        Button btn= (Button) findViewById(R.id.placeOrderButton);
        btnAddNewAddress = (Button) findViewById(R.id.addnewaddress);
        editName=(EditText)findViewById(R.id.orderDetailName);
        editPhone=(EditText)findViewById(R.id.orderDetailPhone);
        mFavouriteAddressArrayList = new ArrayList<FavouriteAddress>();
        listView = (ListView)findViewById(R.id.listView_address);
        addressListAdapater = new AddressListAdapater(this,R.layout.address_list_item,mFavouriteAddressArrayList);
        listView.setAdapter(addressListAdapater);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        //session.clearAddress();
        if(session.isLoggedIn()) {
            try {
                editName.setText(session.getName());
                editPhone.setText(session.getKeyPhone());
            }
            catch(Exception e)
            {

            }
      }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                view.setBackgroundColor(Color.LTGRAY);
                deliveryAddress = mFavouriteAddressArrayList.get(position).getAddress();
            }
        });
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReviewDetailsActivity.this, AddAdressActivity.class);
                startActivity(i);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber(editPhone.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Phone Number ", Toast.LENGTH_LONG).show();
                }
                else if(editName.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter Name ", Toast.LENGTH_LONG).show();
                }
                else if(deliveryAddress == null){
                    Toast.makeText(getApplicationContext(), "Address Empty", Toast.LENGTH_LONG).show();
                }
                else if(hotelDetail.getMinimumOrder() > order.getTotalCost())
                {
                    String text  = "Minimum Order for this Hotel is Rs." +  Integer.toString(hotelDetail.getMinimumOrder()) + " Kindly add more items";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
//                else if(!checkTimeAllowedForOrder())
//                {
//                    Toast.makeText(getApplicationContext(), "This time no delivery for this Hotel Kindly Check Timings of Hotel for Order timings", Toast.LENGTH_LONG).show();
//                }
                else {
                    alertMessage();
                }
            }
        });

        setToolBar("Delivery Details");
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if(session.getFavoutrateAddress() !=null) {
            mFavouriteAddressArrayList = session.getFavoutrateAddress();
            addressListAdapater.setmFavouriteAddressArrayList(mFavouriteAddressArrayList);
            deliveryAddress = mFavouriteAddressArrayList.get(0).getAddress();
        }
    }
    private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
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
    public void alertMessage()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                    {
                        order.getCustomer().setPhone(editPhone.getText().toString());
                        order.getCustomer().setName(editName.getText().toString());

                        order.getCustomer().setAddress(deliveryAddress);
                        Gson gson = new Gson();
                        String strOrder = gson.toJson(order);
                        postOrder(strOrder);
                    }
                    break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(getApplicationContext(), "Correct the Information", Toast.LENGTH_LONG).show();
                        break;
                    }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure about this order(Address, Phone)?" ) .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    public boolean checkTimeAllowedForOrder()
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date orderTime  = dateFormat.parse( dateFormat.format(new Date()));
            if (hotelDetail.getOrderAcceptTimings().getMorning().getAvailable().equals("Yes")) {
                Date starttime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getMorning().getStartTime()   );
                starttime.setMinutes( starttime.getMinutes() -  hotelDetail.getDeliveryTime());
                Date endtime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getMorning().getEndTime());
                if ((orderTime.after(starttime) && orderTime.before(endtime))) {
                    return true;
                }
            }
            if (hotelDetail.getOrderAcceptTimings().getLunch().getAvailable().equals("Yes")) {
                Date starttime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getLunch().getStartTime());
                starttime.setMinutes( starttime.getMinutes() -  hotelDetail.getDeliveryTime());
                Date endtime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getLunch().getEndTime());
                if ((orderTime.after(starttime) && orderTime.before(endtime))) {
                    return true;
                }
            }
            if (hotelDetail.getOrderAcceptTimings().getDinner().getAvailable().equals("Yes")) {
                Date starttime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getDinner().getStartTime());
                starttime.setMinutes( starttime.getMinutes() -  hotelDetail.getDeliveryTime());
                Date endtime = dateFormat.parse(hotelDetail.getOrderAcceptTimings().getDinner().getEndTime());
                if ((orderTime.after(starttime) && orderTime.before(endtime))) {
                    return true;
                }
            }
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean validatePhoneNumber(String phoneNo)
    {
        if (phoneNo.matches("\\d{10}"))
            return true;
        else if(phoneNo.matches("\\+\\d{12}")) return true;
        else return false;
    }

    public void postOrder(String order)
    {
       new PostJSONAsyncTask().execute(Constants.ORDER_URL, order);
    }

    public  class PostJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        public  PostJSONAsyncTask()
        {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ReviewDetailsActivity.this);
            dialog.setMessage("Posting Order, please wait");
            dialog.setTitle("Connecting....");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                HttpPost request = new HttpPost(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                StringEntity se = new StringEntity(urls[1]);
                request.setEntity(se);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    responseOrder = EntityUtils.toString(entity);
                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
           return false;
        }
        protected void onPostExecute(Boolean result) {

            dialog.cancel();
            if(result == true){
                Intent i = new Intent(ReviewDetailsActivity.this, FinishActivity.class);
                i.putExtra("order", responseOrder);
                startActivity(i);
                finish();
        }
            else if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
}
