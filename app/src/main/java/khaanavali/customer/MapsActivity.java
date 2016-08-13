package khaanavali.customer;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.splunk.mint.Mint;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnCameraChangeListener ,GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Button btnpicklocation;
    Button btnManuallAddress;
    Marker mCurrLocationMarker;
    int zoomleval = 15;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private LatLng mSelectedLatlang;
    List<android.location.Address> mAddresses;
    String mAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(MapsActivity.this, "49d903c2");
        setContentView(R.layout.activity_maps);
        btnpicklocation= (Button) findViewById(R.id.pickaddressbtn);
        btnManuallAddress = (Button)findViewById(R.id.buttonEnterManual);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           checkLocationPermission();
        }
        //checkLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnpicklocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedLatlang != null)
                    setAddress(mSelectedLatlang.latitude,mSelectedLatlang.longitude);
                else {
                    LatLng bangalore = new LatLng(12.9716, 77.5946);
                    setAddress(bangalore.latitude, bangalore.longitude);
                }
                Intent i = new Intent(MapsActivity.this, AddAdressActivity.class);
                Gson gson = new Gson();
                String locationaddress = gson.toJson(mAddresses);

                i.putExtra("locationaddress", locationaddress);
                startActivityForResult(i,1);
               // finish();
            }
        });

        btnManuallAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MapsActivity.this, AddAdressActivity.class);
                Gson gson = new Gson();
                startActivityForResult(i,1);
            }
        });
        setToolBar("Add Address");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        // Add a marker in Sydney and move the camera
        LatLng bangalore = new LatLng(12.9716, 77.5946);
        setPosition(bangalore);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    public void getCurrentLocation()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            }
        }
        if(mLastLocation!=null) {
            setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        LatLng position = cameraPosition.target;
        mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
        mCurrLocationMarker.setPosition(position);
        cameraPosition.toString();
        mSelectedLatlang = position;
        // setAddress(position.latitude,position.longitude);
    }
    private void setPosition(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomleval));
        mSelectedLatlang = latLng;
    }
    public void setAddress(double latitude, double longitude)
    {
        mAddress = "";
        Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());
        try {
            mAddresses = geoCoder.getFromLocation(
                    latitude,
                    longitude, 1);

            if (mAddresses.size() > 0) {
                for (int index = 0;
                     index < mAddresses.get(0).getMaxAddressLineIndex(); index++)
                    mAddress += mAddresses.get(0).getAddressLine(index) + " ";
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e2) {
            // TODO: handle exception

            e2.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionSuspended(int i) {
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
    super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Permission Required")
                        .setMessage("This permission was denied earlier by you. This permission is required to get your locatin .So, in order to use this feature please allow this permission by clicking ok.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
//                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                                .findFragmentById(R.id.map);
//                        mapFragment.getMapAsync(this);
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getCurrentLocation();
        return false;
    }
    private void setToolBar(String title) {
//        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
//        setSupportActionBar(tb);
//
//        ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle(title);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Gson gson = new Gson();

            khaanavali.customer.model.Address address = gson.fromJson(intent.getStringExtra("locationaddress"), khaanavali.customer.model.Address.class);
            intent.putExtra("locationaddress", intent.getStringExtra("locationaddress"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
