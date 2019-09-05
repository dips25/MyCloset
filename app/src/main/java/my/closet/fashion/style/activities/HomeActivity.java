package my.closet.fashion.style.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Realm_database.MyMigration;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.adapters.DefaultFollowersAdapter;
import my.closet.fashion.style.fragments.ClosetFragment;
import my.closet.fashion.style.fragments.HomeFragment;
import my.closet.fashion.style.fragments.MyProfileFragment;
import my.closet.fashion.style.modesl.DefaultFollowers;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.FeedResponse;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class HomeActivity extends AppCompatActivity {

    private static final int PERMISSION_ALL = 1111;
    private static final int ACTIVITY_INDEX = 0;

    Realm realm;
    private File rootDir;
    private DisplayMetrics dm;
    private static FragmentManager manager;
    private MixpanelAPI mixpanelAPI;
    private FragmentTransaction transaction;
    public  BottomNavigationView bottomnav;
    public static DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public TextView title;
    Intent i;
    ImageView cross;
    TextView ok_text;

    ArrayList<Dresses> meta = new ArrayList<>();




    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA,
    };
    private List<DefaultFollowers> defaultFollowers;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
    KonfettiView konfettiView;
    private Button follow_all;
    private String My_Dbkey;
    private String my_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this,new Crashlytics());
        setContentView( R.layout.activity_home);

        My_Dbkey = Utilities.loadPref(this, "My_DbKey", "");;
        my_email = Utilities.loadPref(HomeActivity.this,"email","");





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

       /* final Fragment fragment1 = new HomeFragment();
        final Fragment fragment2 = new ClosetFragment();
        final Fragment fragment3 = new LookbookFragment();
        final Fragment fragment4 = new MyProfileFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment[] active_fragment = {fragment1}; */

       /* fragmentManager.beginTransaction().add(R.id.container,fragment1,"1").commit();
        fragmentManager.beginTransaction().add(R.id.container,fragment2,"2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.container,fragment3,"3").hide(fragment3).commit();
        fragmentManager.beginTransaction().add(R.id.container,fragment4,"4").hide(fragment4).commit(); */

        konfettiView = (KonfettiView) findViewById(R.id.viewKonfetti);
        konfettiView.setVisibility(View.GONE);



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


                bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    private boolean firstClick = true;

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        if (firstClick) {
                            int position = item.getItemId();
                            if (position == R.id.home) {
                                //firstClick = false;

                               /* fragmentManager.beginTransaction().hide(active_fragment[0]).show(fragment1).commit();
                                active_fragment[0] = fragment1; */

                                mixpanelAPI.track("Home");
                                HomeFragment homeFragment = new HomeFragment();
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.container, homeFragment);
                               // transaction.addToBackStack("HomeFragment");
                                transaction.commit();
                               // closeDrawer();
                                return true;

                            } else if (position == R.id.closet) {
                                mixpanelAPI.track("Closet");



                                ClosetFragment closetFragment = new ClosetFragment();
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.container, closetFragment);
                               // transaction.addToBackStack("ClosetFragment");
                                transaction.commit();
                               // closeDrawer();

                                return true;

                            }  else if (position == R.id.profile) {

                                mixpanelAPI.track("Profile");


                                if (!Utilities.loadPref(HomeActivity.this, "email", "").equalsIgnoreCase("")) {

                                  /*  fragmentManager.beginTransaction().hide(active_fragment[0]).show(fragment4).commit();
                                    active_fragment[0] = fragment4; */

                                    MyProfileFragment myProfileFragment = new MyProfileFragment();
                                    transaction = manager.beginTransaction();
                                    transaction.replace(R.id.container, myProfileFragment);
                                   // transaction.addToBackStack("MyProfileFragment");
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

    @Override
    protected void onStart() {
        super.onStart();
       // int i = getIntent().getExtras().getInt("position");

        Menu menu = bottomnav.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_INDEX);
        menuItem.setChecked(true);

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

                    if(!getSharedPreferences("prefs",MODE_PRIVATE).getBoolean("firsttimelaunch",true)
                    || getSharedPreferences("prefs",MODE_PRIVATE).getBoolean("secondtimelaunch",true)){

                        konfettiView.setVisibility(View.VISIBLE);


                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {



                                konfettiView.build().addColors(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.gold))
                                        .setDirection(0.0, 359.0)
                                        .setSpeed(2f, 5f)
                                        .setFadeOutEnabled(true)
                                        .setTimeToLive(4000L)
                                        .addShapes(Shape.RECT, Shape.CIRCLE)
                                        .addSizes(new Size(12,6f),new  Size(16, 6f))

                                        .setPosition(konfettiView.getX()+ konfettiView.getWidth()/2 ,konfettiView.getY() + konfettiView.getHeight()/3)
                                        .burst(150);





                            }
                        });

                        loadFragment(new ClosetFragment());




                    }




                }else {

                    loadFragment(new ClosetFragment());
                }

            } else if (getIntent().getExtras().getInt("position") == 3){

                loadFragment(new MyProfileFragment());


            } else {



                if (getIntent().getExtras().containsKey("celebration")){

                    if(getSharedPreferences("prefs",MODE_PRIVATE).getBoolean("post",true)) {

                        Utilities.showToast(HomeActivity.this,"Congrats!!You've posted your first picture");
                        konfettiView.setVisibility(View.VISIBLE);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {

                                konfettiView.build()
                                        .addColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.gold))
                                        .setDirection(0.0, 359.0)
                                        .setSpeed(2f, 5f)
                                        .setFadeOutEnabled(true)
                                        .setTimeToLive(4000L)
                                        .addShapes(Shape.RECT, Shape.CIRCLE)
                                        .addSizes(new Size(12, 6f), new Size(16, 6f))

                                        .setPosition(konfettiView.getX() + konfettiView.getWidth() / 2, konfettiView.getY() + konfettiView.getHeight() / 3)
                                        .burst(150);


                                //Utilities.showToast(HomeActivity.this, "Congrats,You've Posted your First Picture");


                                SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
                                editor.putBoolean("post", false);
                                editor.apply();


                            }
                        });
                    }

                }



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





    public void closeDrawer() {
       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        toggle.syncState();
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
        listView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
       // ok_text = (TextView) view.findViewById(R.id.ok_text);
        follow_all = (Button) view.findViewById(R.id.follow_all_text);
        DefaultFollowersAdapter defaultFollowersAdapter = new DefaultFollowersAdapter(this,defaultFollowers);
        listView.setAdapter(defaultFollowersAdapter);
        listView.setHasFixedSize(true);

        defaultFollowersAdapter.notifyDataSetChanged();
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);



     /*   ok_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("dialog",false);
                editor.apply();

                dialog.dismiss();

            }
        }); */

        follow_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("dialog",false);
                editor.apply();

                FirebaseFirestore.getInstance().collection("Default Followers").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            Utilities.showLoading(HomeActivity.this,"Loading");

                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                FirebaseFirestore.getInstance()
                                        .collection("UsersList")
                                        .document(My_Dbkey)
                                        .collection("Followee")
                                        .document(Objects.requireNonNull(documentSnapshot.get("email")).toString())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()){

                                            if (!Objects.requireNonNull(task.getResult()).exists()){

                                                String id = UUID.randomUUID().toString();

                                                HashMap<String,Object> hashMap = new HashMap<>();

                                                hashMap.put("id",id);
                                                hashMap.put("email", Objects.requireNonNull(documentSnapshot.get("email")).toString());
                                                hashMap.put("name", Objects.requireNonNull(documentSnapshot.get("name")).toString());

                                                if (documentSnapshot.get("profile_pic")!=null){

                                                    hashMap.put("imgname", Objects.requireNonNull(documentSnapshot.get("profile_pic")).toString());

                                                }else {

                                                    hashMap.put("imgname","");
                                                }






                                                FirebaseFirestore.getInstance().collection("UsersList").document(My_Dbkey)
                                                        .collection("Followee")
                                                        .document(documentSnapshot.get("email").toString()).set(hashMap);


                                                getPosts(documentSnapshot.get("key").toString());


                                            }


                                        }

                                    }
                                });


                                FirebaseFirestore.getInstance().collection("UsersList")
                                        .document(documentSnapshot.get("key").toString())
                                        .collection("FollowCollection").document(my_email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()){

                                            if (!task.getResult().exists()){

                                                Map<String, Object> data = new HashMap<>();
                                                String id = UUID.randomUUID().toString();

                                                data.put("id", id);
                                                data.put("email", Utilities.loadPref(HomeActivity.this, "email", ""));
                                                data.put("imgname",Utilities.loadPref(HomeActivity.this, "Profile_Pic", ""));
                                                data.put("name",Utilities.loadPref(HomeActivity.this, "Pen_Name","" ));

                                                FirebaseFirestore.getInstance().collection("UsersList")
                                                        .document(documentSnapshot.get("key").toString())
                                                        .collection("FollowCollection").document(my_email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Utilities.hideLoading();
                                                       // dialog.dismiss();

                                                    }
                                                });


                                            }
                                        }

                                    }
                                });


                            }
                        }

                        Utilities.hideLoading();
                        dialog.dismiss();

                    }
                });

            }
        });




    }



    @Override
    public void onBackPressed() {

        List<Fragment> mraglist = getSupportFragmentManager().getFragments();
        for (Fragment f : mraglist){

            if (f instanceof ClosetFragment){



                ((ClosetFragment) f).onBackPressed();
            }
        }
        super.onBackPressed();
    }

    private void getPosts(String key) {

        FirebaseFirestore.getInstance().collection("UsersList").document(key).collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){

                        FeedResponse feedResponse = documentSnapshot.toObject(FeedResponse.class);
                        if (feedResponse != null) {
                            FirebaseFirestore.getInstance().collection("UsersList").document(My_Dbkey).collection("Feed").add(feedResponse);
                        }
                    }




                }else {

                    Toast.makeText(HomeActivity.this,"Check your connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void fireLongToast() {

        Thread t = new Thread() {
            public void run() {
                int count = 0;
                try {
                    while (true && count < 10) {
                      //  toast.show();
                        sleep(1850);
                        count++;

                        // DO SOME LOGIC THAT BREAKS OUT OF THE WHILE LOOP
                    }
                } catch (Exception e) {
                   // Log.e("LongToast", "", e);
                }
            }
        };
        t.start();
    }
}
