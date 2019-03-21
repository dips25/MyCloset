package my.closet.fashion.style.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import my.closet.fashion.style.CustomView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.fragments.ClosetFragment;
import my.closet.fashion.style.fragments.HomeFragment;
import my.closet.fashion.style.fragments.LookbookFragment;
import my.closet.fashion.style.fragments.MyProfileFragment;
import my.closet.fashion.style.modesl.Dresses;

import static my.closet.fashion.style.fragments.ClosetFragment.clearrefresh;
import static my.closet.fashion.style.fragments.ClosetFragment.madapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mbottomadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mfootwearadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mtopadapter;

public class HomeActivity extends AppCompatActivity {

    private static final int PERMISSION_ALL = 1111;
    Realm realm;
    private File rootDir;
    private DisplayMetrics dm;
    private static FragmentManager manager;
    private MixpanelAPI mixpanelAPI;
    private FragmentTransaction transaction;
    public static BottomNavigationView bottomnav;
    public static DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public TextView title;
    Intent i;
    ImageView cross;

    private LinearLayout looktext;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> topnames = new ArrayList<>();
    ArrayList<String> bottomnames = new ArrayList<>();
    ArrayList<String> footnames = new ArrayList<>();
    public static RelativeLayout look_tab;

    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_home);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        displayActionBar();
        ActionBar avy = getSupportActionBar();
        Objects.requireNonNull(avy).setTitle(null);

        cross = (ImageView) findViewById(R.id.cross);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clearrefresh();
                look_tab.setVisibility(View.GONE);
                bottomnav.setVisibility(View.VISIBLE);
            }
        });


        mixpanelAPI= MixpanelAPI.getInstance(HomeActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refreshedToken = instanceIdResult.getToken();
                //  Utilities.showToast(MainActivity.this,refreshedToken);
                Utilities.savePref(HomeActivity.this,"Fcm_Token",refreshedToken);
            }
        });
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close){

            @Override
            public void onDrawerClosed(View view) {

                //fab_plus.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                //fab_plus.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //fab_plus.setAlpha(1-slideOffset);
            }



        };
      //  toggle.setDrawerIndicatorEnabled(false);
       // toolbar.setNavigationIcon(R.drawable.ic_toggle_icon);
       /* if (Utilities.getBooleanPref(getApplicationContext(), "HasLogged_In", false)) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });

        } else {
            toolbar.setNavigationIcon(null);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }*/


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        rootDir = new File(Environment.getExternalStorageDirectory() + "/MyCloset");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        Utilities.picturePath = rootDir.toString();

        dm = new DisplayMetrics();
        HomeActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Utilities.screenWidth = dm.widthPixels;
        Utilities.screenHeight = dm.heightPixels;

        manager = getSupportFragmentManager();

        bottomnav = (BottomNavigationView) findViewById(R.id.bnve_icon_selector);

        look_tab = (RelativeLayout) findViewById(R.id.linear);
        looktext = (LinearLayout) findViewById(R.id.create_lay);

        looktext.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            mixpanelAPI.track("Create Look Tab");

                                            names = madapter.getItems();
                                            topnames = mtopadapter.getItems();
                                            bottomnames = mbottomadapter.getItems();
                                            footnames = mfootwearadapter.getItems();

                                            Intent intent = new Intent(HomeActivity.this, CustomView.class);
                                            //bundle.putStringArrayList();
                                            intent.putExtra("acckey", names);
                                            intent.putExtra("topkey", topnames);
                                            intent.putExtra("bottomkey", bottomnames);
                                            intent.putExtra("footkey", footnames);
                                            startActivity(intent);


                                        }
                                    });

                bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    private boolean firstClick = true;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        if (firstClick) {
                            int position = item.getItemId();
                            if (position == R.id.home) {
                                //firstClick = false;

                                mixpanelAPI.track("Home");
                                HomeFragment homeFragment = new HomeFragment();
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.container, homeFragment);
                                transaction.addToBackStack("HomeFragment");
                                transaction.commit();
                                closeDrawer();
                                return true;

                            } else if (position == R.id.closet) {
                                mixpanelAPI.track("Closet");
                                ClosetFragment closetFragment = new ClosetFragment();
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.container, closetFragment);
                                transaction.addToBackStack("ClosetFragment");
                                transaction.commit();
                                closeDrawer();
                                return true;
                            } else if (position == R.id.lookbook) {

                                mixpanelAPI.track("Lookbook");
                                LookbookFragment lookbookFragment = new LookbookFragment();
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.container, lookbookFragment);
                                transaction.addToBackStack("LookbookFragment");
                                transaction.commit();
                                closeDrawer();
                                return true;

                            } else if (position == R.id.profile) {

                                mixpanelAPI.track("Profile");
                                if (!Utilities.loadPref(HomeActivity.this, "email", "").equalsIgnoreCase("")) {
                                    MyProfileFragment myProfileFragment = new MyProfileFragment();
                                    transaction = manager.beginTransaction();
                                    transaction.replace(R.id.container, myProfileFragment);
                                    transaction.addToBackStack("MyProfileFragment");
                                    transaction.commit();
                                    closeDrawer();
                                    return true;

                                } else {
                                    Intent ii = new Intent(HomeActivity.this, FbGmailActivity.class);
                                    startActivity(ii);
                                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                                    //  closeDrawer();
                                }

                            }
                        }
                        // do other
                        return true;
                    }
                });


        loadFragment(new HomeFragment());

    }

    private void loadFragment(Fragment fragment) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
      //  closeDrawer();
    }

    public void onClickcalled() {

        RealmResults<Dresses> realmcheck = realm.where(Dresses.class).findAll();

        for (Dresses dresses : realmcheck) {

            if (dresses.isSelected()) {
                bottomnav.setVisibility(View.GONE);
                look_tab.setVisibility(View.VISIBLE);
                break;
            } else {

                look_tab.setVisibility(View.INVISIBLE);
                bottomnav.setVisibility(View.VISIBLE);
            }
        }
    }



    public void closeDrawer() {
       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    public void displayActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_search:

                // Not implemented here
                return false;

            case R.id.filter:

               //Not implemented here
                return false;

            default:
                break;
        }

        return false;
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                } else {

                }
                return false;
            }
        }
        return true;
    }

   }
