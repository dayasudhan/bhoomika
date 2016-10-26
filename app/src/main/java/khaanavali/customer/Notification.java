package khaanavali.customer;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import khaanavali.customer.utils.Constants;

/**
 * Created by Gagan on 8/30/2016.
 */
public class Notification extends Fragment {

    View rootview;
    ImageView notifImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.notification, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("Offers");
        final Dialog dialog1 =new Dialog(getContext());
        dialog1.setContentView(R.layout.notification);
        notifImage=(ImageView)dialog1.findViewById(R.id.notifimg);
        Picasso.with(getContext()).load(Constants.NOTIFICATION_URL).into(notifImage);
        dialog1.show();

        return rootview;
    }




}
