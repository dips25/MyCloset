package my.closet.fashion.style.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.BuildConfig;
import my.closet.fashion.style.EraserActivity;
import my.closet.fashion.style.R;
import my.closet.fashion.style.activities.HomeActivity;
import my.closet.fashion.style.adapters.AccessoryAdapter;
import my.closet.fashion.style.adapters.BottomAdapter;
import my.closet.fashion.style.adapters.FootWearAdapter;
import my.closet.fashion.style.adapters.TopAdapter;
import my.closet.fashion.style.modesl.Dresses;

import static android.content.Context.MODE_PRIVATE;
import static my.closet.fashion.style.R.id.upload_menu;
import static my.closet.fashion.style.adapters.AccessoryAdapter.mcontext;
import static my.closet.fashion.style.adapters.BottomAdapter.mbcontext;
import static my.closet.fashion.style.adapters.FootWearAdapter.mfcontext;
import static my.closet.fashion.style.adapters.TopAdapter.ccontext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClosetFragment extends Fragment implements View.OnClickListener {


    private static final int GALLERY_REQUEST = 110 ;
    public static RecyclerView footrecycler;
    public static RecyclerView toprecycler;
    public static RecyclerView bottomrecycler;
    public static RecyclerView accrecycler;

    public static AccessoryAdapter madapter;
    public static FootWearAdapter mfootwearadapter;
    public static TopAdapter mtopadapter;
    public static BottomAdapter mbottomadapter;
    public ClosetFragment instance = this;

    public RecyclerView.LayoutManager mlayoutmanager;
    public RecyclerView.LayoutManager toplayout;
    public RecyclerView.LayoutManager bottomlayoutmanager;
    public RecyclerView.LayoutManager footlayout;

    static Realm realm;
    RealmResults<Dresses> r1;
    RealmResults<Dresses> r2;
    RealmResults<Dresses> r3;
    RealmResults<Dresses> r4;
    RealmResults<Dresses> r5;
    RealmResults<Dresses> r6;
    RealmResults<Dresses> r7;

    View view;
    private TextView toolbar_title;
    private TextView scrntst;
    public TextView glrytst;
    public TextView camertst;
    private FloatingActionButton fab_camera;
    public FloatingActionButton fab_gallery;
    public FloatingActionButton fab_scrnshot;
    private RelativeLayout add_layout;
    private Uri finaluri;
    private String STORE_DIRECTORY;
    private final int CAMERA_REQUEST = 100;
    private TextView apply;
    private LinearLayout clearall;
    private MixpanelAPI mixpanelAPI;

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

    int nformalss;
    int ncasualss;
    int nspecialss;
    int npartyss;

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

    String formalss;
    String casualss;
    String specialss;
    String partyss;


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

    final String formalf = "Formal";
    final String casualsf = "Casual";
    final String specialsf = "Special";
    final String partyf = "Party";

    final String nooption = " ";


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

    TextView formals;
    TextView casuals;
    TextView specials;
    TextView partys;

    TextView sformals;
    TextView scasuals;
    TextView sspecials;
    TextView spartys;
    private DrawerLayout mdr;
    private ActionBarDrawerToggle toogle;

    RelativeLayout upload_tutorial;
    View menuitemview;

    Uri imguri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mixpanelAPI= MixpanelAPI.getInstance(getContext(),"257c7d2e1c44d7d1ab6105af372f65a6");
        view = inflater.inflate(R.layout.fragment_closet, container, false);

        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA,
        };

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

        findView(view);

        return view;
    }


    private void findView(View v) {

        toolbar_title = (TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.title);
        if (toolbar_title!=null) {
            toolbar_title.setText(R.string.closet);
        }

        upload_tutorial = (RelativeLayout)v.findViewById(R.id.upload_tutorial);
        upload_tutorial.setVisibility(View.GONE);

        SharedPreferences preferences = getContext().getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstlaunch = preferences.getBoolean("firsttime",true);

        if (firstlaunch){

            createtutorial();
        }

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

        mdr = (DrawerLayout) v.findViewById(R.id.drawerlayout);

        toogle = new ActionBarDrawerToggle(getActivity(), mdr, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View view) {

                HomeActivity.bottomnav.setVisibility(View.VISIBLE);
                HomeActivity.look_tab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                HomeActivity.bottomnav.setVisibility(View.INVISIBLE);
                HomeActivity.look_tab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                HomeActivity.bottomnav.setAlpha(1-slideOffset);
                HomeActivity.look_tab.setAlpha(1-slideOffset);
            }
        };

        mdr.addDrawerListener(toogle);


       // scrntst = (TextView) v.findViewById(R.id.scrntst);
        camertst = (TextView) v.findViewById(R.id.camertst);
        glrytst = (TextView) v.findViewById(R.id.glrytst);
        add_layout = (RelativeLayout) v.findViewById(R.id.add_layout);

        fab_camera = (FloatingActionButton) v.findViewById(R.id.fab_camera);
        fab_gallery = (FloatingActionButton) v.findViewById(R.id.fab_gallery);
       // fab_scrnshot = (FloatingActionButton) v.findViewById(R.id.scrnshot);

        apply = (TextView) v.findViewById(R.id.applybutton);
        clearall = (LinearLayout) v.findViewById(R.id.clear);

        add_layout.setVisibility(View.GONE);
        fab_camera.setVisibility(View.INVISIBLE);
        fab_gallery.setVisibility(View.INVISIBLE);
       // fab_scrnshot.setVisibility(View.INVISIBLE);
        camertst.setVisibility(View.INVISIBLE);
        glrytst.setVisibility(View.INVISIBLE);
       // scrntst.setVisibility(View.INVISIBLE);

        flblack = (ImageView) v.findViewById(R.id.blacksq);
        flwhite = (ImageView) v.findViewById(R.id.whitesq);
        flblue = (ImageView) v.findViewById(R.id.bluesq);
        flgreen = (ImageView) v.findViewById(R.id.greensq);
        flyellow = (ImageView) v.findViewById(R.id.yellowsq);
        florange = (ImageView) v.findViewById(R.id.orangesq);
        flbeige = (ImageView) v.findViewById(R.id.beigesq);
        flbrown = (ImageView) v.findViewById(R.id.brownsq);
        flred = (ImageView) v.findViewById(R.id.redsq);
        flgrey = (ImageView) v.findViewById(R.id.greysq);
        flsilver = (ImageView) v.findViewById(R.id.silversq);
        flgold = (ImageView) v.findViewById(R.id.goldsq);
        flpink = (ImageView) v.findViewById(R.id.pinksq);
        flpurple = (ImageView) v.findViewById(R.id.purplesq);


        flmblack = (ImageView) v.findViewById(R.id.blacksqtck);

        if (flmblack!=null) {
            flmblack.setVisibility(View.INVISIBLE);
        }

        flmwhite = (ImageView) v.findViewById(R.id.whitesqtck);
        if (flwhite!=null) {
            flmwhite.setVisibility(View.INVISIBLE);
        }
        flmblue = (ImageView) v.findViewById(R.id.bluesqtck);
        if (flmblue!=null) {
            flmblue.setVisibility(View.INVISIBLE);
        }

        flmgreen = (ImageView) v.findViewById(R.id.greensqtck);
        if (flmgreen!=null) {
            flmgreen.setVisibility(View.INVISIBLE);
        }
        flmyellow = (ImageView) v.findViewById(R.id.yellowsqtck);
        if (flmyellow!=null) {
            flmyellow.setVisibility(View.INVISIBLE);
        }
        flmbeige = (ImageView) v.findViewById(R.id.beigesqtck);
        if (flmbeige!=null) {
            flmbeige.setVisibility(View.INVISIBLE);
        }
        flmorange = (ImageView) v.findViewById(R.id.orangesqtck);
        if (flmorange!=null) {
            flmorange.setVisibility(View.INVISIBLE);
        }
        flmbrown = (ImageView) v.findViewById(R.id.brownsqtck);
        if (flmbrown!=null) {
            flmbrown.setVisibility(View.INVISIBLE);
        }
        flmred = (ImageView) v.findViewById(R.id.redsqtck);
        if (flmred!=null) {
            flmred.setVisibility(View.INVISIBLE);
        }
        flmgrey = (ImageView) v.findViewById(R.id.greysqtck);
        if (flmgrey!=null) {
            flmgrey.setVisibility(View.INVISIBLE);
        }
        flmsilver = (ImageView) v.findViewById(R.id.silversqtck);
        if (flmsilver!=null) {
            flmsilver.setVisibility(View.INVISIBLE);
        }
        flmgold = (ImageView) v.findViewById(R.id.goldsqtck);
        if (flmgold!=null) {
            flmgold.setVisibility(View.INVISIBLE);
        }
        flmpink = (ImageView) v.findViewById(R.id.pinksqtck);
        if (flmpink!=null) {
            flmpink.setVisibility(View.INVISIBLE);
        }
        flmpurple = (ImageView) v.findViewById(R.id.purplesqtck);
        if (flmpurple!=null) {
            flmpurple.setVisibility(View.INVISIBLE);
        }
        formals = (TextView) v.findViewById(R.id.ff1);
        casuals = (TextView) v.findViewById(R.id.cc1);
        specials = (TextView) v.findViewById(R.id.ss1);
        partys = (TextView) v.findViewById(R.id.pp1);


        sformals = (TextView) v.findViewById(R.id.ff2);
        if (sformals!=null) {
            sformals.setVisibility(View.INVISIBLE);
        }
        scasuals = (TextView) v.findViewById(R.id.cc2);
        if (scasuals!=null) {
            scasuals.setVisibility(View.INVISIBLE);
        }
        sspecials = (TextView) v.findViewById(R.id.ss2);
        if (sspecials!=null) {
            sspecials.setVisibility(View.INVISIBLE);
        }
        spartys = (TextView) v.findViewById(R.id.pp2);
        if (spartys!=null) {
            spartys.setVisibility(View.INVISIBLE);
        }

        upload_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (upload_tutorial.getVisibility() == View.VISIBLE){

                    upload_tutorial.setVisibility(View.GONE);
                }

            }
        });


        if (formals!=null) {
            formals.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    formals.setVisibility(View.INVISIBLE);
                    sformals.setVisibility(View.VISIBLE);
                    formalss = formalf;
                    nformalss = 1;

                }
            });
        }

        if (sformals!=null) {


            sformals.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    sformals.setVisibility(View.INVISIBLE);
                    formals.setVisibility(View.VISIBLE);
                    formalss = null;
                    nformalss = 0;
                }
            });
        }

        if (casuals!=null) {
            casuals.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    casuals.setVisibility(View.INVISIBLE);
                    scasuals.setVisibility(View.VISIBLE);
                    casualss = casualsf;

                    ncasualss = 1;
                }
            });
        }

        if (scasuals!=null) {

            scasuals.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    scasuals.setVisibility(View.INVISIBLE);
                    casuals.setVisibility(View.VISIBLE);
                    casualss = null;
                    ncasualss = 0;
                }
            });
        }

        if (specials!=null) {

            specials.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    sspecials.setVisibility(View.VISIBLE);
                    specials.setVisibility(View.INVISIBLE);
                    specialss = specialsf;
                    nspecialss = 1;
                }
            });
        }

        if (sspecials!=null) {

            sspecials.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    sspecials.setVisibility(View.INVISIBLE);
                    specials.setVisibility(View.VISIBLE);
                    specialss = null;
                    nspecialss = 0;
                }
            });
        }

        if (partys!=null) {

            partys.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    partys.setVisibility(View.INVISIBLE);
                    spartys.setVisibility(View.VISIBLE);
                    partyss = partyf;
                    npartyss = 1;
                }
            });
        }

        Objects.requireNonNull(spartys).setOnClickListener(new View.OnClickListener() {
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

        clearall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

               mixpanelAPI.track("Clear All");

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

        apply.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                mixpanelAPI.track("Apply");

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


        fab_camera.setOnClickListener(this);
        fab_gallery.setOnClickListener(this);


        accrecycler = (RecyclerView) v.findViewById(R.id.recycler);
        mlayoutmanager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        accrecycler.setLayoutManager(mlayoutmanager);
        madapter = new AccessoryAdapter(mcontext, getAccessories());
        accrecycler.setAdapter(madapter);


        toprecycler = (RecyclerView) v.findViewById(R.id.toprecycle);
        toplayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        toprecycler.setLayoutManager(toplayout);
        mtopadapter = new TopAdapter(ccontext, setTop());
        toprecycler.setAdapter(mtopadapter);

        bottomrecycler = (RecyclerView) v.findViewById(R.id.bottomrecycle);
        bottomlayoutmanager = new LinearLayoutManager(mbcontext, LinearLayoutManager.HORIZONTAL, false);
        bottomrecycler.setLayoutManager(bottomlayoutmanager);
        mbottomadapter = new BottomAdapter(mbcontext, getBottom());
        bottomrecycler.setAdapter(mbottomadapter);

        footrecycler = (RecyclerView) v.findViewById(R.id.footrecycler);
        footlayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        footrecycler.setLayoutManager(footlayout);
        mfootwearadapter = new FootWearAdapter(mfcontext, getFoot());
        footrecycler.setAdapter(mfootwearadapter);

    }


    private ArrayList<Dresses> getFoot() {

        ArrayList<Dresses> footdresss = new ArrayList<>();
        RealmResults<Dresses> footdressesresults = realm.where(Dresses.class).contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : footdressesresults) {
            footdresss.add(dresses);
        }
        return footdresss;

    }

    private ArrayList<Dresses> getBottom() {

        ArrayList<Dresses> bottomdresss = new ArrayList<>();
        RealmResults<Dresses> bottomdressesresults = realm.where(Dresses.class).
                contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : bottomdressesresults) {
            bottomdresss.add(dresses);
        }
        return bottomdresss;
    }

    private ArrayList<Dresses> setTop() {
        ArrayList<Dresses> topdresss = new ArrayList<>();
        RealmResults<Dresses> topdressesresults = realm.where(Dresses.class).
                contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : topdressesresults) {
            topdresss.add(dresses);
        }
        return topdresss;
    }

    private ArrayList<Dresses> getAccessories() {

        ArrayList<Dresses> accdresss = new ArrayList<>();
        RealmResults<Dresses> accdressesresults = realm.where(Dresses.class)
                .contains("category", "Accessories").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : accdressesresults) {
            accdresss.add(dresses);
        }
        return accdresss;
    }

    public static void clearrefresh() {

        ArrayList<Dresses> accdresss = new ArrayList<>();
        RealmResults<Dresses> accdressesresults = realm.where(Dresses.class).contains("category", "Accessories").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : accdressesresults) {
            accdresss.add(dresses);
        }
        madapter = new AccessoryAdapter(mcontext, accdresss);
        accrecycler.setAdapter(madapter);


        ArrayList<Dresses> topdresss = new ArrayList<>();
        RealmResults<Dresses> topdressesresults = realm.where(Dresses.class).contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : topdressesresults) {
            topdresss.add(dresses);
        }
        mtopadapter = new TopAdapter(ccontext, topdresss);
        toprecycler.setAdapter(mtopadapter);


        ArrayList<Dresses> bottomdresss = new ArrayList<>();
        RealmResults<Dresses> bottomdressesresults = realm.where(Dresses.class).contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : bottomdressesresults) {
            bottomdresss.add(dresses);
        }
        mbottomadapter = new BottomAdapter(mbcontext, bottomdresss);
        bottomrecycler.setAdapter(mbottomadapter);


        ArrayList<Dresses> footdresss = new ArrayList<>();
        RealmResults<Dresses> footdressesresults = realm.where(Dresses.class).contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);
        for (Dresses dresses : footdressesresults) {
            footdresss.add(dresses);
        }
        mfootwearadapter = new FootWearAdapter(mfcontext, footdresss);
        footrecycler.setAdapter(mfootwearadapter);

    }


    private void refresh() {


        if (r3.isEmpty()) {

            mdr.closeDrawer(Gravity.END);
            Toast.makeText(getActivity(), R.string.no_match, Toast.LENGTH_SHORT).show();
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


            madapter = new AccessoryAdapter(getActivity(), filterdresses);
            accrecycler.setAdapter(madapter);
            madapter.notifyDataSetChanged();

            mtopadapter = new TopAdapter(getActivity(), filtertops);
            toprecycler.setAdapter(mtopadapter);
            mtopadapter.notifyDataSetChanged();

            mbottomadapter = new BottomAdapter(getActivity(), filterbottoms);
            bottomrecycler.setAdapter(mbottomadapter);
            mbottomadapter.notifyDataSetChanged();

            mfootwearadapter = new FootWearAdapter(getActivity(), filterfootwear);
            footrecycler.setAdapter(mfootwearadapter);
            mfootwearadapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), R.string.filtered, Toast.LENGTH_SHORT).show();

        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_search).setVisible(false);

    }

    private void createtutorial() {

       upload_tutorial.setVisibility(View.VISIBLE);

        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firsttime",false);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.filter:

                mixpanelAPI.track("Filter");

                if (mdr.isDrawerOpen(Gravity.END)) {
                    mdr.closeDrawer(Gravity.END);
                    //fab_plus.setVisibility(View.VISIBLE);


                } else {
                    mdr.openDrawer(Gravity.END);
                    //fab_plus.setVisibility(View.INVISIBLE);

                }
                return true;

            case upload_menu:


                    if (fab_camera.getVisibility() == View.VISIBLE &&
                            fab_gallery.getVisibility() == View.VISIBLE &&
                            camertst.getVisibility() == View.VISIBLE &&
                            glrytst.getVisibility() == View.VISIBLE) {

                        fab_camera.setVisibility(View.INVISIBLE);
                        fab_gallery.setVisibility(View.INVISIBLE);
                       // fab_scrnshot.setVisibility(View.INVISIBLE);
                       // scrntst.setVisibility(View.INVISIBLE);
                        glrytst.setVisibility(View.INVISIBLE);
                        camertst.setVisibility(View.INVISIBLE);
                        add_layout.setVisibility(View.GONE);

                    } else {

                        add_layout.setVisibility(View.VISIBLE);
                        fab_camera.setVisibility(View.VISIBLE);
                        fab_gallery.setVisibility(View.VISIBLE);
                       // fab_scrnshot.setVisibility(View.VISIBLE);
                       // scrntst.setVisibility(View.VISIBLE);
                        glrytst.setVisibility(View.VISIBLE);
                        camertst.setVisibility(View.VISIBLE);

                    }

                return true;

            case R.id.action_search:

                // Do Fragment menu item stuff here
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab_camera:


                mixpanelAPI.track("Camera");

                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraintent.resolveActivity(getActivity().getPackageManager()) != null) {

                    File photofile = null;
                    photofile = getImgFile();
                    finaluri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", photofile);

                    if (finaluri != null) {

                        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, finaluri);
                        //Toast.makeText(getApplicationContext(),finaluri.getPath(),Toast.LENGTH_SHORT).show();
                        startActivityForResult(cameraintent, CAMERA_REQUEST);
                    }

                }
                add_layout.setVisibility(View.GONE);
                break;


            case R.id.fab_gallery:

                mixpanelAPI.track("Gallery");

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);

                add_layout.setVisibility(View.GONE);

        }
    }

    private File getImgFile() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File externaldirectory = Objects.requireNonNull(getActivity()).getExternalFilesDir(null);
        assert externaldirectory != null;
        STORE_DIRECTORY = externaldirectory.getAbsolutePath() + "/camera/";
        File file1 = new File(STORE_DIRECTORY);
        // file1 =

        if (!file1.exists()) {

            file1.mkdirs();
        }
        return new File(file1, imageFileName);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CAMERA_REQUEST:

                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                    // Uri uri=data.getData();

                    Intent intent = new Intent(getActivity(), EraserActivity.class);
                    intent.putExtra("source", "add");
                    intent.putExtra("image", "camera");

                    intent.putExtra("uri", finaluri);

                    startActivity(intent);

                }
                break;

            case GALLERY_REQUEST:

                if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {


                    imguri = data.getData();
                    String imagepath = getPath(imguri, "Photo");
                    imguri = Uri.fromFile(new File(imagepath));
                   // Bundle bundle = new Bundle();
                    Intent i = new Intent(getActivity(), EraserActivity.class);
                    i.putExtra("source", "add");
                    i.putExtra("image", "gallery");
                    i.putExtra("uri", imguri);

                    startActivity(i);

                }
                break;
        }
    }



    public String getPath(Uri uri, String type) {
        String document_id = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        }

        String path = "";
        if (type.equalsIgnoreCase("Photo")) {

            cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor.moveToFirst()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        } else {

            cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor.moveToFirst()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                cursor.close();
            }
        }

        if (path.equalsIgnoreCase("")) {
            path = document_id;
        }

        return path;
    }
}