package my.closet.fashion.style.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import my.closet.fashion.style.CustomView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Realm_database.MyMigration;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.adapters.DefaultFollowersAdapter;
import my.closet.fashion.style.fragments.ClosetFragment;
import my.closet.fashion.style.fragments.HomeFragment;
import my.closet.fashion.style.fragments.LookbookFragment;
import my.closet.fashion.style.fragments.MyProfileFragment;
import my.closet.fashion.style.modesl.DefaultFollowers;
import my.closet.fashion.style.modesl.Dresses;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

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
    TextView ok_text;

    ArrayList<Dresses> meta = new ArrayList<>();



    public static LinearLayout looktext;

    ArrayList<Integer> names = new ArrayList<>();
    ArrayList<Integer> topnames = new ArrayList<>();
    ArrayList<Integer> bottomnames = new ArrayList<>();
    ArrayList<Integer> footnames = new ArrayList<>();
    public static RelativeLayout look_tab;

    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA,
    };
    private List<DefaultFollowers> defaultFollowers;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
    KonfettiView konfettiView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this,new Crashlytics());
        setContentView( R.layout.activity_home);





        Realm.init(this);
        final RealmConfiguration configuration = new RealmConfiguration.Builder().name("sample.realm").schemaVersion(1).migration(new MyMigration()).build();

        Realm.setDefaultConfiguration(configuration);
//        Realm.getInstance(configuration);

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

        konfettiView = (KonfettiView) findViewById(R.id.viewKonfetti);
        konfettiView.setVisibility(View.GONE);


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

                                            if (mixpanelAPI!=null) {

                                                mixpanelAPI.track("Create Look Tab");
                                            }

                                            names = madapter.getItems();
                                            topnames = mtopadapter.getItems();
                                            bottomnames = mbottomadapter.getItems();
                                            footnames = mfootwearadapter.getItems();

                                            Intent intent = new Intent(HomeActivity.this, CustomView.class);
                                            //bundle.putStringArrayList();
                                            intent.putIntegerArrayListExtra("acckey", names);
                                            intent.putIntegerArrayListExtra("topkey", topnames);
                                            intent.putIntegerArrayListExtra("bottomkey", bottomnames);
                                            intent.putIntegerArrayListExtra("footkey", footnames);
                                            startActivity(intent);


                                        }
                                    });

                bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    private boolean firstClick = true;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

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


       // loadFragment(new HomeFragment());
     /*   new MaterialTapTargetPrompt.Builder(new HomeFragment())
                .setTarget(R.id.upload_menu)
                .setSecondaryText("Click Here to add Clothes")
                .setIcon(R.drawable.ic_add)
                .setFocalPadding(R.dimen.dp40)
                .setAnimationInterpolator(new LinearOutSlowInInterpolator())

                .show(); */


    }

    @Override
    protected void onStart() {
        super.onStart();
       // int i = getIntent().getExtras().getInt("position");
        SharedPreferences sharedPreferences = getSharedPreferences("preference",MODE_PRIVATE);
        boolean dialog = sharedPreferences.getBoolean("dialog",true);

        if (dialog){

            FirebaseFirestore.getInstance().collection("Default Followers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        defaultFollowers = queryDocumentSnapshots.toObjects(DefaultFollowers.class);
                        open_dialog(defaultFollowers);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {



                }
            });


        }

        if (getIntent()!=null){

            if (Objects.requireNonNull(getIntent().getExtras()).getInt("position")==2) {

                if (getIntent().getExtras().containsKey("celeb")){

                    konfettiView.setVisibility(View.VISIBLE);

                    konfettiView.build()
                            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                            .setDirection(0.0, 359.0)
                            .setSpeed(1f, 5f)
                            .setFadeOutEnabled(true)
                            .setTimeToLive(2000L)
                            .addShapes(Shape.RECT, Shape.CIRCLE)
                            .addSizes(new Size(12,5f))
                            .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                            .stream(300, 5000L);

                    loadFragment(new ClosetFragment());




                }else {

                    loadFragment(new ClosetFragment());
                }

            } else if (getIntent().getExtras().getInt("position") == 3){

                loadFragment(new LookbookFragment());


            } else {

                loadFragment(new HomeFragment());
            }
        }else {

           // loadFragment(new HomeFragment());
        }
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




    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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

    private void open_dialog(List<DefaultFollowers> defaultFollowers) {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_listview_deaultusers,null);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.dialog_list_default_followers);
        listView.setLayoutManager(linearLayoutManager);
        ok_text = (TextView) view.findViewById(R.id.ok_text);
        DefaultFollowersAdapter defaultFollowersAdapter = new DefaultFollowersAdapter(this,defaultFollowers);
        listView.setAdapter(defaultFollowersAdapter);
        listView.setHasFixedSize(true);

        defaultFollowersAdapter.notifyDataSetChanged();
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);



        ok_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("dialog",false);
                editor.apply();

                dialog.dismiss();

            }
        });


    }


}
