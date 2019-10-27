package my.closet.fashion.style.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import io.realm.Realm;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.modesl.Looks;
import nl.dionsegijn.konfetti.KonfettiView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class PostFeedActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mRootRef;
    private MixpanelAPI mixpanelAPI;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Intent i;
    private String bitmap_obj;
    private PhotoEditor mPhotoEditor;
    private ImageView mPhotoEditorView;
    private ImageView back_button;
    private TextView done_txt;
    private UploadTask uploadTask;
    private String downloaded_url="";
    EditText description_text;
    private String My_DbKey="";
    private FirebaseFirestore profCollection;
    ByteArrayOutputStream baos;
    byte[] data;
    Bundle b;
    private String bitmap_lookbook_obj;
    int metaid;
    Looks looks;
    Realm realm;
    SharedPreferences sharedPreferences;
    boolean post_tut;
    KonfettiView konfettiView;
    LinearLayout postprofilelayout;
    Uri myUri;



    ImageView accblacksq;
    ImageView accblacksqtck;
    ImageView accwhitesq;
    ImageView accwhitesqtck;
    ImageView accgreysq;
    ImageView accgreysqtck;
    ImageView accbeigesq;
    ImageView accbeigesqtck;
    ImageView accredsq;
    ImageView accredsqtck;
    ImageView accpinksq;
    ImageView accpinksqtck;
    ImageView accbluesq;
    ImageView accbluesqtck;
    ImageView accgreensq;
    ImageView accgreensqtck;
    ImageView accyellowsq;
    ImageView accyellowsqtck;
    ImageView accorangesq;
    ImageView accorangesqtck;
    ImageView accbrownsq;
    ImageView accbrownsqtck;
    ImageView accpurplesq;
    ImageView accpurplesqtck;
    ImageView accsilversq;
    ImageView accsilversqtck;
    ImageView accgoldsq;
    ImageView accgoldsqtck;

    ImageView topblacksq;
    ImageView topblacksqtck;
    ImageView topwhitesq;
    ImageView topwhitesqtck;
    ImageView topgreysq;
    ImageView topgreysqtck;
    ImageView topbeigesq;
    ImageView topbeigesqtck;
    ImageView topredsq;
    ImageView topredsqtck;
    ImageView toppinksq;
    ImageView toppinksqtck;
    ImageView topbluesq;
    ImageView topbluesqtck;
    ImageView topgreensq;
    ImageView topgreensqtck;
    ImageView topyellowsq;
    ImageView topyellowsqtck;
    ImageView toporangesq;
    ImageView toporangesqtck;
    ImageView topbrownsq;
    ImageView topbrownsqtck;
    ImageView toppurplesq;
    ImageView toppurplesqtck;
    ImageView topsilversq;
    ImageView topsilversqtck;
    ImageView topgoldsq;
    ImageView topgoldsqtck;

    ImageView botblacksq;
    ImageView botblacksqtck;
    ImageView botwhitesq;
    ImageView botwhitesqtck;
    ImageView botgreysq;
    ImageView botgreysqtck;
    ImageView botbeigesq;
    ImageView botbeigesqtck;
    ImageView botredsq;
    ImageView botredsqtck;
    ImageView botpinksq;
    ImageView botpinksqtck;
    ImageView botbluesq;
    ImageView botbluesqtck;
    ImageView botgreensq;
    ImageView botgreensqtck;
    ImageView botyellowsq;
    ImageView botyellowsqtck;
    ImageView botorangesq;
    ImageView botorangesqtck;
    ImageView botbrownsq;
    ImageView botbrownsqtck;
    ImageView botpurplesq;
    ImageView botpurplesqtck;
    ImageView botsilversq;
    ImageView botsilversqtck;
    ImageView botgoldsq;
    ImageView botgoldsqtck;

    ImageView footblacksq;
    ImageView footblacksqtck;
    ImageView footwhitesq;
    ImageView footwhitesqtck;
    ImageView footgreysq;
    ImageView footgreysqtck;
    ImageView footbeigesq;
    ImageView footbeigesqtck;
    ImageView footredsq;
    ImageView footredsqtck;
    ImageView footpinksq;
    ImageView footpinksqtck;
    ImageView footbluesq;
    ImageView footbluesqtck;
    ImageView footgreensq;
    ImageView footgreensqtck;
    ImageView footyellowsq;
    ImageView footyellowsqtck;
    ImageView footorangesq;
    ImageView footorangesqtck;
    ImageView footbrownsq;
    ImageView footbrownsqtck;
    ImageView footpurplesq;
    ImageView footpurplesqtck;
    ImageView footsilversq;
    ImageView footsilversqtck;
    ImageView footgoldsq;
    ImageView footgoldsqtck;
    int n;
    private File file;
    private RequestOptions requestOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feed);

        Realm.init(this);
        realm=Realm.getDefaultInstance();

        mixpanelAPI= MixpanelAPI.getInstance(PostFeedActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        mRootRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://my-closet-fashion-style.appspot.com/");

        profCollection = FirebaseFirestore.getInstance();
        i = getIntent();
        b = getIntent().getExtras();

        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        konfettiView = (KonfettiView) findViewById(R.id.home_celeb);
        konfettiView.setVisibility(View.GONE);

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);



        //metaid = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
        //looks = realm.where(Looks.class).equalTo("id",metaid).findFirst();
        //Toast.makeText(this, looks.getIdint().toString(), Toast.LENGTH_SHORT).show();


        if (i != null ) {

          n = Objects.requireNonNull(i.getExtras()).getInt("lookid");

        }

        My_DbKey=Utilities.loadPref(PostFeedActivity.this, "My_DbKey", "");

        findViews();


    }

    private void findViews() {

        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean post_tut = sharedPreferences.getBoolean("post",true);

        postprofilelayout = (LinearLayout) findViewById(R.id.postprofilelayout);

        accblacksq = findViewById(R.id.postaccblacksq);
        accblacksqtck = findViewById(R.id.postaccblacksqtck);
        accblacksqtck.setVisibility(View.INVISIBLE);

        accsilversq = findViewById(R.id.postaccsilversq);
        accsilversqtck = findViewById(R.id.postaccsilversqtck);
        accsilversqtck.setVisibility(View.INVISIBLE);

        accgoldsq = findViewById(R.id.postaccgoldsq);
        accgoldsqtck = findViewById(R.id.postaccgoldsqtck);
        accgoldsqtck.setVisibility(View.INVISIBLE);

        accwhitesq = findViewById(R.id.postaccwhitesq);
        accwhitesqtck = findViewById(R.id.postaccwhitesqtck);
        accwhitesqtck.setVisibility(View.INVISIBLE);

        accgreysq = findViewById(R.id.postaccgreysq);
        accgreysqtck = findViewById(R.id.postaccgreysqtck);
        accgreysqtck.setVisibility(View.INVISIBLE);

        accbeigesq = findViewById(R.id.postaccbeigesq);
        accbeigesqtck = findViewById(R.id.postaccbeigesqtck);
        accbeigesqtck.setVisibility(View.INVISIBLE);

        accredsq = findViewById(R.id.postaccredsq);
        accredsqtck = findViewById(R.id.postaccredsqtck);
        accredsqtck.setVisibility(View.INVISIBLE);

        accpinksq = findViewById(R.id.postaccpinksq);
        accpinksqtck = findViewById(R.id.postaccpinksqtck);
        accpinksqtck.setVisibility(View.INVISIBLE);

        accbluesq = findViewById(R.id.postaccbluesq);
        accbluesqtck = findViewById(R.id.postaccbluesqtck);
        accbluesqtck.setVisibility(View.INVISIBLE);


        accgreensq = findViewById(R.id.postaccgreensq);
        accgreensqtck = findViewById(R.id.postaccgreensqtck);
        accgreensqtck.setVisibility(View.INVISIBLE);

        accyellowsq = findViewById(R.id.postaccyellowsq);
        accyellowsqtck = findViewById(R.id.postaccyellowsqtck);
        accyellowsqtck.setVisibility(View.INVISIBLE);

        accorangesq = findViewById(R.id.postaccorangesq);
        accorangesqtck = findViewById(R.id.postaccorangesqtck);
        accorangesqtck.setVisibility(View.INVISIBLE);

        accbrownsq = findViewById(R.id.postaccbrownsq);
        accbrownsqtck = findViewById(R.id.postaccbrownsqtck);
        accbrownsqtck.setVisibility(View.INVISIBLE);

        accpurplesq = findViewById(R.id.postaccpurplesq);
        accpurplesqtck = findViewById(R.id.postaccpurplesqtck);
        accpurplesqtck.setVisibility(View.INVISIBLE);

        topblacksq = findViewById(R.id.posttopblacksq);
        topblacksqtck = findViewById(R.id.posttopblacksqtck);
        topblacksqtck.setVisibility(View.INVISIBLE);

        topsilversq = findViewById(R.id.posttopsilversq);
        topsilversqtck = findViewById(R.id.posttopsilversqtck);
        topsilversqtck.setVisibility(View.INVISIBLE);

        topgoldsq = findViewById(R.id.posttopgoldsq);
        topgoldsqtck = findViewById(R.id.posttopgoldsqtck);
        topgoldsqtck.setVisibility(View.INVISIBLE);

        topwhitesq = findViewById(R.id.posttopwhitesq);
        topwhitesqtck = findViewById(R.id.posttopwhitesqtck);
        topwhitesqtck.setVisibility(View.INVISIBLE);

        topgreysq = findViewById(R.id.posttopgreysq);
        topgreysqtck = findViewById(R.id.posttopgreysqtck);
        topgreysqtck.setVisibility(View.INVISIBLE);

        topbeigesq = findViewById(R.id.posttopbeigesq);
        topbeigesqtck = findViewById(R.id.posttopbeigesqtck);
        topbeigesqtck.setVisibility(View.INVISIBLE);

        topredsq = findViewById(R.id.posttopredsq);
        topredsqtck = findViewById(R.id.posttopredsqtck);
        topredsqtck.setVisibility(View.INVISIBLE);

        toppinksq = findViewById(R.id.posttoppinksq);
        toppinksqtck = findViewById(R.id.posttoppinksqtck);
        toppinksqtck.setVisibility(View.INVISIBLE);

        topbluesq = findViewById(R.id.posttopbluesq);
        topbluesqtck = findViewById(R.id.posttopbluesqtck);
        topbluesqtck.setVisibility(View.INVISIBLE);


        topgreensq = findViewById(R.id.posttopgreensq);
        topgreensqtck = findViewById(R.id.posttopgreensqtck);
        topgreensqtck.setVisibility(View.INVISIBLE);

        topyellowsq = findViewById(R.id.posttopyellowsq);
        topyellowsqtck = findViewById(R.id.posttopyellowsqtck);
        topyellowsqtck.setVisibility(View.INVISIBLE);

        toporangesq = findViewById(R.id.posttoporangesq);
        toporangesqtck = findViewById(R.id.posttoporangesqtck);
        toporangesqtck.setVisibility(View.INVISIBLE);

        topbrownsq = findViewById(R.id.posttopbrownsq);
        topbrownsqtck = findViewById(R.id.posttopbrownsqtck);
        topbrownsqtck.setVisibility(View.INVISIBLE);

        toppurplesq = findViewById(R.id.posttoppurplesq);
        toppurplesqtck = findViewById(R.id.posttoppurplesqtck);
        toppurplesqtck.setVisibility(View.INVISIBLE);

        botblacksq = findViewById(R.id.postbottomblacksq);
        botblacksqtck = findViewById(R.id.postbottomblacksqtck);
        botblacksqtck.setVisibility(View.INVISIBLE);

        botsilversq = findViewById(R.id.postbottomsilversq);
        botsilversqtck = findViewById(R.id.postbottomsilversqtck);
        botsilversqtck.setVisibility(View.INVISIBLE);

        botgoldsq = findViewById(R.id.postbottomgoldsq);
        botgoldsqtck = findViewById(R.id.postbottomgoldsqtck);
        botgoldsqtck.setVisibility(View.INVISIBLE);

        botwhitesq = findViewById(R.id.postbottomwhitesq);
        botwhitesqtck = findViewById(R.id.postbottomwhitesqtck);
        botwhitesqtck.setVisibility(View.INVISIBLE);

        botgreysq = findViewById(R.id.postbottomgreysq);
        botgreysqtck = findViewById(R.id.postbottomgreysqtck);
        botgreysqtck.setVisibility(View.INVISIBLE);

        botbeigesq = findViewById(R.id.postbottombeigesq);
        botbeigesqtck = findViewById(R.id.postbottombeigesqtck);
        botbeigesqtck.setVisibility(View.INVISIBLE);

        botredsq = findViewById(R.id.postbottomredsq);
        botredsqtck = findViewById(R.id.postbottomredsqtck);
        botredsqtck.setVisibility(View.INVISIBLE);

        botpinksq = findViewById(R.id.postbottompinksq);
        botpinksqtck = findViewById(R.id.postbottompinksqtck);
        botpinksqtck.setVisibility(View.INVISIBLE);

        botbluesq = findViewById(R.id.postbottombluesq);
        botbluesqtck = findViewById(R.id.postbottombluesqtck);
        botbluesqtck.setVisibility(View.INVISIBLE);


        botgreensq = findViewById(R.id.postbottomgreensq);
        botgreensqtck = findViewById(R.id.postbottomgreensqtck);
        botgreensqtck.setVisibility(View.INVISIBLE);

        botyellowsq = findViewById(R.id.postbottomyellowsq);
        botyellowsqtck = findViewById(R.id.postbottomyellowsqtck);
        botyellowsqtck.setVisibility(View.INVISIBLE);

        botorangesq = findViewById(R.id.postbottomorangesq);
        botorangesqtck = findViewById(R.id.postbottomorangesqtck);
        botorangesqtck.setVisibility(View.INVISIBLE);

        botbrownsq = findViewById(R.id.postbottombrownsq);
        botbrownsqtck = findViewById(R.id.postbottombrownsqtck);
        botbrownsqtck.setVisibility(View.INVISIBLE);

        botpurplesq = findViewById(R.id.postbottompurplesq);
        botpurplesqtck = findViewById(R.id.postbottompurplesqtck);
        botpurplesqtck.setVisibility(View.INVISIBLE);

        footblacksq = findViewById(R.id.postfootwearblacksq);
        footblacksqtck = findViewById(R.id.postfootwearblacksqtck);
        footblacksqtck.setVisibility(View.INVISIBLE);

        footsilversq = findViewById(R.id.postfootwearsilversq);
        footsilversqtck = findViewById(R.id.postfootwearsilversqtck);
        footsilversqtck.setVisibility(View.INVISIBLE);

        footgoldsq = findViewById(R.id.postfootweargoldsq);
        footgoldsqtck = findViewById(R.id.postfootweargoldsqtck);
        footgoldsqtck.setVisibility(View.INVISIBLE);

        footwhitesq = findViewById(R.id.postfootwearwhitesq);
        footwhitesqtck = findViewById(R.id.postfootwearwhitesqtck);
        footwhitesqtck.setVisibility(View.INVISIBLE);

        footgreysq = findViewById(R.id.postfootweargreysq);
        footgreysqtck = findViewById(R.id.postfootweargreysqtck);
        footgreysqtck.setVisibility(View.INVISIBLE);

        footbeigesq = findViewById(R.id.postfootwearbeigesq);
        footbeigesqtck = findViewById(R.id.postfootwearbeigesqtck);
        footbeigesqtck.setVisibility(View.INVISIBLE);

        footredsq = findViewById(R.id.postfootwearredsq);
        footredsqtck = findViewById(R.id.postfootwearredsqtck);
        footredsqtck.setVisibility(View.INVISIBLE);

        footpinksq = findViewById(R.id.postfootwearpinksq);
        footpinksqtck = findViewById(R.id.postfootwearpinksqtck);
        footpinksqtck.setVisibility(View.INVISIBLE);

        footbluesq = findViewById(R.id.postfootwearbluesq);
        footbluesqtck = findViewById(R.id.postfootwearbluesqtck);
        footbluesqtck.setVisibility(View.INVISIBLE);


        footgreensq = findViewById(R.id.postfootweargreensq);
        footgreensqtck = findViewById(R.id.postfootweargreensqtck);
        footgreensqtck.setVisibility(View.INVISIBLE);

        footyellowsq = findViewById(R.id.postfootwearyellowsq);
        footyellowsqtck = findViewById(R.id.postfootwearyellowsqtck);
        footyellowsqtck.setVisibility(View.INVISIBLE);

        footorangesq = findViewById(R.id.postfootwearorangesq);
        footorangesqtck = findViewById(R.id.postfootwearorangesqtck);
        footorangesqtck.setVisibility(View.INVISIBLE);

        footbrownsq = findViewById(R.id.postfootwearbrownsq);
        footbrownsqtck = findViewById(R.id.postfootwearbrownsqtck);
        footbrownsqtck.setVisibility(View.INVISIBLE);

        footpurplesq = findViewById(R.id.postfootwearpurplesq);
        footpurplesqtck = findViewById(R.id.postfootwearpurplesqtck);
        footpurplesqtck.setVisibility(View.INVISIBLE);

        accblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accblacksqtck.setVisibility(View.VISIBLE);


                //black = blackf;

            }
        });

        accblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accblacksqtck.setVisibility(View.INVISIBLE);
                accblacksq.setVisibility(View.VISIBLE);


                //black = nooption;
            }
        });

        accwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accwhitesqtck.setVisibility(View.VISIBLE);


                //white = whitef;

            }
        });

        accwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accwhitesqtck.setVisibility(View.INVISIBLE);
                accwhitesq.setVisibility(View.VISIBLE);



                //white = nooption;
            }
        });

        accgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accgreysqtck.setVisibility(View.VISIBLE);


                //grey = greyf;

            }
        });

        accgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accgreysqtck.setVisibility(View.INVISIBLE);
                accgreysq.setVisibility(View.VISIBLE);


                //grey = nooption;
            }
        });

        accbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accbeigesqtck.setVisibility(View.VISIBLE);



                //beige = beigef;

            }
        });

        accbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accbeigesqtck.setVisibility(View.INVISIBLE);
                accbeigesq.setVisibility(View.VISIBLE);


                //beige = nooption;
            }
        });

        accredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accredsqtck.setVisibility(View.VISIBLE);



                //red = redf;


            }
        });

        accredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accredsqtck.setVisibility(View.INVISIBLE);
                accredsq.setVisibility(View.VISIBLE);



                //red = nooption;
            }
        });


        accpinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accpinksqtck.setVisibility(View.VISIBLE);


                //pink = pinkf;

            }
        });

        accpinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accpinksqtck.setVisibility(View.INVISIBLE);
                accpinksq.setVisibility(View.VISIBLE);



                //pink = nooption;
            }
        });

        accsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                accsilversqtck.setVisibility(View.VISIBLE);



                //silver = silverf;

            }
        });

        accsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                accsilversqtck.setVisibility(View.INVISIBLE);
                accsilversq.setVisibility(View.VISIBLE);



                //silver = nooption;
            }
        });

        accgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                accgoldsqtck.setVisibility(View.VISIBLE);



                //gold = goldf;

            }
        });

        accgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accgoldsqtck.setVisibility(View.INVISIBLE);
                accgoldsq.setVisibility(View.VISIBLE);



                //gold = nooption;
            }
        });


        accyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accyellowsqtck.setVisibility(View.VISIBLE);



                //yellow = yellowf;

            }
        });

        accyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accyellowsqtck.setVisibility(View.INVISIBLE);
                accyellowsq.setVisibility(View.VISIBLE);



                //yellow = nooption;
            }
        });

        accbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accbrownsqtck.setVisibility(View.VISIBLE);



                //brown = brownf;

            }
        });

        accbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accbrownsqtck.setVisibility(View.INVISIBLE);
                accbrownsq.setVisibility(View.VISIBLE);



                //brown = nooption;
            }
        });

        accpurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accpurplesqtck.setVisibility(View.VISIBLE);


                //purple = purplef;

            }
        });

        accpurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accpurplesqtck.setVisibility(View.INVISIBLE);
                accpurplesq.setVisibility(View.VISIBLE);



                //purple = nooption;
            }
        });

        accbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accbluesqtck.setVisibility(View.VISIBLE);



                //blue = bluef;

            }
        });

        accbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accbluesqtck.setVisibility(View.INVISIBLE);
                accbluesq.setVisibility(View.VISIBLE);



                //blue = nooption;
            }
        });


        accorangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accorangesqtck.setVisibility(View.VISIBLE);

                //orange = orangef;

            }
        });

        accorangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accorangesqtck.setVisibility(View.INVISIBLE);
                accorangesq.setVisibility(View.VISIBLE);



                //orange = nooption;
            }
        });


        accgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accgreensqtck.setVisibility(View.VISIBLE);



                //green = greenf;

            }
        });

        accgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accgreensqtck.setVisibility(View.INVISIBLE);
                accgreensq.setVisibility(View.VISIBLE);



                //green = nooption;
            }

        });


        topblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topblacksqtck.setVisibility(View.VISIBLE);


                //black = blackf;

            }
        });

        topblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topblacksqtck.setVisibility(View.INVISIBLE);
                topblacksq.setVisibility(View.VISIBLE);


                //black = nooption;
            }
        });

        topwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topwhitesqtck.setVisibility(View.VISIBLE);


                //white = whitef;

            }
        });

        topwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topwhitesqtck.setVisibility(View.INVISIBLE);
                topwhitesq.setVisibility(View.VISIBLE);



                //white = nooption;
            }
        });

        topgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topgreysqtck.setVisibility(View.VISIBLE);


                //grey = greyf;

            }
        });

        topgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topgreysqtck.setVisibility(View.INVISIBLE);
                topgreysq.setVisibility(View.VISIBLE);


                //grey = nooption;
            }
        });

        topbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topbeigesqtck.setVisibility(View.VISIBLE);



                //beige = beigef;

            }
        });

        topbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topbeigesqtck.setVisibility(View.INVISIBLE);
                topbeigesq.setVisibility(View.VISIBLE);


                //beige = nooption;
            }
        });

        topredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topredsqtck.setVisibility(View.VISIBLE);



                //red = redf;


            }
        });

        topredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topredsqtck.setVisibility(View.INVISIBLE);
                topredsq.setVisibility(View.VISIBLE);



                //red = nooption;
            }
        });


        toppinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                toppinksqtck.setVisibility(View.VISIBLE);


                //pink = pinkf;

            }
        });

        toppinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                toppinksqtck.setVisibility(View.INVISIBLE);
                toppinksq.setVisibility(View.VISIBLE);



                //pink = nooption;
            }
        });

        topsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                topsilversqtck.setVisibility(View.VISIBLE);



                //silver = silverf;

            }
        });

        topsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                topsilversqtck.setVisibility(View.INVISIBLE);
                topsilversq.setVisibility(View.VISIBLE);



                //silver = nooption;
            }
        });

        topgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                topgoldsqtck.setVisibility(View.VISIBLE);



                //gold = goldf;

            }
        });

        topgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                topgoldsqtck.setVisibility(View.INVISIBLE);
                topgoldsq.setVisibility(View.VISIBLE);



                //gold = nooption;
            }
        });


        topyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topyellowsqtck.setVisibility(View.VISIBLE);



                //yellow = yellowf;

            }
        });

        topyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topyellowsqtck.setVisibility(View.INVISIBLE);
                topyellowsq.setVisibility(View.VISIBLE);



                //yellow = nooption;
            }
        });

        topbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topbrownsqtck.setVisibility(View.VISIBLE);



                //brown = brownf;

            }
        });

        topbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topbrownsqtck.setVisibility(View.INVISIBLE);
                topbrownsq.setVisibility(View.VISIBLE);



                //brown = nooption;
            }
        });

        toppurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                toppurplesqtck.setVisibility(View.VISIBLE);



                //purple = purplef;

            }
        });

        toppurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                toppurplesqtck.setVisibility(View.INVISIBLE);
                toppurplesq.setVisibility(View.VISIBLE);



                //purple = nooption;
            }
        });

        topbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topbluesqtck.setVisibility(View.VISIBLE);



                //blue = bluef;

            }
        });

        topbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topbluesqtck.setVisibility(View.INVISIBLE);
                topbluesq.setVisibility(View.VISIBLE);


                //blue = nooption;
            }
        });


        toporangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                toporangesqtck.setVisibility(View.VISIBLE);


                //orange = orangef;

            }
        });

        toporangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                toporangesqtck.setVisibility(View.INVISIBLE);
                toporangesq.setVisibility(View.VISIBLE);




                //orange = nooption;
            }
        });


        topgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topgreensqtck.setVisibility(View.VISIBLE);



                //green = greenf;

            }
        });

        topgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topgreensqtck.setVisibility(View.INVISIBLE);
                topgreensq.setVisibility(View.VISIBLE);



                //green = nooption;
            }

        });


        botblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botblacksqtck.setVisibility(View.VISIBLE);


                //black = blackf;

            }
        });

        botblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botblacksqtck.setVisibility(View.INVISIBLE);
                botblacksq.setVisibility(View.VISIBLE);


                //black = nooption;
            }
        });

        botwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botwhitesqtck.setVisibility(View.VISIBLE);

                //white = whitef;

            }
        });

        botwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botwhitesqtck.setVisibility(View.INVISIBLE);
                botwhitesq.setVisibility(View.VISIBLE);



                //white = nooption;
            }
        });

        botgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botgreysqtck.setVisibility(View.VISIBLE);

                //grey = greyf;

            }
        });

        botgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botgreysqtck.setVisibility(View.INVISIBLE);
                botgreysq.setVisibility(View.VISIBLE);


                //grey = nooption;
            }
        });

        botbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botbeigesqtck.setVisibility(View.VISIBLE);



                //beige = beigef;

            }
        });

        botbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botbeigesqtck.setVisibility(View.INVISIBLE);
                botbeigesq.setVisibility(View.VISIBLE);


                //beige = nooption;
            }
        });

        botredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botredsqtck.setVisibility(View.VISIBLE);



                //red = redf;


            }
        });

        botredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botredsqtck.setVisibility(View.INVISIBLE);
                botredsq.setVisibility(View.VISIBLE);



                //red = nooption;
            }
        });


        botpinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botpinksqtck.setVisibility(View.VISIBLE);


                //pink = pinkf;

            }
        });

        botpinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botpinksqtck.setVisibility(View.INVISIBLE);
                botpinksq.setVisibility(View.VISIBLE);



                //pink = nooption;
            }
        });

        botsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                botsilversqtck.setVisibility(View.VISIBLE);



                //silver = silverf;

            }
        });

        botsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                botsilversqtck.setVisibility(View.INVISIBLE);
                botsilversq.setVisibility(View.VISIBLE);



                //silver = nooption;
            }
        });

        botgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                botgoldsqtck.setVisibility(View.VISIBLE);



                //gold = goldf;

            }
        });

        botgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                botgoldsqtck.setVisibility(View.INVISIBLE);
                botgoldsq.setVisibility(View.VISIBLE);




                //gold = nooption;
            }
        });


        botyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botyellowsqtck.setVisibility(View.VISIBLE);




                //yellow = yellowf;

            }
        });

        botyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botyellowsqtck.setVisibility(View.INVISIBLE);
                botyellowsq.setVisibility(View.VISIBLE);




                //yellow = nooption;
            }
        });

        botbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botbrownsqtck.setVisibility(View.VISIBLE);




                //brown = brownf;

            }
        });

        botbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botbrownsqtck.setVisibility(View.INVISIBLE);
                botbrownsq.setVisibility(View.VISIBLE);



                //brown = nooption;
            }
        });

        botpurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botpurplesqtck.setVisibility(View.VISIBLE);



                //purple = purplef;

            }
        });

        botpurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botpurplesqtck.setVisibility(View.INVISIBLE);
                botpurplesq.setVisibility(View.VISIBLE);




                //purple = nooption;
            }
        });

        botbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botbluesqtck.setVisibility(View.VISIBLE);




                //blue = bluef;

            }
        });

        botbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botbluesqtck.setVisibility(View.INVISIBLE);
                botbluesq.setVisibility(View.VISIBLE);



                //blue = nooption;
            }
        });


        botorangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botorangesqtck.setVisibility(View.VISIBLE);



                //orange = orangef;

            }
        });

        botorangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botorangesqtck.setVisibility(View.INVISIBLE);
                botorangesq.setVisibility(View.VISIBLE);




                //orange = nooption;
            }
        });


        botgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botgreensqtck.setVisibility(View.VISIBLE);




                //green = greenf;

            }
        });

        botgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botgreensqtck.setVisibility(View.INVISIBLE);
                botgreensq.setVisibility(View.VISIBLE);




                //green = nooption;
            }

        });


        footblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footblacksqtck.setVisibility(View.VISIBLE);



                //black = blackf;

            }
        });

        footblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footblacksqtck.setVisibility(View.INVISIBLE);
                footblacksq.setVisibility(View.VISIBLE);



                //black = nooption;
            }
        });

        footwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footwhitesqtck.setVisibility(View.VISIBLE);



                //white = whitef;

            }
        });

        footwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footwhitesqtck.setVisibility(View.INVISIBLE);
                footwhitesq.setVisibility(View.VISIBLE);


                //white = nooption;
            }
        });

        footgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footgreysqtck.setVisibility(View.VISIBLE);


                //grey = greyf;

            }
        });

        footgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footgreysqtck.setVisibility(View.INVISIBLE);
                footgreysq.setVisibility(View.VISIBLE);


                //grey = nooption;
            }
        });

        footbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footbeigesqtck.setVisibility(View.VISIBLE);



                //beige = beigef;

            }
        });

        footbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footbeigesqtck.setVisibility(View.INVISIBLE);
                footbeigesq.setVisibility(View.VISIBLE);



                //beige = nooption;
            }
        });

        footredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footredsqtck.setVisibility(View.VISIBLE);




                //red = redf;


            }
        });

        footredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footredsqtck.setVisibility(View.INVISIBLE);
                footredsq.setVisibility(View.VISIBLE);



                //red = nooption;
            }
        });


        footpinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footpinksqtck.setVisibility(View.VISIBLE);



                //pink = pinkf;

            }
        });

        footpinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footpinksqtck.setVisibility(View.INVISIBLE);
                footpinksq.setVisibility(View.VISIBLE);




                //pink = nooption;
            }
        });

        footsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                footsilversqtck.setVisibility(View.VISIBLE);




                //silver = silverf;

            }
        });

        footsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                footsilversqtck.setVisibility(View.INVISIBLE);
                footsilversq.setVisibility(View.VISIBLE);



                //silver = nooption;
            }
        });

        footgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                footgoldsqtck.setVisibility(View.VISIBLE);




                //gold = goldf;

            }
        });

        footgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                footgoldsqtck.setVisibility(View.INVISIBLE);
                footgoldsq.setVisibility(View.VISIBLE);



                //gold = nooption;
            }
        });


        footyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footyellowsqtck.setVisibility(View.VISIBLE);



                //yellow = yellowf;

            }
        });

        footyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footyellowsqtck.setVisibility(View.INVISIBLE);
                footyellowsq.setVisibility(View.VISIBLE);



                //yellow = nooption;
            }
        });

        footbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footbrownsqtck.setVisibility(View.VISIBLE);



                //brown = brownf;

            }
        });

        footbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footbrownsqtck.setVisibility(View.INVISIBLE);
                footbrownsq.setVisibility(View.VISIBLE);




                //brown = nooption;
            }
        });

        footpurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footpurplesqtck.setVisibility(View.VISIBLE);


                //purple = purplef;

            }
        });

        footpurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footpurplesqtck.setVisibility(View.INVISIBLE);
                footpurplesq.setVisibility(View.VISIBLE);



                //purple = nooption;
            }
        });

        footbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footbluesqtck.setVisibility(View.VISIBLE);



                //blue = bluef;

            }
        });

        footbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footbluesqtck.setVisibility(View.INVISIBLE);
                footbluesq.setVisibility(View.VISIBLE);



                //blue = nooption;
            }
        });


        footorangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footorangesqtck.setVisibility(View.VISIBLE);


                //orange = orangef;

            }
        });

        footorangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footorangesqtck.setVisibility(View.INVISIBLE);
                footorangesq.setVisibility(View.VISIBLE);




                //orange = nooption;
            }
        });


        footgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footgreensqtck.setVisibility(View.VISIBLE);




                //green = greenf;

            }
        });

        footgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footgreensqtck.setVisibility(View.INVISIBLE);
                footgreensq.setVisibility(View.VISIBLE);




                //green = nooption;
            }

        });




        mPhotoEditorView = (ImageView) findViewById(R.id.photoEditorView);

        back_button=(ImageView)findViewById(R.id.back_button);
        back_button.setOnClickListener(this);

        done_txt=(TextView)findViewById(R.id.done_txt);
        done_txt.setOnClickListener(this);

        description_text=(EditText)findViewById(R.id.description_text);
        description_text.setText(getIntent().getStringExtra("stylename"));

        if (post_tut){

            new MaterialTapTargetPrompt.Builder(PostFeedActivity.this,R.style.MaterialTapTargetPromptTheme)
                    .setTarget(findViewById(R.id.done_txt))
                    .setSecondaryText("Click to Post")
                    .setAnimationInterpolator(new FastOutSlowInInterpolator())
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {

                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){

                                if (mixpanelAPI!=null){

                                    mixpanelAPI.track("PostTutorialClicked");

                                }



                                SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("post", false);
                                editor.apply();

                                prompt.finish();
                            }
                        }
                    })

                    .show();


        }



       /* */




        if (b!=null && b.containsKey("Bitmap")){

            bitmap_obj = i.getStringExtra("Bitmap");

            if (bitmap_obj != null && !bitmap_obj.equalsIgnoreCase("")) {

               Glide.with(PostFeedActivity.this).asBitmap().load(bitmap_obj).into(new SimpleTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                       myUri = getLocalBitmapUri(resource);
                       Glide.with(PostFeedActivity.this).load(myUri).apply(requestOptions).into(mPhotoEditorView);

                   }
               });
            }

        }else {

            bitmap_lookbook_obj = i.getStringExtra("Lookbook");


            if (bitmap_lookbook_obj!=null) {
                Bitmap bmp = new ImageSaver(PostFeedActivity.this).setFileName(bitmap_lookbook_obj).setDirectoryName("mycloset").load();
                mPhotoEditorView.setImageBitmap(bmp);
               // bitmap_obj = getFilePath(bitmap_obj);
                baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG,100,baos);
                data = baos.toByteArray();
                postprofilelayout.setVisibility(View.GONE);


            }
        }





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button:
                DeletePath();
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);

                break;

            case R.id.done_txt:
                if (mixpanelAPI!=null) {
                    mixpanelAPI.track("Post");
                }

                    if (!Utilities.loadPref(PostFeedActivity.this, "email", "").equalsIgnoreCase("")) {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                SendPicturetoDB();

                            }
                        });

                    } else {
                        Intent ii = new Intent(PostFeedActivity.this, FbGmailActivity.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                    }


                break;
        }
    }

    private void SendPicturetoDB() {

        Utilities.showLoading(PostFeedActivity.this, "Uploading...");

        final StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());
        if (bitmap_obj!=null) {
            uploadTask = ref.putFile(myUri);
        }else {

            uploadTask = ref.putBytes(data);
        }
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {

                    // downloaded_url will retrive the downloaadlink of imgae url
                    String download_url = Objects.requireNonNull(task.getResult()).toString();
                    if (download_url != null) {

                        Long tsLong = System.currentTimeMillis() / 1000;
                        String timestamp = tsLong.toString();

                        final Map<String, Object> data = new HashMap<>();
                        final String id = UUID.randomUUID().toString();
                        final Map<String,Object> map = new HashMap<>();
                        final ArrayList<String> acclist = new ArrayList<>();
                        final ArrayList<String> toplist = new ArrayList<>();
                        final ArrayList<String> botlist = new ArrayList<>();
                        final ArrayList<String> footlist = new ArrayList<>();

                        if (n==0){

                            n = randomno();
                        }

                        data.put("id", id);
                        data.put("lookid", n);

                        data.put("image", download_url);
                        data.put("description", description_text.getText().toString().toLowerCase());
                        data.put("timestamp", timestamp);
                        data.put("email", Utilities.loadPref(PostFeedActivity.this, "email", ""));
                        data.put("languages", "");
                        data.put("dbkey",My_DbKey);

                        map.put("lookid",n);

                        if (accblacksqtck.getVisibility()==View.VISIBLE
                                || accwhitesqtck.getVisibility()==View.VISIBLE
                                || accgreysqtck.getVisibility()==View.VISIBLE
                                || accbeigesqtck.getVisibility()==View.VISIBLE
                                || accredsqtck.getVisibility()==View.VISIBLE
                                || accpinksqtck.getVisibility()==View.VISIBLE
                                || accsilversqtck.getVisibility()==View.VISIBLE
                                || accgreensqtck.getVisibility()==View.VISIBLE
                                || accbluesqtck.getVisibility()==View.VISIBLE
                                || accyellowsqtck.getVisibility()==View.VISIBLE
                                || accorangesqtck.getVisibility()==View.VISIBLE
                                || accbrownsqtck.getVisibility()==View.VISIBLE
                                || accpurplesqtck.getVisibility()==View.VISIBLE
                                || accgoldsqtck.getVisibility()==View.VISIBLE){

                            if (accblacksqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Black");
                            }

                            if (accwhitesqtck.getVisibility()==View.VISIBLE){

                                acclist.add("White");
                            }

                            if (accgreysqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Grey");
                            }

                            if (accbeigesqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Beige");
                            }

                            if (accredsqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Red");
                            }

                            if (accpinksqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Pink");
                            }

                            if (accsilversqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Silver");
                            }

                            if (accgreensqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Green");
                            }

                            if (accbluesqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Blue");
                            }

                            if (accyellowsqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Yellow");
                            }

                            if (accorangesqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Orange");
                            }

                            if (accbrownsqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Brown");
                            }

                            if (accpurplesqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Purple");
                            }

                            if (accgoldsqtck.getVisibility()==View.VISIBLE){

                                acclist.add("Gold");
                            }


                            map.put("Accessories",acclist);

                        }

                        if (topblacksqtck.getVisibility()==View.VISIBLE
                                || topwhitesqtck.getVisibility()==View.VISIBLE
                                || topgreysqtck.getVisibility()==View.VISIBLE
                                || topbeigesqtck.getVisibility()==View.VISIBLE
                                || topredsqtck.getVisibility()==View.VISIBLE
                                || toppinksqtck.getVisibility()==View.VISIBLE
                                || topsilversqtck.getVisibility()==View.VISIBLE
                                || topgreensqtck.getVisibility()==View.VISIBLE
                                || topbluesqtck.getVisibility()==View.VISIBLE
                                || topyellowsqtck.getVisibility()==View.VISIBLE
                                || toporangesqtck.getVisibility()==View.VISIBLE
                                || topbrownsqtck.getVisibility()==View.VISIBLE
                                || toppurplesqtck.getVisibility()==View.VISIBLE
                                || topgoldsqtck.getVisibility()==View.VISIBLE){

                            if (topblacksqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Black");
                            }

                            if (topwhitesqtck.getVisibility()==View.VISIBLE){

                                toplist.add("White");
                            }

                            if (topgreysqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Grey");
                            }

                            if (topbeigesqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Beige");
                            }

                            if (topredsqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Red");
                            }

                            if (toppinksqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Pink");
                            }

                            if (topsilversqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Silver");
                            }

                            if (topgreensqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Green");
                            }

                            if (topbluesqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Blue");
                            }

                            if (topyellowsqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Yellow");
                            }

                            if (toporangesqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Orange");
                            }

                            if (topbrownsqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Brown");
                            }

                            if (toppurplesqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Purple");
                            }

                            if (topgoldsqtck.getVisibility()==View.VISIBLE){

                                toplist.add("Gold");
                            }


                            map.put("Tops",toplist);

                        }

                        if (botblacksqtck.getVisibility()==View.VISIBLE
                                || botwhitesqtck.getVisibility()==View.VISIBLE
                                || botgreysqtck.getVisibility()==View.VISIBLE
                                || botbeigesqtck.getVisibility()==View.VISIBLE
                                || botredsqtck.getVisibility()==View.VISIBLE
                                || botpinksqtck.getVisibility()==View.VISIBLE
                                || botsilversqtck.getVisibility()==View.VISIBLE
                                || botgreensqtck.getVisibility()==View.VISIBLE
                                || botbluesqtck.getVisibility()==View.VISIBLE
                                || botyellowsqtck.getVisibility()==View.VISIBLE
                                || botorangesqtck.getVisibility()==View.VISIBLE
                                || botbrownsqtck.getVisibility()==View.VISIBLE
                                || botpurplesqtck.getVisibility()==View.VISIBLE
                                || botgoldsqtck.getVisibility()==View.VISIBLE){

                            if (botblacksqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Black");
                            }

                            if (botwhitesqtck.getVisibility()==View.VISIBLE){

                                botlist.add("White");
                            }

                            if (botgreysqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Grey");
                            }

                            if (botbeigesqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Beige");
                            }

                            if (botredsqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Red");
                            }

                            if (botpinksqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Pink");
                            }

                            if (botsilversqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Silver");
                            }

                            if (botgreensqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Green");
                            }

                            if (botbluesqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Blue");
                            }

                            if (botyellowsqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Yellow");
                            }

                            if (botorangesqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Orange");
                            }

                            if (botbrownsqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Brown");
                            }

                            if (botpurplesqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Purple");
                            }

                            if (botgoldsqtck.getVisibility()==View.VISIBLE){

                                botlist.add("Gold");
                            }


                            map.put("Bottoms",botlist);

                        }

                        if (footblacksqtck.getVisibility()==View.VISIBLE
                                || footwhitesqtck.getVisibility()==View.VISIBLE
                                || footgreysqtck.getVisibility()==View.VISIBLE
                                || footbeigesqtck.getVisibility()==View.VISIBLE
                                || footredsqtck.getVisibility()==View.VISIBLE
                                || footpinksqtck.getVisibility()==View.VISIBLE
                                || footsilversqtck.getVisibility()==View.VISIBLE
                                || footgreensqtck.getVisibility()==View.VISIBLE
                                || footbluesqtck.getVisibility()==View.VISIBLE
                                || footyellowsqtck.getVisibility()==View.VISIBLE
                                || footorangesqtck.getVisibility()==View.VISIBLE
                                || footbrownsqtck.getVisibility()==View.VISIBLE
                                || footpurplesqtck.getVisibility()==View.VISIBLE
                                || footgoldsqtck.getVisibility()==View.VISIBLE){

                            if (footblacksqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Black");
                            }

                            if (footwhitesqtck.getVisibility()==View.VISIBLE){

                                footlist.add("White");
                            }

                            if (footgreysqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Grey");
                            }

                            if (footbeigesqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Beige");
                            }

                            if (footredsqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Red");
                            }

                            if (footpinksqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Pink");
                            }

                            if (footsilversqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Silver");
                            }

                            if (footgreensqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Green");
                            }

                            if (footbluesqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Blue");
                            }

                            if (footyellowsqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Yellow");
                            }

                            if (footorangesqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Orange");
                            }

                            if (footbrownsqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Brown");
                            }

                            if (footpurplesqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Purple");
                            }

                            if (footgoldsqtck.getVisibility()==View.VISIBLE){

                                footlist.add("Gold");
                            }


                            map.put("Footwear",footlist);

                        }









                        profCollection.collection("CommonFeed").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {

                                if (task.isSuccessful()){

                                    profCollection.collection("UsersList").document(My_DbKey).collection("Feed").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(Task<DocumentReference> task) {

                                            Utilities.showToast(PostFeedActivity.this, "Posted...");


                                            Utilities.hideLoading();
                                            DeletePath();
                                            boolean post_tut = sharedPreferences.getBoolean("post",true);

                                            Intent returnTOLogin = new Intent(getApplicationContext(), HomeActivity.class);
                                            returnTOLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            returnTOLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            if (post_tut){

                                                returnTOLogin.putExtra("celebration","celeb");
                                            }
                                            startActivity(returnTOLogin);

                                            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);


                                        }
                                    });



                                }
                            }
                        });

                        profCollection.collection("UsersList").document(My_DbKey).collection("MetaData").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(Task<DocumentReference> task) {

                                if (task.isSuccessful()){


                                }



                            }
                        });


                        profCollection.collection("UsersList").document(My_DbKey).collection("FollowCollection").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){

                                    if (!Objects.requireNonNull(task.getResult()).isEmpty()){

                                        for (DocumentSnapshot documentSnapshot : task.getResult()){

                                            profCollection.collection("UsersList").whereEqualTo("Email",documentSnapshot.get("email")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()){

                                                        for (DocumentSnapshot documentSnapshot1 : Objects.requireNonNull(task.getResult())){

                                                            profCollection.collection("UsersList").document(documentSnapshot1.getId()).collection("Feed").add(data);
                                                        }
                                                    }

                                                }
                                            });

                                        }


                                    }
                                }

                            }
                        });


                        profCollection.collection("UsersList")
                                .document(My_DbKey).collection("Posts").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {

                                    if (bitmap_obj!=null) {


                                        if (new File(bitmap_obj).exists()) {
                                            new File(bitmap_obj).delete();
                                        }

                                    }


                                } else {
                                    DeletePath();
                                    Utilities.hideLoading();
                                    Utilities.showToast(PostFeedActivity.this, "Try again Later");
                                }
                            }
                        });
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

                Toast.makeText(PostFeedActivity.this, "Error:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private int randomno() {


        Random random=new Random();
        return random.nextInt(2134499);

    }

    private String getFilePath(String bitmap_obj){

        File directory;
        directory=this.getDir("mycloset",Context.MODE_PRIVATE);
        return new File(directory,bitmap_obj).getPath();


    }

    private Uri getUrifromBitmap(Bitmap bitmap) {

        File tempdirectory = getExternalFilesDir(null);
        File tempname = null;
        if (tempdirectory != null) {
            tempname = new File(tempdirectory.getAbsolutePath() + "/tempuri/");
        }
        File directory = null;
        try {
            directory = File.createTempFile(bitmap_obj, ".png", tempname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (directory != null && !directory.exists()) {

            directory.mkdirs();
        }

        FileOutputStream fileOutputStream = null;
        try {
            if (directory != null) {
                fileOutputStream = new FileOutputStream(directory);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Uri.fromFile(directory);


        }
    }


    private void DeletePath() {

        if (bitmap_obj != null && !bitmap_obj.equalsIgnoreCase("")) {
            File fdelete = new File(Uri.parse(bitmap_obj).getPath());
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + Uri.parse(bitmap_obj).getPath());
                } else {
                    System.out.println("file not Deleted :" + Uri.parse(bitmap_obj).getPath());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DeletePath();

    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {

            file = new File(Utilities.picturePath, "share_image_" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(PostFeedActivity.this,"my.closet.fashion.style.FileProvider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;


    }
}
