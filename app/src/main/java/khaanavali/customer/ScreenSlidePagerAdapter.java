package khaanavali.customer;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import khaanavali.customer.utils.SessionManager;

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private List<String> picList = new ArrayList<>();
    Context mContext;
    public ScreenSlidePagerAdapter(FragmentManager fm , Context context) {

        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        SessionManager session = new SessionManager(mContext);
        return ScreenSlidePageFragment.newInstance(session.getSlider().get(i));
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    public void addAll(List<String> picList) {
        this.picList = picList;
    }
}
