package my.closet.fashion.style;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.Realm_database.RealmHandler;
import my.closet.fashion.style.adapters.AccessoryAdapter;
import my.closet.fashion.style.adapters.BottomAdapter;
import my.closet.fashion.style.adapters.FootWearAdapter;
import my.closet.fashion.style.adapters.TopAdapter;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.modesl.Colors;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.Styles;

import static my.closet.fashion.style.fragments.ClosetFragment.accrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.bottomrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.footrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.madapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mbottomadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mfootwearadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mtopadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.toprecycler;

/**
 * Created by biswa on 6/14/2017.
 */

public class Pic_info extends AppCompatActivity {


    ImageView save;
    TextView acctexttt;
    TextView acctextslct;
    TextView toptxt;
    TextView toptxtslct;
    TextView bottomtxt;
    TextView bottomtxtslct;
    TextView foottxt;
    TextView foottxtslct;

    TextView clrblack;
    TextView clrwhite;
    TextView clrgrey;
    TextView clrbeige;
    TextView clrred;
    TextView clrpink;
    TextView clrsilver;
    TextView clrgreen;
    TextView clrblue;
    TextView clryellow;
    TextView clrorange;
    TextView clrbrown;
    TextView clrpurple;
    TextView clrgold;



    String imagelocation;
    ImageView userimage;
    Realm realm;


    ImageView blacksq;
    ImageView blacksqtck;
    ImageView whitesq;
    ImageView whitesqtck;
    ImageView greysq;
    ImageView greysqtck;
    ImageView beigesq;
    ImageView beigesqtck;
    ImageView redsq;
    ImageView redsqtck;
    ImageView pinksq;
    ImageView pinksqtck;
    ImageView bluesq;
    ImageView bluesqtck;
    ImageView greensq;
    ImageView greensqtck;
    ImageView yellowsq;
    ImageView yellowsqtck;
    ImageView orangesq;
    ImageView orangesqtck;
    ImageView brownsq;
    ImageView brownsqtck;
    ImageView purplesq;
    ImageView purplesqtck;
    ImageView silversq;
    ImageView silversqtck;
    ImageView goldsq;
    ImageView goldsqtck;

    String black;
    String white;
    String grey;
    String beige;
    String red;
    String pink;
    String blue;
    String green;
    String yellow;
    String orange;
    String brown;
    String purple;
    String silver;
    String gold;

    int clset;
    int stset;
    int i;

    final static String blackf = "Black";
    final static String whitef = "White";
    final static String greyf = "Grey";
    final static String beigef = "Beige";
    final static String redf = "Red";
    final static String pinkf = "Pink";
    final static String bluef = "Blue";
    final static String greenf = "Green";
    final static String yellowf = "Yellow";
    final static String orangef = "Orange";
    final static String brownf = "Brown";
    final static String purplef = "Purple";
    final static String silverf = "Silver";
    final static String goldf = "Gold";


    Boolean isUpdate;




    final String accessory = "Accessories";
    final String tops = "Tops";
    final String bottoms = "Bottoms";
    final String footwear = "Footwear";
    final String nooption = " ";

    final String formalf = "Formal";
    final String casualf = "Casual";
    final String specialf = "Special";
    final String partyf = "Party";


    String formal;
    String casual;
    String party;
    String special;


    ImageView accessories;
    ImageView accselect;
    ImageView bottom;
    ImageView bottomselect;
    ImageView top;
    ImageView topselect;
    ImageView foot;
    ImageView footselect;
    Bitmap bmp;
    Bitmap getBmp;



    String cat;
    byte[] bytes;


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

    String cformal;
    String ccasual;
    String cparty;
    String cspecial;

    Button formalsq;
    Button formalsqtck;
    Button partysq;
    Button partysqtck;
    Button casualsq;
    Button casualsqtck;
    Button specialsq;
    Button specialsqtck;

    TextView clrblackslct;
    TextView clrwhiteslct;
    TextView clrgreyslct;
    TextView clrbeigeslct;
    TextView clrredslct;
    TextView clrpinkslct;
    TextView clrsilverslct;
    TextView clrgreenslct;
    TextView clrblueslct;
    TextView clryellowslct;
    TextView clrorangeslct;
    TextView clrbrownslct;
    TextView clrpurpleslct;
    TextView clrgoldslct;


    int Id;
    String category;
    String style;
    Uri uri;
    int id;


    RecyclerView mrecycle = MainActivity.mrecycle;

    Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_info);

        setSupportActionBar((Toolbar) findViewById(R.id.tool1));
        ActionBar bar=getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowCustomEnabled(true);
        bar.setTitle(" ");

        String projectToken = "257c7d2e1c44d7d1ab6105af372f65a6";
        final MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);

        cat=nooption;
        formal=nooption;
        casual=nooption;
        party=nooption;
        special=nooption;

        black=nooption;
        white=nooption;
        grey=nooption;
        beige=nooption;
        red=nooption;
        pink=nooption;
        green=nooption;
        blue=nooption;
        yellow=nooption;
        orange=nooption;
        brown=nooption;
        purple=nooption;
        silver=nooption;
        gold=nooption;

        save = (ImageView)findViewById(R.id.crtxt1);
        acctexttt= findViewById(R.id.acctext);
        acctextslct= findViewById(R.id.acctext1);
        toptxt= findViewById(R.id.toptext);
        toptxtslct= findViewById(R.id.toptext1);
        bottomtxt= findViewById(R.id.bottomtext);
        bottomtxtslct= findViewById(R.id.bottomtext1);
        foottxt= findViewById(R.id.foottext);
        foottxtslct= findViewById(R.id.foottext1);

        acctextslct.setVisibility(View.INVISIBLE);
        toptxtslct.setVisibility(View.INVISIBLE);
        bottomtxtslct.setVisibility(View.INVISIBLE);
        foottxtslct.setVisibility(View.INVISIBLE);



        formalsq = findViewById(R.id.formalsq);
        partysq = findViewById(R.id.partysq);
        specialsq = findViewById(R.id.specialsq);
        casualsq = findViewById(R.id.casualsq);

        formalsqtck = findViewById(R.id.formaltck);
        formalsqtck.setVisibility(View.INVISIBLE);

        casualsqtck = findViewById(R.id.casualtck);
        casualsqtck.setVisibility(View.INVISIBLE);

        specialsqtck = findViewById(R.id.specialtck);
        specialsqtck.setVisibility(View.INVISIBLE);

        partysqtck = findViewById(R.id.partytck);
        partysqtck.setVisibility(View.INVISIBLE);


        accessories = findViewById(R.id.accessories);
        bottom = findViewById(R.id.bottom);
        top = findViewById(R.id.top);
        foot = findViewById(R.id.foot);

        accselect = findViewById(R.id.accselect);
        accselect.setVisibility(View.INVISIBLE);
        bottomselect = findViewById(R.id.bottomselect);
        bottomselect.setVisibility(View.INVISIBLE);
        topselect = findViewById(R.id.topselect);
        topselect.setVisibility(View.INVISIBLE);
        footselect = findViewById(R.id.footselect);
        footselect.setVisibility(View.INVISIBLE);

        blacksq = findViewById(R.id.blacksq);
        blacksqtck = findViewById(R.id.blacksqtck);
        blacksqtck.setVisibility(View.INVISIBLE);

        silversq = findViewById(R.id.silversq);
        silversqtck = findViewById(R.id.silversqtck);
        silversqtck.setVisibility(View.INVISIBLE);

        goldsq = findViewById(R.id.goldsq);
        goldsqtck = findViewById(R.id.goldsqtck);
        goldsqtck.setVisibility(View.INVISIBLE);

        whitesq = findViewById(R.id.whitesq);
        whitesqtck = findViewById(R.id.whitesqtck);
        whitesqtck.setVisibility(View.INVISIBLE);

        greysq = findViewById(R.id.greysq);
        greysqtck = findViewById(R.id.greysqtck);
        greysqtck.setVisibility(View.INVISIBLE);

        beigesq = findViewById(R.id.beigesq);
        beigesqtck = findViewById(R.id.beigesqtck);
        beigesqtck.setVisibility(View.INVISIBLE);

        redsq = findViewById(R.id.redsq);
        redsqtck = findViewById(R.id.redsqtck);
        redsqtck.setVisibility(View.INVISIBLE);

        pinksq = findViewById(R.id.pinksq);
        pinksqtck = findViewById(R.id.pinksqtck);
        pinksqtck.setVisibility(View.INVISIBLE);

        bluesq = findViewById(R.id.bluesq);
        bluesqtck = findViewById(R.id.bluesqtck);
        bluesqtck.setVisibility(View.INVISIBLE);


        greensq = findViewById(R.id.greensq);
        greensqtck = findViewById(R.id.greensqtck);
        greensqtck.setVisibility(View.INVISIBLE);

        yellowsq = findViewById(R.id.yellowsq);
        yellowsqtck = findViewById(R.id.yellowsqtck);
        yellowsqtck.setVisibility(View.INVISIBLE);

        orangesq = findViewById(R.id.orangesq);
        orangesqtck = findViewById(R.id.orangesqtck);
        orangesqtck.setVisibility(View.INVISIBLE);

        brownsq = findViewById(R.id.brownsq);
        brownsqtck = findViewById(R.id.brownsqtck);
        brownsqtck.setVisibility(View.INVISIBLE);

        purplesq = findViewById(R.id.purplesq);
        purplesqtck = findViewById(R.id.purplesqtck);
        purplesqtck.setVisibility(View.INVISIBLE);





        blacksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                blacksqtck.setVisibility(View.VISIBLE);

                black = blackf;

            }
        });

        blacksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                blacksqtck.setVisibility(View.INVISIBLE);
                blacksq.setVisibility(View.VISIBLE);

                black = nooption;
            }
        });

        whitesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                whitesqtck.setVisibility(View.VISIBLE);

                white = whitef;

            }
        });

        whitesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                whitesqtck.setVisibility(View.INVISIBLE);
                whitesq.setVisibility(View.VISIBLE);


                white = nooption;
            }
        });

        greysq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                greysqtck.setVisibility(View.VISIBLE);

                grey = greyf;

            }
        });

        greysqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                greysqtck.setVisibility(View.INVISIBLE);
                greysq.setVisibility(View.VISIBLE);

                grey = nooption;
            }
        });

        beigesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                beigesqtck.setVisibility(View.VISIBLE);


                beige = beigef;

            }
        });

        beigesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                beigesqtck.setVisibility(View.INVISIBLE);
                beigesq.setVisibility(View.VISIBLE);

                beige = nooption;
            }
        });

        redsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                redsqtck.setVisibility(View.VISIBLE);


                red = redf;


            }
        });

        redsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                redsqtck.setVisibility(View.INVISIBLE);
                redsq.setVisibility(View.VISIBLE);


                red = nooption;
            }
        });


        pinksq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                pinksqtck.setVisibility(View.VISIBLE);

                pink = pinkf;

            }
        });

        pinksqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                pinksqtck.setVisibility(View.INVISIBLE);
                pinksq.setVisibility(View.VISIBLE);


                pink = nooption;
            }
        });

        silversq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                silversqtck.setVisibility(View.VISIBLE);


                silver = silverf;

            }
        });

        silversqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                silversqtck.setVisibility(View.INVISIBLE);
                silversq.setVisibility(View.VISIBLE);


                silver = nooption;
            }
        });

        goldsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                goldsqtck.setVisibility(View.VISIBLE);


                gold = goldf;

            }
        });

        goldsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goldsqtck.setVisibility(View.INVISIBLE);
                goldsq.setVisibility(View.VISIBLE);


                gold = nooption;
            }
        });


        yellowsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                yellowsqtck.setVisibility(View.VISIBLE);


                yellow = yellowf;

            }
        });

        yellowsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                yellowsqtck.setVisibility(View.INVISIBLE);
                yellowsq.setVisibility(View.VISIBLE);


                yellow = nooption;
            }
        });

        brownsq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                brownsqtck.setVisibility(View.VISIBLE);


                brown = brownf;

            }
        });

        brownsqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                brownsqtck.setVisibility(View.INVISIBLE);
                brownsq.setVisibility(View.VISIBLE);


                brown = nooption;
            }
        });

        purplesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                purplesqtck.setVisibility(View.VISIBLE);

                purple = purplef;

            }
        });

        purplesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                purplesqtck.setVisibility(View.INVISIBLE);
                purplesq.setVisibility(View.VISIBLE);


                purple = nooption;
            }
        });

        bluesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                bluesqtck.setVisibility(View.VISIBLE);


                blue = bluef;

            }
        });

        bluesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                bluesqtck.setVisibility(View.INVISIBLE);
                bluesq.setVisibility(View.VISIBLE);

                blue = nooption;
            }
        });


        orangesq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                orangesqtck.setVisibility(View.VISIBLE);

                orange = orangef;

            }
        });

        orangesqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                orangesqtck.setVisibility(View.INVISIBLE);
                orangesq.setVisibility(View.VISIBLE);


                orange = nooption;
            }
        });


        greensq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                greensqtck.setVisibility(View.VISIBLE);


                green = greenf;

            }
        });

        greensqtck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                greensqtck.setVisibility(View.INVISIBLE);
                greensq.setVisibility(View.VISIBLE);


                green = nooption;
            }

        });


        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accessories.setVisibility(View.INVISIBLE);
                acctexttt.setVisibility(View.INVISIBLE);
                accselect.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.VISIBLE);

                toptxtslct.setVisibility(View.INVISIBLE);
                toptxt.setVisibility(View.VISIBLE);
                topselect.setVisibility(View.INVISIBLE);
                top.setVisibility(View.VISIBLE);

                bottomselect.setVisibility(View.INVISIBLE);
                bottomtxtslct.setVisibility(View.INVISIBLE);
                bottomtxt.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.VISIBLE);

                footselect.setVisibility(View.INVISIBLE);
                foottxt.setVisibility(View.VISIBLE);
                foottxtslct.setVisibility(View.INVISIBLE);
                foot.setVisibility(View.VISIBLE);


                cat = accessory;

            }
        });
        accselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accselect.setVisibility(View.INVISIBLE);
                accessories.setVisibility(View.VISIBLE);
                acctexttt.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.INVISIBLE);

                toptxtslct.setVisibility(View.INVISIBLE);
                toptxt.setVisibility(View.VISIBLE);
                topselect.setVisibility(View.INVISIBLE);
                top.setVisibility(View.VISIBLE);

                bottomselect.setVisibility(View.INVISIBLE);
                bottomtxtslct.setVisibility(View.INVISIBLE);
                bottomtxt.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.VISIBLE);

                footselect.setVisibility(View.INVISIBLE);
                foottxt.setVisibility(View.VISIBLE);
                foottxtslct.setVisibility(View.INVISIBLE);
                foot.setVisibility(View.VISIBLE);
                cat = accessory;

                cat = nooption;
            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom.setVisibility(View.INVISIBLE);
                bottomselect.setVisibility(View.VISIBLE);
                bottomtxtslct.setVisibility(View.VISIBLE);
                bottomtxt.setVisibility(View.INVISIBLE);

                accselect.setVisibility(View.INVISIBLE);
                accessories.setVisibility(View.VISIBLE);
                acctexttt.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.INVISIBLE);
                toptxtslct.setVisibility(View.INVISIBLE);
                toptxt.setVisibility(View.VISIBLE);
                topselect.setVisibility(View.INVISIBLE);
                top.setVisibility(View.VISIBLE);
                footselect.setVisibility(View.INVISIBLE);
                foottxt.setVisibility(View.VISIBLE);
                foottxtslct.setVisibility(View.INVISIBLE);
                foot.setVisibility(View.VISIBLE);

                cat = bottoms;
            }
        });
        bottomselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomselect.setVisibility(View.INVISIBLE);
                bottomtxtslct.setVisibility(View.INVISIBLE);
                bottom.setVisibility(View.VISIBLE);
                bottomtxt.setVisibility(View.VISIBLE);
                accselect.setVisibility(View.INVISIBLE);
                accessories.setVisibility(View.VISIBLE);
                acctexttt.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.INVISIBLE);
                toptxtslct.setVisibility(View.INVISIBLE);
                toptxt.setVisibility(View.VISIBLE);
                topselect.setVisibility(View.INVISIBLE);
                top.setVisibility(View.VISIBLE);
                footselect.setVisibility(View.INVISIBLE);
                foottxt.setVisibility(View.VISIBLE);
                foottxtslct.setVisibility(View.INVISIBLE);
                foot.setVisibility(View.VISIBLE);

                cat = nooption;
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                top.setVisibility(View.INVISIBLE);
                toptxtslct.setVisibility(View.VISIBLE);
                toptxt.setVisibility(View.INVISIBLE);
                topselect.setVisibility(View.VISIBLE);
                bottomselect.setVisibility(View.INVISIBLE);
                bottomtxtslct.setVisibility(View.INVISIBLE);
                bottom.setVisibility(View.VISIBLE);
                bottomtxt.setVisibility(View.VISIBLE);
                accselect.setVisibility(View.INVISIBLE);
                accessories.setVisibility(View.VISIBLE);
                acctexttt.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.INVISIBLE);
                footselect.setVisibility(View.INVISIBLE);
                foottxt.setVisibility(View.VISIBLE);
                foottxtslct.setVisibility(View.INVISIBLE);
                foot.setVisibility(View.VISIBLE);


                cat = tops;
            }
        });
        topselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                topselect.setVisibility(View.INVISIBLE);
                top.setVisibility(View.VISIBLE);
                toptxtslct.setVisibility(View.INVISIBLE);
                toptxt.setVisibility(View.VISIBLE);
                bottomselect.setVisibility(View.INVISIBLE);
                bottomtxtslct.setVisibility(View.INVISIBLE);
                bottom.setVisibility(View.VISIBLE);
                bottomtxt.setVisibility(View.VISIBLE);
                accselect.setVisibility(View.INVISIBLE);
                accessories.setVisibility(View.VISIBLE);
                acctexttt.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.INVISIBLE);
                footselect.setVisibility(View.INVISIBLE);
                foottxt.setVisibility(View.VISIBLE);
                foottxtslct.setVisibility(View.INVISIBLE);
                foot.setVisibility(View.VISIBLE);


                cat = nooption;
            }
        });

        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                foot.setVisibility(View.INVISIBLE);
                foottxtslct.setVisibility(View.VISIBLE);
                footselect.setVisibility(View.VISIBLE);
                foottxt.setVisibility(View.INVISIBLE);
                topselect.setVisibility(View.INVISIBLE);
                top.setVisibility(View.VISIBLE);
                toptxtslct.setVisibility(View.INVISIBLE);
                toptxt.setVisibility(View.VISIBLE);
                bottomselect.setVisibility(View.INVISIBLE);
                bottomtxtslct.setVisibility(View.INVISIBLE);
                bottom.setVisibility(View.VISIBLE);
                bottomtxt.setVisibility(View.VISIBLE);
                accselect.setVisibility(View.INVISIBLE);
                accessories.setVisibility(View.VISIBLE);
                acctexttt.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.INVISIBLE);


                cat = footwear;
            }
        });
        footselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                footselect.setVisibility(View.INVISIBLE);
                foot.setVisibility(View.VISIBLE);
                foottxt.setVisibility(View.VISIBLE);
                foottxtslct.setVisibility(View.INVISIBLE);
                topselect.setVisibility(View.INVISIBLE);
                top.setVisibility(View.VISIBLE);
                toptxtslct.setVisibility(View.INVISIBLE);
                toptxt.setVisibility(View.VISIBLE);
                bottomselect.setVisibility(View.INVISIBLE);
                bottomtxtslct.setVisibility(View.INVISIBLE);
                bottom.setVisibility(View.VISIBLE);
                bottomtxt.setVisibility(View.VISIBLE);
                accselect.setVisibility(View.INVISIBLE);
                accessories.setVisibility(View.VISIBLE);
                acctexttt.setVisibility(View.VISIBLE);
                acctextslct.setVisibility(View.INVISIBLE);


                cat = nooption;
            }
        });


        formalsq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                formalsq.setVisibility(View.INVISIBLE);
                formalsqtck.setVisibility(View.VISIBLE);
                formal = formalf;
            }
        });
        formalsqtck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                formalsqtck.setVisibility(View.INVISIBLE);
                formalsq.setVisibility(View.VISIBLE);
                formal = nooption;
            }
        });


        partysq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                partysq.setVisibility(View.INVISIBLE);
                partysqtck.setVisibility(View.VISIBLE);
                party = partyf;
            }
        });
        partysqtck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                partysqtck.setVisibility(View.INVISIBLE);
                partysq.setVisibility(View.VISIBLE);
                party = nooption;
            }
        });


        specialsq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                specialsq.setVisibility(View.INVISIBLE);
                specialsqtck.setVisibility(View.VISIBLE);
                special = specialf;
                //Toast.makeText(getApplicationContext(),specialf.length(),Toast.LENGTH_SHORT).show();
            }
        });


        specialsqtck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                specialsqtck.setVisibility(View.INVISIBLE);
                specialsq.setVisibility(View.VISIBLE);
                special = nooption;
            }
        });

        casualsq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                casualsq.setVisibility(View.INVISIBLE);
                casualsqtck.setVisibility(View.VISIBLE);
                casual = casualf;
            }
        });
        casualsqtck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                casualsqtck.setVisibility(View.INVISIBLE);
                casualsq.setVisibility(View.VISIBLE);
                casual = nooption;
            }
        });


        final Bundle bundle = getIntent().getExtras();
        final Intent intent = getIntent();

        if (bundle != null && bundle.containsKey("source")) {


            if (bundle.getString("source").equalsIgnoreCase("add")) {
                isUpdate = false;



                   // uri = (Uri) bundle.get("uri");

                     bytes=bundle.getByteArray("galbtmp");
                    assert bytes!=null;
                bmp = getScaledBitmap(bytes,500,500);


                userimage = findViewById(R.id.userimage);
                    userimage.setImageBitmap(bmp);
                Realm.init(this);
                save();
            }

             else if (bundle.getString("source").equalsIgnoreCase("edit")) {
                isUpdate = true;
                Id = bundle.getInt("id");
                category = bundle.getString("category");
                style = bundle.getString("styles");
                cblack = bundle.getString("black");
                cwhite = bundle.getString("white");
                cgrey = bundle.getString("grey");
                cbeige = bundle.getString("beige");
                cred = bundle.getString("red");
                cpink = bundle.getString("pink");
                cblue = bundle.getString("blue");
                cgreen = bundle.getString("green");
                cyellow = bundle.getString("yellow");
                corange = bundle.getString("orange");
                cbrown = bundle.getString("brown");
                cpurple = bundle.getString("purple");
                csilver = bundle.getString("silver");
                cgold = bundle.getString("gold");
                cformal = bundle.getString("formal");
                ccasual = bundle.getString("casual");
                cparty = bundle.getString("party");
                cspecial = bundle.getString("special");

                imagelocation = bundle.getString("imagepath");

                userimage = findViewById(R.id.userimage);
                loadimage();


                if (cblack != null) {
                    if (cblack.equals(blackf)) {
                        blacksqtck.setVisibility(View.VISIBLE);


                        black = cblack;
                    } else {
                        blacksqtck.setVisibility(View.INVISIBLE);


                        black = nooption;
                    }
                }

                if (cwhite != null) {
                    if (cwhite.equals(whitef)) {
                        whitesqtck.setVisibility(View.VISIBLE);



                        white = cwhite;
                    } else {
                        whitesqtck.setVisibility(View.INVISIBLE);

                        white = nooption;
                    }
                }

                if (cgrey != null) {
                    if (cgrey.equals(greyf)) {
                        greysqtck.setVisibility(View.VISIBLE);


                        grey = cgrey;
                    } else {
                        greysqtck.setVisibility(View.INVISIBLE);


                        grey = nooption;
                    }
                }

                if (cbeige != null) {
                    if (cbeige.equals(beigef)) {
                        beigesqtck.setVisibility(View.VISIBLE);


                        beige = cbeige;
                    } else {
                        beigesqtck.setVisibility(View.INVISIBLE);

                        beige = nooption;
                    }
                }

                if (cred != null) {
                    if (cred.equals(redf)) {
                        redsqtck.setVisibility(View.VISIBLE);


                        red = cred;
                    } else {
                        redsqtck.setVisibility(View.INVISIBLE);

                        red = nooption;
                    }
                }

                if (cpink != null) {
                    if (cpink.equals(pinkf)) {
                        pinksqtck.setVisibility(View.VISIBLE);

                        pink = cpink;
                    } else {
                        pinksqtck.setVisibility(View.INVISIBLE);

                        pink = nooption;
                    }
                }

                if (cblue != null) {
                    if (cblue.equals(bluef)) {
                        bluesqtck.setVisibility(View.VISIBLE);



                        blue = cblue;
                    } else {
                        bluesqtck.setVisibility(View.INVISIBLE);

                        blue = nooption;
                    }
                }

                if (cgreen != null) {
                    if (cgreen.equals(greenf)) {
                        greensqtck.setVisibility(View.VISIBLE);

                        green = cgreen;
                    } else {
                        greensqtck.setVisibility(View.INVISIBLE);

                        green = nooption;
                    }
                }

                if (cyellow != null) {
                    if (cyellow.equals(yellowf)) {
                        yellowsqtck.setVisibility(View.VISIBLE);

                        yellow = cyellow;
                    } else {
                        yellowsqtck.setVisibility(View.INVISIBLE);

                        yellow = nooption;
                    }
                }

                if (corange != null) {
                    if (corange.equals(orangef)) {
                        orangesqtck.setVisibility(View.VISIBLE);


                        orange = corange;
                    } else {
                        orangesqtck.setVisibility(View.INVISIBLE);

                        orange = nooption;
                    }
                }

                if (cbrown != null) {
                    if (cbrown.equals(brownf)) {
                        brownsqtck.setVisibility(View.VISIBLE);

                        brown = cbrown;
                    } else {
                        brownsqtck.setVisibility(View.INVISIBLE);

                        brown = nooption;
                    }
                }

                if (cpurple != null) {
                    if (cpurple.equals(purplef)) {
                        purplesqtck.setVisibility(View.VISIBLE);

                        purple = cpurple;
                    } else {
                        purplesqtck.setVisibility(View.INVISIBLE);

                        purple = nooption;
                    }
                }

                if (csilver != null) {
                    if (csilver.equals(silverf)) {
                        silversqtck.setVisibility(View.VISIBLE);


                        silver = csilver;
                    } else {
                        silversqtck.setVisibility(View.INVISIBLE);

                        silver = nooption;
                    }
                }

                if (cgold != null) {
                    if (cgold.equals(goldf)) {
                        goldsqtck.setVisibility(View.VISIBLE);


                        gold = cgold;
                    } else {
                        goldsqtck.setVisibility(View.INVISIBLE);

                        gold = nooption;
                    }
                }


                if (category != null && !category.equals(nooption)) {


                    if (category.equals(accessory)) {
                        accselect.setVisibility(View.VISIBLE);
                        accessories.setVisibility(View.INVISIBLE);
                        acctextslct.setVisibility(View.VISIBLE);
                        acctexttt.setVisibility(View.INVISIBLE);
                        cat = accessory;

                    } else if (category.equals(bottoms)) {
                        bottomselect.setVisibility(View.VISIBLE);
                        bottom.setVisibility(View.INVISIBLE);
                        bottomtxtslct.setVisibility(View.VISIBLE);
                        bottomtxt.setVisibility(View.INVISIBLE);
                        cat = bottoms;

                    } else if (category.equals(tops)) {
                        topselect.setVisibility(View.VISIBLE);
                        top.setVisibility(View.INVISIBLE);
                        toptxtslct.setVisibility(View.VISIBLE);
                        toptxt.setVisibility(View.INVISIBLE);
                        cat = tops;

                    } else {
                        footselect.setVisibility(View.VISIBLE);
                        foottxtslct.setVisibility(View.VISIBLE);
                        foot.setVisibility(View.INVISIBLE);
                        foottxt.setVisibility(View.INVISIBLE);
                        cat = footwear;
                    }

                } else {

                    accselect.setVisibility(View.INVISIBLE);
                    topselect.setVisibility(View.INVISIBLE);
                    bottomselect.setVisibility(View.INVISIBLE);
                    footselect.setVisibility(View.INVISIBLE);
                    cat=nooption;
                }


                if (cformal != null) {
                    if (cformal.equals(formalf)) {

                        formalsqtck.setVisibility(View.VISIBLE);
                        formalsq.setVisibility(View.INVISIBLE);
                        formal = formalf;
                    } else {

                        formalsqtck.setVisibility(View.INVISIBLE);
                        formal=nooption;
                    }
                }

                if (ccasual != null) {
                    if (ccasual.equals(casualf)) {

                        casualsqtck.setVisibility(View.VISIBLE);
                        casualsq.setVisibility(View.INVISIBLE);
                        casual = casualf;
                    } else {

                        casualsqtck.setVisibility(View.INVISIBLE);
                        casual=nooption;
                    }
                }

                if (cparty != null) {
                    if (cparty.equals(partyf)) {

                        partysqtck.setVisibility(View.VISIBLE);
                        partysq.setVisibility(View.INVISIBLE);
                        party = partyf;
                    } else {

                        partysqtck.setVisibility(View.INVISIBLE);
                        party=nooption;
                    }
                }

                if (cspecial != null) {
                    if (cspecial.equals(specialf)) {

                        specialsqtck.setVisibility(View.VISIBLE);
                        specialsq.setVisibility(View.INVISIBLE);
                        special = specialf;
                    } else {

                        specialsqtck.setVisibility(View.INVISIBLE);
                        special = nooption;
                    }
                }
            }
        }





                Realm.init(this);
                realm = Realm.getDefaultInstance();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mixpanel.track("Save Button Clicked");

                        if(!isValid()){

                            return;
                        }else {

                            if (isUpdate) {
                                realm=Realm.getDefaultInstance();
                                realm.beginTransaction();
                                Dresses deletedress = realm.where(Dresses.class).equalTo("id", Id).findFirst();
                                Styles deletestyles = realm.where(Styles.class).equalTo("styleset", Id).findFirst();
                                Colors deletecolors = realm.where(Colors.class).equalTo("colorset", Id).findFirst();

                                deletecolors.deleteFromRealm();
                                deletestyles.deleteFromRealm();
                                deletedress.deleteFromRealm();
                                realm.commitTransaction();
                            }


                            RealmResults<Dresses> query = realm.where(Dresses.class).findAll();
                            int nextId = query.size();




                            if (nextId == 0) {

                                id=nextId+1;
                                clset = nextId + 1;
                                stset = nextId + 1;

                            } else {

                                RealmResults<Dresses> dresses1 = realm.where(Dresses.class).findAllSorted("id", Sort.DESCENDING);
                                i = dresses1.get(0).getId() + 1;
                                id=i;
                                clset = i;
                                stset = i;


                            }

                            Dresses dresses = new Dresses();
                            dresses.setId(id);



                            if (cat != null) {

                                if (cat.equals(accessory)) {
                                    dresses.setCategory(accessory);
                                } else if (cat.equals(bottoms)) {
                                    dresses.setCategory(bottoms);
                                } else if (cat.equals(tops)) {
                                    dresses.setCategory(tops);
                                } else {
                                    dresses.setCategory(footwear);
                                }
                            } else {
                                dresses.setCategory(nooption);
                            }


                            Styles styles = new Styles();

                            if (formal != null) {
                                if (formal.equals(formalf)) {
                                    styles.setFormal(formalf);
                                } else {
                                    styles.setFormal(nooption);
                                }
                            }

                            if (casual != null) {
                                if (casual.equals(casualf)) {
                                    styles.setCasual(casualf);
                                } else {
                                    styles.setCasual(nooption);
                                }
                            }

                            if (party != null) {
                                if (party.equals(partyf)) {
                                    styles.setParty(partyf);
                                } else {
                                    styles.setParty(nooption);
                                }
                            }

                            if (special != null) {
                                if (special.equals(specialf)) {
                                    styles.setSpecial(specialf);
                                } else {
                                    styles.setSpecial(nooption);
                                }
                            }

                            styles.setStyleset(stset);


                            Colors colors = new Colors();
                            if (black != null) {
                                if (black.equals(blackf)) {
                                    colors.setBlack(blackf);
                                } else {
                                    colors.setBlack(nooption);
                                }
                            }

                            if (white != null) {
                                if (white.equals(whitef)) {
                                    colors.setWhite(whitef);
                                } else {
                                    colors.setWhite(nooption);
                                }
                            }

                            if (grey != null) {
                                if (grey.equals(greyf)) {
                                    colors.setGrey(greyf);
                                } else {
                                    colors.setGrey(nooption);
                                }
                            }

                            if (beige != null) {
                                if (beige.equals(beigef)) {
                                    colors.setBeige(beigef);
                                } else {
                                    colors.setBeige(nooption);
                                }
                            }

                            if (red != null) {
                                if (red.equals(redf)) {
                                    colors.setRed(redf);
                                } else {
                                    colors.setRed(nooption);
                                }
                            }

                            if (pink != null) {
                                if (pink.equals(pinkf)) {
                                    colors.setPink(pinkf);
                                } else {
                                    colors.setPink(nooption);
                                }
                            }

                            if (blue != null) {
                                if (blue.equals(bluef)) {
                                    colors.setBlue(bluef);
                                } else {
                                    colors.setBlue(nooption);
                                }
                            }

                            if (green != null) {
                                if (green.equals(greenf)) {
                                    colors.setGreen(greenf);
                                } else {
                                    colors.setGreen(nooption);
                                }
                            }

                            if (yellow != null) {
                                if (yellow.equals(yellowf)) {
                                    colors.setYellow(yellowf);
                                } else {
                                    colors.setYellow(nooption);
                                }
                            }

                            if (orange != null) {
                                if (orange.equals(orangef)) {
                                    colors.setOrange(orangef);
                                } else {
                                    colors.setOrange(nooption);
                                }
                            }

                            if (brown != null) {
                                if (brown.equals(brownf)) {
                                    colors.setBrown(brownf);
                                } else {
                                    colors.setBrown(nooption);
                                }
                            }

                            if (purple != null) {
                                if (purple.equals(purplef)) {
                                    colors.setPurple(purplef);
                                } else {
                                    colors.setPurple(nooption);
                                }
                            }

                            if (silver != null) {
                                if (silver.equals(silverf)) {
                                    colors.setSilver(silverf);
                                } else {
                                    colors.setSilver(nooption);
                                }
                            }

                            if (gold != null) {
                                if (gold.equals(goldf)) {
                                    colors.setGold(goldf);
                                } else {
                                    colors.setGold(nooption);
                                }
                            }

                            colors.setColorset(clset);


                            realm.beginTransaction();
                            final Colors color = realm.copyToRealm(colors);
                            final Styles styless = realm.copyToRealm(styles);
                            realm.commitTransaction();


                            dresses.getColorsRealmList().add(color);
                            dresses.getStylesRealmList().add(styless);
                            dresses.setImagename(imagelocation);

                            RealmHandler handler = new RealmHandler(realm);
                            handler.save(dresses);
                            finish();
                            refresh();

                           // Intent intent1=new Intent(Pic_info.this,HomeActivity.class);
                           // intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           // startActivity(intent1);


                            Pic_info.super.onBackPressed();

                        }
                    }


                });




    }

    public Bitmap getBitmap() {
       // Uri uri = getImageUri(path);
        InputStream in = null;

            final int IMAGE_MAX_SIZE = 500;
         //   in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(bytes,0,bytes.length,o);
            //in.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math
                        .round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
        //    in = mContentResolver.openInputStream(uri);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length,o2);
            //in.close();


    }

    public boolean onSupportNavigateUp(){

        finish();

        return true;
    }


    private void refresh() {

        ArrayList<Dresses> accdresss = new ArrayList<>();
        RealmResults<Dresses> accdressesresults = realm.where(Dresses.class).contains("category", "Accessories").findAllSorted("id", Sort.DESCENDING);


        for (Dresses dressess : accdressesresults) {
            accdresss.add(dressess);
        }

        madapter = new AccessoryAdapter(Pic_info.this, accdresss);
        accrecycler.setAdapter(madapter);
        madapter.notifyDataSetChanged();


        ArrayList<Dresses> topdresss = new ArrayList<>();
        RealmResults<Dresses> topdressesresults = realm.where(Dresses.class).contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);


        for (Dresses dressesss : topdressesresults) {
            topdresss.add(dressesss);
        }

        mtopadapter = new TopAdapter(Pic_info.this, topdresss);
        toprecycler.setAdapter(mtopadapter);
        mtopadapter.notifyDataSetChanged();


        ArrayList<Dresses> bottomdresss = new ArrayList<>();
        RealmResults<Dresses> bottomdressesresults = realm.where(Dresses.class).contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);


        for (Dresses dressessss : bottomdressesresults) {
            bottomdresss.add(dressessss);
        }

        mbottomadapter = new BottomAdapter(Pic_info.this, bottomdresss);
        bottomrecycler.setAdapter(mbottomadapter);
        mbottomadapter.notifyDataSetChanged();


        ArrayList<Dresses> footdresss = new ArrayList<>();
        RealmResults<Dresses> footdressesresults = realm.where(Dresses.class).contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);


        for (Dresses dressesssss : footdressesresults) {
            footdresss.add(dressesssss);
        }

        mfootwearadapter = new FootWearAdapter(Pic_info.this, footdresss);
        footrecycler.setAdapter(mfootwearadapter);
        mfootwearadapter.notifyDataSetChanged();

    }

    private boolean isValid() {

     if (cat.equals(nooption)) {

        Toast.makeText(getApplicationContext(), R.string.caution, Toast.LENGTH_SHORT).show();
        return false;
    }
        return true;

    }


    private void save() {

            Realm.init(this);
            realm = Realm.getDefaultInstance();
            Date date=new Date();
            Random random=new Random();
            int n=random.nextInt(30000);



            imagelocation = "userimage" + date.getTime() + n + ".png";

            if (imagelocation.length() > 0) {

                new ImageSaver(Pic_info.this).setFileName(imagelocation).setDirectoryName("mycloset").saveImage(bmp);
            }
        }




    private void loadimage() {

        if (imagelocation != null && imagelocation.length() > 0) {


            Bitmap bitmap = new ImageSaver(Pic_info.this).setFileName(imagelocation).setDirectoryName("mycloset").load();
            userimage = findViewById(R.id.userimage);
            userimage.setImageBitmap(bitmap);

        }
    }


    private Bitmap getScaledBitmap(byte[] cropuri, int width, int height) {

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeByteArray(cropuri,0,cropuri.length,options);
        int cropsampleSize=calculateInSampleSize(options,width,height);
        options.inJustDecodeBounds=false;
        options.inSampleSize=cropsampleSize;
        return BitmapFactory.decodeByteArray(cropuri,0,cropuri.length,options);

    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqwidth, int reqheight) {

        final int width=options.outWidth;
        final int height=options.outHeight;

        Matrix matrix=new Matrix();
        matrix.postScale(width,height);


        int Samplesize=1;

        if (width>reqwidth || height>reqheight){

            final int widthratio=Math.round((float)width/(float)reqwidth);
            final int heightratio=Math.round((float)height/(float)reqheight);
            Samplesize=heightratio<widthratio ? heightratio:widthratio;

        }
        return Samplesize;


    }


}
