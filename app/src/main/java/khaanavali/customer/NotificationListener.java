package khaanavali.customer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;

import khaanavali.customer.utils.Constants;
import khaanavali.customer.utils.SessionManager;


public class NotificationListener extends Service {

    SessionManager session;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Firebase.setAndroidContext(getApplicationContext());
        //Creating a firebase object
        Firebase firebase = new Firebase(Constants.FIREBASE_APP + '/' + "customer");
        session = new SessionManager(getApplicationContext());
        //Adding a valueevent listener to firebase
        //this will help us to  track the value changes on firebase
        firebase.addValueEventListener(new ValueEventListener() {

            //This method is called whenever we change the value in firebase
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.child("update").exists()) {
                    String msg = snapshot.child("update").getValue().toString();
                    showNotification(Calendar.getInstance().getTimeInMillis(), msg, 2);
                } else if (snapshot.child("info").exists()) {
                    String msg = snapshot.child("info").getValue().toString();
                    showNotification(Calendar.getInstance().getTimeInMillis(), msg, 3);
                }
                else if (session.getCurrentOrderId() != null && snapshot.child(session.getCurrentOrderId()).exists()) {
                    String msg = snapshot.child(session.getCurrentOrderId()).getValue().toString();
                    showNotification(Calendar.getInstance().getTimeInMillis(), msg, 1);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });

        return START_STICKY;
    }


    private void showNotification(long when, String msg, int intent_type) {
        //Creating a notification
        final String GROUP_KEY_ORDER_IDS = "group_order_ids";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent;
//        if (intent_type == 1 )
//        {
//            intent = new Intent(getApplicationContext(), StatusTrackerFragment.class);
//        }
        if (intent_type == 1 )
        {
            String status = msg.substring(msg.indexOf(" - ") + 3);
            if(status.equals("DELIVERED")) {
                intent = new Intent(getApplicationContext(), VendorRating.class);
                String hotelID = msg.substring(msg.indexOf("H"),msg.indexOf("R"));
                intent.putExtra("hotelId",hotelID);
            }
            else
            {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            }
        }
        else if(intent_type == 3)
            intent = new Intent(getApplicationContext(), MainActivity.class);
        else
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=khaanavali.customer"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Khaanavali");
        builder.setContentText(msg);
        builder.setAutoCancel(true);
        builder.setWhen(when);
//        builder.setGroup(GROUP_KEY_ORDER_IDS);
//        builder.setGroupSummary(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
