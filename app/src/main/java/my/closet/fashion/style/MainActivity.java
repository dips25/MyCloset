package my.closet.fashion.style;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.github.clans.fab.FloatingActionButton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.Realm_database.RealmHandler;
import my.closet.fashion.style.adapters.AccessoryAdapter;
import my.closet.fashion.style.adapters.BottomAdapter;
import my.closet.fashion.style.adapters.FootWearAdapter;
import my.closet.fashion.style.adapters.TopAdapter;
import my.closet.fashion.style.modesl.Dresses;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE = 100;
    private static String STORE_DIRECTORY;
    private static int IMAGES_PRODUCED;
    private static final String SCREENCAP_NAME = "screencap";
    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private static MediaProjection sMediaProjection;
    ImageView cross;


    private MediaProjectionManager mProjectionManager;
    private ImageReader mImageReader;
    private Handler mHandler;
    private Display mDisplay;
    private VirtualDisplay mVirtualDisplay;
    private int mDensity;
    private int mWidth;
    private int mHeight;
    private int mRotation;
    private OrientationChangeCallback mOrientationChangeCallback;


    String filename = "myscreen_.png";
    Bitmap alteredbitmap;
    RelativeLayout look_tab;
    RelativeLayout add_layout;
    LinearLayout looktext;

    BottomNavigationView bottomNavigationView;


    private BroadcastReceiver broadcast;
    ByteArrayOutputStream byteArrayOutputStream;

    Bitmap bitmap = null;
    ArrayList<Dresses> griddresses = new ArrayList<>();


    public static final int GALLERY_REQUEST = 1000;
    public static final int MULTIPLE_PERMISSION = 10;
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

    NavigationView navigationView;

    TextView formals;
    TextView casuals;
    TextView specials;
    TextView partys;

    ImageView acctext;
    ImageView toptxt;

    TextView scrntst;
    TextView camertst;
    TextView glrytst;


    LinearLayout clearall;


    TextView sformals;
    TextView scasuals;
    TextView sspecials;
    TextView spartys;


    static View view;


    RealmResults<Dresses> r1;
    RealmResults<Dresses> r2;
    RealmResults<Dresses> r3;
    RealmResults<Dresses> r4;
    RealmResults<Dresses> r5;
    RealmResults<Dresses> r6;
    RealmResults<Dresses> r7;
    RealmResults<Dresses> r8;
    RealmResults<Dresses> r9;
    RealmResults<Dresses> r10;
    RealmResults<Dresses> r11;
    TextView apply;


    public static RecyclerView footrecycler;
    public static RecyclerView toprecycler;
    public static RecyclerView bottomrecycler;
    public static RecyclerView mrecycle;

    public static AccessoryAdapter madapter;
    public static FootWearAdapter mfootwearadapter;
    public static TopAdapter mtopadapter;
    public static BottomAdapter mbottomadapter;

    public RecyclerView.LayoutManager mlayoutmanager;
    public RecyclerView.LayoutManager toplayout;
    public RecyclerView.LayoutManager bottomlayoutmanager;
    public RecyclerView.LayoutManager footlayout;

    private static final int CAMERA_REQUEST = 1888;
    Uri finaluri;


    final String accessory = "Accessories";
    final String tops = "Tops";
    final String bottoms = "Bottoms";
    final String footwear = "Footwear";
    final String nooption = " ";


    String cblack;
    String cwhite;
    String cgrey;
    String cbeige;
    String cred;
    String cpink;
    String cblue;
    String cgreen;
    String cyellow;
    String corange;
    String cbrown;
    String cpurple;
    String csilver;
    String cgold;

    int nblack;
    int nwhite;
    int ngrey;
    int nbeige;
    int nred;
    int npink;
    int nblue;
    int ngreen;
    int nyellow;
    int norange;
    int nbrown;
    int npurple;
    int nsilver;
    int ngold;


    String formalss;
    String casualss;
    String specialss;
    String partyss;

    int nformalss;
    int ncasualss;
    int nspecialss;
    int npartyss;


    final String formalf = "Formal";
    final String casualsf = "Casual";
    final String specialsf = "Special";
    final String partyf = "Party";

    com.github.clans.fab.FloatingActionButton fab;


    final String black = "Black";
    final String white = "White";
    final String grey = "Grey";
    final String beige = "Beige";
    final String red = "Red";
    final String pink = "Pink";
    final String blue = "Blue";
    final String green = "Green";
    final String yellow = "Yellow";
    final String orange = "Orange";
    final String brown = "Brown";
    final String purple = "Purple";
    final String silver = "Silver";
    final String gold = "Gold";

    String s;
    private static File file;


    Realm realm;
    ImageView flblack;
    ImageView flwhite;
    ImageView flgrey;
    ImageView flbeige;
    ImageView flred;
    ImageView flpink;
    ImageView flsilver;
    ImageView flbrown;
    ImageView flblue;
    ImageView flgreen;
    ImageView flyellow;
    ImageView florange;
    ImageView flpurple;
    ImageView flgold;


    DrawerLayout mdr;
    //RelativeLayout corlayout;

    boolean doubleBackToExit = false;
    private ActionBarDrawerToggle toogle;


    ImageView flmblack;
    ImageView flmwhite;
    ImageView flmgrey;
    ImageView flmbeige;
    ImageView flmred;
    ImageView flmpink;
    ImageView flmbrown;
    ImageView flmsilver;
    ImageView flmblue;
    ImageView flmgreen;
    ImageView flmyellow;
    ImageView flmorange;
    ImageView flmpurple;
    ImageView flmgold;
    View tutorialview;
    //RelativeLayout tutorlayout;

    static MainActivity instance;


    MixpanelAPI mixpanel;
    FloatingActionButton fab_camera, fab_gallery, fab_scrnshot;
    // FloatingActionMenu fab_plus;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> topnames = new ArrayList<>();
    ArrayList<String> bottomnames = new ArrayList<>();
    ArrayList<String> footnames = new ArrayList<>();
    // Lookbook_fragment fragment;


    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;

            FileOutputStream foss = null;


            try {
                image = reader.acquireLatestImage();
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * mWidth;

                    // create bitmap
                    bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);


                    // write bitmap to a file

                    foss = new FileOutputStream(STORE_DIRECTORY + filename);


                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foss);


                    IMAGES_PRODUCED++;
                    Log.e(TAG, "captured image: " + IMAGES_PRODUCED);


                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (foss != null) {
                    try {
                        foss.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                if (image != null) {

                    image.close();
                }
            }
        }
    }

    private class OrientationChangeCallback extends OrientationEventListener {

        OrientationChangeCallback(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            final int rotation = mDisplay.getRotation();
            if (rotation != mRotation) {
                mRotation = rotation;
                try {
                    // clean up
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);

                    // re-create virtual display depending on device width / height
                    createVirtualDisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("ScreenCapture", "stopping projection.");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);
                    if (mOrientationChangeCallback != null) mOrientationChangeCallback.disable();
                    sMediaProjection.unregisterCallback(MainActivity.MediaProjectionStopCallback.this);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        instance = this;
        String projectToken = "257c7d2e1c44d7d1ab6105af372f65a6";
        mixpanel = MixpanelAPI.getInstance(this, projectToken);


        if (mixpanel != null) {
            mixpanel.identify(mixpanel.getDistinctId());
            mixpanel.getPeople().identify(mixpanel.getDistinctId());
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Gender", "Female");
            props.put("Gender", "Male");
            props.put("Logged in", false);
            mixpanel.track("MainActivity - onCreate called", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        acctext = (ImageView) findViewById(R.id.acctxti);
        scrntst = (TextView) findViewById(R.id.scrntst);
        camertst = (TextView) findViewById(R.id.camertst);
        glrytst = (TextView) findViewById(R.id.glrytst);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnve_icon_selector);
        look_tab = (RelativeLayout) findViewById(R.id.linear);
        add_layout = (RelativeLayout) findViewById(R.id.add_layout);

        fab_camera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab_gallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fab_scrnshot = (FloatingActionButton) findViewById(R.id.scrnshot);

        cross = (ImageView) findViewById(R.id.cross);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clearrefresh();
                look_tab.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        add_layout.setVisibility(View.GONE);
        fab_camera.setVisibility(View.INVISIBLE);
        fab_gallery.setVisibility(View.INVISIBLE);
        fab_scrnshot.setVisibility(View.INVISIBLE);
        camertst.setVisibility(View.INVISIBLE);
        glrytst.setVisibility(View.INVISIBLE);
        scrntst.setVisibility(View.INVISIBLE);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.home:

                        return true;

                    case R.id.plus:
                        if (fab_camera.getVisibility() == View.VISIBLE && fab_gallery.getVisibility() == View.VISIBLE && fab_scrnshot.getVisibility() == View.VISIBLE && scrntst.getVisibility() == View.VISIBLE && camertst.getVisibility() == View.VISIBLE && glrytst.getVisibility() == View.VISIBLE) {

                            fab_camera.setVisibility(View.INVISIBLE);
                            fab_gallery.setVisibility(View.INVISIBLE);
                            fab_scrnshot.setVisibility(View.INVISIBLE);
                            scrntst.setVisibility(View.INVISIBLE);
                            glrytst.setVisibility(View.INVISIBLE);
                            camertst.setVisibility(View.INVISIBLE);
                            add_layout.setVisibility(View.GONE);

                        } else {

                            add_layout.setVisibility(View.VISIBLE);
                            fab_camera.setVisibility(View.VISIBLE);
                            fab_gallery.setVisibility(View.VISIBLE);
                            fab_scrnshot.setVisibility(View.VISIBLE);
                            scrntst.setVisibility(View.VISIBLE);
                            glrytst.setVisibility(View.VISIBLE);
                            camertst.setVisibility(View.VISIBLE);

                        }
                        return true;

                    case R.id.lookbook:

                        Intent lookintent = new Intent(MainActivity.this, Lookbook.class);
                        startActivity(lookintent);

                        return true;
                }
                return true;
            }
        });


        looktext = (LinearLayout) findViewById(R.id.create_lay);


        fab_scrnshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mixpanel.track("ScreenShot Icon Clicked");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                }

                startProjection();
                startService(new Intent(getApplicationContext(), ScreenShotService.class));
            }
        });

        mdr = (DrawerLayout) findViewById(R.id.drawerlayout);

        toogle = new ActionBarDrawerToggle(this, mdr, R.string.open, R.string.close) {
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

        mdr.addDrawerListener(toogle);


        nblack = 0;
        nwhite = 0;
        ngrey = 0;
        nbeige = 0;
        nred = 0;
        npink = 0;
        nblue = 0;
        ngreen = 0;
        nyellow = 0;
        norange = 0;
        nbrown = 0;
        npurple = 0;
        nsilver = 0;
        ngold = 0;

        nformalss = 0;
        ncasualss = 0;
        npartyss = 0;
        nspecialss = 0;


        navigationView = (NavigationView) findViewById(R.id.nav);


        apply = (TextView) findViewById(R.id.applybutton);
        clearall = (LinearLayout) findViewById(R.id.clear);

        mrecycle = (RecyclerView) findViewById(R.id.recycler);
        mlayoutmanager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mrecycle.setLayoutManager(mlayoutmanager);


        toprecycler = (RecyclerView) findViewById(R.id.toprecycle);
        toplayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        toprecycler.setLayoutManager(toplayout);

        bottomrecycler = (RecyclerView) findViewById(R.id.bottomrecycle);
        bottomlayoutmanager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        bottomrecycler.setLayoutManager(bottomlayoutmanager);

        footrecycler = (RecyclerView) findViewById(R.id.footrecycler);
        footlayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        footrecycler.setLayoutManager(footlayout);


        Realm.init(this);
        realm = Realm.getDefaultInstance();

        if (!checkpermission()) {

            checkpermission();
        }


        clearall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mixpanel.track("Clear All Clicked");

                cblack = null;
                cwhite = null;
                cgrey = null;
                cbeige = null;
                cred = null;
                cpink = null;
                cblue = null;
                cgreen = null;
                cyellow = null;
                corange = null;
                cbrown = null;
                cpurple = null;
                csilver = null;
                cgold = null;

                formalss = null;
                casualss = null;
                specialss = null;
                partyss = null;

                nblack = 0;
                nwhite = 0;
                ngrey = 0;
                nbeige = 0;
                nred = 0;
                npink = 0;
                nblue = 0;
                ngreen = 0;
                nyellow = 0;
                norange = 0;
                nbrown = 0;
                npurple = 0;
                nsilver = 0;
                ngold = 0;

                nformalss = 0;
                ncasualss = 0;
                npartyss = 0;
                nspecialss = 0;


                flblack.setVisibility(View.VISIBLE);
                flmblack.setVisibility(View.INVISIBLE);

                flwhite.setVisibility(View.VISIBLE);
                flmwhite.setVisibility(View.INVISIBLE);

                flblue.setVisibility(View.VISIBLE);
                flmblue.setVisibility(View.INVISIBLE);

                flgreen.setVisibility(View.VISIBLE);
                flmgreen.setVisibility(View.INVISIBLE);

                flgrey.setVisibility(View.VISIBLE);
                flmgrey.setVisibility(View.INVISIBLE);

                flyellow.setVisibility(View.VISIBLE);
                flmyellow.setVisibility(View.INVISIBLE);

                flbeige.setVisibility(View.VISIBLE);
                flmbeige.setVisibility(View.INVISIBLE);

                florange.setVisibility(View.VISIBLE);
                flmorange.setVisibility(View.INVISIBLE);

                flred.setVisibility(View.VISIBLE);
                flmred.setVisibility(View.INVISIBLE);

                flbrown.setVisibility(View.VISIBLE);
                flmbrown.setVisibility(View.INVISIBLE);

                flpink.setVisibility(View.VISIBLE);
                flmpink.setVisibility(View.INVISIBLE);

                flpurple.setVisibility(View.VISIBLE);
                flmpurple.setVisibility(View.INVISIBLE);

                flsilver.setVisibility(View.VISIBLE);
                flmsilver.setVisibility(View.INVISIBLE);

                flgold.setVisibility(View.VISIBLE);
                flmgold.setVisibility(View.INVISIBLE);

                formals.setVisibility(View.VISIBLE);
                sformals.setVisibility(View.INVISIBLE);

                casuals.setVisibility(View.VISIBLE);
                scasuals.setVisibility(View.INVISIBLE);

                specials.setVisibility(View.VISIBLE);
                sspecials.setVisibility(View.INVISIBLE);

                partys.setVisibility(View.VISIBLE);
                spartys.setVisibility(View.INVISIBLE);

                clearrefresh();


            }
        });


        flblack = (ImageView) findViewById(R.id.blacksq);
        flwhite = (ImageView) findViewById(R.id.whitesq);
        flblue = (ImageView) findViewById(R.id.bluesq);
        flgreen = (ImageView) findViewById(R.id.greensq);
        flyellow = (ImageView) findViewById(R.id.yellowsq);
        florange = (ImageView) findViewById(R.id.orangesq);
        flbeige = (ImageView) findViewById(R.id.beigesq);
        flbrown = (ImageView) findViewById(R.id.brownsq);
        flred = (ImageView) findViewById(R.id.redsq);
        flgrey = (ImageView) findViewById(R.id.greysq);
        flsilver = (ImageView) findViewById(R.id.silversq);
        flgold = (ImageView) findViewById(R.id.goldsq);
        flpink = (ImageView) findViewById(R.id.pinksq);
        flpurple = (ImageView) findViewById(R.id.purplesq);


        flmblack = (ImageView) findViewById(R.id.blacksqtck);
        flmblack.setVisibility(View.INVISIBLE);

        flmwhite = (ImageView) findViewById(R.id.whitesqtck);
        flmwhite.setVisibility(View.INVISIBLE);

        flmblue = (ImageView) findViewById(R.id.bluesqtck);
        flmblue.setVisibility(View.INVISIBLE);

        flmgreen = (ImageView) findViewById(R.id.greensqtck);
        flmgreen.setVisibility(View.INVISIBLE);

        flmyellow = (ImageView) findViewById(R.id.yellowsqtck);
        flmyellow.setVisibility(View.INVISIBLE);

        flmbeige = (ImageView) findViewById(R.id.beigesqtck);
        flmbeige.setVisibility(View.INVISIBLE);

        flmorange = (ImageView) findViewById(R.id.orangesqtck);
        flmorange.setVisibility(View.INVISIBLE);

        flmbrown = (ImageView) findViewById(R.id.brownsqtck);
        flmbrown.setVisibility(View.INVISIBLE);

        flmred = (ImageView) findViewById(R.id.redsqtck);
        flmred.setVisibility(View.INVISIBLE);

        flmgrey = (ImageView) findViewById(R.id.greysqtck);
        flmgrey.setVisibility(View.INVISIBLE);

        flmsilver = (ImageView) findViewById(R.id.silversqtck);
        flmsilver.setVisibility(View.INVISIBLE);

        flmgold = (ImageView) findViewById(R.id.goldsqtck);
        flmgold.setVisibility(View.INVISIBLE);

        flmpink = (ImageView) findViewById(R.id.pinksqtck);
        flmpink.setVisibility(View.INVISIBLE);

        flmpurple = (ImageView) findViewById(R.id.purplesqtck);
        flmpurple.setVisibility(View.INVISIBLE);

        formals = (TextView) findViewById(R.id.ff1);
        casuals = (TextView) findViewById(R.id.cc1);
        specials = (TextView) findViewById(R.id.ss1);
        partys = (TextView) findViewById(R.id.pp1);


        sformals = (TextView) findViewById(R.id.ff2);
        sformals.setVisibility(View.INVISIBLE);

        scasuals = (TextView) findViewById(R.id.cc2);
        scasuals.setVisibility(View.INVISIBLE);

        sspecials = (TextView) findViewById(R.id.ss2);
        sspecials.setVisibility(View.INVISIBLE);

        spartys = (TextView) findViewById(R.id.pp2);
        spartys.setVisibility(View.INVISIBLE);

        formals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                formals.setVisibility(View.INVISIBLE);
                sformals.setVisibility(View.VISIBLE);
                formalss = formalf;
                nformalss = 1;

            }
        });


        sformals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sformals.setVisibility(View.INVISIBLE);
                formals.setVisibility(View.VISIBLE);
                formalss = null;
                nformalss = 0;
            }
        });

        casuals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                casuals.setVisibility(View.INVISIBLE);
                scasuals.setVisibility(View.VISIBLE);
                casualss = casualsf;

                ncasualss = 1;
            }
        });

        scasuals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                scasuals.setVisibility(View.INVISIBLE);
                casuals.setVisibility(View.VISIBLE);
                casualss = null;
                ncasualss = 0;
            }
        });

        specials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                sspecials.setVisibility(View.VISIBLE);
                specials.setVisibility(View.INVISIBLE);
                specialss = specialsf;
                nspecialss = 1;
            }
        });

        sspecials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                sspecials.setVisibility(View.INVISIBLE);
                specials.setVisibility(View.VISIBLE);
                specialss = null;
                nspecialss = 0;
            }
        });

        partys.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                partys.setVisibility(View.INVISIBLE);
                spartys.setVisibility(View.VISIBLE);
                partyss = partyf;
                npartyss = 1;
            }
        });

        spartys.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                spartys.setVisibility(View.INVISIBLE);
                partys.setVisibility(View.VISIBLE);
                partyss = null;
                npartyss = 0;
            }
        });


        flblack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmblack.setVisibility(View.VISIBLE);

                cblack = black;
                nblack = 1;


            }
        });


        flmblack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmblack.setVisibility(View.INVISIBLE);
                flblack.setVisibility(View.VISIBLE);
                cblack = null;
                nblack = 0;
            }
        });


        flwhite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmwhite.setVisibility(View.VISIBLE);

                cwhite = white;
                nwhite = 1;

            }
        });


        flmwhite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmwhite.setVisibility(View.INVISIBLE);
                flwhite.setVisibility(View.VISIBLE);
                cwhite = null;
                nwhite = 0;
            }
        });


        flgrey.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmgrey.setVisibility(View.VISIBLE);

                cgrey = grey;
                ngrey = 1;

            }
        });


        flmgrey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmgrey.setVisibility(View.INVISIBLE);
                flgrey.setVisibility(View.VISIBLE);
                cgrey = null;
                ngrey = 0;
            }
        });


        flbeige.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmbeige.setVisibility(View.VISIBLE);

                cbeige = beige;
                nbeige = 1;

            }
        });


        flmbeige.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmbeige.setVisibility(View.INVISIBLE);
                flbeige.setVisibility(View.VISIBLE);
                cbeige = null;
                nbeige = 0;
            }
        });


        flred.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmred.setVisibility(View.VISIBLE);

                cred = red;
                nred = 1;

            }
        });


        flmred.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmred.setVisibility(View.INVISIBLE);
                flred.setVisibility(View.VISIBLE);
                cred = null;
                nred = 0;

            }
        });


        flpink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmpink.setVisibility(View.VISIBLE);

                cpink = pink;
                npink = 1;

            }
        });


        flmpink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmpink.setVisibility(View.INVISIBLE);
                flpink.setVisibility(View.VISIBLE);
                cpink = null;
                npink = 0;

            }
        });


        flsilver.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmsilver.setVisibility(View.VISIBLE);

                csilver = silver;
                nsilver = 1;

            }
        });


        flmsilver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmsilver.setVisibility(View.INVISIBLE);
                flsilver.setVisibility(View.VISIBLE);
                csilver = null;
                nsilver = 0;
            }
        });

        flbrown.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmbrown.setVisibility(View.VISIBLE);

                cbrown = brown;
                nbrown = 1;

            }
        });


        flmbrown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmbrown.setVisibility(View.INVISIBLE);
                flbrown.setVisibility(View.VISIBLE);
                cbrown = null;
                nbrown = 0;
            }
        });

        flblue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmblue.setVisibility(View.VISIBLE);

                cblue = blue;
                nblue = 1;

            }
        });


        flmblue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmblue.setVisibility(View.INVISIBLE);
                flblue.setVisibility(View.VISIBLE);
                cblue = null;
                nblue = 0;
            }
        });

        flgreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmgreen.setVisibility(View.VISIBLE);

                cgreen = green;
                ngreen = 1;

            }
        });


        flmgreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmgreen.setVisibility(View.INVISIBLE);
                flgreen.setVisibility(View.VISIBLE);
                cgreen = null;
                ngreen = 0;
            }
        });

        flyellow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmyellow.setVisibility(View.VISIBLE);

                cyellow = yellow;
                nyellow = 1;

            }
        });


        flmyellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmyellow.setVisibility(View.INVISIBLE);
                flyellow.setVisibility(View.VISIBLE);
                cyellow = null;
                nyellow = 0;
            }
        });

        florange.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmorange.setVisibility(View.VISIBLE);

                corange = orange;
                norange = 1;

            }
        });


        flmorange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmorange.setVisibility(View.INVISIBLE);
                florange.setVisibility(View.VISIBLE);
                corange = null;
                norange = 0;
            }
        });


        flpurple.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                flmpurple.setVisibility(View.VISIBLE);

                cpurple = purple;
                npurple = 1;

            }
        });


        flmpurple.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flmpurple.setVisibility(View.INVISIBLE);
                flpurple.setVisibility(View.VISIBLE);
                cpurple = null;
                npurple = 0;
            }
        });

        flgold.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                flmgold.setVisibility(View.VISIBLE);

                cgold = gold;
                ngold = 1;

            }
        });


        flmgold.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                flmgold.setVisibility(View.INVISIBLE);
                flgold.setVisibility(View.VISIBLE);
                cgold = null;
                ngold = 0;


            }
        });


        fab_camera.setOnClickListener(this);
        fab_gallery.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                mixpanel.track("Gallery Icon Clicked");

                pickimage();


            }
        });

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler();
                Looper.loop();
            }
        }.start();


        apply.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                mixpanel.track("Apply Button Clicked");


                //fab_plus.setVisibility(View.VISIBLE);

                if ((nblack == 0 && nwhite == 0 && ngrey == 0 && nred == 0 && nbeige == 0 && npink == 0 && nsilver == 0 && ngreen == 0 && nblue == 0 && nyellow == 0 && norange == 0 && nbrown == 0 && npurple == 0 && ngold == 0) && (nformalss == 0 && ncasualss == 0 && npartyss == 0 && nspecialss == 0)) {


                    r3 = realm.where(Dresses.class).equalTo("colorsRealmList.black", nooption).or()
                            .equalTo("colorsRealmList.white", nooption).or()
                            .equalTo("colorsRealmList.grey", nooption).or()
                            .equalTo("colorsRealmList.beige", nooption).or()
                            .equalTo("colorsRealmList.red", nooption).or()
                            .equalTo("colorsRealmList.pink", nooption).or()
                            .equalTo("colorsRealmList.blue", nooption).or()
                            .equalTo("colorsRealmList.green", nooption).or()
                            .equalTo("colorsRealmList.yellow", nooption).or()
                            .equalTo("colorsRealmList.orange", nooption).or()
                            .equalTo("colorsRealmList.brown", nooption).or()
                            .equalTo("colorsRealmList.purple", nooption).or()
                            .equalTo("colorsRealmList.gold", nooption).or()
                            .equalTo("colorsRealmList.silver", nooption).or()
                            .equalTo("stylesRealmList.formal", nooption).or()
                            .equalTo("stylesRealmList.casual", nooption).or()
                            .equalTo("stylesRealmList.party", nooption).or()
                            .equalTo("stylesRealmList.special", nooption)

                            .findAll();

                } else if ((nblack == 1 || nwhite == 1 || ngrey == 1 || nred == 1 || nbeige == 1 || npink == 1 || nsilver == 1 || ngreen == 1 || nblue == 1 || nyellow == 1 || norange == 1 || nbrown == 1 || npurple == 1 || ngold == 1) && (nformalss == 1 || ncasualss == 1 || npartyss == 1 || nspecialss == 1)) {

                    r1 = realm.where(Dresses.class).equalTo("colorsRealmList.black", cblack).or()
                            .equalTo("colorsRealmList.white", cwhite).or()
                            .equalTo("colorsRealmList.grey", cgrey).or()
                            .equalTo("colorsRealmList.beige", cbeige).or()
                            .equalTo("colorsRealmList.red", cred).or()
                            .equalTo("colorsRealmList.pink", cpink).or()
                            .equalTo("colorsRealmList.blue", cblue).or()
                            .equalTo("colorsRealmList.green", cgreen).or()
                            .equalTo("colorsRealmList.yellow", cyellow).or()
                            .equalTo("colorsRealmList.orange", corange).or()
                            .equalTo("colorsRealmList.brown", cbrown).or()
                            .equalTo("colorsRealmList.purple", cpurple).or()
                            .equalTo("colorsRealmList.gold", cgold).or()
                            .equalTo("colorsRealmList.silver", csilver)
                            .findAll();

                    r3 = r1.where()
                            .equalTo("stylesRealmList.formal", formalss).or()
                            .equalTo("stylesRealmList.casual", casualss).or()
                            .equalTo("stylesRealmList.party", partyss).or()
                            .equalTo("stylesRealmList.special", specialss)
                            .findAll();

                } else if ((nblack == 0 && nwhite == 0 && ngrey == 0 && nred == 0 && nbeige == 0 && npink == 0 && nsilver == 0 && ngreen == 0 && nblue == 0 && nyellow == 0 && norange == 0 && nbrown == 0 && npurple == 0 && ngold == 0) && (nformalss == 1 || ncasualss == 1 || npartyss == 1 || nspecialss == 1)) {

                    r3 = realm.where(Dresses.class)
                            .equalTo("stylesRealmList.formal", formalss).or()
                            .equalTo("stylesRealmList.casual", casualss).or()
                            .equalTo("stylesRealmList.party", partyss).or()
                            .equalTo("stylesRealmList.special", specialss)
                            .findAll();

                } else if ((nblack == 1 || nwhite == 1 || ngrey == 1 || nred == 1 || nbeige == 1 || npink == 1 || nsilver == 1 || ngreen == 1 || nblue == 1 || nyellow == 1 || norange == 1 || nbrown == 1 || npurple == 1 || ngold == 1) && (nformalss == 0 && ncasualss == 0 && npartyss == 0 && nspecialss == 0)) {

                    r3 = realm.where(Dresses.class).equalTo("colorsRealmList.black", cblack).or()
                            .equalTo("colorsRealmList.white", cwhite).or()
                            .equalTo("colorsRealmList.grey", cgrey).or()
                            .equalTo("colorsRealmList.beige", cbeige).or()
                            .equalTo("colorsRealmList.red", cred).or()
                            .equalTo("colorsRealmList.pink", cpink).or()
                            .equalTo("colorsRealmList.blue", cblue).or()
                            .equalTo("colorsRealmList.green", cgreen).or()
                            .equalTo("colorsRealmList.yellow", cyellow).or()
                            .equalTo("colorsRealmList.orange", corange).or()
                            .equalTo("colorsRealmList.brown", cbrown).or()
                            .equalTo("colorsRealmList.purple", cpurple).or()
                            .equalTo("colorsRealmList.gold", cgold).or()
                            .equalTo("colorsRealmList.silver", csilver)
                            .findAll();

                }

                refresh();


            }

        });


        RealmHandler handler = new RealmHandler(realm);

        ArrayList<Dresses> accdresss = new ArrayList<>();
        RealmResults<Dresses> accdressesresults = realm.where(Dresses.class).contains("category", "Accessories").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : accdressesresults) {
            accdresss.add(dresses);
        }
        madapter = new AccessoryAdapter(this, accdresss);
        mrecycle.setAdapter(madapter);

        mrecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                looktext.setVisibility(View.INVISIBLE);
            }
        });


        ArrayList<Dresses> topdresss = new ArrayList<>();
        RealmResults<Dresses> topdressesresults = realm.where(Dresses.class).contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : topdressesresults) {
            topdresss.add(dresses);
        }
        mtopadapter = new TopAdapter(this, topdresss);
        toprecycler.setAdapter(mtopadapter);


        ArrayList<Dresses> bottomdresss = new ArrayList<>();
        RealmResults<Dresses> bottomdressesresults = realm.where(Dresses.class).contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : bottomdressesresults) {
            bottomdresss.add(dresses);
        }
        mbottomadapter = new BottomAdapter(this, bottomdresss);
        bottomrecycler.setAdapter(mbottomadapter);


        ArrayList<Dresses> footdresss = new ArrayList<>();
        RealmResults<Dresses> footdressesresults = realm.where(Dresses.class).contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : footdressesresults) {
            footdresss.add(dresses);
        }
        mfootwearadapter = new FootWearAdapter(this, footdresss);
        footrecycler.setAdapter(mfootwearadapter);

        looktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                names = madapter.getItems();
                topnames = mtopadapter.getItems();
                bottomnames = mbottomadapter.getItems();
                footnames = mfootwearadapter.getItems();


                // Bundle bundle=new Bundle();

                Intent intent = new Intent(MainActivity.this, CustomView.class);
                //bundle.putStringArrayList();
                intent.putExtra("acckey", names);
                intent.putExtra("topkey", topnames);
                intent.putExtra("bottomkey", bottomnames);
                intent.putExtra("footkey", footnames);
                startActivity(intent);

                //Toast.makeText(MainActivity.this,names.get(0),Toast.LENGTH_SHORT).show();


            }
        });


    }


    private void clearrefresh() {

        ArrayList<Dresses> accdresss = new ArrayList<>();
        RealmResults<Dresses> accdressesresults = realm.where(Dresses.class).contains("category", "Accessories").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : accdressesresults) {
            accdresss.add(dresses);
        }
        madapter = new AccessoryAdapter(this, accdresss);
        mrecycle.setAdapter(madapter);


        ArrayList<Dresses> topdresss = new ArrayList<>();
        RealmResults<Dresses> topdressesresults = realm.where(Dresses.class).contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : topdressesresults) {
            topdresss.add(dresses);
        }
        mtopadapter = new TopAdapter(this, topdresss);
        toprecycler.setAdapter(mtopadapter);


        ArrayList<Dresses> bottomdresss = new ArrayList<>();
        RealmResults<Dresses> bottomdressesresults = realm.where(Dresses.class).contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : bottomdressesresults) {
            bottomdresss.add(dresses);
        }
        mbottomadapter = new BottomAdapter(this, bottomdresss);
        bottomrecycler.setAdapter(mbottomadapter);


        ArrayList<Dresses> footdresss = new ArrayList<>();
        RealmResults<Dresses> footdressesresults = realm.where(Dresses.class).contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : footdressesresults) {
            footdresss.add(dresses);
        }
        mfootwearadapter = new FootWearAdapter(this, footdresss);
        footrecycler.setAdapter(mfootwearadapter);

    }

    private boolean checkpermission() {

        int result;
        ArrayList<String> permissionarray = new ArrayList<>();
        for (String p : permissions) {

            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {

                permissionarray.add(p);

            }

        }
        if (!permissionarray.isEmpty()) {

            ActivityCompat.requestPermissions(MainActivity.this, permissionarray.toArray(new String[permissionarray.size()]), MULTIPLE_PERMISSION);
            return false;
        }
        return true;
    }


    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantresults) {

        switch (requestCode) {

            case MULTIPLE_PERMISSION:
                if (grantresults.length > 0 && grantresults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter:


                mixpanel.track("Pic_info Button Clicked");


                if (mdr.isDrawerOpen(Gravity.END)) {
                    mdr.closeDrawer(Gravity.END);
                    //fab_plus.setVisibility(View.VISIBLE);

                    return true;
                } else {
                    mdr.openDrawer(Gravity.END);
                    //fab_plus.setVisibility(View.INVISIBLE);
                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        mixpanel.track("Exit Button Pressed");

        if (doubleBackToExit) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExit = true;
        Toast.makeText(this, R.string.exit, Toast.LENGTH_LONG).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, 2000);

    }


    private void refresh() {


        if (r3.isEmpty()) {

            mdr.closeDrawer(Gravity.END);
            Toast.makeText(this, R.string.no_match, Toast.LENGTH_SHORT).show();
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mdr.openDrawer(Gravity.END);

                }
            }, 600);

            if (mdr.isDrawerOpen(Gravity.END)) {

                //fab_plus.setVisibility(View.INVISIBLE);
            } else {

                //fab_plus.setVisibility(View.VISIBLE);
            }

        } else {

            mdr.closeDrawer(Gravity.END);

            r4 = r3.where().contains("category", "Accessories").findAllSorted("id", Sort.DESCENDING);
            r5 = r3.where().contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);
            r6 = r3.where().contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);
            r7 = r3.where().contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);

            ArrayList<Dresses> filterdresses = new ArrayList<>();
            ArrayList<Dresses> filtertops = new ArrayList<>();
            ArrayList<Dresses> filterbottoms = new ArrayList<>();
            ArrayList<Dresses> filterfootwear = new ArrayList<>();

            for (Dresses filterdressess : r4) {

                filterdresses.add(filterdressess);
            }

            for (Dresses tops : r5) {

                filtertops.add(tops);
            }

            for (Dresses bottoms : r6) {

                filterbottoms.add(bottoms);
            }

            for (Dresses footwear : r7) {
                filterfootwear.add(footwear);

            }


            madapter = new AccessoryAdapter(this, filterdresses);
            mrecycle.setAdapter(madapter);
            madapter.notifyDataSetChanged();

            mtopadapter = new TopAdapter(this, filtertops);
            toprecycler.setAdapter(mtopadapter);
            mtopadapter.notifyDataSetChanged();

            mbottomadapter = new BottomAdapter(this, filterbottoms);
            bottomrecycler.setAdapter(mbottomadapter);
            mbottomadapter.notifyDataSetChanged();

            mfootwearadapter = new FootWearAdapter(this, filterfootwear);
            footrecycler.setAdapter(mfootwearadapter);
            mfootwearadapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.filtered, Toast.LENGTH_SHORT).show();

        }
    }

    public void onClickcalled() {

        RealmResults<Dresses> realmcheck = realm.where(Dresses.class).findAll();

        for (Dresses dresses : realmcheck) {

            if (dresses.isSelected()) {
                bottomNavigationView.setVisibility(View.GONE);
                look_tab.setVisibility(View.VISIBLE);
                break;
            } else {

                look_tab.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
    }


    private void pickimage() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);

    }

    @Override
    public void onClick(View v) {

        mixpanel.track("Camera Icon Clicked");


        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraintent.resolveActivity(getPackageManager()) != null) {

            File photofile = null;
            photofile = getImgFile();
            finaluri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", photofile);

            if (finaluri != null) {

                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, finaluri);
                //Toast.makeText(getApplicationContext(),finaluri.getPath(),Toast.LENGTH_SHORT).show();
                startActivityForResult(cameraintent, CAMERA_REQUEST);
            }

        }
    }

    private File getImgFile() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File externaldirectory = getExternalFilesDir(null);
        assert externaldirectory != null;
        STORE_DIRECTORY = externaldirectory.getAbsolutePath() + "/screenshots/";
        File file1 = new File(STORE_DIRECTORY);
        // file1 =

        if (!file1.exists()) {

            file1.mkdirs();
        }
        return new File(file1, imageFileName);


    }


    protected void onActivityResult(int requestcode, int resultcode, Intent data) {

        switch (requestcode) {

            case CAMERA_REQUEST:

                if (requestcode == CAMERA_REQUEST && resultcode == Activity.RESULT_OK) {

                    // Uri uri=data.getData();

                    Intent intent = new Intent(this, EraserActivity.class);
                    intent.putExtra("source", "add");
                    intent.putExtra("image", "camera");

                    intent.putExtra("uri", finaluri);

                    startActivity(intent);

                }
                break;


            case GALLERY_REQUEST:
                if (requestcode == GALLERY_REQUEST && resultcode == Activity.RESULT_OK) {


                    Uri imguri = data.getData();
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(this, EraserActivity.class);
                    i.putExtra("source", "add");
                    i.putExtra("image", "gallery");
                    i.putExtra("uri", imguri);

                    startActivity(i);

                }
            case REQUEST_CODE:
                if (requestcode == REQUEST_CODE) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sMediaProjection = mProjectionManager.getMediaProjection(resultcode, data);
                    }


                    if (sMediaProjection != null) {
                        File externalFilesDir = getExternalFilesDir(null);
                        if (externalFilesDir != null) {
                            STORE_DIRECTORY = externalFilesDir.getAbsolutePath() + "/screenshots/";
                            File storeDirectory = new File(STORE_DIRECTORY);
                            if (!storeDirectory.exists()) {
                                boolean success = storeDirectory.mkdirs();
                                if (!success) {
                                    Log.e(TAG, "failed to create file storage directory.");
                                    return;
                                }
                            }
                        } else {
                            Log.e(TAG, "failed to create file storage directory, getExternalFilesDir is null.");
                            return;
                        }


                        // display metrics
                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        mDensity = metrics.densityDpi;
                        mDisplay = getWindowManager().getDefaultDisplay();

                        // create virtual display depending on device width / height
                        createVirtualDisplay();

                        // register orientation change callback
                        mOrientationChangeCallback = new MainActivity.OrientationChangeCallback(this);
                        if (mOrientationChangeCallback.canDetectOrientation()) {
                            mOrientationChangeCallback.enable();
                        }

                        // register media projection stop callback
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            sMediaProjection.registerCallback(new MainActivity.MediaProjectionStopCallback(), mHandler);
                        }


                    }


                    break;
                }

        }
    }

    private void startProjection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        }
    }

    private void stopProjection() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sMediaProjection != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sMediaProjection.stop();
                    }
                }
            }
        });
    }


    private void createVirtualDisplay() {
        // get width and height
        Point size = new Point();
        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        // start capture reader
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVirtualDisplay = sMediaProjection.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
        }
        mImageReader.setOnImageAvailableListener(new MainActivity.ImageAvailableListener(), mHandler);
    }

    public void carry() {

        mixpanel.track("ScreenShot Clicked");


        stopProjection();
        //  stopService(new Intent(this,ScreenShotService.class));

        if (bitmap != null) {

            alteredbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
            Bitmap bmp = alteredbitmap;

            byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            Intent intent = new Intent(getBaseContext(), Crop.class);
            intent.putExtra("bytearray", bytes);
            startActivity(intent);
            bmp.recycle();


        } else {

            Toast.makeText(getApplicationContext(), "Please Switch On service and revert back", Toast.LENGTH_SHORT).show();
        }
    }


}
