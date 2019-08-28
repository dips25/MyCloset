package my.closet.fashion.style.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class FollowerFollowingViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> mtitle = new ArrayList<>();
    ArrayList<Fragment> mfragments = new ArrayList<>();

    public FollowerFollowingViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mfragments.get(position);
    }

    @Override
    public int getCount() {
        return mfragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mtitle.get(position);
    }

    public void addFragment(Fragment fragment,String title){

        mtitle.add(title);
        mfragments.add(fragment);


    }
}
