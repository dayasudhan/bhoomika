package khaanavali.customer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

/**
 * Created by Gagan on 8/30/2016.
 */
public class Notification extends Fragment {

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.about_khaanavali, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Khaanavali");


        return rootview;
    }



}
