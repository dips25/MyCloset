package my.closet.fashion.style.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.adapters.BottomSheetViewAdapter;
import my.closet.fashion.style.customs.IconizedMenu;
import my.closet.fashion.style.modesl.BookmarkResponse;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.FeedResponse;
import xyz.hanks.library.bang.SmallBangView;

public class PictureDeletingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView, delete_icon,menu;
    ImageView share_icon;
    private Intent i;
    private String picture_path;
    private Uri bmpUri;
    private FeedResponse feedResponse_obj;
    private RequestOptions requestOptions;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String key="";
    private Toolbar toolbar;
    private IconizedMenu popup;
    private TagContainerLayout selectedTags;
    private TextView tags_txt;
    private String My_DbKey="";
    private MixpanelAPI mixpanelAPI;
    BookmarkResponse bookmarkResponse_obj;
    private SmallBangView like_text, bookmark_anim;


    String bookmark_dockey;
    private CircleImageView profile_pic;
    private TextView username_txt;
    private TextView description;

    private ArrayList<String> data=new ArrayList<>();
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
    private Realm realm;
    private GridView recyclerView;
    private RelativeLayout profile_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_deleting);

        android.transition.Fade fade = new android.transition.Fade();
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        My_DbKey = Utilities.loadPref(PictureDeletingActivity.this, "My_DbKey", "");



        mixpanelAPI= MixpanelAPI.getInstance(PictureDeletingActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");
        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);




        i = getIntent();
        if (i != null && Objects.requireNonNull(i.getExtras()).containsKey("picture")) {
            feedResponse_obj = (FeedResponse) i.getSerializableExtra("picture");
            picture_path=feedResponse_obj.getImage();
        }else{

            bookmarkResponse_obj = (BookmarkResponse) i.getSerializableExtra("bookmark");
            picture_path = bookmarkResponse_obj.getImage();


        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar avy = getSupportActionBar();
        Objects.requireNonNull(avy).setTitle(null);

        toolbar.setNavigationIcon(R.drawable.ic_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        imageView = (ImageView) findViewById(R.id.image);

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        profile_layout = (RelativeLayout) findViewById(R.id.dprofile_layout);

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(PictureDeletingActivity.this, UserPersonalActivity.class);
                ii.putExtra("bookmark", (Serializable) bookmarkResponse_obj);
                startActivity(ii);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                
            }
        });
        
        
        
        

        profile_pic = (CircleImageView) findViewById(R.id.dprofile_pic);
        profile_pic.setBackgroundResource(R.drawable.ic_user_profile);

        recyclerView = (GridView) findViewById(R.id.dmatching_recycler);
        tags_txt = (TextView) findViewById(R.id.dmatching_clothes_heading);

        tags_txt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);


        // tags_txt = (TextView) findViewById(R.id.tags_txt);
        selectedTags = (TagContainerLayout) findViewById(R.id.selectedTags);
        selectedTags.setTheme(ColorFactory.NONE);
        selectedTags.setTagBackgroundColor(Color.TRANSPARENT);
        selectedTags.setBorderColor(Color.TRANSPARENT);
        description = (TextView) findViewById(R.id.dpostdescription);

       // like_text = findViewById(R.id.dlike_heart);
       // bookmark_anim = findViewById(R.id.dbookmark_anim);

       // like_text.setOnClickListener(this);

        share_icon = (ImageView) findViewById(R.id.dblog_comment_btn);
        username_txt = (TextView) findViewById(R.id.dusername_txt);

        share_icon.setOnClickListener(this);

        if (picture_path != null) {

                Glide.with(PictureDeletingActivity.this).load(picture_path).apply(requestOptions).into(imageView);
            }



      /*  if (feedResponse_obj.getText() != null) {
            selectedTags.setTags(feedResponse_obj.getText());
        }

        if (feedResponse_obj.getCaption() != null) {
            tags_txt.setText(feedResponse_obj.getCaption());
        } */

        if(Utilities.MyTab){
            getMytabDetails();
        }else {
            getLikes();

        }
    }

    private void getMytabDetails() {

        String imgname = Utilities.loadPref(PictureDeletingActivity.this,"Profile_Pic","");
        String name = Utilities.loadPref(PictureDeletingActivity.this,"Pen_Name","");

        if (imgname!=null && !imgname.equals("")){

            Glide.with(PictureDeletingActivity.this).load(imgname).apply(requestOptions).into(profile_pic);


        }

        if (name!=null){

            username_txt.setText(name);

        }

        if (feedResponse_obj.getDescription()!=null && !feedResponse_obj.getDescription().equals("")){

            description.setText(feedResponse_obj.getDescription());

        }else {

            description.setVisibility(View.GONE);
        }







    }

    private void getLikes() {

        if (bookmarkResponse_obj.getDescription()!=null && !bookmarkResponse_obj.getDescription().equals("")){

            description.setText(bookmarkResponse_obj.getDescription());
        }else {

            description.setVisibility(View.GONE);
        }

        db.collection("UsersList").whereEqualTo("Email",bookmarkResponse_obj.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        bookmark_dockey = documentSnapshot.getId();

                        String profpic = Objects.requireNonNull(documentSnapshot.get("Profile_Pic")).toString();

                        if (profpic!=null && !profpic.equals("")){

                            Glide.with(PictureDeletingActivity.this).load(profpic).apply(requestOptions).into(profile_pic);


                        }

                        if (documentSnapshot.get("Pen_Name") != null && !documentSnapshot.get("Pen_Name").toString().equalsIgnoreCase("")) {
                            if (documentSnapshot.get("Pen_Name").toString().equalsIgnoreCase(Utilities.loadPref(PictureDeletingActivity.this, "Pen_Name", ""))) {
                                username_txt.setText("You");
                            } else {
                                username_txt.setText(Objects.requireNonNull(documentSnapshot.get("Pen_Name")).toString());

                            }
                        }


                    }


                    db.collection("UsersList").document(bookmark_dockey).collection("MetaData").whereEqualTo("lookid", bookmarkResponse_obj.getLookid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.menu:
                Edit_Popup(v);
                break;

            case R.id.share_icon:
                mixpanelAPI.track("Profiletab Share");
                if (picture_path != null) {
                    Utilities.showToast(PictureDeletingActivity.this, "Please Wait,sharing.....");
                    shareImage(picture_path);
                }
                break;


        }
    }

    private void likeAction() {

        if (!Utilities.MyTab)

        db.collection("CommonFeed").document(bookmark_dockey).collection("Likes")
                .document(Utilities.loadPref(PictureDeletingActivity.this,"email",""))
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    if (Objects.requireNonNull(task.getResult()).exists()){

                        db.collection("UsersList").document(bookmark_dockey).collection("Likes")
                                .document(Utilities.loadPref(PictureDeletingActivity.this,"email",""))
                                .delete();
                    }else {

                        Map<String, Object> data = new HashMap<>();
                        String id = UUID.randomUUID().toString();

                        data.put("id", id);
                        data.put("email", Utilities.loadPref(PictureDeletingActivity.this, "email", ""));

                        db.collection("CommonFeed/" + bookmark_dockey + "/Likes")
                                .document(Utilities.loadPref(PictureDeletingActivity.this,"email",""))
                                .set(data);


                    }
                }

            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_edit_feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_edit:
                // do your code
                break;
            case R.id.profile_delete:
                if (picture_path != null) {
                    final AlertDialog.Builder alert_Dialog = new AlertDialog.Builder(PictureDeletingActivity.this);
                    alert_Dialog.setCancelable(false);
                    alert_Dialog.setTitle("Alert");
                    alert_Dialog.setMessage("Do you want to delete ?");
                    alert_Dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (Utilities.MyTab) {
                                getFeedKey();
                            } else {
                                getBookmarkKey();
                            }
                            dialog.dismiss();

                        }
                    });

                    alert_Dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert_Dialog.show();
                }

                break;
        }

        return  super.onOptionsItemSelected(item);
    }
*/


    public void Edit_Popup(View v) {
        popup = new IconizedMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.delete_edit_feed_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.profile_edit:

                        break;

                    case R.id.profile_delete:
                        mixpanelAPI.track("Delete");
                        if (picture_path != null) {
                            final AlertDialog.Builder alert_Dialog = new AlertDialog.Builder(PictureDeletingActivity.this);
                            alert_Dialog.setCancelable(false);
                            alert_Dialog.setMessage(R.string.deletequery);
                            alert_Dialog.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(Utilities.MyTab){
                                        getFeedKey();
                                    }else {
                                        getBookmarkKey();
                                    }
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



    private void shareImage(final String picture_path) {

        if (picture_path.contains("http")) {
            Glide.with(PictureDeletingActivity.this)
                    .asBitmap()
                    .load(picture_path)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            bmpUri = getLocalBitmapUri(resource);
                            if (bmpUri != null) {
                                Utilities.hideLoading();
                                Intent i = new Intent(Intent.ACTION_SEND);

                                ///write description of closet app
                                i.putExtra(Intent.EXTRA_TEXT, "Get nice Quotes and Thoughts from Thought Writer App.Click Here to know more." + "https://play.google.com/store/apps/details?id=thought.writer.quotes");
                                i.setType("image/*");
                                i.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                startActivity(Intent.createChooser(i, "Share Image"));
                            }

                        }
                    });
        }
    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {

            File file = new File(Utilities.picturePath, "share_image_" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(PictureDeletingActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;


    }

    private void getFeedKey() {

        db.collection("UsersList").document(My_DbKey).collection("Feed").whereEqualTo("id",feedResponse_obj.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot : task.getResult()){

                        String fkey = documentSnapshot.getId();
                        if (fkey!=null){

                            db.collection("UsersList").document(My_DbKey).collection("Feed").document(fkey).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {

                                    if(task.isSuccessful()){

                                        db.collection("UsersList").document(My_DbKey)
                                                .collection("Posts")
                                                .whereEqualTo("id",feedResponse_obj.getId())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(Task<QuerySnapshot> task) {

                                                        if (task.isSuccessful()){

                                                            for (DocumentSnapshot documentSnapshot1:task.getResult().getDocuments()){


                                                                db.collection("UsersList").document(My_DbKey)
                                                                        .collection("Posts")
                                                                        .document(documentSnapshot1.getId())
                                                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {




                                                                    }
                                                                });


                                                            }

                                                            db.collection("UsersList")
                                                                    .document(My_DbKey)
                                                                    .collection("FollowCollection")
                                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(Task<QuerySnapshot> task) {

                                                                    if (task.isSuccessful()){

                                                                        for (DocumentSnapshot documentSnapshot1 : task.getResult().getDocuments()){

                                                                            db.collection("UsersList")
                                                                                    .whereEqualTo("Email",documentSnapshot1.get("email"))
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(Task<QuerySnapshot> task) {

                                                                                            if (task.isSuccessful()){

                                                                                                for (DocumentSnapshot documentSnapshot2 : task.getResult().getDocuments()){

                                                                                                    db.collection("UsersList")
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
                                                                        Utilities.showToast(PictureDeletingActivity.this, getResources().getString(R.string.postdelete));
                                                                        Intent login_Intent = new Intent(PictureDeletingActivity.this, HomeActivity.class);
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

    private void getBookmarkKey() {

        db.collection("UsersList/" + My_DbKey + "/Bookmarks")
                .whereEqualTo("id", bookmarkResponse_obj.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        key = document.getId();

                        if (key != null && !key.equalsIgnoreCase("")) {
                            DeletePathBookmarks();
                        }
                    }
                } else {
                   // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Utilities.hideLoading();
            }
        });
    }

    private void DeletePathBookmarks() {
        db.collection("UsersList/" + My_DbKey + "/Bookmarks").document(key)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Utilities.showToast(PictureDeletingActivity.this, getResources().getString(R.string.postdelete));
                        Intent login_Intent = new Intent(PictureDeletingActivity.this, HomeActivity.class);
                        startActivity(login_Intent);
                        finish();
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Utilities.showToast(PictureDeletingActivity.this, "Try Later");
                    }
                });
    }


    private void DeletePathFeed() {

        db.collection("CommonFeed").document(key)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Utilities.showToast(PictureDeletingActivity.this, getResources().getString(R.string.postdelete));
                        Intent login_Intent = new Intent(PictureDeletingActivity.this, HomeActivity.class);
                        startActivity(login_Intent);
                        finish();
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Utilities.showToast(PictureDeletingActivity.this, "Try Later");
                    }
                });
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



        BottomSheetViewAdapter bottomSheetViewAdapter = new BottomSheetViewAdapter(PictureDeletingActivity.this,R.layout.card_accessories,dressesArrayList);
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
        finish();
        super.onBackPressed();


        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
