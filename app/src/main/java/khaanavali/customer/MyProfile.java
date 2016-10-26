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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import khaanavali.customer.adapter.AddressListAdapater;
import khaanavali.customer.model.Address;
import khaanavali.customer.model.FavouriteAddress;
import khaanavali.customer.utils.SessionManager;

/**
 * Created by Gagan on 8/27/2016.
 */
public class MyProfile extends Fragment {
    SessionManager session;
    boolean isTrue=true;
    Address address;
    List<android.location.Address> mAddresses;

    // ImageView headerCoverImage;
   ListView addresslistview;
    Button btnAddNewAddress;
    //    LocationAdapter dataAdapter;
    ArrayList<FavouriteAddress> mFavouriteAddressArrayList;
    AddressListAdapater addressListAdapater;
    Button save;
    EditText editTagLabel,editCity,editHouseNo,editAreaName,editLandmark,editAddress;

    ImageView coverPhoto,profilePhoto;
    TextView name,phno,email;
    View rootview;
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
         ((MainActivity) getActivity())
                .setActionBarTitle("My Profile");
        name=(TextView) rootview.findViewById(R.id.name);
        phno=(TextView) rootview.findViewById(R.id.phoneNumber);
        email=(TextView) rootview.findViewById(R.id.email);
     // addrOne=(TextView) rootview.findViewById(R.id.addressOne);
       // addrTwo=(TextView) rootview.findViewById(R.id.addressTwo);
        //addrThree=(TextView) rootview.findViewById(R.id.addressThree);
        session = new SessionManager(getActivity().getApplicationContext());
       // if(session.isLoggedIn()) {

        if (session.getName().trim().length() != 0) {
            name.setText(session.getName());
        }
        if (session.getKeyPhone().trim().length() != 0) {
            phno.setText(session.getKeyPhone());
        }
        if (session.getEmail().trim().length() != 0) {
               email.setText(session.getEmail());
           }
        if (session.getEmail().trim().length() != 0) {
            email.setText(session.getEmail());
        }


        btnAddNewAddress = (Button) rootview.findViewById(R.id.addAddress);

        mFavouriteAddressArrayList = new ArrayList<FavouriteAddress>();
        if(session.getFavoutrateAddress() !=null) {
            mFavouriteAddressArrayList = session.getFavoutrateAddress();
        }

        addressListAdapater = new AddressListAdapater(getActivity(),R.layout.address_list_item,mFavouriteAddressArrayList);
        addresslistview = (ListView) rootview.findViewById(R.id.listViewAddresProfile);
        addresslistview.setAdapter(addressListAdapater);
        addresslistview.setEmptyView(rootview.findViewById(R.id.emptyElement));

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                addressListAdapater.notifyDataSetChanged();
                handler.postDelayed( this, 1 * 1000 );
            }
        }, 60 * 1000 );

        addresslistview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        addresslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                builder.setTitle("Khaanavali");

                builder.setMessage("are you sure to edit yes/no");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int whch){
                        final Dialog dialog1 =new Dialog(getContext());
                        dialog1.setContentView(R.layout.add_address_layout);
                        save=(Button) dialog1.findViewById(R.id.saveAddressbutton);
                        editCity=(EditText)dialog1.findViewById(R.id.orderDetailEmail);
                        editHouseNo=(EditText)dialog1.findViewById(R.id.orderDetailAddress_house_no);
                        editAreaName=(EditText)dialog1.findViewById(R.id.orderDetailAddress_areaname);
                        editLandmark=(EditText)dialog1.findViewById(R.id.orderDetailAddress_landmark);
                        editAddress=(EditText)dialog1.findViewById(R.id.orderDetailAddress_address);
                        editTagLabel=(EditText)dialog1.findViewById(R.id.tag_address_label);
                        dialog1.show();
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String house =editHouseNo.getText().toString();
                                String addresss=editAddress.getText().toString();
                                String areaname =editAreaName.getText().toString();
                                String landmark =editLandmark.getText().toString();
                                String tagLabel =editTagLabel.getText().toString();
                                if(house.trim().length() == 0){
                                    //Toast.makeText(getApplicationContext(), "Enter House No or Flat No ", Toast.LENGTH_LONG).show();
                                    alertMessage("Enter House or Flat No ");
                                }
                                else if(addresss.trim().length() <= 0){
                                    //Toast.makeText(getApplicationContext(), "Enter areaname ", Toast.LENGTH_LONG).show();
                                    alertMessage("Enter adress ");
                                }
                                else if(areaname.trim().length() <= 0){
                                    //Toast.makeText(getApplicationContext(), "Enter Address ", Toast.LENGTH_LONG).show();
                                    alertMessage("Enter Areaname ");
                                }
                                else if(landmark.trim().length()<= 0){
                                    //Toast.makeText(getApplicationContext(), "Enter Landmark/locality ", Toast.LENGTH_LONG).show();
                                    alertMessage("Enter Landmark/locality ");
                                }

                                else if(tagLabel.trim().length() <= 0){
                                    //Toast.makeText(getApplicationContext(), "Enter City ", Toast.LENGTH_LONG).show();
                                    alertMessage("Enter lable for this address");
                                }
                                else{

                                    address = mFavouriteAddressArrayList.get(position).getAddress();
                                    address.setAreaName(editAreaName.getText().toString());
                                    address.setLandMark(editLandmark.getText().toString());
                                    address.setAddressLine1(editHouseNo.getText().toString());
                                    address.setAddressLine2(editAddress.getText().toString());
                                    address.setCity(editCity.getText().toString());
                                    if(mAddresses != null) {
                                        address.setZip(mAddresses.get(0).getPostalCode());
                                        address.setLatitude(String.valueOf(mAddresses.get(0).getLatitude()));
                                        address.setLongitude(String.valueOf(mAddresses.get(0).getLongitude()));
                                    }

                                    mFavouriteAddressArrayList.get(position).setLabel(editTagLabel.getText().toString());
                                    mFavouriteAddressArrayList.get(position).setAddress(address);
                                    SessionManager session = new SessionManager(getActivity().getApplicationContext());
                                    session.setFavoutrateAddress(mFavouriteAddressArrayList.get(position),position);
                                    Intent intent = new Intent();
                                    Gson gson = new Gson();
                                    String locationaddress = gson.toJson(address);
                                    intent.putExtra("locationaddress", locationaddress);
                                   // getActivity().setResult(getActivity().RESULT_OK, intent);
                                   // getActivity().finish();
                                    dialog1.cancel();
                                    addressListAdapater.notifyDataSetChanged();
                                }
                            }
                        });



                        Toast.makeText(getActivity(),"pressed yes",Toast.LENGTH_LONG).show();

                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        Toast.makeText(getActivity(),"pressed no",Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
                //edit
               // Address locaddress =  mFavouriteAddressArrayList.get(position).getAddress();
               // goBackwithAddress(locaddress);
            }
        });
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), MapsActivity.class);
                //startActivityForResult(i,1);
                Bundle b = new Bundle();
                b.putInt("key", 1); //Your id
                i.putExtras(b); //
                startActivity(i);
            }
        });

       //}
        coverPhoto=(ImageView) rootview.findViewById(R.id.header_cover_image);
        profilePhoto=(ImageView) rootview.findViewById(R.id.user_profile_photo);
        if(Image!=null)
        {
            profilePhoto.setImageBitmap(Image);
            coverPhoto.setImageBitmap(Image);
            coverPhoto.setImageAlpha(128);
          //  editCoverPhoto.setImageBitmap(Image);

        }

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




