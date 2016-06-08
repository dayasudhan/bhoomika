package khaanavali.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
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

public class ProductDetailViewActivity extends AppCompatActivity implements PlusMinusButtonListener {

    HotelDetail hotelDetail;
    Order order;
    ArrayList<MenuAdapter> mMenulist;
    ProductAdapter mDataAdapter;
    TextView counttxt;
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
        for(int i = 0; i< hotelDetail.getMenuItem().size();i++)
        {
            MenuAdapter menuAdapter = new MenuAdapter( hotelDetail.getMenuItem().get(i));
            mMenulist.add(menuAdapter);
        }
        mDataAdapter = new ProductAdapter(ProductDetailViewActivity.this,
                R.layout.product_detail_list_layout,mMenulist);
        mDataAdapter.setListener(this);
        ListView listView = (ListView) findViewById(R.id.listView_product_detail);
        listView.setAdapter(mDataAdapter);


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ProductDetailViewActivity.this, CartActivity.class);

                startActivity(i);
            }
        });*/



    }

    private void setToolBar(String areaClicked) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(areaClicked);
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
        int count = mDataAdapter.totalCount;
        counttxt.setText(String.valueOf(count));
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_product_detail_list, menu);
        //  menu_hotlist = menu.findItem(R.id.menu_hotlist).getActionView();
        // counttxt= (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.menu_hotlist).getActionView();
        counttxt = (TextView) badgeLayout.findViewById(R.id.count_indicator);
        //counttxt.setVisibility(View.INVISIBLE);
        // updateHotCount(0);
        counttxt.setText("0");
        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (mpref.getMainOrder().isEmpty()) {
                    counttxt.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "There are no items in the cart", Toast.LENGTH_LONG).show();
                    dialog2 = ProgressDialog.show(ProductDetailListActivity.this,
                            "", "Loading..  Please wait", true);
                    reLoadProducts();
                } else {
                    if (!counttxt.getText().toString().isEmpty()) {
                        Integer noItems = new Integer(counttxt.getText().toString());

                        Intent gotoSetCart = new Intent(ProductDetailListActivity.this, CartDetailActivity.class);
                        startActivity(gotoSetCart);
                    }
                }*/
                if(mDataAdapter.totalCount <= 0)
                {
                    Toast.makeText(getApplicationContext(), "Cart Empty -please select Some Items", Toast.LENGTH_LONG).show();
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
                    startActivity(i);
                }
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
