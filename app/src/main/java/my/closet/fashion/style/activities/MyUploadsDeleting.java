package my.closet.fashion.style.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.customs.IconizedMenu;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.FeedResponse;

public class MyUploadsDeleting extends AppCompatActivity {

    Intent intent;
    private FeedResponse feedResponse_obj;
    String picture_path;
    ImageView imageView;
    CircleImageView profimage;
    TextView musername,mpostdescription;
    private RequestOptions requestOptions;
    private String My_DbKey;
    LinearLayout savelayout;
    LinearLayout uploadcard;

    private ArrayList<String> data = new ArrayList<>();
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

    RealmResults<Dresses> r1;
    RealmResults<Dresses> r2;
    ArrayList<Dresses> dressesArrayList = new ArrayList<>();

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
    String key = null;
    FeedResponse feedResponse;
    private RelativeLayout profile_layout;
    private IconizedMenu popup;
    private MixpanelAPI mixpanelAPI;
    private ImageView menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_uploads_fullscreen);

        uploadcard = (LinearLayout) findViewById(R.id.uploadscard);
        uploadcard.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        if (intent != null) {

            feedResponse_obj = (FeedResponse) intent.getSerializableExtra("picture");
        }

        profile_layout = (RelativeLayout) findViewById(R.id.mprofile_layout);

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(MyUploadsDeleting.this, UserPersonalActivity.class);
                ii.putExtra("key", (Serializable) feedResponse_obj);
                startActivity(ii);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });


        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);


        imageView = (ImageView) findViewById(R.id.myuploadspicture);
        musername = (TextView) findViewById(R.id.musername_txt);
        mpostdescription = (TextView) findViewById(R.id.mpostdescription);
        profimage = (CircleImageView) findViewById(R.id.mprofile_pic);
        savelayout = (LinearLayout) findViewById(R.id.savelayout);

        menu = (ImageView) findViewById(R.id.my_uploads_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Popup(v);
            }
        });

        savelayout.setVisibility(View.INVISIBLE);


        savelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyUploadsDeleting.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        accblacksq = findViewById(R.id.accblacksq);
        accblacksqtck = findViewById(R.id.accblacksqtck);
        accblacksqtck.setVisibility(View.INVISIBLE);

        accsilversq = findViewById(R.id.accsilversq);
        accsilversqtck = findViewById(R.id.accsilversqtck);
        accsilversqtck.setVisibility(View.INVISIBLE);

        accgoldsq = findViewById(R.id.accgoldsq);
        accgoldsqtck = findViewById(R.id.accgoldsqtck);
        accgoldsqtck.setVisibility(View.INVISIBLE);

        accwhitesq = findViewById(R.id.accwhitesq);
        accwhitesqtck = findViewById(R.id.accwhitesqtck);
        accwhitesqtck.setVisibility(View.INVISIBLE);

        accgreysq = findViewById(R.id.accgreysq);
        accgreysqtck = findViewById(R.id.accgreysqtck);
        accgreysqtck.setVisibility(View.INVISIBLE);

        accbeigesq = findViewById(R.id.accbeigesq);
        accbeigesqtck = findViewById(R.id.accbeigesqtck);
        accbeigesqtck.setVisibility(View.INVISIBLE);

        accredsq = findViewById(R.id.accredsq);
        accredsqtck = findViewById(R.id.accredsqtck);
        accredsqtck.setVisibility(View.INVISIBLE);

        accpinksq = findViewById(R.id.accpinksq);
        accpinksqtck = findViewById(R.id.accpinksqtck);
        accpinksqtck.setVisibility(View.INVISIBLE);

        accbluesq = findViewById(R.id.accbluesq);
        accbluesqtck = findViewById(R.id.accbluesqtck);
        accbluesqtck.setVisibility(View.INVISIBLE);


        accgreensq = findViewById(R.id.accgreensq);
        accgreensqtck = findViewById(R.id.accgreensqtck);
        accgreensqtck.setVisibility(View.INVISIBLE);

        accyellowsq = findViewById(R.id.accyellowsq);
        accyellowsqtck = findViewById(R.id.accyellowsqtck);
        accyellowsqtck.setVisibility(View.INVISIBLE);

        accorangesq = findViewById(R.id.accorangesq);
        accorangesqtck = findViewById(R.id.accorangesqtck);
        accorangesqtck.setVisibility(View.INVISIBLE);

        accbrownsq = findViewById(R.id.accbrownsq);
        accbrownsqtck = findViewById(R.id.accbrownsqtck);
        accbrownsqtck.setVisibility(View.INVISIBLE);

        accpurplesq = findViewById(R.id.accpurplesq);
        accpurplesqtck = findViewById(R.id.accpurplesqtck);
        accpurplesqtck.setVisibility(View.INVISIBLE);

        topblacksq = findViewById(R.id.topblacksq);
        topblacksqtck = findViewById(R.id.topblacksqtck);
        topblacksqtck.setVisibility(View.INVISIBLE);

        topsilversq = findViewById(R.id.topsilversq);
        topsilversqtck = findViewById(R.id.topsilversqtck);
        topsilversqtck.setVisibility(View.INVISIBLE);

        topgoldsq = findViewById(R.id.topgoldsq);
        topgoldsqtck = findViewById(R.id.topgoldsqtck);
        topgoldsqtck.setVisibility(View.INVISIBLE);

        topwhitesq = findViewById(R.id.topwhitesq);
        topwhitesqtck = findViewById(R.id.topwhitesqtck);
        topwhitesqtck.setVisibility(View.INVISIBLE);

        topgreysq = findViewById(R.id.topgreysq);
        topgreysqtck = findViewById(R.id.topgreysqtck);
        topgreysqtck.setVisibility(View.INVISIBLE);

        topbeigesq = findViewById(R.id.topbeigesq);
        topbeigesqtck = findViewById(R.id.topbeigesqtck);
        topbeigesqtck.setVisibility(View.INVISIBLE);

        topredsq = findViewById(R.id.topredsq);
        topredsqtck = findViewById(R.id.topredsqtck);
        topredsqtck.setVisibility(View.INVISIBLE);

        toppinksq = findViewById(R.id.toppinksq);
        toppinksqtck = findViewById(R.id.toppinksqtck);
        toppinksqtck.setVisibility(View.INVISIBLE);

        topbluesq = findViewById(R.id.topbluesq);
        topbluesqtck = findViewById(R.id.topbluesqtck);
        topbluesqtck.setVisibility(View.INVISIBLE);


        topgreensq = findViewById(R.id.topgreensq);
        topgreensqtck = findViewById(R.id.topgreensqtck);
        topgreensqtck.setVisibility(View.INVISIBLE);

        topyellowsq = findViewById(R.id.topyellowsq);
        topyellowsqtck = findViewById(R.id.topyellowsqtck);
        topyellowsqtck.setVisibility(View.INVISIBLE);

        toporangesq = findViewById(R.id.toporangesq);
        toporangesqtck = findViewById(R.id.toporangesqtck);
        toporangesqtck.setVisibility(View.INVISIBLE);

        topbrownsq = findViewById(R.id.topbrownsq);
        topbrownsqtck = findViewById(R.id.topbrownsqtck);
        topbrownsqtck.setVisibility(View.INVISIBLE);

        toppurplesq = findViewById(R.id.toppurplesq);
        toppurplesqtck = findViewById(R.id.toppurplesqtck);
        toppurplesqtck.setVisibility(View.INVISIBLE);

        botblacksq = findViewById(R.id.bottomblacksq);
        botblacksqtck = findViewById(R.id.bottomblacksqtck);
        botblacksqtck.setVisibility(View.INVISIBLE);

        botsilversq = findViewById(R.id.bottomsilversq);
        botsilversqtck = findViewById(R.id.bottomsilversqtck);
        botsilversqtck.setVisibility(View.INVISIBLE);

        botgoldsq = findViewById(R.id.bottomgoldsq);
        botgoldsqtck = findViewById(R.id.bottomgoldsqtck);
        botgoldsqtck.setVisibility(View.INVISIBLE);

        botwhitesq = findViewById(R.id.bottomwhitesq);
        botwhitesqtck = findViewById(R.id.bottomwhitesqtck);
        botwhitesqtck.setVisibility(View.INVISIBLE);

        botgreysq = findViewById(R.id.bottomgreysq);
        botgreysqtck = findViewById(R.id.bottomgreysqtck);
        botgreysqtck.setVisibility(View.INVISIBLE);

        botbeigesq = findViewById(R.id.bottombeigesq);
        botbeigesqtck = findViewById(R.id.bottombeigesqtck);
        botbeigesqtck.setVisibility(View.INVISIBLE);

        botredsq = findViewById(R.id.bottomredsq);
        botredsqtck = findViewById(R.id.bottomredsqtck);
        botredsqtck.setVisibility(View.INVISIBLE);

        botpinksq = findViewById(R.id.bottompinksq);
        botpinksqtck = findViewById(R.id.bottompinksqtck);
        botpinksqtck.setVisibility(View.INVISIBLE);

        botbluesq = findViewById(R.id.bottombluesq);
        botbluesqtck = findViewById(R.id.bottombluesqtck);
        botbluesqtck.setVisibility(View.INVISIBLE);


        botgreensq = findViewById(R.id.bottomgreensq);
        botgreensqtck = findViewById(R.id.bottomgreensqtck);
        botgreensqtck.setVisibility(View.INVISIBLE);

        botyellowsq = findViewById(R.id.bottomyellowsq);
        botyellowsqtck = findViewById(R.id.bottomyellowsqtck);
        botyellowsqtck.setVisibility(View.INVISIBLE);

        botorangesq = findViewById(R.id.bottomorangesq);
        botorangesqtck = findViewById(R.id.bottomorangesqtck);
        botorangesqtck.setVisibility(View.INVISIBLE);

        botbrownsq = findViewById(R.id.bottombrownsq);
        botbrownsqtck = findViewById(R.id.bottombrownsqtck);
        botbrownsqtck.setVisibility(View.INVISIBLE);

        botpurplesq = findViewById(R.id.bottompurplesq);
        botpurplesqtck = findViewById(R.id.bottompurplesqtck);
        botpurplesqtck.setVisibility(View.INVISIBLE);

        footblacksq = findViewById(R.id.footwearblacksq);
        footblacksqtck = findViewById(R.id.footwearblacksqtck);
        footblacksqtck.setVisibility(View.INVISIBLE);

        footsilversq = findViewById(R.id.footwearsilversq);
        footsilversqtck = findViewById(R.id.footwearsilversqtck);
        footsilversqtck.setVisibility(View.INVISIBLE);

        footgoldsq = findViewById(R.id.footweargoldsq);
        footgoldsqtck = findViewById(R.id.footweargoldsqtck);
        footgoldsqtck.setVisibility(View.INVISIBLE);

        footwhitesq = findViewById(R.id.footwearwhitesq);
        footwhitesqtck = findViewById(R.id.footwearwhitesqtck);
        footwhitesqtck.setVisibility(View.INVISIBLE);

        footgreysq = findViewById(R.id.footweargreysq);
        footgreysqtck = findViewById(R.id.footweargreysqtck);
        footgreysqtck.setVisibility(View.INVISIBLE);

        footbeigesq = findViewById(R.id.footwearbeigesq);
        footbeigesqtck = findViewById(R.id.footwearbeigesqtck);
        footbeigesqtck.setVisibility(View.INVISIBLE);

        footredsq = findViewById(R.id.footwearredsq);
        footredsqtck = findViewById(R.id.footwearredsqtck);
        footredsqtck.setVisibility(View.INVISIBLE);

        footpinksq = findViewById(R.id.footwearpinksq);
        footpinksqtck = findViewById(R.id.footwearpinksqtck);
        footpinksqtck.setVisibility(View.INVISIBLE);

        footbluesq = findViewById(R.id.footwearbluesq);
        footbluesqtck = findViewById(R.id.footwearbluesqtck);
        footbluesqtck.setVisibility(View.INVISIBLE);


        footgreensq = findViewById(R.id.footweargreensq);
        footgreensqtck = findViewById(R.id.footweargreensqtck);
        footgreensqtck.setVisibility(View.INVISIBLE);

        footyellowsq = findViewById(R.id.footwearyellowsq);
        footyellowsqtck = findViewById(R.id.footwearyellowsqtck);
        footyellowsqtck.setVisibility(View.INVISIBLE);

        footorangesq = findViewById(R.id.footwearorangesq);
        footorangesqtck = findViewById(R.id.footwearorangesqtck);
        footorangesqtck.setVisibility(View.INVISIBLE);

        footbrownsq = findViewById(R.id.footwearbrownsq);
        footbrownsqtck = findViewById(R.id.footwearbrownsqtck);
        footbrownsqtck.setVisibility(View.INVISIBLE);

        footpurplesq = findViewById(R.id.footwearpurplesq);
        footpurplesqtck = findViewById(R.id.footwearpurplesqtck);
        footpurplesqtck.setVisibility(View.INVISIBLE);

        accblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accblacksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Black"));

                //black = blackf;

            }
        });

        accblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accblacksqtck.setVisibility(View.INVISIBLE);
                accblacksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Black"));

                //black = nooption;
            }
        });

        accwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accwhitesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("White"));

                //white = whitef;

            }
        });

        accwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accwhitesqtck.setVisibility(View.INVISIBLE);
                accwhitesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("White"));


                //white = nooption;
            }
        });

        accgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accgreysqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Grey"));

                //grey = greyf;

            }
        });

        accgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accgreysqtck.setVisibility(View.INVISIBLE);
                accgreysq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Grey"));

                //grey = nooption;
            }
        });

        accbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accbeigesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Beige"));


                //beige = beigef;

            }
        });

        accbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accbeigesqtck.setVisibility(View.INVISIBLE);
                accbeigesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Beige"));

                //beige = nooption;
            }
        });

        accredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accredsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Red"));


                //red = redf;


            }
        });

        accredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accredsqtck.setVisibility(View.INVISIBLE);
                accredsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Red"));


                //red = nooption;
            }
        });


        accpinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accpinksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Pink"));

                //pink = pinkf;

            }
        });

        accpinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accpinksqtck.setVisibility(View.INVISIBLE);
                accpinksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Pink"));


                //pink = nooption;
            }
        });

        accsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                accsilversqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Silver"));


                //silver = silverf;

            }
        });

        accsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                accsilversqtck.setVisibility(View.INVISIBLE);
                accsilversq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Silver"));


                //silver = nooption;
            }
        });

        accgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                accgoldsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Gold"));


                //gold = goldf;

            }
        });

        accgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accgoldsqtck.setVisibility(View.INVISIBLE);
                accgoldsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Gold"));


                //gold = nooption;
            }
        });


        accyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accyellowsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Yellow"));


                //yellow = yellowf;

            }
        });

        accyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accyellowsqtck.setVisibility(View.INVISIBLE);
                accyellowsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Yellow"));


                //yellow = nooption;
            }
        });

        accbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accbrownsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Brown"));


                //brown = brownf;

            }
        });

        accbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accbrownsqtck.setVisibility(View.INVISIBLE);
                accbrownsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Brown"));


                //brown = nooption;
            }
        });

        accpurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accpurplesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Purple"));

                //purple = purplef;

            }
        });

        accpurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accpurplesqtck.setVisibility(View.INVISIBLE);
                accpurplesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Purple"));


                //purple = nooption;
            }
        });

        accbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accbluesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Blue"));


                //blue = bluef;

            }
        });

        accbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accbluesqtck.setVisibility(View.INVISIBLE);
                accbluesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Blue"));


                //blue = nooption;
            }
        });


        accorangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accorangesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Orange"));

                //orange = orangef;

            }
        });

        accorangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accorangesqtck.setVisibility(View.INVISIBLE);
                accorangesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Orange"));


                //orange = nooption;
            }
        });


        accgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                accgreensqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayUnion("Green"));


                //green = greenf;

            }
        });

        accgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                accgreensqtck.setVisibility(View.INVISIBLE);
                accgreensq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Accessories", FieldValue.arrayRemove("Green"));


                //green = nooption;
            }

        });


        topblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topblacksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Black"));

                //black = blackf;

            }
        });

        topblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topblacksqtck.setVisibility(View.INVISIBLE);
                topblacksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Black"));

                //black = nooption;
            }
        });

        topwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topwhitesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("White"));

                //white = whitef;

            }
        });

        topwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topwhitesqtck.setVisibility(View.INVISIBLE);
                topwhitesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("White"));


                //white = nooption;
            }
        });

        topgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topgreysqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Grey"));

                //grey = greyf;

            }
        });

        topgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topgreysqtck.setVisibility(View.INVISIBLE);
                topgreysq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Grey"));

                //grey = nooption;
            }
        });

        topbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topbeigesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Beige"));


                //beige = beigef;

            }
        });

        topbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topbeigesqtck.setVisibility(View.INVISIBLE);
                topbeigesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Beige"));

                //beige = nooption;
            }
        });

        topredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topredsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Red"));


                //red = redf;


            }
        });

        topredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topredsqtck.setVisibility(View.INVISIBLE);
                topredsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Red"));


                //red = nooption;
            }
        });


        toppinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                toppinksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Pink"));

                //pink = pinkf;

            }
        });

        toppinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                toppinksqtck.setVisibility(View.INVISIBLE);
                toppinksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Pink"));


                //pink = nooption;
            }
        });

        topsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                topsilversqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Silver"));


                //silver = silverf;

            }
        });

        topsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                topsilversqtck.setVisibility(View.INVISIBLE);
                topsilversq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Silver"));


                //silver = nooption;
            }
        });

        topgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                topgoldsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Gold"));


                //gold = goldf;

            }
        });

        topgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                topgoldsqtck.setVisibility(View.INVISIBLE);
                topgoldsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Gold"));


                //gold = nooption;
            }
        });


        topyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topyellowsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Yellow"));


                //yellow = yellowf;

            }
        });

        topyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topyellowsqtck.setVisibility(View.INVISIBLE);
                topyellowsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Yellow"));


                //yellow = nooption;
            }
        });

        topbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topbrownsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Brown"));


                //brown = brownf;

            }
        });

        topbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topbrownsqtck.setVisibility(View.INVISIBLE);
                topbrownsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Brown"));


                //brown = nooption;
            }
        });

        toppurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                toppurplesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Purple"));


                //purple = purplef;

            }
        });

        toppurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                toppurplesqtck.setVisibility(View.INVISIBLE);
                toppurplesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Purple"));


                //purple = nooption;
            }
        });

        topbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topbluesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Blue"));


                //blue = bluef;

            }
        });

        topbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topbluesqtck.setVisibility(View.INVISIBLE);
                topbluesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Blue"));

                //blue = nooption;
            }
        });


        toporangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                toporangesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Orange"));

                //orange = orangef;

            }
        });

        toporangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                toporangesqtck.setVisibility(View.INVISIBLE);
                toporangesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Orange"));


                //orange = nooption;
            }
        });


        topgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                topgreensqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayUnion("Green"));


                //green = greenf;

            }
        });

        topgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                topgreensqtck.setVisibility(View.INVISIBLE);
                topgreensq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Tops", FieldValue.arrayRemove("Green"));


                //green = nooption;
            }

        });


        botblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botblacksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Black"));

                //black = blackf;

            }
        });

        botblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botblacksqtck.setVisibility(View.INVISIBLE);
                botblacksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Black"));

                //black = nooption;
            }
        });

        botwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botwhitesqtck.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("White"));

                //white = whitef;

            }
        });

        botwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botwhitesqtck.setVisibility(View.INVISIBLE);
                botwhitesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("White"));


                //white = nooption;
            }
        });

        botgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botgreysqtck.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Grey"));

                //grey = greyf;

            }
        });

        botgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botgreysqtck.setVisibility(View.INVISIBLE);
                botgreysq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Grey"));

                //grey = nooption;
            }
        });

        botbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botbeigesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Beige"));


                //beige = beigef;

            }
        });

        botbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botbeigesqtck.setVisibility(View.INVISIBLE);
                botbeigesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Beige"));

                //beige = nooption;
            }
        });

        botredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botredsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Red"));


                //red = redf;


            }
        });

        botredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botredsqtck.setVisibility(View.INVISIBLE);
                botredsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Red"));


                //red = nooption;
            }
        });


        botpinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botpinksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Pink"));

                //pink = pinkf;

            }
        });

        botpinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botpinksqtck.setVisibility(View.INVISIBLE);
                botpinksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Pink"));


                //pink = nooption;
            }
        });

        botsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                botsilversqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Silver"));


                //silver = silverf;

            }
        });

        botsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                botsilversqtck.setVisibility(View.INVISIBLE);
                botsilversq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Silver"));


                //silver = nooption;
            }
        });

        botgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                botgoldsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Gold"));


                //gold = goldf;

            }
        });

        botgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                botgoldsqtck.setVisibility(View.INVISIBLE);
                botgoldsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Gold"));


                //gold = nooption;
            }
        });


        botyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botyellowsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Yellow"));


                //yellow = yellowf;

            }
        });

        botyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botyellowsqtck.setVisibility(View.INVISIBLE);
                botyellowsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Yellow"));


                //yellow = nooption;
            }
        });

        botbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botbrownsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Brown"));


                //brown = brownf;

            }
        });

        botbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botbrownsqtck.setVisibility(View.INVISIBLE);
                botbrownsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Brown"));


                //brown = nooption;
            }
        });

        botpurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botpurplesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Purple"));

                //purple = purplef;

            }
        });

        botpurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botpurplesqtck.setVisibility(View.INVISIBLE);
                botpurplesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Purple"));


                //purple = nooption;
            }
        });

        botbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botbluesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Blue"));


                //blue = bluef;

            }
        });

        botbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botbluesqtck.setVisibility(View.INVISIBLE);
                botbluesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Blue"));

                //blue = nooption;
            }
        });


        botorangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botorangesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Orange"));

                //orange = orangef;

            }
        });

        botorangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botorangesqtck.setVisibility(View.INVISIBLE);
                botorangesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Orange"));


                //orange = nooption;
            }
        });


        botgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                botgreensqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayUnion("Green"));


                //green = greenf;

            }
        });

        botgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                botgreensqtck.setVisibility(View.INVISIBLE);
                botgreensq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Bottoms", FieldValue.arrayRemove("Green"));


                //green = nooption;
            }

        });


        footblacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footblacksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Black"));

                //black = blackf;

            }
        });

        footblacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footblacksqtck.setVisibility(View.INVISIBLE);
                footblacksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Black"));

                //black = nooption;
            }
        });

        footwhitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footwhitesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("White"));

                //white = whitef;

            }
        });

        footwhitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footwhitesqtck.setVisibility(View.INVISIBLE);
                footwhitesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("White"));


                //white = nooption;
            }
        });

        footgreysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footgreysqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Grey"));

                //grey = greyf;

            }
        });

        footgreysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footgreysqtck.setVisibility(View.INVISIBLE);
                footgreysq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Grey"));

                //grey = nooption;
            }
        });

        footbeigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footbeigesqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Beige"));


                //beige = beigef;

            }
        });

        footbeigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footbeigesqtck.setVisibility(View.INVISIBLE);
                footbeigesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Beige"));


                //beige = nooption;
            }
        });

        footredsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footredsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Red"));


                //red = redf;


            }
        });

        footredsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footredsqtck.setVisibility(View.INVISIBLE);
                footredsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Red"));


                //red = nooption;
            }
        });


        footpinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footpinksqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Pink"));

                //pink = pinkf;

            }
        });

        footpinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footpinksqtck.setVisibility(View.INVISIBLE);
                footpinksq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Pink"));


                //pink = nooption;
            }
        });

        footsilversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                footsilversqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Silver"));


                //silver = silverf;

            }
        });

        footsilversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                footsilversqtck.setVisibility(View.INVISIBLE);
                footsilversq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Silver"));


                //silver = nooption;
            }
        });

        footgoldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                footgoldsqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Gold"));


                //gold = goldf;

            }
        });

        footgoldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                footgoldsqtck.setVisibility(View.INVISIBLE);
                footgoldsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Gold"));


                //gold = nooption;
            }
        });


        footyellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footyellowsqtck.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Yellow"));


                //yellow = yellowf;

            }
        });

        footyellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footyellowsqtck.setVisibility(View.INVISIBLE);
                footyellowsq.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Yellow"));


                //yellow = nooption;
            }
        });

        footbrownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footbrownsqtck.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Brown"));


                //brown = brownf;

            }
        });

        footbrownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footbrownsqtck.setVisibility(View.INVISIBLE);
                footbrownsq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Brown"));


                //brown = nooption;
            }
        });

        footpurplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footpurplesqtck.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Purple"));

                //purple = purplef;

            }
        });

        footpurplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footpurplesqtck.setVisibility(View.INVISIBLE);
                footpurplesq.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Purple"));


                //purple = nooption;
            }
        });

        footbluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footbluesqtck.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Blue"));


                //blue = bluef;

            }
        });

        footbluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footbluesqtck.setVisibility(View.INVISIBLE);
                footbluesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Blue"));

                //blue = nooption;
            }
        });


        footorangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footorangesqtck.setVisibility(View.VISIBLE);
                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Orange"));

                //orange = orangef;

            }
        });

        footorangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footorangesqtck.setVisibility(View.INVISIBLE);
                footorangesq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Orange"));


                //orange = nooption;
            }
        });


        footgreensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                footgreensqtck.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayUnion("Green"));


                //green = greenf;

            }
        });

        footgreensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                footgreensqtck.setVisibility(View.INVISIBLE);
                footgreensq.setVisibility(View.VISIBLE);

                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                        .collection("MetaData")
                        .document(key).update("Footwear", FieldValue.arrayRemove("Green"));


                //green = nooption;
            }

        });


        String imgname = Utilities.loadPref(MyUploadsDeleting.this, "Profile_Pic", "");
        String name = Utilities.loadPref(MyUploadsDeleting.this, "Pen_Name", "");
        My_DbKey = Utilities.loadPref(MyUploadsDeleting.this, "My_DbKey", "");


        intent = getIntent();
        if (intent != null && Objects.requireNonNull(intent.getExtras()).containsKey("picture")) {
            feedResponse_obj = (FeedResponse) intent.getSerializableExtra("picture");
            picture_path = feedResponse_obj.getImage();

        }

        if (picture_path != null) {

            Glide.with(MyUploadsDeleting.this).load(picture_path).apply(requestOptions).into(imageView);


        }

        if (imgname != null) {

            Glide.with(MyUploadsDeleting.this).load(imgname).apply(requestOptions).into(profimage);

        }

        if (name != null) {

            musername.setText(name);

        }

        if (feedResponse_obj.getDescription() != null) {

            mpostdescription.setText(feedResponse_obj.getDescription());

        } else {

            mpostdescription.setVisibility(View.GONE);
        }

        if (uploadcard.getVisibility() == View.VISIBLE) {


        }
    }

    public void checkcolors(ArrayList<String> data, String cat) {

        if (cat.equals("Accessories")){

            for (String s : data) {



                if (s.equals(black)) {

                    cblack = black;
                    accblacksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(white)) {

                    cwhite = white;
                    accwhitesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(grey)) {

                    cgrey = grey;
                    accgreysqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(beige)) {

                    cbeige = beige;
                    accbeigesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(red)) {

                    cred = red;
                    accredsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(pink)) {

                    cpink = pink;
                    accpinksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(silver)) {

                    csilver = silver;
                    accsilversqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(green)) {

                    cgreen = green;
                    accgreensqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(blue)) {

                    cblue = blue;
                    accbluesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(yellow)) {

                    cyellow = yellow;
                    accyellowsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(orange)) {

                    corange = orange;
                    accorangesqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(brown)) {

                    cbrown = brown;
                    accbrownsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(purple)) {

                    cpurple = purple;
                    accpurplesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(gold)) {

                    cgold = gold;
                    accgoldsqtck.setVisibility(View.VISIBLE);
                }
            }

        }


        if (cat.equals("Tops")){

            for (String s : data) {

                if (s.equals(black)) {

                    cblack = black;
                    topblacksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(white)) {

                    cwhite = white;
                    topwhitesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(grey)) {

                    cgrey = grey;
                    topgreysqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(beige)) {

                    cbeige = beige;
                    topbeigesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(red)) {

                    cred = red;
                    topredsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(pink)) {

                    cpink = pink;
                    toppinksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(silver)) {

                    csilver = silver;
                    topsilversqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(green)) {

                    cgreen = green;
                    topgreensqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(blue)) {

                    cblue = blue;
                    topbluesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(yellow)) {

                    cyellow = yellow;
                    topyellowsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(orange)) {

                    corange = orange;
                    toporangesqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(brown)) {

                    cbrown = brown;
                    topbrownsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(purple)) {

                    cpurple = purple;
                    toppurplesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(gold)) {

                    cgold = gold;
                    topgoldsqtck.setVisibility(View.VISIBLE);
                }
            }

        }


        if (cat.equals("Footwear")){

            for (String s : data) {

                if (s.equals(black)) {

                    cblack = black;
                    footblacksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(white)) {

                    cwhite = white;
                    footwhitesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(grey)) {

                    cgrey = grey;
                    footgreysqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(beige)) {

                    cbeige = beige;
                    footbeigesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(red)) {

                    cred = red;
                    footredsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(pink)) {

                    cpink = pink;
                    footpinksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(silver)) {

                    csilver = silver;
                    footsilversqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(green)) {

                    cgreen = green;
                    footgreensqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(blue)) {

                    cblue = blue;
                    footbluesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(yellow)) {

                    cyellow = yellow;
                    footyellowsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(orange)) {

                    corange = orange;
                    footorangesqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(brown)) {

                    cbrown = brown;
                    footbrownsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(purple)) {

                    cpurple = purple;
                    footpurplesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(gold)) {

                    cgold = gold;
                    footgoldsqtck.setVisibility(View.VISIBLE);
                }
            }

        }

        if (cat.equals("Bottoms")){

            for (String s : data) {

                if (s.equals(black)) {

                    cblack = black;
                    botblacksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(white)) {

                    cwhite = white;
                    botwhitesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(grey)) {

                    cgrey = grey;
                    botgreysqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(beige)) {

                    cbeige = beige;
                    botbeigesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(red)) {

                    cred = red;
                    botredsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(pink)) {

                    cpink = pink;
                    botpinksqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(silver)) {

                    csilver = silver;
                    botsilversqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(green)) {

                    cgreen = green;
                    botgreensqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(blue)) {

                    cblue = blue;
                    botbluesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(yellow)) {

                    cyellow = yellow;
                    botyellowsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(orange)) {

                    corange = orange;
                    botorangesqtck.setVisibility(View.VISIBLE);

                }

                if (s.equals(brown)) {

                    cbrown = brown;
                    botbrownsqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(purple)) {

                    cpurple = purple;
                    botpurplesqtck.setVisibility(View.VISIBLE);
                }

                if (s.equals(gold)) {

                    cgold = gold;
                    botgoldsqtck.setVisibility(View.VISIBLE);
                }
            }

        }


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




    }

    public void Edit_Popup(View v) {
        popup = new IconizedMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.delete_edit_feed_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.profile_edit:

                        uploadcard.setVisibility(View.VISIBLE);
                        savelayout.setVisibility(View.VISIBLE);

                        FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("MetaData")
                                .whereEqualTo("lookid", feedResponse_obj.getLookid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@Nonnull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {

                                            if (!Objects.requireNonNull(task.getResult()).isEmpty()) {


                                                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                                    key = documentSnapshot.getId();

                                                    if (documentSnapshot.contains("Accessories")) {

                                                        data = (ArrayList<String>) documentSnapshot.get("Accessories");
                                                        checkcolors(data, "Accessories");

                                                    }

                                                    if (documentSnapshot.contains("Tops")) {

                                                        data = (ArrayList<String>) documentSnapshot.get("Tops");
                                                        checkcolors(data, "Tops");

                                                    }

                                                    if (documentSnapshot.contains("Bottoms")) {

                                                        data = (ArrayList<String>) documentSnapshot.get("Bottoms");
                                                        checkcolors(data, "Bottoms");

                                                    }

                                                    if (documentSnapshot.contains("Footwear")) {

                                                        data = (ArrayList<String>) documentSnapshot.get("Footwear");
                                                        checkcolors(data, "Footwear");

                                                    }

                                                    if (documentSnapshot.contains("data")) {

                                                        data = (ArrayList<String>) documentSnapshot.get("data");
                                                        if (data != null) {
                                                            checkcolors(data, "Data");
                                                        }
                                                    }


                                                }
                                            }

                                        }
                                    }
                                });


                        break;

                    case R.id.profile_delete:
                       // mixpanelAPI.track("Delete");
                        if (picture_path != null) {
                            final AlertDialog.Builder alert_Dialog = new AlertDialog.Builder(MyUploadsDeleting.this);
                            alert_Dialog.setCancelable(false);
                            alert_Dialog.setMessage(R.string.deletequery);
                            alert_Dialog.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getFeedKey();
                                    dialog.dismiss();

                                }
                            } );

                            alert_Dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert_Dialog.show();
                        }

                        break;
                }

                return false;
            }
        });

        popup.show();
    }

    private void getFeedKey() {

        FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Feed").whereEqualTo("id",feedResponse_obj.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot : task.getResult()){

                        String fkey = documentSnapshot.getId();
                        if (fkey!=null){

                            FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Feed").document(fkey).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {

                                    if(task.isSuccessful()){

                                        FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                                                .collection("Posts")
                                                .whereEqualTo("id",feedResponse_obj.getId())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(Task<QuerySnapshot> task) {

                                                        if (task.isSuccessful()){

                                                            for (DocumentSnapshot documentSnapshot1:task.getResult().getDocuments()){


                                                                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey)
                                                                        .collection("Posts")
                                                                        .document(documentSnapshot1.getId())
                                                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {




                                                                    }
                                                                });


                                                            }

                                                            FirebaseFirestore.getInstance().collection("UsersList")
                                                                    .document(My_DbKey)
                                                                    .collection("FollowCollection")
                                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(Task<QuerySnapshot> task) {

                                                                    if (task.isSuccessful()){

                                                                        for (DocumentSnapshot documentSnapshot1 : task.getResult().getDocuments()){

                                                                            FirebaseFirestore.getInstance().collection("UsersList")
                                                                                    .whereEqualTo("Email",documentSnapshot1.get("email"))
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(Task<QuerySnapshot> task) {

                                                                                            if (task.isSuccessful()){

                                                                                                for (DocumentSnapshot documentSnapshot2 : task.getResult().getDocuments()){

                                                                                                    FirebaseFirestore.getInstance().collection("UsersList")
                                                                                                            .document(documentSnapshot2.getId())
                                                                                                            .collection("Feed")
                                                                                                            .whereEqualTo("id",feedResponse_obj.getId())
                                                                                                            .get()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(Task<QuerySnapshot> task) {

                                                                                                                    for (DocumentSnapshot documentSnapshot3 : task.getResult().getDocuments()){

                                                                                                                        documentSnapshot3.getReference().delete();
                                                                                                                    }

                                                                                                                }
                                                                                                            });

                                                                                                }
                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }
                                                                        Utilities.showToast(MyUploadsDeleting.this, getResources().getString(R.string.postdelete));
                                                                        Intent login_Intent = new Intent(MyUploadsDeleting.this, HomeActivity.class);
                                                                        startActivity(login_Intent);
                                                                        finish();
                                                                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);

                                                                    }

                                                                }
                                                            });
                                                        }

                                                    }
                                                });

                                    }

                                }
                            });
                        }
                    }
                }

            }
        });



    }


}

