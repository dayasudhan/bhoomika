package khaanavali.customer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.splunk.mint.Mint;

import java.util.ArrayList;

import khaanavali.customer.adapter.AddressListAdapater;
import khaanavali.customer.model.Address;
import khaanavali.customer.model.FavouriteAddress;
import khaanavali.customer.utils.SessionManager;


/**
 * Created by dganeshappa on 7/14/2016.
 */
public class SelectAddressActivity extends AppCompatActivity{

    private ArrayList<String> mCityCoverage;
    ListView addresslistview;
    Button btnAddNewAddress;
//    LocationAdapter dataAdapter;
    ArrayList<FavouriteAddress> mFavouriteAddressArrayList;
    AddressListAdapater addressListAdapater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.select_address);
        btnAddNewAddress = (Button) findViewById(R.id.addnewaddress);
        SessionManager  session = new SessionManager(getApplicationContext());
        mFavouriteAddressArrayList = new ArrayList<FavouriteAddress>();
        if(session.getFavoutrateAddress() !=null) {
            mFavouriteAddressArrayList = session.getFavoutrateAddress();
        }
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                addressListAdapater.notifyDataSetChanged();
                handler.postDelayed( this, 1 * 1000 );
            }
        }, 60 * 1000 );

        addressListAdapater = new AddressListAdapater(this,R.layout.address_list_item,mFavouriteAddressArrayList);
        addresslistview = (ListView) findViewById(R.id.listView_address);
        addresslistview.setAdapter(addressListAdapater);
        addresslistview.setEmptyView(findViewById(R.id.emptyElement));
        addresslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Address locaddress =  mFavouriteAddressArrayList.get(position).getAddress();
                goBackwithAddress(locaddress);
            }
        });
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SelectAddressActivity.this, MapsActivity.class);
                startActivityForResult(i,1);
//                Bundle b = new Bundle();
//                b.putInt("key", 1); //Your id
//                i.putExtras(b); //
//                startActivity(i);
            }
        });
        setToolBar("Select address");
    }

    public void goBackwithAddress(Address locaddress)
    {

        Intent intent = new Intent();
        Gson gson = new Gson();
        String locationaddress = gson.toJson(locaddress);
        intent.putExtra("locationaddress", locationaddress);
        setResult(RESULT_OK, intent);
        finish();
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
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            intent.putExtra("locationaddress", intent.getStringExtra("locationaddress"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
