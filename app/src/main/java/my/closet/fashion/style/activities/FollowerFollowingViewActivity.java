package my.closet.fashion.style.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    TextView toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_following);




        intent = getIntent();
        key = Objects.requireNonNull(getIntent().getExtras()).getString("key");

        toolbar = (Toolbar) findViewById(R.id.follower_following_toolbar);
        toolbar_text = (TextView) findViewById(R.id.follower_following_toolbar_text);
       // toolbar.setTitle(intent.getStringExtra("name"));
       // toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);
        toolbar_text.setText(Objects.requireNonNull(intent.getExtras()).getString("name"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        toolbar.isTitleTruncated();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });




        tabLayout  = (TabLayout) findViewById(R.id.follower_following_tab);
        viewPager = (ViewPager) findViewById(R.id.follower_following_viewpager);

        tabLayout.setupWithViewPager(viewPager);

        setupViewpager();
    }

    private void setupViewpager() {

        FollowerFollowingViewPagerAdapter followerFollowingViewPagerAdapter =
                new FollowerFollowingViewPagerAdapter(getSupportFragmentManager());

        followerFollowingViewPagerAdapter.addFragment(new FollowersFragment(key),getResources().getString(R.string.followers));
        followerFollowingViewPagerAdapter.addFragment(new FollowingFragment(key),getResources().getString(R.string.following));

        viewPager.setAdapter(followerFollowingViewPagerAdapter);
    }
}
