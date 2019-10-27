package my.closet.fashion.style.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;

import co.lujun.androidtagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.adapters.BottomSheetViewAdapter;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.FeedResponse;
import xyz.hanks.library.bang.SmallBangView;

public class FullScreenViewActivity extends AppCompatActivity implements View.OnClickListener {

    private StorageReference mStorage;
    private DatabaseReference mRootRef;
    private String myemail = "";
    private String My_DbKey = "";
    private FirebaseFirestore userCollection;
    private RequestOptions requestOptions;
    private Intent i;
    private FeedResponse feedResponse_obj;
    private String imageUri = "";
    private String blogPostId = "";
    private File file;
    private Uri myUri;
    private ProgressBar progressBar;
    private ImageView blog_comment_btn;
    private ImageView blog_bookmark_btn;
    private ImageView blog_like_btn;
    private ImageView fullimage;
    private TextView no_of_likes;
    private SmallBangView like_text, bookmark_anim;
    private MixpanelAPI mixpanelAPI;
    private TextView username_txt,description;
    private TextView tags_txt;
    private TagContainerLayout selectedTags;
    private CircleImageView profile_pic;
    private RelativeLayout profile_layout;
    private Button follow_btn;
    private String key="";
    private FragmentTransaction transaction;
    private FragmentManager manager;
    AdView madview;
    LinearLayout coordinatorLayout;
    GridView recyclerView;

    int lookid;
    ArrayList<String> data = new ArrayList<>();
    private int n;

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

    GridView gridView;
    BottomSheetBehavior behavior;

    Realm realm;

    private String dbkey;
    private Toolbar bottomsheet_tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen);

        android.transition.Fade fade = new android.transition.Fade();
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);



        Realm.init(this);
        realm = Realm.getDefaultInstance();


     /*   MobileAds.initialize(this,"ca-app-pub-3828826031268173~5645990144");

        madview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest); */


        manager = getSupportFragmentManager();
        mixpanelAPI= MixpanelAPI.getInstance(FullScreenViewActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        mStorage = FirebaseStorage.getInstance().getReference();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        recyclerView = (GridView) findViewById(R.id.matching_recycler);


        myemail = Utilities.loadPref(FullScreenViewActivity.this, "email", "");

        My_DbKey = Utilities.loadPref(FullScreenViewActivity.this, "My_DbKey", "");
        userCollection = FirebaseFirestore.getInstance();
        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        i = getIntent();




        if (i != null) {
            feedResponse_obj = (FeedResponse) i.getSerializableExtra("model");
            if (feedResponse_obj.getImage() != null) {
                imageUri = feedResponse_obj.getImage();
            }

            if (feedResponse_obj.getDocumentId() != null && !feedResponse_obj.getDocumentId().equalsIgnoreCase("")) {
                blogPostId = feedResponse_obj.getDocumentId();
            }

            if (feedResponse_obj!=null && !String.valueOf(feedResponse_obj.getLookid()).equalsIgnoreCase("")){

                lookid = feedResponse_obj.getLookid();

            }


            if (feedResponse_obj != null && feedResponse_obj.getDbkey()!=null){

                dbkey = feedResponse_obj.getDbkey();
            }
        }

        if (Objects.requireNonNull(feedResponse_obj).getDbkey() != null) {

           }


        findViews();


    }


    private void findViews() {

      //  progressBar = (ProgressBar) findViewById(R.id.connectProgress);

        blog_comment_btn = (ImageView) findViewById(R.id.blog_comment_btn);
        blog_comment_btn.setOnClickListener(this);

        blog_bookmark_btn = (ImageView) findViewById(R.id.blog_bookmark_btn);
        // blog_bookmark_btn.setOnClickListener(this);

        blog_like_btn = (ImageView) findViewById(R.id.blog_like_btn);

        description = (TextView) findViewById(R.id.postdescription);

        String s = feedResponse_obj.getDescription();

        if (s!=null && !s.equals("")) {

            description.setText(feedResponse_obj.getDescription());
        }else {

            description.setVisibility(View.GONE);
        }







        fullimage = (ImageView) findViewById(R.id.fullimage);
     //   no_of_likes = (TextView) findViewById(R.id.no_of_likes);
     //   no_of_likes.setOnClickListener(this);

        like_text = findViewById(R.id.like_heart);
        bookmark_anim = findViewById(R.id.bookmark_anim);
        like_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mixpanelAPI.track("Fullscreen Likes");
                if (like_text.isSelected()) {
                    like_text.setSelected(false);
                } else {
                    like_text.setSelected(true);
                    like_text.likeAnimation();
                }
                if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase("")) {
                    Likepost();
                } else {
                    Intent ii = new Intent(FullScreenViewActivity.this, FbGmailActivity.class);
                    startActivity(ii);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                }
            }
        });

        bookmark_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mixpanelAPI.track("Bookmarked");
                if (bookmark_anim.isSelected()) {
                    bookmark_anim.setSelected(false);

                } else {
                    bookmark_anim.setSelected(true);
                    bookmark_anim.likeAnimation();
                }
                if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase("")) {
                    Bookmarksave();
                } else {
                    Intent ii = new Intent(FullScreenViewActivity.this, FbGmailActivity.class);
                    startActivity(ii);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                }
            }
        });

        username_txt = (TextView) findViewById(R.id.username_txt);
        tags_txt = (TextView) findViewById(R.id.matching_clothes_heading);
       // selectedTags = (TagContainerLayout) findViewById(R.id.selectedTags);
       // selectedTags.setTheme(ColorFactory.NONE);
       // selectedTags.setTagBackgroundColor(Color.TRANSPARENT);
       // selectedTags.setBorderColor(Color.TRANSPARENT);

        coordinatorLayout = (LinearLayout) findViewById(R.id.root_layout);





        profile_pic = (CircleImageView) findViewById(R.id.profile_pic);
        profile_pic.setBackgroundResource(R.drawable.ic_user_profile);


        profile_layout = (RelativeLayout) findViewById(R.id.profile_layout);
        profile_layout.setOnClickListener(this);

        tags_txt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

       // follow_btn = (Button) findViewById(R.id.follow_btn);
      //  follow_btn.setText("Follow");
     //   follow_btn.setOnClickListener(this);


        if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase("")) {
            if (!feedResponse_obj.getEmail().equalsIgnoreCase("") && feedResponse_obj.getEmail() != null) {
                getFeedKey();
            }

        }

        if (feedResponse_obj.getEmail() != null && !feedResponse_obj.getEmail().equalsIgnoreCase("")) {
            if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase("")) {

                if (feedResponse_obj.getEmail().equalsIgnoreCase(Utilities.loadPref(FullScreenViewActivity.this, "email", ""))) {
                  //  follow_btn.setVisibility(View.GONE);
                } else {
                    // follow_btn.setVisibility(View.VISIBLE);
                }
            }

        }

        if (feedResponse_obj.getText() != null) {
            selectedTags.setTags(feedResponse_obj.getText());
        }

        if (feedResponse_obj.getCaption() != null) {
            tags_txt.setText(feedResponse_obj.getCaption());
        }

        if (imageUri != null && imageUri.contains("https")) {
            Glide.with(FullScreenViewActivity.this).load(imageUri).apply(requestOptions).into(fullimage);

        } else {
            Bitmap bmp = Utilities.StringToBitMap(imageUri);
            Glide.with(FullScreenViewActivity.this).load(bmp).apply(requestOptions).into(fullimage);
        }

        if (My_DbKey != null && !My_DbKey.equalsIgnoreCase("")) {

            //check bookmarked or not

            DocumentReference bookmark = userCollection.collection("UsersList")
                    .document(My_DbKey).collection("Bookmarks")
                    .document(blogPostId);


            bookmark.addSnapshotListener(new EventListener<DocumentSnapshot>() {

                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (documentSnapshot.exists()) {
                        bookmark_anim.setSelected(true);
                    } else {
                        bookmark_anim.setSelected(false);
                    }
                }
            });
        }

        if (myemail != null && !myemail.equalsIgnoreCase("")) {

            //Getting if post like or not
            DocumentReference likereference = userCollection.collection("CommonFeed")
                    .document(blogPostId).collection("Likes")
                    .document(myemail);

            likereference.addSnapshotListener(new EventListener<DocumentSnapshot>() {

                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (documentSnapshot.exists()) {
                        like_text.setSelected(true);
                    } else {
                        like_text.setSelected(false);
                    }
                }
            });

            //Getting likes count
            CollectionReference likeCollectionRef = userCollection.collection("CommonFeed")
                    .document(blogPostId).collection("Likes");
            likeCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    if (documentSnapshots.isEmpty()) {
                    } else {
                        int likeCount = documentSnapshots.size();
                       // no_of_likes.setText(String.valueOf(likeCount) + " likes");
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



        BottomSheetViewAdapter bottomSheetViewAdapter = new BottomSheetViewAdapter(FullScreenViewActivity.this,R.layout.card_accessories,dressesArrayList);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blog_comment_btn:
                if (imageUri != null) {
                    mixpanelAPI.track("Share");
                    Utilities.showLoading(FullScreenViewActivity.this, "Please wait...");
                    shareImage(imageUri);
                }
                break;


            case R.id.no_of_likes:
                mixpanelAPI.track("No of Likes");
               /* Intent follow = new Intent(this, FollowingActivity.class);
                follow.putExtra("blogPostId", blogPostId);
                startActivity(follow);*/
                break;

            case R.id.follow_btn:
                mixpanelAPI.track("Follow ");
                if (key != null && !key.equalsIgnoreCase("")) {

                    if (My_DbKey != null && !My_DbKey.equalsIgnoreCase("")) {
                        FollowingAction();
                    } else {
                        Intent ii = new Intent(FullScreenViewActivity.this, FbGmailActivity.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                    }
                }

                break;


            case R.id.profile_layout:
                mixpanelAPI.track("Fullscreen Profileview");
                if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase("")) {

                    if (feedResponse_obj.getEmail().equalsIgnoreCase(Utilities.loadPref(FullScreenViewActivity.this, "email", ""))) {

                        Intent ii = new Intent(FullScreenViewActivity.this, UserPersonalActivity.class);
                        ii.putExtra("key", (Serializable) feedResponse_obj);
                        startActivity(ii);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    } else {
                        if (key != null) {
                            Intent ii = new Intent(FullScreenViewActivity.this, UserPersonalActivity.class);
                            ii.putExtra("key", (Serializable) feedResponse_obj);
                            startActivity(ii);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    }
                } else {
                    Utilities.showToast(FullScreenViewActivity.this, "Please Login to continue");
                }
                break;


        }
    }


    private void getFeedKey() {
       // progressBar.setVisibility(View.VISIBLE);

        userCollection.collection("UsersList")
                .whereEqualTo("Email", feedResponse_obj.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  //  progressBar.setVisibility(View.GONE);
                    for (DocumentSnapshot document : task.getResult()) {

                        key = document.getId();

                        if (document.get("Profile_Pic") != null && !document.get("Profile_Pic").toString().equalsIgnoreCase("")) {
                            Glide.with(FullScreenViewActivity.this).load(document.get("Profile_Pic")).apply(requestOptions).into(profile_pic);
                        }

                        if (document.get("Pen_Name") != null && !document.get("Pen_Name").toString().equalsIgnoreCase("")) {
                            if (document.get("Pen_Name").toString().equalsIgnoreCase(Utilities.loadPref(FullScreenViewActivity.this, "Pen_Name", ""))) {
                                username_txt.setText("You");
                            } else {
                                username_txt.setText(Objects.requireNonNull(document.get("Pen_Name")).toString());

                            }
                        }

                        if (key != null && !key.equalsIgnoreCase("")) {

                            DocumentReference followreference = userCollection.collection("UsersList")
                                    .document(My_DbKey).collection("Followee").document(feedResponse_obj.getEmail());
                            followreference.addSnapshotListener(new EventListener<DocumentSnapshot>() {

                                @Override
                                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                                    if (e != null) {
                                        //  Log.w(TAG, "listen:error", e);
                                        return;
                                    }
                                    if (documentSnapshot.exists()) {

                                       // follow_btn.setVisibility(View.GONE);
                                      //  follow_btn.setBackgroundResource(R.drawable.following_button_square);
                                       // follow_btn.setTextColor(ContextCompat.getColor(FullScreenViewActivity.this,R.color.colorAccent));

                                    } else {

                                       // follow_btn.setText("Follow");
                                      //  follow_btn.setBackgroundResource(R.drawable.follow_button_square);
                                       // follow_btn.setTextColor(ContextCompat.getColor(FullScreenViewActivity.this, R.color.white));
                                    }
                                }
                            });
                        }

                    }
                } else {
                   // progressBar.setVisibility(View.GONE);
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }

                userCollection.collection("UsersList").document(key).collection("MetaData").whereEqualTo("lookid", lookid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
               // progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void Likepost() {
        userCollection.collection("CommonFeed/" + blogPostId + "/Likes").document(myemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@Nonnull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!Objects.requireNonNull(task.getResult()).exists()) {

                        Map<String, Object> data = new HashMap<>();
                        String id = UUID.randomUUID().toString();

                        data.put("id", id);
                        data.put("email", Utilities.loadPref(FullScreenViewActivity.this, "email", ""));

                        userCollection.collection("CommonFeed/" + blogPostId + "/Likes").document(myemail).set(data);

                    } else {
                        userCollection.collection("CommonFeed/" + blogPostId + "/Likes").document(myemail).delete();
                    }
                } else {
                    Log.i("LikeError", task.getException().getMessage());
                    Toast.makeText(FullScreenViewActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Bookmarksave() {

        if (imageUri != null) {
            userCollection.collection("UsersList").document(My_DbKey).collection("Bookmarks").
                    document(blogPostId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete( Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!Objects.requireNonNull(task.getResult()).exists()) {

                            Map<String, Object> data = new HashMap<>();
                            String id = UUID.randomUUID().toString();

                            Long tsLong = System.currentTimeMillis() / 1000;
                            String timestamp = tsLong.toString();

                            data.put("id", id);
                            data.put("image", imageUri);
                            data.put("email", feedResponse_obj.getEmail());
                            data.put("timestamp",timestamp);
                            data.put("description",feedResponse_obj.getDescription());
                            data.put("lookid",lookid);

                            userCollection.collection("UsersList").document(My_DbKey).collection("Bookmarks").document(blogPostId).set(data);
                            Utilities.showToast(FullScreenViewActivity.this, getResources().getString(R.string.bookmark_saved));
                            //Toast.makeText(FullScreenViewActivity.this, R.string.bookmark_saved, Toast.LENGTH_SHORT).show();
                        } else {
                            userCollection.collection("UsersList").document(My_DbKey).collection("Bookmarks").document(blogPostId).delete();
                        }
                    } else {
                        Log.i("LikeError", Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(FullScreenViewActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    private void FollowingAction() {

        userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(feedResponse_obj.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    if (!Objects.requireNonNull(task.getResult()).exists()){

                        String id = UUID.randomUUID().toString();

                        HashMap<String,Object> hashMap = new HashMap<>();

                        hashMap.put("id",id);
                        hashMap.put("email",feedResponse_obj.getEmail());
                        hashMap.put("name",feedResponse_obj.getPenname());
                        hashMap.put("imgname",feedResponse_obj.getImage());

                        userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(feedResponse_obj.getEmail()).set(hashMap);
                        getPosts();



                    }else {

                        userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(feedResponse_obj.getEmail()).delete();

                    }
                }

            }
        });

        if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase(feedResponse_obj.getEmail())) {
            userCollection.collection("UsersList/" + key + "/FollowCollection").document(myemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {

                            Map<String, Object> data = new HashMap<>();
                            String id = UUID.randomUUID().toString();

                            data.put("id", id);
                            data.put("email", Utilities.loadPref(FullScreenViewActivity.this, "email", ""));
                            data.put("imgname",Utilities.loadPref(FullScreenViewActivity.this, "Profile_Pic", ""));
                            data.put("name",Utilities.loadPref(FullScreenViewActivity.this, "Pen_Name","" ));

                            userCollection.collection("UsersList/" + key + "/FollowCollection").document(myemail).set(data);

                        } else {
                            userCollection.collection("UsersList/" + key + "/FollowCollection").document(myemail).delete();
                        }
                    } else {
                        Log.i("LikeError", task.getException().getMessage());
                        Toast.makeText(FullScreenViewActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        }

    private void getPosts() {

        userCollection.collection("UsersList")
                .document(My_DbKey)
                .collection("Feed")
                .whereEqualTo("email", feedResponse_obj.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            if (Objects.requireNonNull(task.getResult()).isEmpty()) {

                                userCollection.collection("UsersList").document(key).collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {

                                            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                                FeedResponse feedResponse = documentSnapshot.toObject(FeedResponse.class);
                                                if (feedResponse != null) {
                                                    userCollection.collection("UsersList").document(My_DbKey).collection("Feed").add(feedResponse);


                                                }
                                            }
                                        }

                                    }
                                });
                            }
                        }
                    }

                });
    }






    private void shareImage(final String imagePath) {

        if (imagePath.contains("http")) {
            Glide.with(FullScreenViewActivity.this)
                    .asBitmap()
                    .load(imagePath)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            myUri = getLocalBitmapUri(resource);
                            if (myUri != null) {
                                Utilities.hideLoading();
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.putExtra(Intent.EXTRA_TEXT, "I thought you would like this, click here" + " " +  "https://play.google.com/store/apps/details?id=my.closet.fashion.style");
                                i.setType("image/*");
                                i.putExtra(Intent.EXTRA_STREAM, myUri);
                                startActivity(Intent.createChooser(i, "Share Image"));
                            }

                        }
                    });


        }
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {

            file = new File(Utilities.picturePath, "share_image_" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(FullScreenViewActivity.this,"my.closet.fashion.style.FileProvider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();




    }
}
