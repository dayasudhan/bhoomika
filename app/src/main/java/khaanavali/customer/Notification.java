package khaanavali.customer;


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
        notifImage=(ImageView)rootview.findViewById(R.id.notifimg);
        Picasso.with(getContext()).load(Constants.SLIDER_URL1).into(notifImage);
        notifImage.
        //notifImage.setImageURI(Uri.parse("http://l.yimg.com/a/i/us/we/52/21.gif"));
        return rootview;
    }




}
