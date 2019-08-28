package my.closet.fashion.style.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import my.closet.fashion.style.modesl.MyTabItem;

/**
 * Created by charles on 14/09/15.
 */
public class TabsAdapter
    extends FragmentPagerAdapter {


        private ArrayList<MyTabItem> items;

        public TabsAdapter(FragmentManager fm, ArrayList<MyTabItem> items) {
            super(fm);
            this.items = items;
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position).getFragment();
        }



    @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items.get(position).getTitle();
        }
    }
