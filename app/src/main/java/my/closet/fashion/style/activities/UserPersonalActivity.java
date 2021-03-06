package my.closet.fashion.style.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.adapters.UserFeedsViewHolder;
import my.closet.fashion.style.customs.SpacesItemDecoration;
import my.closet.fashion.style.modesl.BookmarkResponse;
import my.closet.fashion.style.modesl.FeedResponse;
import my.closet.fashion.style.modesl.FollowerFollowing;

import static io.fabric.sdk.android.Fabric.TAG;

public class UserPersonalActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent i;
    private FeedResponse feedResponse_obj;
    FollowerFollowing followerFollowing;
    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference feedRef;
    private ProgressBar prbLoading;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private FirestorePagingAdapter adapter;
    private CircleImageView profile_image;
    private TextView username_txt;
    private TextView follower_count_txt, post_count_txt;
    private TextView toolbar_txt, link_txt, bio_txt;
    private List posts = new ArrayList();
    private List followings = new ArrayList();
    Button follow_btn;
    private String key = "";
    private View Fullscreen_view;
    private Dialog Fullscreen_dialog;
    private ImageView cancel_btn;
    private LinearLayout share_btn, download_btn;
    private ImageView fullimage;
    private RequestOptions requestOptions;
    private Uri myUri;
    private ImageButton unfollow_btn;
    private View unFollow_view;
    private Dialog unfollow_dialog;
    private CircleImageView profile_follow_image;
    private TextView username_follow_txt;
    Button cancel_flw_popup, unflw_popup_btn;
    private String followingkey = "";
    private TextView following_count_txt;
    private List followers = new ArrayList();
    LinearLayout follower_layout,following_layout;
    private BookmarkResponse bookmarkResponse_obj;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pursonal);

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        feedRef = db.collection("CommonFeed");

        i = getIntent();
        if (i != null) {
            if (Objects.requireNonNull(getIntent().getExtras()).containsKey("key")) {

                feedResponse_obj = (FeedResponse) i.getSerializableExtra("key");
                email = feedResponse_obj.getEmail();

            }else if(getIntent().getExtras().containsKey("followerkey")) {

                followerFollowing = (FollowerFollowing) i.getSerializableExtra("follower");
                email = followerFollowing.getEmail();



            }else {

                bookmarkResponse_obj = (BookmarkResponse) i.getSerializableExtra("bookmark");
                email = bookmarkResponse_obj.getEmail();


            }
        }



        findViews();

        getUserData();

        //getFollowers();

       // FollowingQuery();

    }

    private void findViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_txt = (TextView) toolbar.findViewById(R.id.toolbar_txt);
        setSupportActionBar(toolbar);
        ActionBar avy = getSupportActionBar();
        if (avy != null) {
            avy.setTitle(null);
        }
        toolbar.setBackgroundColor(getResources().getColor(R.color.secondarycolor));


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

       // follow_btn = (Button) findViewById(R.id.follow_btn);
       // follow_btn.setText("Follow");

        follower_layout = (LinearLayout) findViewById(R.id.followersection);
        following_layout = (LinearLayout) findViewById(R.id.followingsection);

        follower_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (feedResponse_obj!=null){

                    Intent intent = new Intent(UserPersonalActivity.this,FollowerFollowingViewActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("name",feedResponse_obj.getPenname());
                    startActivity(intent);


                } else if (followerFollowing!=null){

                    Intent intent = new Intent(UserPersonalActivity.this,FollowerFollowingViewActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("name",followerFollowing.getName());
                    startActivity(intent);

                }else {

                    Intent intent = new Intent(UserPersonalActivity.this,FollowerFollowingViewActivity.class);
                    intent.putExtra("key",key);
                    startActivity(intent);


                }

            }
        });

        following_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (feedResponse_obj!=null){

                    Intent intent = new Intent(UserPersonalActivity.this,FollowerFollowingViewActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("name",feedResponse_obj.getPenname());
                    startActivity(intent);


                } else if (followerFollowing!=null){

                    Intent intent = new Intent(UserPersonalActivity.this,FollowerFollowingViewActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("name",followerFollowing.getName());
                    startActivity(intent);

                }else {

                    Intent intent = new Intent(UserPersonalActivity.this,FollowerFollowingViewActivity.class);
                    intent.putExtra("key",key);
                    startActivity(intent);


                }


            }
        });

       // follow_btn.setOnClickListener(this);

        following_count_txt = (TextView) findViewById(R.id.followerss_count_txt);
        follower_count_txt = (TextView) findViewById(R.id.follower_count_txt);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
       // profile_image.setBackgroundResource(R.drawable.ic_user_profile);
       // post_count_txt = (TextView) findViewById(R.id.post_count_txt);
      //  link_txt = (TextView) findViewById(R.id.link_txt);
        bio_txt = (TextView) findViewById(R.id.bio_txt);



        if (feedResponse_obj!=null) {
            Glide.with(UserPersonalActivity.this).load(feedResponse_obj.getProfile()).into(profile_image);

        } else if (followerFollowing!=null){

            Glide.with(UserPersonalActivity.this).load(followerFollowing.getImgname()).into(profile_image);

        }else if(bookmarkResponse_obj!=null) {




        }

        username_txt = (TextView) findViewById(R.id.username_txt);

        if (feedResponse_obj != null) {
            username_txt.setText(feedResponse_obj.getPenname());

        } else if(followerFollowing!= null) {

            username_txt.setText(followerFollowing.getName());

        }else if(bookmarkResponse_obj!=null) {

           // Glide.with(UserPersonalActivity.this).load(bookmarkResponse_obj.getProfileimage()).into(profile_image);


        }

        recyclerView = (RecyclerView) findViewById(R.id.rvVideos);

       // unfollow_btn = (ImageButton) findViewById(R.id.unfollow_btn);
       // unfollow_btn.setOnClickListener(this);

        follower_count_txt.setOnClickListener(this);

      //  postCount();

       // checkFollowers();

    }

    private void postCount() {

        db.collection("CommonFeed")
                .whereEqualTo("email", feedResponse_obj.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        posts.add(document.getId());
                        post_count_txt.setText(String.valueOf(posts.size()));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    private void getUserData() {
        Utilities.showLoading(UserPersonalActivity.this, "Loading");
        db.collection("UsersList")
                .whereEqualTo("Email", email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    Utilities.hideLoading();
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        key = document.getId();
                        username_txt.setText(Objects.requireNonNull(document.get("Pen_Name")).toString());
                        bio_txt.setText(Objects.requireNonNull(document.get("Bio")).toString());
                        //link_txt.setText(Objects.requireNonNull(document.get("Website")).toString());
                        toolbar_txt.setText(Objects.requireNonNull(document.get("Display_Name")).toString());

                        Glide.with(UserPersonalActivity.this)
                                .load(document.get("Profile_Pic"))
                                .into(profile_image);

                        if (key != null && !key.equalsIgnoreCase("")) {
                            FollowerCount();
                            FollowingCount();
                            SetUpRecycleView();
                            //checkFollowers();
                        }

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Utilities.hideLoading();
            }
        });
    }

    private void FollowingCount() {

        db.collection("UsersList")
                .document(key)
                .collection("Followee")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        followings.add(document.get("email"));
                        followingkey = document.getId();
                        following_count_txt.setText(String.valueOf(followings.size()));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void FollowerCount() {
        db.collection("UsersList")
                .document(key).
                collection("FollowCollection").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        followers.add(document.get("email"));
                       // followingkey = document.getId();
                        follower_count_txt.setText(String.valueOf(followers.size()));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

   public void checkFollowers(){

       DocumentReference followreference = db.collection("UsersList")
               .document(key).collection("FollowCollection").document(Utilities.loadPref(UserPersonalActivity.this, "email", ""));

       followreference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

               if (documentSnapshot.exists()) {

                   follow_btn.setText("Following");
                   follow_btn.setBackgroundResource(R.drawable.following_btn);
                   follow_btn.setTextColor(ContextCompat.getColor(UserPersonalActivity.this, R.color.selected_tab));
                   Utilities.Following = true;

               }else {

                   Utilities.Following = false;
                   follow_btn.setText("Follow");
                   follow_btn.setBackgroundResource(R.drawable.follow_btn);
                   follow_btn.setTextColor(ContextCompat.getColor(UserPersonalActivity.this, R.color.white));


               }

           }
       });
   }


    private void FollowingQuery() {

        db.collection("UsersList").document(key).
                collection("FollowCollection").
                whereEqualTo("email",
                        Utilities.loadPref(UserPersonalActivity.this, "email", ""))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    follow_btn.setText("Following");
                    follow_btn.setBackgroundResource(R.drawable.following_btn);
                    follow_btn.setTextColor(ContextCompat.getColor(UserPersonalActivity.this, R.color.selected_tab));
                    Utilities.Following = true;
                } else {

                    Utilities.Following = false;
                    follow_btn.setText("Follow");
                    follow_btn.setBackgroundResource(R.drawable.follow_btn);
                    follow_btn.setTextColor(ContextCompat.getColor(UserPersonalActivity.this, R.color.white));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

                Utilities.Following = false;
                follow_btn.setText("Follow");
                follow_btn.setBackgroundResource(R.drawable.follow_btn);
                follow_btn.setTextColor(ContextCompat.getColor(UserPersonalActivity.this, R.color.white));
            }
        });
    }


    private void SetUpRecycleView() {


        Query query = db.collection("UsersList")
                .document(key)
                .collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING);


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<FeedResponse> options = new FirestorePagingOptions.Builder<FeedResponse>()
                .setLifecycleOwner(this)
                .setQuery(query, config, FeedResponse.class)
                .build();

        adapter = new FirestorePagingAdapter<FeedResponse, UserFeedsViewHolder>(options) {

            @Override
            public UserFeedsViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_feed_item, parent, false);
                return new UserFeedsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserFeedsViewHolder holder,
                                            int position,
                                            final FeedResponse model) {
                holder.bind(UserPersonalActivity.this, model);


                holder.picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent textint = new Intent(UserPersonalActivity.this, UserPersonalFullScreenActivity.class);
                        textint.putExtra("picture", (Serializable) model);
                        startActivity(textint);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                });
            }

            @Override
            protected void onLoadingStateChanged(LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        //   prbLoading.setVisibility(View.VISIBLE);
                        break;
                    case LOADED:
                        //   prbLoading.setVisibility(View.GONE);
                        break;
                    case FINISHED:
                        //  prbLoading.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        retry();
                        //  prbLoading.setVisibility(View.GONE);
                        break;
                }
            }
        };

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));

        recyclerView.setAdapter(adapter);

    }

/*
    private void FullScreen_Popup(final FeedResponse model) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Fullscreen_view = inflater.inflate(R.layout.userpic_dialog, null);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(UserPersonalActivity.this);


        Fullscreen_dialog = new Dialog(this);
        Fullscreen_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Fullscreen_dialog.setContentView(Fullscreen_view);
        Fullscreen_dialog.setCanceledOnTouchOutside(false);
        Fullscreen_dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Fullscreen_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        Fullscreen_dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        Fullscreen_dialog.getWindow().setAttributes(lp);

        cancel_btn = (ImageView) Fullscreen_view.findViewById(R.id.back_button);
        share_btn = (LinearLayout) Fullscreen_view.findViewById(R.id.share_btn);
        download_btn = (LinearLayout) Fullscreen_view.findViewById(R.id.download_btn);
        fullimage = (ImageView) Fullscreen_view.findViewById(R.id.fullimage);


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fullscreen_dialog.dismiss();
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getImage().contains("https")) {
                    ShareAction(model.getImage());
                }
            }
        });

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getImage().contains("https")) {
                    if (Utilities.bookmarked == false) {
                        Bookmarksave(model.getImage());
                    }
                }
            }
        });

        if (!model.getImage().equalsIgnoreCase("")) {
            if (model.getImage().contains("https")) {
                Glide.with(UserPersonalActivity.this).load(model.getImage()).apply(requestOptions).into(fullimage);

            }
        }
        Fullscreen_dialog.show();

    }
*/


    private void Bookmarksave(String imageUri) {

        if (imageUri != null) {
            Map<String, Object> data = new HashMap<>();
            String id = UUID.randomUUID().toString();

            data.put("id", id);
            data.put("image", imageUri);
            data.put("bywhom", username_txt.getText().toString());
            data.put("email", Utilities.loadPref(UserPersonalActivity.this, "email", ""));

            db.collection("Bookmarks").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        Utilities.bookmarked = true;
                        Utilities.showToast(UserPersonalActivity.this, "Saved...");
                    } else {
                        Utilities.showToast(UserPersonalActivity.this, "Try again Later");
                    }
                }
            });
        }
    }

    private void ShareAction(String image) {

        Glide.with(UserPersonalActivity.this)
                .asBitmap()
                .load(image)
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

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {

            File file = new File(Utilities.picturePath, "share_image_" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(UserPersonalActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_btn:
                if (!follow_btn.getText().toString().equalsIgnoreCase("Following")) {
                    if (key != null && !key.equalsIgnoreCase("")) {

                        if (!Utilities.loadPref(UserPersonalActivity.this, "email", "").equalsIgnoreCase("")) {
                            FollowingAction();
                        } else {
                            Intent ii = new Intent(UserPersonalActivity.this, FbGmailActivity.class);
                            startActivity(ii);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                        }
                    }
                }
                break;

           /* case R.id.unfollow_btn:
                if (Utilities.Following) {
                    UnFollowPopup();
                }
                break; */

            case R.id.follower_count_txt:
                /*if (followings.size() > 0) {
                    getFollowers();
                }*/
                break;
        }
    }

    private void getFollowers() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(UserPersonalActivity.this);
        TextView title = new TextView(this);
        title.setText("Followings");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setPadding(10, 20, 10, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(16);
        builderSingle.setCustomTitle(title);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UserPersonalActivity.this, R.layout.simple_textview);
        arrayAdapter.clear();
        arrayAdapter.addAll(followings);


        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builderSingle.create();
        ListView listView = dialog.getListView();
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(new ColorDrawable(Color.parseColor("#EFEFEF")));
        listView.setDividerHeight(1);

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void UnFollowPopup() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        unFollow_view = inflater.inflate(R.layout.unfollow_dialog, null);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(UserPersonalActivity.this);


        unfollow_dialog = new Dialog(this);
        unfollow_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        unfollow_dialog.setContentView(unFollow_view);
        unfollow_dialog.setCanceledOnTouchOutside(false);
        unfollow_dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(unfollow_dialog.getWindow().getAttributes());
        lp.width = (int) (Utilities.screenWidth * 0.80);

        lp.height = (int) (Utilities.screenHeight * 0.35);
        lp.gravity = Gravity.CENTER;


        unfollow_dialog.getWindow().setBackgroundDrawableResource(R.drawable.unfollow_border);
        unfollow_dialog.getWindow().setAttributes(lp);


        profile_follow_image = (CircleImageView) unfollow_dialog.findViewById(R.id.profile_follow_image);
        username_follow_txt = (TextView) unfollow_dialog.findViewById(R.id.username_follow_txt);

        cancel_flw_popup = (Button) unfollow_dialog.findViewById(R.id.cancel_flw_popup);
        unflw_popup_btn = (Button) unfollow_dialog.findViewById(R.id.unflw_popup_btn);


        cancel_flw_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unfollow_dialog.isShowing()) {
                    unfollow_dialog.dismiss();
                }
            }
        });

        unflw_popup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (followingkey != null && !followingkey.equalsIgnoreCase("")) {
                    UnfollowAction();
                }
            }
        });

        if (feedResponse_obj.getProfile() != null && feedResponse_obj.getProfile().contains("http")) {
            Glide.with(UserPersonalActivity.this).load(feedResponse_obj.getProfile()).into(profile_follow_image);
        } else {
            profile_follow_image.setBackgroundResource(R.drawable.ic_user_profile);
        }

        if (feedResponse_obj.getPenname() != null) {
            // Spanned styledText = Html.fromHtml("Unfollow " + "<b>"+ feedResponse_obj.getPenname() +"</b>");
            username_follow_txt.setText(username_txt.getText().toString());
        }
        if (!unfollow_dialog.isShowing()) {
            unfollow_dialog.show();
        }

    }


    private void UnfollowAction() {
        if (!Utilities.loadPref(UserPersonalActivity.this, "email", "").equalsIgnoreCase(feedResponse_obj.getEmail())) {

            db.collection("UsersList").
                    document(key).collection("FollowCollection").document(followingkey)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            if (unfollow_dialog.isShowing()) {
                                unfollow_dialog.dismiss();
                            }

                            Utilities.showToast(UserPersonalActivity.this, "Unfollwed");
                            Intent login_Intent = new Intent(UserPersonalActivity.this, HomeActivity.class);
                            startActivity(login_Intent);
                            finish();
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Utilities.showToast(UserPersonalActivity.this, "Try Later");
                        }
                    });

        }

    }


    private void FollowingAction() {

        if (!Utilities.loadPref(UserPersonalActivity.this, "email", "").equalsIgnoreCase(feedResponse_obj.getEmail())) {

            Map<String, Object> data = new HashMap<>();
            String id = UUID.randomUUID().toString();

            data.put("id", id);
            data.put("email", Utilities.loadPref(UserPersonalActivity.this, "email", ""));
            db.collection("UsersList").document(key).collection("FollowCollection").add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(Task<DocumentReference> task) {
                            if (task.isComplete()) {

                                //follow_btn.setText("Following");
                                //follow_btn.setBackgroundResource(R.drawable.following_btn);
                                //follow_btn.setTextColor(ContextCompat.getColor(UserPersonalActivity.this, R.color.selected_tab));

                                //follow_btn.setEnabled(false);
                            } else {
                                Utilities.showToast(UserPersonalActivity.this, "Try again later");
                            }
                        }
                    });
        }
    }


}
