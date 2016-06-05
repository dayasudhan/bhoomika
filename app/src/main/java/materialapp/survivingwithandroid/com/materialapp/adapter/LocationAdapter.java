package materialapp.survivingwithandroid.com.materialapp.adapter;


import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import materialapp.survivingwithandroid.com.materialapp.R;

import java.util.ArrayList;


public class LocationAdapter extends BaseAdapter{
    Activity con;
    Typeface cr;
    int layoutResID;
    int pos;
    private ArrayList<String> mCityCoverage;
    // PreferenceManager mpref;

    public LocationAdapter(Activity context, int layoutResourceID,
                           String[] cityList ,ArrayList<String> cityCoverage) {
       con = context;
        layoutResID = layoutResourceID;
        mCityCoverage = cityCoverage;
    }

    @Override
    public int getCount() {
        return mCityCoverage.size();
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
        CityItemHolder itemHolder;
        View view = convertView;
        //  mpref= PreferenceManager.instance(con);
      //  LocationListNameModel city = getItem(position);

        itemHolder = new CityItemHolder();
        if(view==null){
            LayoutInflater inflater = ((Activity)con).getLayoutInflater();
            view = inflater.inflate(layoutResID, parent, false);


            itemHolder = new CityItemHolder();
            itemHolder.city= (TextView) view.findViewById(R.id.location_name);

            view.setTag(itemHolder);
        }else{
            itemHolder = (CityItemHolder) view.getTag();
        }

     //   itemHolder.city.setTypeface(cr);
        itemHolder.city.setText(mCityCoverage.get(position));



        return view;

    }
    /*public void update(List<LocationListNameModel> rowItems) {
        city=rowItems;
    }*/
    private static class CityItemHolder {
        TextView city;
    }
}
