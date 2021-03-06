package khaanavali.customer.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import khaanavali.customer.R;
import khaanavali.customer.model.HotelDetail;
import khaanavali.customer.utils.Constants;
import khaanavali.customer.utils.ImageLoader;


public class HotelListAdapter extends BaseAdapter{
    Activity con;
    Typeface cr;
    int layoutResID;
    private ArrayList<HotelDetail> mhotelList;
    public ImageLoader imageLoader;
    public HotelListAdapter(Activity context, int layoutResourceID,
                            ArrayList<HotelDetail> hotelList) {
        con = context;
        mhotelList = hotelList;
        layoutResID = layoutResourceID;
        imageLoader = new ImageLoader(con.getApplicationContext());
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
            itemHolder.hotellogo = (ImageView)view.findViewById(R.id.vendor_image_view);
            itemHolder.hotelIsClosed = (TextView) view.findViewById(R.id.textViewIsopen);
//            ratingbar1=(RatingBar)view.findViewById(R.id.ratingBar1);
//            ratingbar1.setNumStars(5);
//            ratingbar1.setRating(3);
            view.setTag(itemHolder);
        }else{
            itemHolder = (HotelItemHolder) view.getTag();
        }

        //   itemHolder.city.setTypeface(cr);
        itemHolder.hotelName.setText(mhotelList.get(position).getHotel().getName());
        itemHolder.hotelDeliveryTime.setText(String.valueOf(mhotelList.get(position).getDeliveryTime()).concat(" mins"));
        itemHolder.hotelRating.setText(String.valueOf(mhotelList.get(position).getRating()).concat("/5*"));
        itemHolder.hotelSpeciality.setText(mhotelList.get(position).getSpeciality());
        if(mhotelList.get(position).getIsOpen() == 0)
        {
            itemHolder.hotelIsClosed.setText("Closed now");
        }
        else
        {
            itemHolder.hotelIsClosed.setText("");
        }

        String image_url = Constants.MAIN_URL + '/' + mhotelList.get(position).getHotel().getLogo();
        imageLoader.DisplayImage(image_url,  itemHolder.hotellogo);
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
        TextView hotelIsClosed;
        ImageView hotellogo;

    }
}