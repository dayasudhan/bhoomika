package khaanavali.customer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import khaanavali.customer.model.Order;
import khaanavali.customer.utils.GPSTracker;
import khaanavali.customer.utils.LocationAddress;

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
import java.util.ArrayList;

public class CutomerEnterDetailsActivity extends AppCompatActivity {

    Order order;
    String responseOrder;
    Button btnShowLocation;
    EditText editName,editPhone,editEmail,editHouseNo,editAreaName,editLandmark;
    // GPSTracker class
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_enter_details);
        Intent intent = getIntent();
        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        responseOrder = new String();
        Button btn= (Button) findViewById(R.id.placeOrderButton);
        btnShowLocation= (Button) findViewById(R.id.locationButton);
        editName=(EditText)findViewById(R.id.orderDetailName);
        editPhone=(EditText)findViewById(R.id.orderDetailPhone);
        editEmail=(EditText)findViewById(R.id.orderDetailEmail);
        editHouseNo=(EditText)findViewById(R.id.orderDetailAddress_house_no);
        editAreaName=(EditText)findViewById(R.id.orderDetailAddress_areaname);
        editLandmark=(EditText)findViewById(R.id.orderDetailAddress_landmark);
        TextView orderTotalCharge = (TextView) findViewById(R.id.textView);
        orderTotalCharge.setText(String.valueOf(order.getTotalCost()));
//        if(true) {
//            editName.setText("name");
//            editPhone.setText("9566229075");
//            editEmail.setText("hjgjhgj");
//            editAreaName.setText("areaname");
//            editLandmark.setText("landmark");
//            editHouseNo.setText("houseno");
//        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber(editPhone.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Phone Number ", Toast.LENGTH_LONG).show();
                }
                else if(editName.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter Name ", Toast.LENGTH_LONG).show();
                }
                else if(editHouseNo.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter House No or Flat No ", Toast.LENGTH_LONG).show();
                }
                else if(editAreaName.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter areaname ", Toast.LENGTH_LONG).show();
                }
                else if(editLandmark.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter Landmark/locality ", Toast.LENGTH_LONG).show();
                }
                else {
                    alertMessage();
                }

            }
        });

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(CutomerEnterDetailsActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    //                    double latitude2 = 12.9096282;//13.0714072,77.5654451
//                    double longitude2 = 80.2273703;//12.9671603,77.5352851
                    //only for debugging
//                    if(latitude == 0 && longitude == 0) {
//                        latitude = 12.9708084;//13.0714072,77.5654451
//                        longitude = 77.530879;
//                    }
                    order.getCustomer().getAddress().setLatitude(Double.toString(latitude));
                    order.getCustomer().getAddress().setLongitude(Double.toString(longitude));
                    LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(latitude, longitude,
                                getApplicationContext(), new GeocoderHandler());

                    // \n is for new line
//                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

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
                        order.getCustomer().setEmail(editEmail.getText().toString());
                        order.getCustomer().getAddress().setAreaName(editAreaName.getText().toString());
                        order.getCustomer().getAddress().setLandMark(editLandmark.getText().toString());
                        order.getCustomer().getAddress().setAddressLine1(editHouseNo.getText().toString());
                        order.getCustomer().getAddress().setCity(editEmail.getText().toString());
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
        builder.setMessage("Are you sure about this order?" ) .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();

                    locationAddress = bundle.getString("address");
                    if(LocationAddress.address != null)
                    {
                            editEmail.setText(LocationAddress.address.getLocality());
                            editAreaName.setText(LocationAddress.address.getAddressLine(1));
                            editHouseNo.setText(LocationAddress.address.getAddressLine(0));

                    }
                    break;
                default:
                    locationAddress = null;
            }
           // tvAddress.setText(locationAddress);
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + locationAddress, Toast.LENGTH_LONG).show();
        }
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
        //192.168.1.105
      //  String order_url = "http://192.168.1.105:3000/v1/vendor/order";
       String order_url = "http://oota.herokuapp.com/v1/vendor/order";

        new PostJSONAsyncTask().execute(order_url,order);
    }
    public  class PostJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        public  PostJSONAsyncTask()
        {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CutomerEnterDetailsActivity.this);
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

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            // adapter.notifyDataSetChanged();
            // bindView();
            if (result == true){
                Intent i = new Intent(CutomerEnterDetailsActivity.this, FinishActivity.class);
                i.putExtra("order", responseOrder);
                startActivity(i);
                finish();
        }
            else if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
}
