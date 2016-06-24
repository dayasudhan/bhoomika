package khaanavali.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import khaanavali.customer.adapter.PlusMinusButtonListener;
import khaanavali.customer.adapter.ProductAdapter;
import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.model.Menu;
import khaanavali.customer.model.MenuAdapter;
import khaanavali.customer.model.Order;

import java.util.ArrayList;
import java.util.Date;

public class ProductDetailViewActivity extends AppCompatActivity implements PlusMinusButtonListener {

    HotelDetail hotelDetail;
    Order order;
    ArrayList<MenuAdapter> mMenulist;
    ProductAdapter mDataAdapter;
    TextView counttxt,priceTxt;
    TextView vendorName,speciality,phone,deliveryTime,minimumOrder,deliverycharge,orderTimings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_view);

        order = new Order();
        Intent intent = getIntent();
        Gson gson = new Gson();
        hotelDetail = gson.fromJson(intent.getStringExtra("hotel"), HotelDetail.class);
        order.setHotel(hotelDetail.getHotel());
        mMenulist = new ArrayList<MenuAdapter>();
        for(int i = 0; i< hotelDetail.getMenu().size(); i++)
        {
            MenuAdapter menuAdapter = new MenuAdapter( hotelDetail.getMenu().get(i));
            mMenulist.add(menuAdapter);
        }
        mDataAdapter = new ProductAdapter(ProductDetailViewActivity.this,
                R.layout.product_detail_list_layout,mMenulist);
        mDataAdapter.setListener(this);
        ListView listView = (ListView) findViewById(R.id.listView_product_detail);
        listView.setAdapter(mDataAdapter);
        vendorName  = (TextView)findViewById(R.id.vendor_name_info);
        vendorName.setText(hotelDetail.getHotel().getName());

        vendorName  = (TextView)findViewById(R.id.vendor_name_info);
        vendorName.setText(hotelDetail.getHotel().getName());
        speciality  = (TextView)findViewById(R.id.vendor_speciality_info);
        speciality.setText(hotelDetail.getSpeciality());
        deliverycharge  = (TextView)findViewById(R.id.delievercharge);
        deliverycharge.setText("Deliver Charge: ₹"+Integer.toString(hotelDetail.getDeliverCharge()));
        deliveryTime  = (TextView)findViewById(R.id.vendor_delivery_time_info);
        deliveryTime.setText("Deliver Time: " + Integer.toString(hotelDetail.getDeliveryTime()) + " mins");
        minimumOrder  = (TextView)findViewById(R.id.vendor_rating_info);
        minimumOrder.setText("Minumu Order: ₹"+Integer.toString(hotelDetail.getMinimumOrder()));
        phone    = (TextView)findViewById(R.id.phone);
        phone.setText("Phone :"+Integer.toString(hotelDetail.getPhone()));
        orderTimings = (TextView)findViewById(R.id.ordertimings);
        orderTimings.setText("Morning: 09.30 - 12.00" + "\n" + "Lunch   :  09.30 - 12.00 " +"\n" + "Dinner :  09.30 - 12.00");

        String strorderTimings = new String();
        if (hotelDetail.getOrderAcceptTimings().getMorning().getAvailable().equals("Yes")) {
            strorderTimings = "Morning: " + hotelDetail.getOrderAcceptTimings().getMorning().getStartTime() + "-" + hotelDetail.getOrderAcceptTimings().getMorning().getEndTime();
        }
        if (hotelDetail.getOrderAcceptTimings().getLunch().getAvailable().equals("Yes")) {
            strorderTimings = strorderTimings + "\n"  + "Lunch   : " + hotelDetail.getOrderAcceptTimings().getLunch().getStartTime() + "-" + hotelDetail.getOrderAcceptTimings().getLunch().getEndTime();
        }
        if (hotelDetail.getOrderAcceptTimings().getDinner().getAvailable().equals("Yes")) {
            strorderTimings = strorderTimings + "\n"  + "Dinner  : " + hotelDetail.getOrderAcceptTimings().getDinner().getStartTime() + "-" + hotelDetail.getOrderAcceptTimings().getDinner().getEndTime();
        }
        orderTimings.setText(strorderTimings);
        Button nextButton = (Button) findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                moveNext();
            }
        });
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ProductDetailViewActivity.this, CartActivity.class);

                startActivity(i);
            }
        });*/


        setToolBar(hotelDetail.getHotel().getName());
    }

    private void setToolBar(String areaClicked) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(areaClicked);
    }

    public void moveNext()
    {
        if(mDataAdapter.totalCount <= 0)
        {
            Toast.makeText(getApplicationContext(), "Cart Empty -please select Some Items", Toast.LENGTH_LONG).show();
        }
        else if(mDataAdapter.totalCost < hotelDetail.getMinimumOrder())
        {
            String text  = "Minimum Order for this Hotel is Rs." +  Integer.toString(hotelDetail.getMinimumOrder()) + " Kindly add more items";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
        else {
            Intent i = new Intent(ProductDetailViewActivity.this, CartActivity.class);
            Gson gson = new Gson();
            order.getMenuItems().clear();
            for (int j = 0; j < mDataAdapter.getmMenulist().size(); j++) {
                if (mDataAdapter.getmMenulist().get(j).getMenuOrder().getNo_of_order() > 0) {
                    Menu menu = mDataAdapter.getmMenulist().get(j).getMenuOrder();
                    order.getMenuItems().add(menu);
                }
            }
            String strOrder = gson.toJson(order);
            i.putExtra("order", strOrder);
            String strHotelDetail = gson.toJson(hotelDetail);
            i.putExtra("HotelDetail",strHotelDetail);
            startActivity(i);
        }
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
    @Override
    public void buttonClicked(int postion,int value) {

        counttxt.setText(String.valueOf(mDataAdapter.totalCount));
        priceTxt.setText("₹ " + String.valueOf(mDataAdapter.totalCost));
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_product_detail_list, menu);
        //  menu_hotlist = menu.findItem(R.id.menu_hotlist).getActionView();
        // counttxt= (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.menu_hotlist).getActionView();
        //badgeLayout.set
        counttxt = (TextView) badgeLayout.findViewById(R.id.count_indicator);
        priceTxt = (TextView) badgeLayout.findViewById(R.id.checkoutprice);
        //counttxt.setVisibility(View.INVISIBLE);
        // updateHotCount(0);
        counttxt.setText("0");
        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveNext();
            }
        });
        /*new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
            @Override
            public void onClick(View v) {

            }

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                return false;
            }
        };*/
        return super.onCreateOptionsMenu(menu);
    }
}
