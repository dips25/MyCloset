package my.closet.fashion.style.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.fragments.MyProfileFragment;
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
    private TextView username_txt;
    private TextView tags_txt;
    private TagContainerLayout selectedTags;
    private CircleImageView profile_pic;
    private LinearLayout profile_layout;
    private Button follow_btn;
    private String key="";
    private FragmentTransaction transaction;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);


        manager = getSupportFragmentManager();
        mixpanelAPI= MixpanelAPI.getInstance(FullScreenViewActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        mStorage = FirebaseStorage.getInstance().getReference();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        myemail = Utilities.loadPref(FullScreenViewActivity.this, "email", "");

        My_DbKey = Utilities.loadPref(FullScreenViewActivity.this, "My_DbKey", "");
        userCollection = FirebaseFirestore.getInstance();
        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        i = getIntent();
        if (i != null) {
            feedResponse_obj = (FeedResponse) i.getSerializableExtra("position");
            if (feedResponse_obj.getImage() != null) {
                imageUri = feedResponse_obj.getImage();
            }

            if (feedResponse_obj.getDocumentId() != null && !feedResponse_obj.getDocumentId().equalsIgnoreCase("")) {
                blogPostId = feedResponse_obj.getDocumentId();
            }
        }

        findViews();
    }

    private void findViews() {

        progressBar = (ProgressBar) findViewById(R.id.connectProgress);

        blog_comment_btn = (ImageView) findViewById(R.id.blog_comment_btn);
        blog_comment_btn.setOnClickListener(this);

        blog_bookmark_btn = (ImageView) findViewById(R.id.blog_bookmark_btn);
        // blog_bookmark_btn.setOnClickListener(this);

        blog_like_btn = (ImageView) findViewById(R.id.blog_like_btn);
        //  blog_like_btn.setOnClickListener(this);


        fullimage = (ImageView) findViewById(R.id.fullimage);
        no_of_likes = (TextView) findViewById(R.id.no_of_likes);
        no_of_likes.setOnClickListener(this);

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
        tags_txt = (TextView) findViewById(R.id.tags_txt);
        selectedTags = (TagContainerLayout) findViewById(R.id.selectedTags);
        selectedTags.setTheme(ColorFactory.NONE);
        selectedTags.setTagBackgroundColor(Color.TRANSPARENT);
        selectedTags.setBorderColor(Color.TRANSPARENT);


        profile_pic = (CircleImageView) findViewById(R.id.profile_pic);
        profile_pic.setBackgroundResource(R.drawable.ic_user_profile);


        profile_layout = (LinearLayout) findViewById(R.id.profile_layout);
        profile_layout.setOnClickListener(this);

        follow_btn = (Button) findViewById(R.id.follow_btn);
        follow_btn.setText("Follow");
        follow_btn.setOnClickListener(this);
        follow_btn.setVisibility(View.GONE);

        if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase("")) {
            if (!feedResponse_obj.getEmail().equalsIgnoreCase("") && feedResponse_obj.getEmail() != null) {
                getFeedKey();
            }

        }

        if (feedResponse_obj.getEmail() != null && !feedResponse_obj.getEmail().equalsIgnoreCase("")) {
            if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase("")) {

                if (feedResponse_obj.getEmail().equalsIgnoreCase(Utilities.loadPref(FullScreenViewActivity.this, "email", ""))) {
                    follow_btn.setVisibility(View.GONE);
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
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                        no_of_likes.setText(String.valueOf(likeCount) + " likes");
                    }
                }
            });
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

                        MyProfileFragment myProfileFragment = new MyProfileFragment();
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.container, myProfileFragment);
                        transaction.addToBackStack("MyProfileFragment");
                        transaction.commit();

                    } else {
                        if (key != null) {
                            Intent ii = new Intent(FullScreenViewActivity.this, UserProfileActivity.class);
                            ii.putExtra("key", (Serializable) feedResponse_obj);
                            startActivity(ii);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                        }
                    }
                } else {
                    Utilities.showToast(FullScreenViewActivity.this, "Please Login to continue");
                }
                break;


        }
    }


    private void getFeedKey() {
        progressBar.setVisibility(View.VISIBLE);

        userCollection.collection("UsersList")
                .whereEqualTo("Email", feedResponse_obj.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    for (DocumentSnapshot document : task.getResult()) {

                        key = document.getId();

                        if (document.get("Profile_Pic") != null && !document.get("Profile_Pic").toString().equalsIgnoreCase("")) {
                            Glide.with(FullScreenViewActivity.this).load(document.get("Profile_Pic")).apply(requestOptions).into(profile_pic);
                        }

                        if (document.get("Pen_Name") != null && !document.get("Pen_Name").toString().equalsIgnoreCase("")) {
                            if (document.get("Pen_Name").toString().equalsIgnoreCase(Utilities.loadPref(FullScreenViewActivity.this, "Pen_Name", ""))) {
                                username_txt.setText("You");
                            } else {
                                username_txt.setText(document.get("Pen_Name").toString());
                            }
                        }

                        if (key != null && !key.equalsIgnoreCase("")) {

                            DocumentReference followreference = userCollection.collection("UsersList")
                                    .document(key).collection("FollowCollection").document(myemail);
                            followreference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                                    if (e != null) {
                                      //  Log.w(TAG, "listen:error", e);
                                        return;
                                    }
                                    if (documentSnapshot.exists()) {
                                        follow_btn.setText("Following");
                                        //follow_btn.setBackgroundResource(R.drawable.following_btn);
                                        follow_btn.setTextColor(ContextCompat.getColor(FullScreenViewActivity.this, R.color.selected_tab));
                                    } else {
                                        follow_btn.setText("Follow");
                                        follow_btn.setBackgroundResource(R.drawable.follow_btn);
                                        follow_btn.setTextColor(ContextCompat.getColor(FullScreenViewActivity.this, R.color.white));
                                    }
                                }
                            });
                        }

                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                   // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void Likepost() {
        userCollection.collection("Feeds/" + blogPostId + "/Likes").document(myemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {

                        Map<String, Object> data = new HashMap<>();
                        String id = UUID.randomUUID().toString();

                        data.put("id", id);
                        data.put("email", Utilities.loadPref(FullScreenViewActivity.this, "email", ""));

                        userCollection.collection("Feeds/" + blogPostId + "/Likes").document(myemail).set(data);

                    } else {
                        userCollection.collection("Feeds/" + blogPostId + "/Likes").document(myemail).delete();
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
            userCollection.collection("UsersList/" + My_DbKey + "/Bookmarks").
                    document(blogPostId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {

                            Map<String, Object> data = new HashMap<>();
                            String id = UUID.randomUUID().toString();

                            data.put("id", id);
                            data.put("image", imageUri);
                            data.put("email", feedResponse_obj.getEmail());
                            userCollection.collection("UsersList/" + My_DbKey + "/Bookmarks").document(blogPostId).set(data);
                        } else {
                            userCollection.collection("UsersList/" + My_DbKey + "/Bookmarks").document(blogPostId).delete();
                        }
                    } else {
                        Log.i("LikeError", task.getException().getMessage());
                        Toast.makeText(FullScreenViewActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    private void FollowingAction() {

        if (!Utilities.loadPref(FullScreenViewActivity.this, "email", "").equalsIgnoreCase(feedResponse_obj.getEmail())) {
            userCollection.collection("UsersList/" + key + "/FollowCollection").document(myemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {

                            Map<String, Object> data = new HashMap<>();
                            String id = UUID.randomUUID().toString();

                            data.put("id", id);
                            data.put("email", Utilities.loadPref(FullScreenViewActivity.this, "email", ""));

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
                                i.putExtra(Intent.EXTRA_TEXT, "Get nice Quotes and Thoughts from Thought Writer App.Click Here to know more." + "https://play.google.com/store/apps/details?id=thought.writer.quotes");
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
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(FullScreenViewActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);
    }
}
