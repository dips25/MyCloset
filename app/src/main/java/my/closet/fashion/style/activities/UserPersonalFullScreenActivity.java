package my.closet.fashion.style.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.BottomSheetViewAdapter;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.FeedResponse;


public class UserPersonalFullScreenActivity extends AppCompatActivity {

    TextView username,description;
    ImageView userpersonalimage;
    private CircleImageView profile_pic;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FeedResponse feedResponse;
    private RequestOptions requestOptions;
    private GridView recyclerView;
    private TextView tags_txt;
    private String key;
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
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpersonalfullscreen);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        username = (TextView) findViewById(R.id.userpersonalusername_txt);
        description = (TextView)findViewById(R.id.userpersonalpostdescription);
        profile_pic = (CircleImageView) findViewById(R.id.userpersonalprofile_pic);
        userpersonalimage = (ImageView) findViewById(R.id.userpersonalimage);
        tags_txt = (TextView) findViewById(R.id.userpersonal_clothes_heading);
        recyclerView = (GridView) findViewById(R.id.userpersonalmatching_recycler);

        tags_txt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);



        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        Intent intent = getIntent();

        if (intent!=null){

            feedResponse = (FeedResponse) intent.getSerializableExtra("picture");

            String email = feedResponse.getEmail();
            String desc = feedResponse.getDescription();
            String image = feedResponse.getImage();

            if (image!=null){

                Glide.with(UserPersonalFullScreenActivity.this)
                        .load(image)
                        .apply(requestOptions)
                        .into(userpersonalimage);
            }



            if (desc!=null){

                description.setText(desc);
            }else {

                description.setVisibility(View.GONE);
            }

            db.collection("UsersList").whereEqualTo("Email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {

                    if (task.isSuccessful()){

                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()).getDocuments()){

                            key=documentSnapshot.getId();

                            String name = documentSnapshot.get("Pen_Name").toString();
                            String propic = documentSnapshot.get("Profile_Pic").toString();

                            if (name!=null && !name.equals("")){

                                username.setText(name);
                            }

                            if (propic!=null && !propic.equals("")){

                                Glide.with(UserPersonalFullScreenActivity.this).load(propic).apply(requestOptions).into(profile_pic);

                            }else {

                                profile_pic.setBackgroundResource(R.drawable.ic_user_profile);
                            }
                        }

                        db.collection("UsersList").document(key).collection("MetaData").whereEqualTo("lookid", feedResponse.getLookid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@Nonnull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    if (!Objects.requireNonNull(task.getResult()).isEmpty()) {


                                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                            if (documentSnapshot.contains("Accessories")) {

                                                data = (ArrayList<String>) documentSnapshot.get("Accessories");
                                                checkcolors(data,"Accessories");

                                            }

                                            if (documentSnapshot.contains("Tops")){

                                                data = (ArrayList<String>) documentSnapshot.get("Tops");
                                                checkcolors(data,"Tops");

                                            }

                                            if (documentSnapshot.contains("Bottoms")){

                                                data = (ArrayList<String>) documentSnapshot.get("Bottoms");
                                                checkcolors(data,"Bottoms");

                                            }

                                            if (documentSnapshot.contains("Footwear")){

                                                data = (ArrayList<String>) documentSnapshot.get("Footwear");
                                                checkcolors(data,"Footwear");

                                            }

                                            if (documentSnapshot.contains("data")){

                                                data = (ArrayList<String>) documentSnapshot.get("data");
                                                if(data!=null) {
                                                    checkcolors(data, "Data");
                                                }
                                            }



                                        }
                                    }

                                }
                            }
                        });

                    }

                }
            });




        }

    }

    public void checkcolors(ArrayList<String> data, String cat) {

        for (String s : data) {



            if (s.equals(black)) {

                cblack = black;
            }

            if (s.equals(white)) {

                cwhite = white;
            }

            if (s.equals(grey)) {

                cgrey = grey;
            }

            if (s.equals(beige)) {

                cbeige = beige;
            }

            if (s.equals(red)) {

                cred = red;
            }

            if (s.equals(pink)) {

                cpink = pink;
            }

            if (s.equals(silver)) {

                csilver = silver;
            }

            if (s.equals(green)) {

                cgreen = green;

            }

            if (s.equals(blue)) {

                cblue = blue;
            }

            if (s.equals(yellow)) {

                cyellow = yellow;
            }

            if (s.equals(orange)) {

                corange = orange;
            }

            if (s.equals(brown)) {

                cbrown = brown;
            }

            if (s.equals(purple)) {

                cpurple = purple;
            }

            if (s.equals(gold)) {

                cgold = gold;
            }
        }

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




        if (cat.equals("Accessories")){

            r2 = r1.where().contains("category",cat).findAll();

            for (Dresses dresses : r2){

                dressesArrayList.add(dresses);
            }

        }else if (cat.equals("Tops")) {

            r2 = r1.where().contains("category", cat).findAll();

            for (Dresses dresses : r2) {

                dressesArrayList.add(dresses);
            }

        }else if (cat.equals("Bottoms")) {

            r2 = r1.where().contains("category", cat).findAll();

            for (Dresses dresses : r2) {

                dressesArrayList.add(dresses);
            }

        }else if (cat.equals("Footwear")) {

            r2 = r1.where().contains("category", cat).findAll();

            for (Dresses dresses : r2) {

                dressesArrayList.add(dresses);
            }

        }else {

            for (Dresses dresses : r1){

                dressesArrayList.add(dresses);
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



        BottomSheetViewAdapter bottomSheetViewAdapter = new BottomSheetViewAdapter(UserPersonalFullScreenActivity.this,R.layout.card_accessories,dressesArrayList);
        bottomSheetViewAdapter.notifyDataSetChanged();

        if (bottomSheetViewAdapter.getCount()>0 || !dressesArrayList.isEmpty()){

            tags_txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(bottomSheetViewAdapter);

        } else {

            tags_txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

        }



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
