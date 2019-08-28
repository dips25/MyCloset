package my.closet.fashion.style.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.FollowerFollowingViewPagerAdapter;
import my.closet.fashion.style.fragments.FollowersFragment;
import my.closet.fashion.style.fragments.FollowingFragment;

public class FollowerFollowingViewActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Intent intent;
    String key;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_following);




        intent = getIntent();
        key = Objects.requireNonNull(getIntent().getExtras()).getString("key");

        toolbar = (Toolbar) findViewById(R.id.follower_following_toolbar);
       // toolbar.setTitle(intent.getStringExtra("name"));
       // toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Objects.requireNonNull(intent.getExtras()).getString("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().isTitleTruncated();









        tabLayout  = (TabLayout) findViewById(R.id.follower_following_tab);
        viewPager = (ViewPager) findViewById(R.id.follower_following_viewpager);

        tabLayout.setupWithViewPager(viewPager);

        setupViewpager();
    }

    private void setupViewpager() {

        FollowerFollowingViewPagerAdapter followerFollowingViewPagerAdapter =
                new FollowerFollowingViewPagerAdapter(getSupportFragmentManager());

        followerFollowingViewPagerAdapter.addFragment(new FollowersFragment(key),"Followers");
        followerFollowingViewPagerAdapter.addFragment(new FollowingFragment(key),"Following");

        viewPager.setAdapter(followerFollowingViewPagerAdapter);
    }
}
