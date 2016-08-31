package khaanavali.customer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.Arrays;

import khaanavali.customer.adapter.HotelListAdapter;
import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.model.MenuItem;
import khaanavali.customer.model.OrderAcceptTimings;
import khaanavali.customer.utils.Constants;
import khaanavali.customer.utils.SessionManager;


/**
 * Created by dganeshappa on 7/14/2016.
 */
public class HotelFragment extends Fragment {

    private static final String TAG_ID = "_id";
    private static final String TAG_ID2 = "id";
    private static final String TAG_LOGO = "logo";
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
    private static final String TAG_DELIVERY_TIME = "deliveryTime";
    private static final String TAG_ORDER_ACCEPT_TIMINGS = "orderAcceptTimings";
    private static final String TAG_RATING = "rating";
    private static final String TAG_ISOPEN = "isOpen";
    private static final String TAG_MINIMUM_ORDER = "minimumOrder";
    private static final String TAG_BULK_DELIVERY_TIME = "bulkdeliveryTime";
    private static final String TAG_BULK_DELIVERY_RANGE = "bulkdeliverRange";
    private static final String TAG_BULK_DELIVERY_CHARGE = "bulkdeliverCharge";
    private static final String TAG_BULK_MINIMUM_ORDER = "bulkminimumOrder";
    private static final String TAG_BULK_TYPE = "isBulkVendor";




    private ArrayList<HotelDetail> hotellist;
    ListView listView ;
    TextView textview;

    //gagan
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String[] IMAGES = new String[] {
            Constants.SLIDER_URL1,
            Constants.SLIDER_URL2,
            Constants.SLIDER_URL3,
            Constants.SLIDER_URL4
    };
    private ViewPager pager;
    //gagan
    private boolean isBulk;

    public boolean isBulk() {
        return isBulk;
    }

    public void setBulk(boolean bulk) {
        isBulk = bulk;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



         View v = inflater.inflate(R.layout.activity_hotel, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);



        hotellist =  new ArrayList<HotelDetail>();
        SessionManager session = new SessionManager(getActivity().getApplicationContext());


        final String areaClicked = session.getlastareasearched();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!areaClicked.isEmpty())
                    getHotelList(areaClicked);
                else
                    getHotelList("VijayaNagara");
            }

        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);
        swipeRefreshLayout.setProgressBackgroundColor(android.R.color.transparent);



        if(!areaClicked.isEmpty())
            getHotelList(areaClicked);
        else
            getHotelList("VijayaNagara");

        //gagan


        pager = (ViewPager) v.findViewById(R.id.pager);
        ScreenSlidePagerAdapter pagerAdapter =new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());

        pagerAdapter.addAll(Arrays.asList(IMAGES));
        pager.setAdapter(pagerAdapter);
        CirclePageIndicator indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        //gagan end

        textview = (TextView) v.findViewById(R.id.textView_no_vendors);
        listView = (ListView) v.findViewById(R.id.listView_vendor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(hotellist.get(position).getIsOpen() !=0) {
                    Intent i = new Intent(getActivity(), ProductDetailViewActivity.class);
                    Gson gson = new Gson();
                    String hotel = gson.toJson(hotellist.get(position));
                    i.putExtra("hotel", hotel);
                    if(isBulk() == true)
                    {
                        i.putExtra("isBulk","true");
                    }
                    else
                    {
                        i.putExtra("isBulk","false");
                    }
                    startActivity(i);
                }
                else
                {
                    alertMessage("Delevery for this hotel is not available at this time. kindly try other hotel near by you");
                }
            }
        });
        setHasOptionsMenu(true);
        return v;
    }

    public void getHotelList(String areaClicked)
    {
        ((MainActivity) getActivity())
                .setActionBarTitle(areaClicked);
        hotellist.clear();
        String order_url = Constants.GET_HOTEL_BY_DELIVERY_AREAS;
        String area = areaClicked.replace(" ", "%20");
        order_url = order_url + area;
        if(isBulk)
        {
            order_url = order_url + "&isbulkrequest=1";
        }
        else
        {
            order_url = order_url + "&isbulkrequest=0";
        }
        new JSONAsyncTask().execute(order_url);
    }
    public void initHotelList()
    {


            HotelListAdapter dataAdapter = new HotelListAdapter(getActivity(),
                    R.layout.hotel_list_item, hotellist);
            listView.setAdapter(dataAdapter);

            dataAdapter.notifyDataSetChanged();
        if(hotellist.size() > 0 ) {
            textview.setVisibility(View.INVISIBLE);
        }
        else
        {
            textview.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu , MenuInflater inflater) {
           inflater.inflate(R.menu.home_menu, menu);
    }
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        Dialog dialog;
        public JSONAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_progress_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            dialog.setCancelable(true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet request = new HttpGet(urls[0]);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(request);

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
                            hotelDetail.getMenu().clear();
                            for (int j = 0; j < menuArray.length(); j++) {
                                JSONObject menu_object = menuArray.getJSONObject(j);
                                MenuItem menuItem = new MenuItem();
//                                if(menu_object.has(TAG_ID))
//                                    menuItem.set_id(menu_object.getString(TAG_ID));
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
                                            menuItem.setAvailable(1);
                                        else
                                            menuItem.setAvailable(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                hotelDetail.getMenu().add(menuItem);
                            }
                        }
                        if(object.has(TAG_ID))
                            hotelDetail.set_id(object.getString(TAG_ID));

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
                            if (hotelObj.has(TAG_LOGO)) {
                                hotelDetail.getHotel().setLogo(hotelObj.getString(TAG_LOGO));
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

                        if(object.has(TAG_DELIVERY_AREAS))
                        {
                            JSONArray delieveryArray = object.getJSONArray(TAG_DELIVERY_AREAS);
                            hotelDetail.getDeliverAreas().clear();
                            for (int j = 0; j < delieveryArray.length(); j++) {
                                JSONObject da_object = delieveryArray.getJSONObject(j);
                                if (da_object.has(TAG_NAME))
                                    hotelDetail.getDeliverAreas().add(da_object.getString(TAG_NAME));
                            }
                        }
                        if(object.has(TAG_ISOPEN))
                        {
                            int var=0 ;
                            try {
                                var = object.getInt(TAG_ISOPEN);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hotelDetail.setIsOpen(var);
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

                        if(object.has(TAG_ORDER_ACCEPT_TIMINGS))
                        {
                            JSONObject ordAcctime = object.getJSONObject(TAG_ORDER_ACCEPT_TIMINGS);
                            Gson gson = new Gson();
                            OrderAcceptTimings ordacctimeobj =  gson.fromJson(ordAcctime.toString(),OrderAcceptTimings.class);
                            hotelDetail.setOrderAcceptTimings(ordacctimeobj);

                        }
                        if(isBulk)
                        {
                            if (object.has(TAG_BULK_DELIVERY_RANGE)) {
                                int range = 0;
                                try {
                                    range = object.getInt(TAG_BULK_DELIVERY_RANGE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setDeliverRange(range);
                            }

                            if (object.has(TAG_BULK_DELIVERY_CHARGE)) {
                                int charge = 0;
                                try {
                                    charge = object.getInt(TAG_BULK_DELIVERY_CHARGE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setDeliverCharge(charge);
                            }

                            if (object.has(TAG_BULK_MINIMUM_ORDER)) {
                                int rating = 0;
                                try {
                                    rating = object.getInt(TAG_BULK_MINIMUM_ORDER);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setMinimumOrder(rating);
                            }

                            if (object.has(TAG_BULK_DELIVERY_TIME)) {
                                int delieveryTime = 0;
                                try {
                                    delieveryTime = object.getInt(TAG_BULK_DELIVERY_TIME);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setDeliveryTime(delieveryTime);
                            }
                        }
                        else {

                            if (object.has(TAG_DELIVERY_RANGE)) {
                                int range = 0;
                                try {
                                    range = object.getInt(TAG_DELIVERY_RANGE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setDeliverRange(range);
                            }

                            if (object.has(TAG_DELIVERY_CHARGE)) {
                                int charge = 0;
                                try {
                                    charge = object.getInt(TAG_DELIVERY_CHARGE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setDeliverCharge(charge);
                            }

                            if (object.has(TAG_MINIMUM_ORDER)) {
                                int rating = 0;
                                try {
                                    rating = object.getInt(TAG_MINIMUM_ORDER);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setMinimumOrder(rating);
                            }

                            if (object.has(TAG_DELIVERY_TIME)) {
                                int delieveryTime = 0;
                                try {
                                    delieveryTime = object.getInt(TAG_DELIVERY_TIME);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hotelDetail.setDeliveryTime(delieveryTime);
                            }
                        }
                        hotellist.add(hotelDetail);

                    }
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;

        }
        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            if(getActivity() != null) {
                if (result == false) {

                    Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
                    //alertMessage("Unable to fetch data from server");
                } else {
                    initHotelList();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

        }
    }
    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Khaanavali");
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                    .setIcon(R.drawable.ic_action_about).show();

    }
}
