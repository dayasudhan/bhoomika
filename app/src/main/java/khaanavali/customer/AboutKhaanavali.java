package khaanavali.customer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


/**
 * Created by dganeshappa on 6/4/2016.
 */

public class AboutKhaanavali extends Fragment {

    View rootview;
    Button faq,legal,feedback;
    // Session Manager Class


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.about_khaanavali, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("About Khaanavali");
        faq=(Button) rootview.findViewById(R.id.faq);
        legal=(Button) rootview.findViewById(R.id.legal);
        feedback=(Button) rootview.findViewById(R.id.feedback);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getActivity(),Faq.class);
                startActivity(i);
            }

            });
        legal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.help_legal);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                // dialog.getWindow().setBackgroundDrawable(
                //       new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.help_feedback);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                // dialog.getWindow().setBackgroundDrawable(
                //       new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });





        return rootview;
    }



}
