package khaanavali.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * Created by dganeshappa on 6/4/2016.
 */

public class AboutKhaanavali extends Fragment {

    View rootview;
    // Session Manager Class
    // Button Logout
    ImageButton facebook;
    ImageButton gmail;
    TextView webPage;

    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about).show();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.about_khaanavali, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Khaanavali");
        facebook = (ImageButton) rootview.findViewById(R.id.facebook);
        gmail = (ImageButton) rootview.findViewById(R.id.gmail);
        webPage=(TextView) rootview.findViewById(R.id.webPage);
        try {
            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    goToUrl("http://www.facebook.com/khaanavali/");
                }
            });
            gmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    goToUrl("http://gmail.com");
                }
            });
            webPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    goToUrl("http://www.khaanavali.com");
                }
            });

        }
        catch (Exception e)
        {
            alertMessage("Please check your internet connection");
        }


        return rootview;
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
