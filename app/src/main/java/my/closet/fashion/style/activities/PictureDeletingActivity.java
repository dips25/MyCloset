package my.closet.fashion.style.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.customs.IconizedMenu;
import my.closet.fashion.style.modesl.FeedResponse;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_deleting);

        My_DbKey = Utilities.loadPref(PictureDeletingActivity.this, "My_DbKey", "");


        mixpanelAPI= MixpanelAPI.getInstance(PictureDeletingActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");
        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);


        i = getIntent();
        if (i != null) {
            feedResponse_obj = (FeedResponse) i.getSerializableExtra("picture");
            picture_path=feedResponse_obj.getImage();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar avy = getSupportActionBar();
        avy.setTitle(null);

        toolbar.setNavigationIcon(R.drawable.ic_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);
            }
        });
        imageView = (ImageView) findViewById(R.id.image);

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        tags_txt = (TextView) findViewById(R.id.tags_txt);
        selectedTags = (TagContainerLayout) findViewById(R.id.selectedTags);
        selectedTags.setTheme(ColorFactory.NONE);
        selectedTags.setTagBackgroundColor(Color.TRANSPARENT);
        selectedTags.setBorderColor(Color.TRANSPARENT);

        share_icon = (ImageView) findViewById(R.id.share_icon);

        share_icon.setOnClickListener(this);

        if (picture_path != null) {
            if (feedResponse_obj.getImage().contains("https")) {
                Glide.with(PictureDeletingActivity.this).load(feedResponse_obj.getImage()).apply(requestOptions).into(imageView);
            }

        }

        if (feedResponse_obj.getText() != null) {
            selectedTags.setTags(feedResponse_obj.getText());
        }

        if (feedResponse_obj.getCaption() != null) {
            tags_txt.setText(feedResponse_obj.getCaption());
        }
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
                            alert_Dialog.setTitle("Alert");
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
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(PictureDeletingActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;


    }

    private void getFeedKey() {

        db.collection("UsersList/" + My_DbKey + "/Posts")
                .whereEqualTo("id", feedResponse_obj.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                       String  mkey = document.getId();

                        if (mkey != null && !mkey.equalsIgnoreCase("")) {

                            db.collection("UsersList/" + My_DbKey + "/Posts").document(mkey)
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                }


            }
        });
        db.collection("CommonFeed")
                .whereEqualTo("id", feedResponse_obj.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        key = document.getId();

                        if (key != null && !key.equalsIgnoreCase("")) {
                            DeletePathFeed();
                        }
                    }
                } else {
                  //  Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utilities.hideLoading();
            }
        });
    }

    private void getBookmarkKey() {

        db.collection("UsersList/" + My_DbKey + "/Bookmarks")
                .whereEqualTo("id", feedResponse_obj.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
            public void onFailure(@NonNull Exception e) {
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

                        Utilities.showToast(PictureDeletingActivity.this, "Deleted");
                        Intent login_Intent = new Intent(PictureDeletingActivity.this, HomeActivity.class);
                        startActivity(login_Intent);
                        finish();
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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

                        Utilities.showToast(PictureDeletingActivity.this, "Deleted");
                        Intent login_Intent = new Intent(PictureDeletingActivity.this, HomeActivity.class);
                        startActivity(login_Intent);
                        finish();
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utilities.showToast(PictureDeletingActivity.this, "Try Later");
                    }
                });
    }

}
