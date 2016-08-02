package khaanavali.customer;



import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private DrawerLayout dLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        layout = (RelativeLayout) findViewById(R.id.layout);
        setNavigationDrawer();
        setToolBar();
        if (!checkNotificationListenerServiceRunning()) {
            startService(new Intent(this, NotificationListener.class));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
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
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
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

        transaction.commit();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment frag = null;
                int itemId = menuItem.getItemId();
            if (itemId == R.id.hotel) {
                    frag = new HotelFragment();
            }
            else if (itemId == R.id.about_knvl) {
                    frag = new AboutKhaanavali();
            }
            else if(itemId == R.id.status)
            {
                frag = new StatusTrackerFragment();
            }
            else if(itemId == R.id.invite)
            {
                frag = new ShareAppFragment();
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

        switch(itemId) {
            case android.R.id.home: {
                dLayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.menu_search: {
              //  Toast.makeText(getApplicationContext(), "menu selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, PlacesActivity.class);
                startActivityForResult(i,1);
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

    @Override
    public void onBackPressed() {
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}
