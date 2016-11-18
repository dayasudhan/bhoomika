package khaanavali.customer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import khaanavali.customer.R;
import khaanavali.customer.model.FavouriteAddress;
import khaanavali.customer.utils.SessionManager;


/**
 * Created by dganeshappa on 7/26/2016.
 */
public class AddressListAdapater extends BaseAdapter {
    Activity mContext;
    int mLayoutResID;
    List<android.location.Address> mAddresses;
    int pos;
    SessionManager session;
   public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_action_about).show();

    }

    public void setmFavouriteAddressArrayList(ArrayList<FavouriteAddress> favouriteAddressArrayList) {
        mFavouriteAddressArrayList.clear();
        mFavouriteAddressArrayList = (ArrayList<FavouriteAddress>) favouriteAddressArrayList.clone();

        notifyDataSetChanged();
    }

    ArrayList<FavouriteAddress> mFavouriteAddressArrayList;
    public AddressListAdapater(Activity context, int layoutResourceID,
                               ArrayList<FavouriteAddress> favouriteAddressArrayList) {
        mContext = context;
        mLayoutResID = layoutResourceID;
        mFavouriteAddressArrayList = favouriteAddressArrayList;

    }

    @Override
    public int getCount() {
        return mFavouriteAddressArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        pos=position;
        session=new SessionManager(mContext);
        ItemHolder itemHolder = new ItemHolder();
        if(view == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            view = inflater.inflate(mLayoutResID, parent, false);
            itemHolder.addressdetail = (TextView)view.findViewById(R.id.address_detail);
            itemHolder.addressLabel = (TextView)view.findViewById(R.id.address_label);
            view.setTag(itemHolder);
         }
        else{
            itemHolder = (ItemHolder) view.getTag();
        }
        itemHolder.addressLabel.setText(String.valueOf(mFavouriteAddressArrayList.get(position).getLabel()));
        itemHolder.addressdetail.setText(String.valueOf(mFavouriteAddressArrayList.get(position).getAddress().toString()));
       final String mes= itemHolder.addressLabel.getText().toString();



        return view;
    }
    private static class ItemHolder {
        TextView addressLabel;
        TextView addressdetail;
        ImageView hotellogo;


    }



}