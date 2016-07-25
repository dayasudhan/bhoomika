package khaanavali.customer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.model.Order;
import khaanavali.customer.utils.Constants;
import khaanavali.customer.utils.GPSTracker;
import khaanavali.customer.utils.LocationAddress;
import khaanavali.customer.utils.SessionManager;
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

public class CutomerEnterDetailsActivity extends AppCompatActivity {

    Order order;
    String responseOrder;
    Button btnShowLocation;
    EditText editName,editPhone,editCity,editHouseNo,editAreaName,editLandmark,editAddress;
    GPSTracker gps;
    HotelDetail hotelDetail;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_enter_details);
        Intent intent = getIntent();
        Gson gson = new Gson();
        order = gson.fromJson(intent.getStringExtra("order"), Order.class);
        hotelDetail = gson.fromJson(intent.getStringExtra("HotelDetail"), HotelDetail.class);
        responseOrder = new String();
        session = new SessionManager(getApplicationContext());
        Button btn= (Button) findViewById(R.id.placeOrderButton);
        btnShowLocation= (Button) findViewById(R.id.locationButton);
        editName=(EditText)findViewById(R.id.orderDetailName);
        editPhone=(EditText)findViewById(R.id.orderDetailPhone);
        editCity=(EditText)findViewById(R.id.orderDetailEmail);
        editHouseNo=(EditText)findViewById(R.id.orderDetailAddress_house_no);
        editAreaName=(EditText)findViewById(R.id.orderDetailAddress_areaname);
        editLandmark=(EditText)findViewById(R.id.orderDetailAddress_landmark);
        editAddress=(EditText)findViewById(R.id.orderDetailAddress_address);
        final TextView orderTotalCharge = (TextView) findViewById(R.id.textView);
        orderTotalCharge.setText(String.valueOf(order.getTotalCost()));
        if(session.isLoggedIn()) {
            try {
                editName.setText(session.getName());
                editPhone.setText(session.getKeyPhone());
                editAreaName.setText(session.getAddress().getAreaName());
                editLandmark.setText(session.getAddress().getLandMark());
                editHouseNo.setText(session.getAddress().getAddressLine1());
                editAddress.setText(session.getAddress().getAddressLine2());
                editCity.setText(session.getAddress().getCity());
            }
            catch(Exception e)
            {

            }
      }
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
                else if(editAddress.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter Address ", Toast.LENGTH_LONG).show();
                }
                else if(editLandmark.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter Landmark/locality ", Toast.LENGTH_LONG).show();
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

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CutomerEnterDetailsActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        new AlertDialog.Builder(CutomerEnterDetailsActivity.this)
                                .setTitle("Permission Required")
                                .setMessage("This permission was denied earlier by you. This permission is required to get current location.So, in order to use this feature please allow this permission by clicking ok.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ActivityCompat.requestPermissions(CutomerEnterDetailsActivity.this,
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                1);

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {

                        ActivityCompat.requestPermissions(CutomerEnterDetailsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                        ActivityCompat.requestPermissions(CutomerEnterDetailsActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);

                    }
                    return;
                }
//                else {
//                    // permission has been granted, continue as usual
//                    Location myLocation =
//                            LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//                }

                // create class object
                gps = new GPSTracker(CutomerEnterDetailsActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    //                    double latitude2 = 12.9096282;//13.0714072,77.5654451
//                    double longitude2 = 80.2273703;//12.9671603,77.5352851
                    //only for debugging
//                    boolean isDebuggable = 0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE);
//                    if(latitude == 0 && longitude == 0 && isDebuggable) {
//                        longitude = 77.530879;
//                    }                       latitude = 12.9708084;//13.0714072,77.5654451

                    order.getCustomer().getAddress().setLatitude(Double.toString(latitude));
                    order.getCustomer().getAddress().setLongitude(Double.toString(longitude));
                    LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(latitude, longitude,
                                getApplicationContext(), new GeocoderHandler());

                }else{

                    gps.showSettingsAlert();
                }

            }
        });
        setToolBar("Delivery Details");
    }
    private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
//                } else {
//                    String permission = permissions[0];
//                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
//                    if (!showRationale) {
//                        Toast.makeText(this, "Permission Denied with never show options", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                }
//        }
//    }
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
                        order.getCustomer().setEmail(editCity.getText().toString());
                        order.getCustomer().getAddress().setAreaName(editAreaName.getText().toString());
                        order.getCustomer().getAddress().setLandMark(editLandmark.getText().toString());
                        order.getCustomer().getAddress().setAddressLine1(editHouseNo.getText().toString());
                        order.getCustomer().getAddress().setAddressLine2(editAddress.getText().toString());
                        order.getCustomer().getAddress().setCity(editCity.getText().toString());
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
                        editCity.setText(LocationAddress.address.getLocality());
                            editAreaName.setText(LocationAddress.address.getAddressLine(1));
                        editAddress.setText(LocationAddress.address.getAddressLine(0));

                    }
                    break;
                default:
                    locationAddress = null;
            }
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
