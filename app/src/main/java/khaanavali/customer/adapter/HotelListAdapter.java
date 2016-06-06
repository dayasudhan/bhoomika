package khaanavali.customer.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import khaanavali.customer.R;
import khaanavali.customer.model.HotelDetail;

import java.util.ArrayList;


public class HotelListAdapter extends BaseAdapter{
    Activity con;
    Typeface cr;
    int layoutResID;
    private String[] city;
    private ArrayList<HotelDetail> mhotelList;
    int pos;

    // PreferenceManager mpref;

    public HotelListAdapter(Activity context, int layoutResourceID,
                            ArrayList<HotelDetail> hotelList) {

        con = context;
        mhotelList = hotelList;
        layoutResID = layoutResourceID;
        // this.cr=cr;

    }

    @Override
    public int getCount() {
        return mhotelList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        HotelItemHolder itemHolder;
        View view = convertView;
        //  mpref= PreferenceManager.instance(con);
        //  LocationListNameModel city = getItem(position);

        itemHolder = new HotelItemHolder();
        if(view==null){
            LayoutInflater inflater = ((Activity)con).getLayoutInflater();
            view = inflater.inflate(layoutResID, parent, false);


            itemHolder = new HotelItemHolder();
            itemHolder.hotelName= (TextView) view.findViewById(R.id.vendor_name);
            itemHolder.hotelDeliveryTime= (TextView) view.findViewById(R.id.vendor_delivery_time);
            itemHolder.hotelRating= (TextView) view.findViewById(R.id.vendor_rating);
            itemHolder.hotelSpeciality= (TextView) view.findViewById(R.id.vendor_speciality);
//            ratingbar1=(RatingBar)view.findViewById(R.id.ratingBar1);
//            ratingbar1.setNumStars(5);
//            ratingbar1.setRating(3);
            view.setTag(itemHolder);
        }else{
            itemHolder = (HotelItemHolder) view.getTag();
        }

        //   itemHolder.city.setTypeface(cr);
        itemHolder.hotelName.setText(mhotelList.get(position).getHotel().getName());
        itemHolder.hotelDeliveryTime.setText(String.valueOf(mhotelList.get(position).getDelivery_time()).concat(" mins"));
        itemHolder.hotelRating.setText(String.valueOf(mhotelList.get(position).getRating()).concat("/5*"));
        itemHolder.hotelSpeciality.setText(mhotelList.get(position).getSpeciality());
        return view;

    }
    /*public void update(List<LocationListNameModel> rowItems) {
        city=rowItems;
    }*/
    private static class HotelItemHolder {
        TextView hotelName;
        TextView hotelSpeciality;
        TextView hotelRating;
        TextView hotelDeliveryTime;
        TextView hotelDeliveryCharge;


    }
}