package khaanavali.customer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import khaanavali.customer.utils.Constants;


/**
 * Created by dganeshappa on 6/4/2016.
 */

public class AboutKhaanavali extends Fragment {

    View rootview;
    ImageView aboutUsWeb,faq,contactUs,tAndC;
    WebView popUpAboutUs,popUpFaq,popUpContactUs,popUpTandC;

    // Session Manager Class


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.about_khaanavali, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Khaanavali");
        aboutUsWeb=(ImageView) rootview.findViewById(R.id.aboutus);
        faq=(ImageView) rootview.findViewById(R.id.faq);
        contactUs=(ImageView) rootview.findViewById(R.id.contactus);
        tAndC=(ImageView) rootview.findViewById(R.id.tandc);

         aboutUsWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());

                popUpAboutUs =(WebView) dialog1.findViewById(R.id.popup);
                popUpAboutUs.loadUrl(Constants.ABOUT_US_URL);
                dialog1.show();
            }

            });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());

                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.FAQ_URL);

                dialog1.show();
            }

        });
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());

                popUpContactUs =(WebView) dialog1.findViewById(R.id.popup);
                popUpContactUs.loadUrl(Constants.CONTACT_US_URL);

                dialog1.show();
            }

        });
        tAndC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());

                popUpTandC =(WebView) dialog1.findViewById(R.id.popup);
                popUpTandC.loadUrl(Constants.TERMS_AND_CONDITION_URL);
                dialog1.show();
            }

        });






        return rootview;
    }



}
