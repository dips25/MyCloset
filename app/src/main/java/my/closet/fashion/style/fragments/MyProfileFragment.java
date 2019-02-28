package my.closet.fashion.style.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.activities.FbGmailActivity;
import my.closet.fashion.style.activities.PostFeedActivity;
import my.closet.fashion.style.activities.UserProfileActivity;
import my.closet.fashion.style.adapters.ViewPagerAdapter;
import my.closet.fashion.style.modesl.FBGmailData;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener {

    View view;
    private MixpanelAPI mixpanelAPI;
    private RequestOptions requestOptions;
    private FirebaseFirestore profCollection;
    private FrameLayout mContainer;
    private CircleImageView profile_pic;
    private TextView username_txt;
    private Button edit_profile_btn;
    private TextView bio_txt;
    private TextView post_count_txt;
    private TextView follower_count_txt;
    private ViewPager frame;
    private TabLayout tabLayout;
    private String key = "";
    private List posts = new ArrayList();
    private List followings = new ArrayList();
    private FBGmailData fbGmailData;
    private ArrayList<Object> langlist=new ArrayList<>();
    private TextView toolbar_title;
    private static final int REQUEST_CHOOSE_PHOTO = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 9;
    private File destination;
    private Uri selectedImageUri;
    private Bitmap imageBitmap;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        mixpanelAPI= MixpanelAPI.getInstance(getActivity(),"257c7d2e1c44d7d1ab6105af372f65a6");
        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        profCollection = FirebaseFirestore.getInstance();

         findView(view);

         return  view;
    }

    private void findView(View view) {
        toolbar_title = (TextView) getActivity().findViewById(R.id.title);
        toolbar_title.setText(R.string.profile);

        profile_pic = (CircleImageView) view.findViewById(R.id.profile_pic);
        username_txt = (TextView) view.findViewById(R.id.username_txt);

        edit_profile_btn = (Button) view.findViewById(R.id.edit_profile_btn);
        edit_profile_btn.setOnClickListener(this);


        mContainer = (FrameLayout) getActivity().findViewById(R.id.container);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mContainer.getLayoutParams();
        params.setBehavior(null);
        mContainer.requestLayout();


        bio_txt = (TextView) view.findViewById(R.id.bio_txt);
        post_count_txt = (TextView) view.findViewById(R.id.post_count_txt);
        follower_count_txt = (TextView) view.findViewById(R.id.follower_count_txt);

        frame = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager();

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(frame);

        if (!Utilities.loadPref(getActivity(), "email", "").equalsIgnoreCase("")) {
            LoginUsingEmail();
            postCount();
        } else {

        }
    }

    private void postCount() {

        profCollection.collection("Feeds")
                .whereEqualTo("email", Utilities.loadPref(getActivity(), "email", ""))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void LoginUsingEmail() {

        Utilities.showLoading(getActivity(), "Please Wait...");

        profCollection.collection("UsersList")
                .whereEqualTo("Email", Utilities.loadPref(getActivity(), "email", ""))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    Utilities.hideLoading();
                    for (DocumentSnapshot document : task.getResult()) {

                        fbGmailData = new FBGmailData();
                        fbGmailData.setEmail(document.get("Email").toString());
                        fbGmailData.setName(document.get("Pen_Name").toString());
                        fbGmailData.setPicture(document.get("Profile_Pic").toString());
                        fbGmailData.setId(document.getId());

                        key = document.getId();
                        username_txt.setText(document.get("Pen_Name").toString());

                        if(!document.get("Profile_Pic").toString().equalsIgnoreCase("")) {
                            Glide.with(getActivity()).load(document.get("Profile_Pic")).apply(requestOptions).into(profile_pic);

                        }else {
                            profile_pic.setBackgroundResource(R.drawable.ic_user_profile);
                        }

                        bio_txt.setText(document.get("Bio").toString());
                        Utilities.savePref(getActivity(), "Profile_Pic", document.get("Profile_Pic").toString());

                        Utilities.savePref(getActivity(), "My_DbKey",key);
                       /*if (!document.get("Language").toString().equalsIgnoreCase("")) {

                            langlist= (ArrayList<Object>) document.get("Language");

                        }
                        Utilities.savelist(getActivity(), "Languages", langlist);*/
                        if (key != null && !key.equalsIgnoreCase("")) {
                            FollowerCount();
                        }

                    }
                } else {
                    Utilities.hideLoading();
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utilities.hideLoading();
            }
        });
    }

    private void FollowerCount() {
        CollectionReference likeCollectionRef = profCollection.collection("UsersList")
                .document(key).collection("FollowCollection");
        likeCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }
                if (documentSnapshots.isEmpty()) {
                } else {
                    int likeCount = documentSnapshots.size();
                    follower_count_txt.setText(String.valueOf(likeCount));
                }
            }
        });

    }

    private void setupViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new MyPicsFragment(), "My Uploads");
        adapter.addFrag(new BookmarkFragment(), "Bookmarks");
        frame.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile_btn:
                mixpanelAPI.track("FBandGoogle or Profile");
                if (!Utilities.loadPref(getActivity(), "email", "").equalsIgnoreCase("")) {
                    Intent ii = new Intent(getActivity(), UserProfileActivity.class);
                    ii.putExtra("LoginData", fbGmailData);
                    startActivity(ii);
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);

                } else {
                    Intent ii = new Intent(getActivity(), FbGmailActivity.class);
                    startActivity(ii);
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                }
                break;
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
        menu.findItem(R.id.filter).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.upload_menu:

                if (!Utilities.loadPref(getActivity(), "email", "").equalsIgnoreCase("")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    intent = Intent.createChooser(intent, "Open");
                    startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);

                }else {
                    Intent ii = new Intent(getActivity(), FbGmailActivity.class);
                    startActivity(ii);
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                }
                return true;
            case R.id.action_search:

                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {

            selectedImageUri = data.getData();
            String selectedImagePath = "";
            if (requestCode == 1) {
                selectedImagePath = getPath(selectedImageUri, "Photo");
            } else {
                imageBitmap = (Bitmap) data.getExtras().get("data");
            }

            selectedImageUri = Uri.fromFile(new File(selectedImagePath));

            Intent newIntent = new Intent(getActivity(), PostFeedActivity.class);
            newIntent.putExtra("Bitmap", selectedImageUri.toString());
            getActivity().startActivity(newIntent);
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
