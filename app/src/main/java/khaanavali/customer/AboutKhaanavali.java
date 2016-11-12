package khaanavali.customer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import khaanavali.customer.utils.Constants;


/**
 * Created by dganeshappa on 6/4/2016.
 */

public class AboutKhaanavali extends Fragment {

    View rootview;
    Button faq,aboutus,contactUs,tandC;
    WebView popUpFaq;
    // Session Manager Class


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.about_khaanavali, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Khaanavali");
        faq=(Button) rootview.findViewById(R.id.faq);
        aboutus=(Button) rootview.findViewById(R.id.aboutus);
        tandC=(Button) rootview.findViewById(R.id.tandc);
        contactUs=(Button) rootview.findViewById(R.id.contactus);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());
                dialog1.setContentView(R.layout.aboutuspopup);
                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.ABOUT_US_URL);

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

                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.CONTACT_US_URL);

                dialog1.show();
            }

        });
        tandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1 =new Dialog(getContext());

                popUpFaq =(WebView) dialog1.findViewById(R.id.popup);
                popUpFaq.loadUrl(Constants.TERMS_AND_CONDITION_URL);

                dialog1.show();
            }

        });





        return rootview;
    }



}
