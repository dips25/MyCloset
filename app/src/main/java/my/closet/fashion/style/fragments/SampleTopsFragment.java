package my.closet.fashion.style.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.TabsAdapter;
import my.closet.fashion.style.extra.SlidingTabLayout;
import my.closet.fashion.style.modesl.MyTabItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SampleTopsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SampleTopsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<MyTabItem> tabItems;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridView gridview;
    ProgressDialog progressDialog;
    private Query query;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;


    public SampleTopsFragment() {
        tabItems = new ArrayList<>();
        tabItems.add(new MyTabItem("Tees",TeesFragment.class));
        tabItems.add(new MyTabItem("Shirts",ShirtsFragment.class));
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SampleTopsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SampleTopsFragment newInstance(String param1, String param2) {
        SampleTopsFragment fragment = new SampleTopsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sample_tops, container, false);
        slidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.tops_slidingtab);
        viewPager = (ViewPager) v.findViewById(R.id.tops_viewpager);
        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager(),tabItems));
        slidingTabLayout.setViewPager(viewPager);

        return v;


    }



}
