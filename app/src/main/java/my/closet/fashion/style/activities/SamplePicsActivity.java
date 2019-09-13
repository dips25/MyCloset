package my.closet.fashion.style.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.TabsAdapter;
import my.closet.fashion.style.extra.SlidingTabLayout;
import my.closet.fashion.style.fragments.SampleAccessoriesFragment;
import my.closet.fashion.style.fragments.SampleBottomsFragment;
import my.closet.fashion.style.fragments.SampleFootwearFragment;
import my.closet.fashion.style.fragments.SampleTopsFragment;
import my.closet.fashion.style.modesl.MyTabItem;

public class SamplePicsActivity extends AppCompatActivity {

    SlidingTabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samplepics);

        toolbar = (Toolbar)findViewById(R.id.samplepic_toolbar);
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(null);
        }

        toolbar_title = (TextView) findViewById(R.id.title);
        if (toolbar_title!=null) {
            toolbar_title.setText(R.string.sample_pics_heading);
        }

        ArrayList<MyTabItem> mainMenuItems = new ArrayList<>();
        mainMenuItems.add(new MyTabItem("Tops", SampleTopsFragment.class));
        mainMenuItems.add(new MyTabItem("Bottoms", SampleBottomsFragment.class));
        mainMenuItems.add(new MyTabItem("Footwear", SampleFootwearFragment.class));
        mainMenuItems.add(new MyTabItem("Accessories", SampleAccessoriesFragment.class));


        tabLayout = (SlidingTabLayout) findViewById(R.id.samplepics_tab);
        viewPager = (ViewPager) findViewById(R.id.samplepics_viewpager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(),mainMenuItems));

        tabLayout.setViewPager(viewPager);

        if (getSharedPreferences("prefs",MODE_PRIVATE).getBoolean("secondtimelaunch",true)
                && !getSharedPreferences("prefs",MODE_PRIVATE).getBoolean("firsttimelaunch",true) ){

            viewPager.setCurrentItem(1);

            SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            boolean firstsamplebottom = sharedPreferences.getBoolean("firstsamplebottom",true);
        }

       // setupViewpager();


    }

    private void setupViewpager() {


       /* SamplePicsViewPagerAdapter samplePicsViewPagerAdapter = new SamplePicsViewPagerAdapter(getSupportFragmentManager());
        samplePicsViewPagerAdapter.addFragment(new SampleAccessoriesFragment(),"Accessories");
        samplePicsViewPagerAdapter.addFragment(new SampleTopsFragment(),"Tops");
        samplePicsViewPagerAdapter.addFragment(new SampleBottomsFragment(),"Bottoms");
        samplePicsViewPagerAdapter.addFragment(new SampleFootwearFragment(),"Footwear");
        viewPager.setAdapter(samplePicsViewPagerAdapter); */
    }
}
