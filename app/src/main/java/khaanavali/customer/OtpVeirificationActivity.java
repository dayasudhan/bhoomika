package khaanavali.customer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.splunk.mint.Mint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import khaanavali.customer.model.Address;
import khaanavali.customer.model.FavouriteAddress;
import khaanavali.customer.utils.Constants;
import khaanavali.customer.utils.SessionManager;

/**
 * Created by dganeshappa on 8/5/2016.
 */
public class OtpVeirificationActivity extends AppCompatActivity {
    Button btnSubmit;
    private EditText otp;
    private boolean verifyPressed=true;
    String apiReponse;
    private String order ,hotelDetail,phoneNumber,name,email;
    SessionManager session;
    FavouriteAddress favadd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.otp_verification_layout);

        Intent intent = getIntent();
        order = intent.getStringExtra("order");
        hotelDetail = intent.getStringExtra("HotelDetail");
        phoneNumber = intent.getStringExtra("phoneNumber");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        otp = (EditText)findViewById(R.id.otpInput);

        session = new SessionManager(getApplicationContext());



        btnSubmit= (Button) findViewById(R.id.verifyButton);
        if(verifyPressed) {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (otp.getText().toString().equals("")) {
                        alertMessage("Enter valid OTP");
                    } else {
                        confirmOtp(phoneNumber, otp.getText().toString());
                        verifyPressed=false;



                    }
                }
            });
        }

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                Toast.makeText(OtpVeirificationActivity.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                String numbers = messageText.substring(messageText.length() - 4);
                otp.setText(numbers.trim());
            }
        });
        setToolBar("OTP Verification");
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
                .setIcon(R.drawable.ic_action_about).show();

    }

    public void confirmOtp(String phoneNumber,String otp)
    {
        String url = Constants.OTP_CONFIRM_URL;
        //String review_url = "http://10.239.54.38:3000/v1/vendor/review/";

        new PostJSONAsyncTask().execute(url, phoneNumber,otp);
    }
    public  class PostJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        Dialog dialog;
        public  PostJSONAsyncTask()
        {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(OtpVeirificationActivity.this,android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_progress_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            dialog.setCancelable(true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("phoneNumber", urls[1]));
                postParameters.add(new BasicNameValuePair("otpText", urls[2]));
                postParameters.add(new BasicNameValuePair("name", name));
                postParameters.add(new BasicNameValuePair("email", email));
                HttpPost request = new HttpPost(urls[0]);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    apiReponse = EntityUtils.toString(entity);

                    try {


                        JSONObject jObject  = new JSONObject(apiReponse);



                                    JSONArray address =jObject.getJSONArray("addresses");
                                    if(address != null) {
                                        int length = address.length();
                                        for (int j = 0; j < address.length(); j++) {
                                            JSONObject addressObject = address.getJSONObject(j);

                                            String areaName, landMark, addressLine1, addressLine2, city,zip,lon,lat;

                                            areaName = addressObject.getString("label");
                                            landMark = addressObject.getString("LandMark");
                                            addressLine1 = addressObject.getString("addressLine1");
                                            addressLine2 = addressObject.getString("addressLine2");
                                            city = addressObject.getString("city");
                                            zip=addressObject.getString("zip");
                                            lon=addressObject.getString("latitude");
                                            lat=addressObject.getString("longitude");



                                            favadd =new FavouriteAddress();
                                            Address addr1=new Address();
                                            addr1.setAddressLine1(addressLine1);
                                            addr1.setAddressLine2(addressLine2);
                                            addr1.setAreaName(areaName);
                                            addr1.setCity(city);
                                            addr1.setZip(zip);
                                            addr1.setLongitude(lon);
                                            addr1.setLatitude(lat);
                                            addr1.setLandMark(landMark);

                                            favadd.setLabel(areaName);
                                            favadd.setAddress(addr1);
                                            session.setFavoutrateAddress(favadd);

                                        }
                                    }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

//gagan

                        return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return false;
        }
        protected void onPostExecute(Boolean result) {

            dialog.cancel();
            if(result == true){
                if(apiReponse.length()>0)
                {
                    //Toast.makeText(OtpVeirificationActivity.this , apiReponse, Toast.LENGTH_LONG).show();

                    session.createLoginSession(name,phoneNumber,email);
                    session.commiting();
                    Intent i = new Intent(OtpVeirificationActivity.this, CutomerEnterDetailsActivity.class);
                    i.putExtra("order", order);
                    i.putExtra("HotelDetail",hotelDetail);

                    startActivity(i);

                }
                else
                {
                    alertMessage("Invalid OTP");
                }
            }
            else if (result == false)
                Toast.makeText(getApplicationContext(), "welcome to khaanvali", Toast.LENGTH_LONG).show();
        }
    }

}
