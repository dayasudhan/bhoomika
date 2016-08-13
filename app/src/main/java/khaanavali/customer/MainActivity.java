package khaanavali.customer;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.splunk.mint.Mint;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout dLayout;

    private boolean ishotelFragmentOpen;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_main_nav);
        ishotelFragmentOpen = true;
        //gaganwelcome
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //gaganwelcome
        //gagan internet
        if (!isOnline(MainActivity.this))
        {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });

                alertDialog.show();
            }
            catch(Exception e)
            {

            }
        }


        //gagan internet


        setNavigationDrawer();
        setToolBar();
        if (!checkNotificationListenerServiceRunning()) {
            startService(new Intent(this, NotificationListener.class));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public boolean checkNotificationListenerServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("khaanavali.customer.NotificationListener"
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // extract data

            String areaClicked = new String(intent.getStringExtra("area"));
            HotelFragment fragment = (HotelFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
            fragment.getHotelList(areaClicked);
            //getHotelList(areaClicked);
        }
    }

    private void setToolBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //    transaction.addToBackStack(null);
        transaction.replace(R.id.frame, new HotelFragment());
        ishotelFragmentOpen = true;
        transaction.commit();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment frag = null;
                int itemId = menuItem.getItemId();
                if (itemId == R.id.hotel) {
                    frag = new HotelFragment();

                    ((HotelFragment) frag).setBulk(false);
                    ishotelFragmentOpen = true;

                } else if (itemId == R.id.bulk_activity) {
                    frag = new HotelFragment();
                    ((HotelFragment) frag).setBulk(true);
                    ishotelFragmentOpen = true;
                } else if (itemId == R.id.about_knvl) {

                    frag = new AboutKhaanavali();
                    ishotelFragmentOpen = false;

                } else if (itemId == R.id.status) {
                    frag = new StatusTrackerFragment();
                    ishotelFragmentOpen = false;
                } else if (itemId == R.id.invite) {
                    frag = new ShareAppFragment();
                    ishotelFragmentOpen = false;
                }
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frame, frag);
//                if(itemId != R.id.location) {
//                    transaction.addToBackStack(null);
//                }
                    transaction.commit();

                    dLayout.closeDrawers();
                    return true;
                }

                return false;
            }
        });
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String btnName = null;

        switch (itemId) {
            case android.R.id.home: {
                dLayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.menu_search: {
                //  Toast.makeText(getApplicationContext(), "menu selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, PlacesActivity.class);
                startActivityForResult(i, 1);

                return true;
            }
        }
        return true;
    }

    //
//    @Override
//    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.home_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else if (ishotelFragmentOpen == false) {
            dLayout.openDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://khaanavali.customer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://khaanavali.customer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
