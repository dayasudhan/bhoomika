package khaanavali.customer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

import khaanavali.customer.utils.SessionManager;

/**
 * Created by Gagan on 8/27/2016.
 */
public class MyProfile extends Fragment {
    SessionManager session;
    ImageView headerCoverImage;
    ImageView coverPhoto,profilePhoto;
    TextView name,phno,email;//addrOne,addrTwo,addrThree;
    EditText editName,editPhno,editEmail;//editAddrOne,editaddrTwo,editAddrThree;
    View rootview,editView;
    Button editButton;
    Button save;
    String nameText,phnoText,emailText;//addrOneText,addrTwoText,addrThreeText;
    // private int PICK_IMAGE_REQUEST = 1;
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
     private static final int GALLERY = 1;



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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.my_profile, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        ((MainActivity) getActivity())
                .setActionBarTitle("My Profile");

        name=(TextView) rootview.findViewById(R.id.name);
        phno=(TextView) rootview.findViewById(R.id.phoneNumber);
        email=(TextView) rootview.findViewById(R.id.email);
      // addrOne=(TextView) rootview.findViewById(R.id.addressOne);
       // addrTwo=(TextView) rootview.findViewById(R.id.addressTwo);
       // addrThree=(TextView) rootview.findViewById(R.id.addressThree);

        name.setText(session.getName());
        phno.setText(session.getKeyPhone());
        email.setText(session.getEmail());

        /*
        if(nameText!=null) {
            name.setText(nameText);
            editName.setText(nameText);
        }else{
            name.setText("Name");
           // editName.setText("Name");
        }
        if(phnoText!=null)
        {
           // editPhno.setText(phnoText);
            phno.setText(phnoText);
        }else{
          //  editPhno.setText("PhoneNumber");
            phno.setText("PhoneNumber");
        }
        if(emailText!=null)
        {
           // email.setText(emailText);
            editEmail.setText(emailText);
        }else{
            email.setText("PhoneNumber");
            //editEmail.setText("PhoneNumber");
        }*/

        /*  if(addrOneText!=null){
           // addrOne.setText(addrOneText);
        }else{
           // addrOne.setText("Android one");
        }
        if(addrTwoText!=null){
            addrTwo.setText(addrTwoText);
        }else{
            addrTwo.setText("Android Two");
        }
        if(addrThreeText!=null){
            addrThree.setText(addrThreeText);
        }else{
            addrThree.setText("Android Three");
        }*/

        coverPhoto=(ImageView) rootview.findViewById(R.id.header_cover_image);
         profilePhoto=(ImageView) rootview.findViewById(R.id.user_profile_photo);
        if(Image!=null)
        {
            profilePhoto.setImageBitmap(Image);
            coverPhoto.setImageBitmap(Image);
            coverPhoto.setImageAlpha(128);
          //  editCoverPhoto.setImageBitmap(Image);

        }


        //edit
        editButton=(Button) rootview.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop upPage;
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.my_profile_edit);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
               // dialog.getWindow().setBackgroundDrawable(
                 //       new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                editName =(EditText) dialog.findViewById(R.id.editName);
                editPhno =(EditText) dialog.findViewById(R.id.editPhoneNumber);
                editEmail=(EditText) dialog.findViewById(R.id.editEmail);
               /* editAddrOne =(EditText) dialog.findViewById(R.id.edotAddressOne);
                editaddrTwo =(EditText) dialog.findViewById(R.id.editAddressTwo);
                editAddrThree =(EditText) dialog.findViewById(R.id.editAddressThree);
                editCoverPhoto=(ImageView) dialog.findViewById(R.id.header_cover_image);*/
                Button save = (Button) dialog.findViewById(R.id.saveEdit);

               /* editCoverPhoto.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          if (Image != null)
                                                          {
                                                              profilePhoto.setImageBitmap(Image);
                                                              coverPhoto.setImageBitmap(Image);
                                                              coverPhoto.setImageAlpha(128);
                                                              //Image.recycle();
                                                          }
                                                          Intent intent = new Intent();
                                                          intent.setType("image/*");
                                                          intent.setAction(Intent.ACTION_GET_CONTENT);
                                                          startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
                                                      }
                                                  });*/
                        save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nameText = editName.getText().toString();
                        phnoText = editPhno.getText().toString();
                        emailText = editEmail.getText().toString();

                        if(nameText.trim().length()<=0) {
                            alertMessage1("enter the name");
                        }else if(phnoText.trim().length()<=0){
                            alertMessage1("enter the ph no");
                        }else if(emailText.trim().length()<=0){
                            alertMessage1("enter the email");
                        }else {


                            /*addrOneText=editAddrOne.getText().toString();
                            addrTwoText=editaddrTwo.getText().toString();
                            addrThreeText=editAddrThree.getText().toString();*/
                            session.setName(nameText);
                            session.setKeyPhone(phnoText);
                            session.setEmail(emailText);
                            if (nameText != null) {
                                name.setText(session.getName());
                            } else {
                                name.setText("Name");
                            }
                            if (phnoText != null) {
                                phno.setText(session.getKeyPhone());
                            } else {
                                phno.setText("PhoneNumber");
                            }
                            if (emailText != null) {
                                email.setText(session.getEmail());
                            } else {
                                email.setText("E-Mail");
                            }
                        }

                        dialog.dismiss();

                    }
                });

            }
        });






        return rootview;
    }
    public void alertMessage1(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Khaanavali");
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about);

        final AlertDialog dialog = builder.create();
        dialog.show(); //show() should be called before dialog.getButton().


        final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        neutralButton.setLayoutParams(positiveButtonLL);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            Uri mImageUri = data.getData();
            try {
                Image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                if (getOrientation(getActivity().getApplicationContext(), mImageUri) != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(getOrientation(getActivity().getApplicationContext(), mImageUri));
                    if (rotateImage != null)
                        rotateImage.recycle();
                    rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix,true);

                    profilePhoto.setImageBitmap(rotateImage);
                    coverPhoto.setImageBitmap(rotateImage);
                    coverPhoto.setImageAlpha(128);
                } else {
                    profilePhoto.setImageBitmap(Image);
                    coverPhoto.setImageBitmap(Image);
                    coverPhoto.setImageAlpha(128);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}




