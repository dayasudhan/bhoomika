package khaanavali.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.splunk.mint.Mint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    List<android.location.Address> mAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.add_address_layout);
        Intent intent = getIntent();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<android.location.Address>>() {}.getType();
        mAddresses = gson.fromJson(intent.getStringExtra("locationaddress"), listType);
    //    String strAddress = intent.getStringExtra("address");

        editCity=(EditText)findViewById(R.id.orderDetailEmail);
        editHouseNo=(EditText)findViewById(R.id.orderDetailAddress_house_no);
        editAreaName=(EditText)findViewById(R.id.orderDetailAddress_areaname);
        editLandmark=(EditText)findViewById(R.id.orderDetailAddress_landmark);
        editAddress=(EditText)findViewById(R.id.orderDetailAddress_address);
        editTagLabel=(EditText)findViewById(R.id.tag_address_label);
        editAddress.setText(mAddresses.get(0).getAddressLine(1));
        editCity.setText(mAddresses.get(0).getLocality());
        editAreaName.setText(mAddresses.get(0).getSubLocality());
        btnSave= (Button) findViewById(R.id.saveAddressbutton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editHouseNo.getText().length() == 0){
                    //Toast.makeText(getApplicationContext(), "Enter House No or Flat No ", Toast.LENGTH_LONG).show();
                    alertMessage("Enter House or Flat No ");
                }
                else if(editAreaName.getText().length() == 0){
                    //Toast.makeText(getApplicationContext(), "Enter areaname ", Toast.LENGTH_LONG).show();
                    alertMessage("Enter areaname ");
                }
                else if(editAddress.getText().length() == 0){
                    //Toast.makeText(getApplicationContext(), "Enter Address ", Toast.LENGTH_LONG).show();
                    alertMessage("Enter Address ");
                }
                else if(editLandmark.getText().length() == 0){
                    //Toast.makeText(getApplicationContext(), "Enter Landmark/locality ", Toast.LENGTH_LONG).show();
                    alertMessage("Enter Landmark/locality ");
                }

                else if(editTagLabel.getText().length() == 0){
                    //Toast.makeText(getApplicationContext(), "Enter City ", Toast.LENGTH_LONG).show();
                    alertMessage("Enter lable for this address");
                }
                else
                {
                    address = new Address();
                    address.setAreaName(editAreaName.getText().toString());
                    address.setLandMark(editLandmark.getText().toString());
                    address.setAddressLine1(editHouseNo.getText().toString());
                    address.setAddressLine2(editAddress.getText().toString());
                    address.setCity(editCity.getText().toString());

                    address.setZip(mAddresses.get(0).getPostalCode());
                    address.setLatitude(String.valueOf(mAddresses.get(0).getLatitude()));
                    address.setLongitude(String.valueOf(mAddresses.get(0).getLongitude()));
                    FavouriteAddress favouriteAddress = new FavouriteAddress();
                    favouriteAddress.setLabel(editTagLabel.getText().toString());
                    favouriteAddress.setAddress(address);
                    SessionManager session = new SessionManager(getApplicationContext());
                    session.setFavoutrateAddress(favouriteAddress);

                    Intent intent = new Intent();
                    Gson gson = new Gson();
                    String locationaddress = gson.toJson(address);
                    intent.putExtra("locationaddress", locationaddress);
                    setResult(RESULT_OK, intent);
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
    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Khaanavali");
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about);

        final AlertDialog dialog = builder.create();
        dialog.show(); //show() should be called before dialog.getButton().


        final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        neutralButton.setLayoutParams(positiveButtonLL);

    }
}
