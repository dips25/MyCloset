package my.closet.fashion.style.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.TabsAdapter;
import my.closet.fashion.style.extra.SlidingTabLayout;
import my.closet.fashion.style.modesl.MyTabItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class SampleBottomsFragment extends Fragment {



    ArrayList<MyTabItem> myTabItems;
    SlidingTabLayout slidingTabLayout;
    ViewPager viewPager;

    public SampleBottomsFragment() {
        myTabItems = new ArrayList<>();
        myTabItems.add(new MyTabItem("Skirts",SkirtsFragment.class));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_sample_bottoms, container, false);
        //gridview = (GridView) v.findViewById(R.id.samplebottoms_gridview);
        slidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.bottoms_slidingtab);
        viewPager = (ViewPager) v.findViewById(R.id.bottoms_viewpager);
        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager(),myTabItems));
        slidingTabLayout.setViewPager(viewPager);

        return v;

    }



}
