package khaanavali.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import khaanavali.customer.adapter.HotelListAdapter;
import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.model.MenuItem;
import khaanavali.customer.utils.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HotelActivity extends AppCompatActivity {
    private static final String TAG_ID = "_id";
    private static final String TAG_ID2 = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_MENU = "menu";
    private static final String TAG_HOTEL = "hotel";
    private static final String TAG_SPECIALITY = "speciality";
    private static final String TAG_DELIVERY_AREAS = "deliverAreas";
    private static final String TAG_DELIVERY_RANGE = "deliverRange";
    private static final String TAG_DELIVERY_CHARGE = "deliverCharge";
    private static final String TAG_AVAILIBILITY = "availability";
    private static final String TAG_DELIVERY_TIME = "deliverTime";
    private static final String TAG_RATING = "rating";
    private ArrayList<HotelDetail> hotellist;
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        hotellist =  new ArrayList<HotelDetail>();
        Intent intent = getIntent();
        String areaClicked = new String(intent.getStringExtra("area"));
        TextView areaname= (TextView) findViewById(R.id.location_text_view_vendor_list);
        areaname.setText(areaClicked);
        getHotelList(areaClicked);

        listView = (ListView) findViewById(R.id.listView_vendor);

        //setToolBar(areaClicked);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HotelActivity.this, ProductDetailViewActivity.class);
                Gson gson = new Gson();
                String hotel = gson.toJson(hotellist.get(position));
                i.putExtra("hotel", hotel);
                startActivity(i);
            }
        });
//        ImageView filter= (ImageView) findViewById(R.id.filter_btn_vendor_list);
//
//        filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showAlertDialogForVendorFilter();
//            }
//        });
    }
//    private void setToolBar(String areaClicked) {
//        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(tb);
//
//        ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle(areaClicked);
//    }
    //http://oota.herokuapp.com/v1/admin/coverageArea
    public void getHotelList(String areaClicked)
    {
        hotellist.clear();
      //  String order_url = "http://oota.herokuapp.com/v1/vendor/city?city=Bangalore";
        //String order_url = "http://oota.herokuapp.com/v1/vendor/area?areaName=";
        String order_url = Constants.GET_HOTEL_BY_DELIVERY_AREAS;
        order_url = order_url + areaClicked;
        new JSONAsyncTask().execute(order_url);
    }
    public void initHotelList()
    {
        HotelListAdapter dataAdapter = new HotelListAdapter(HotelActivity.this,
                R.layout.hotel_list_item,hotellist);
        listView.setAdapter(dataAdapter);
    }
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        ListView mListView;
        Activity mContex;

        public JSONAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HotelActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    JSONArray jarray = new JSONArray(data);

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        HotelDetail hotelDetail = new HotelDetail();
                        if (object.has(TAG_MENU)) {
                            JSONArray menuArray = object.getJSONArray(TAG_MENU);
                            hotelDetail.getMenuItem().clear();
                            for (int j = 0; j < menuArray.length(); j++) {
                                JSONObject menu_object = menuArray.getJSONObject(j);
                                MenuItem menuItem = new MenuItem();
                                if(menu_object.has(TAG_ID))
                                    menuItem.setId(menu_object.getString(TAG_ID));
                                if(menu_object.has(TAG_NAME))
                                    menuItem.setName(menu_object.getString(TAG_NAME));
                                String dd =menu_object.getString(TAG_PRICE);
                                if(menu_object.has(TAG_PRICE) ) {
                                    try {
                                        menuItem.setPrice(menu_object.getInt(TAG_PRICE));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(menu_object.has(TAG_AVAILIBILITY)) {

                                    try {
                                        if(menu_object.getInt(TAG_AVAILIBILITY) == 1)
                                            menuItem.setAvailable(true);
                                        else
                                            menuItem.setAvailable(false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                hotelDetail.getMenuItem().add(menuItem);
                            }
                        }
                        if(object.has(TAG_ID))
                            hotelDetail.setId(object.getString(TAG_ID));

                        if(object.has(TAG_SPECIALITY))
                            hotelDetail.setSpeciality(object.getString(TAG_SPECIALITY));

                        if(object.has(TAG_HOTEL))
                        {
                            JSONObject hotelObj = object.getJSONObject(TAG_HOTEL);
                            if (hotelObj.has(TAG_NAME)) {
                                hotelDetail.getHotel().setName(hotelObj.getString(TAG_NAME));
                            }
                            if (hotelObj.has(TAG_EMAIL)) {
                                hotelDetail.getHotel().setEmail(hotelObj.getString(TAG_EMAIL));
                            }
                            if (hotelObj.has(TAG_ID2)) {
                                hotelDetail.getHotel().setId(hotelObj.getString(TAG_ID2));
                            }
                        }
                        if (object.has(TAG_PHONE)) {
                            int phone=0 ;
                            try {
                                phone = object.getInt(TAG_PHONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hotelDetail.setPhone(new Integer(phone));
                            hotelDetail.getHotel().setPhone(hotelDetail.getPhone());
                        }
                        if (object.has(TAG_ADDRESS)) {
                            JSONObject addrObj = object.getJSONObject(TAG_ADDRESS);
                            if(addrObj.has("addressLine1"))
                                hotelDetail.getAddress().setAddressLine1(addrObj.getString("addressLine1"));
                            if(addrObj.has("addressLine2"))
                                hotelDetail.getAddress().setAddressLine2(addrObj.getString("addressLine2"));
                            if(addrObj.has("areaName"))
                                hotelDetail.getAddress().setAreaName(addrObj.getString("areaName"));
                            if(addrObj.has("city"))
                                hotelDetail.getAddress().setCity(addrObj.getString("city"));
                            if(addrObj.has("LandMark"))
                                hotelDetail.getAddress().setLandMark(addrObj.getString("LandMark"));
                            if(addrObj.has("street"))
                                hotelDetail.getAddress().setStreet(addrObj.getString("street"));
                            if(addrObj.has("street"))
                                hotelDetail.getAddress().setZip(addrObj.getString("street"));
                        }
                        if(object.has(TAG_DELIVERY_RANGE))
                        {
                            int range=0 ;
                            try {
                                range = object.getInt(TAG_DELIVERY_RANGE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hotelDetail.setDeliveryRange(range);
                        }
                        if(object.has(TAG_DELIVERY_AREAS))
                        {
                            JSONArray delieveryArray = object.getJSONArray(TAG_DELIVERY_AREAS);
                            hotelDetail.getDeliveryAreas().clear();
                            for (int j = 0; j < delieveryArray.length(); j++) {
                                JSONObject da_object = delieveryArray.getJSONObject(j);
                                if (da_object.has(TAG_NAME))
                                    hotelDetail.getDeliveryAreas().add(da_object.getString(TAG_NAME));
                            }
                        }
                        if(object.has(TAG_DELIVERY_CHARGE))
                        {
                            int charge=0 ;
                            try {
                                charge = object.getInt(TAG_DELIVERY_CHARGE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hotelDetail.getHotel().setDeliveryCharges(charge);
                        }
                        if(object.has(TAG_RATING))
                        {
                            int rating=0 ;
                            try {
                                rating = object.getInt(TAG_RATING);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hotelDetail.setRating(rating);
                        }
                        if(object.has(TAG_DELIVERY_TIME))
                        {
                            int delieveryTime=0 ;
                            try {
                                delieveryTime = object.getInt(TAG_DELIVERY_TIME);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hotelDetail.setDelivery_time(delieveryTime);
                        }
                        hotellist.add(hotelDetail);
                    }
                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        protected void onPostExecute(Boolean result) {
            dialog.cancel();

            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            else
            {
                initHotelList();
            }

        }
    }
    private void showAlertDialogForVendorFilter( ) {


        final Dialog detailDialog= new Dialog(HotelActivity.this);
        detailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = detailDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wmlp.x = 30;   //x position
        wmlp.y = 130;
        detailDialog.setContentView(R.layout.filter_hotel_dialog);

        TextView meal= (TextView)detailDialog.findViewById(R.id.checkbox_title_1);
       // meal.setTypeface(cr);

        TextView night= (TextView)detailDialog.findViewById(R.id.checkbox_title);
      //  night.setTypeface(cr);
        CheckBox check= (CheckBox) detailDialog.findViewById(R.id.checkBox_filter);
        CheckBox check1= (CheckBox) detailDialog.findViewById(R.id.checkBox_filter_1);
       /* if(mpref.getNightStore().equals("nighttrue")){
            check.setChecked(true);
            mNightStoreId=1;
        }else{
            check.setChecked(false);
            mNightStoreId=0;
        }
        if(mpref.getMealVoucher().equals("mealtrue")){
            check1.setChecked(true);
            mMealVoucher=2;
        }else{
            check1.setChecked(false);
            mMealVoucher=0;
        }*/
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckBox cb= (CheckBox)view;
                if(cb.isChecked()){
                    // Toast.makeText(getApplicationContext(), "Night Store",Toast.LENGTH_SHORT).show();
                   // mNightStoreId=1;
                   // mpref.setNightStore("nighttrue");
                }else{
                    // Toast.makeText(getApplicationContext(), "Night Store Deactivated",Toast.LENGTH_SHORT).show();
                  //  mNightStoreId=0;
                  //  mpref.setNightStore("nightfalse");
                }
            }
        });

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb1= (CheckBox)view;
                //   Toast.makeText(getApplicationContext(), "Accept Meal Voucaher",Toast.LENGTH_SHORT).show();
                if(cb1.isChecked()){
                    //      Toast.makeText(getApplicationContext(), "Accept Meal Voucaher",Toast.LENGTH_SHORT).show();
                  //  mMealVoucher=2;
                  //  mpref.setMealVoucher("mealtrue");
                }else{

                    //     Toast.makeText(getApplicationContext(), "Accept Meal Voucaher Deactivated",Toast.LENGTH_SHORT).show();
                  //  mMealVoucher=0;
                  //  mpref.setMealVoucher("mealfalse");
                }

            }
        });
        TextView update= (TextView) detailDialog.findViewById(R.id.update_filter);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailDialog.dismiss();
               // UpdateVendrorList();
            }
        });
        TextView reset= (TextView) detailDialog.findViewById(R.id.reset_filter);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailDialog.dismiss();
               // mpref.setMealVoucher("no");
               // mpref.setNightStore("noNight");
              //  mMealVoucher=0;
              //  mNightStoreId=0;
              //  UpdateVendrorList();
            }
        });
        detailDialog.show();


    }


}
