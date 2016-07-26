package khaanavali.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import khaanavali.customer.model.Address;
import khaanavali.customer.model.FavouriteAddress;
import khaanavali.customer.utils.SessionManager;

/**
 * Created by dganeshappa on 7/26/2016.
 */
public class AddAdressActivity  extends AppCompatActivity {
    EditText editTagLabel,editCity,editHouseNo,editAreaName,editLandmark,editAddress;
    Button btnSave;
    Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_layout);
        editCity=(EditText)findViewById(R.id.orderDetailEmail);
        editHouseNo=(EditText)findViewById(R.id.orderDetailAddress_house_no);
        editAreaName=(EditText)findViewById(R.id.orderDetailAddress_areaname);
        editLandmark=(EditText)findViewById(R.id.orderDetailAddress_landmark);
        editAddress=(EditText)findViewById(R.id.orderDetailAddress_address);
        editTagLabel=(EditText)findViewById(R.id.tag_address_label);
        btnSave= (Button) findViewById(R.id.saveAddressbutton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editHouseNo.getText().length() == 0){
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

                else if(editTagLabel.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Enter City ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    address = new Address();
                    address.setAreaName(editAreaName.getText().toString());
                    address.setLandMark(editLandmark.getText().toString());
                    address.setAddressLine1(editHouseNo.getText().toString());
                    address.setAddressLine2(editAddress.getText().toString());
                    address.setCity(editCity.getText().toString());
                    FavouriteAddress favouriteAddress = new FavouriteAddress();
                    favouriteAddress.setLabel(editTagLabel.getText().toString());
                    favouriteAddress.setAddress(address);
                    SessionManager session = new SessionManager(getApplicationContext());
                    session.setFavoutrateAddress(favouriteAddress);
                    Intent intent = new Intent();

//                    Gson gson = new Gson();
//                    String strfaddress = gson.toJson(favouriteAddress);
//                    intent.putExtra("FavouriteAddress", strfaddress);
//                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        setToolBar("Add Address");
    }


private void setToolBar(String title) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
        }
}
