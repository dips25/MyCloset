package my.closet.fashion.style.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.realm.Realm;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
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
    private PhotoEditorView mPhotoEditorView;
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


        //metaid = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
        //looks = realm.where(Looks.class).equalTo("id",metaid).findFirst();
        //Toast.makeText(this, looks.getIdint().toString(), Toast.LENGTH_SHORT).show();


        if (i != null ) {

        }

        My_DbKey=Utilities.loadPref(PostFeedActivity.this, "My_DbKey", "");

        findViews();


    }

    private void findViews() {

        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean post_tut = sharedPreferences.getBoolean("post",true);


        mPhotoEditorView = (PhotoEditorView) findViewById(R.id.photoEditorView);

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


        mPhotoEditor = new PhotoEditor.Builder(PostFeedActivity.this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();

        if (b!=null && b.containsKey("Bitmap")){

            bitmap_obj = i.getStringExtra("Bitmap");

            if (bitmap_obj != null && !bitmap_obj.equalsIgnoreCase("")) {

                Glide.with(PostFeedActivity.this)
                        .asBitmap()
                        .load(bitmap_obj.toString()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mPhotoEditorView.getSource().setImageBitmap(resource);
                    }
                });
            }

        }else {

            bitmap_lookbook_obj = i.getStringExtra("Lookbook");


            if (bitmap_lookbook_obj!=null) {
                Bitmap bmp = new ImageSaver(PostFeedActivity.this).setFileName(bitmap_lookbook_obj).setDirectoryName("mycloset").load();
                mPhotoEditorView.getSource().setImageBitmap(bmp);
               // bitmap_obj = getFilePath(bitmap_obj);
                baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG,100,baos);
                data = baos.toByteArray();


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
                        SendPicturetoDB();
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
        final StorageReference ref = storageRef.child(new StringBuilder("images/").append(UUID.randomUUID().toString()).toString());
        if (bitmap_obj!=null) {
            uploadTask = ref.putFile(Uri.parse(bitmap_obj));
        }else {

            uploadTask = ref.putBytes(data);
        }
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {

                    // downloaded_url will retrive the downloaadlink of imgae url
                    downloaded_url = task.getResult().toString();

                    if (downloaded_url != null) {

                        Long tsLong = System.currentTimeMillis() / 1000;
                        String timestamp = tsLong.toString();

                        final Map<String, Object> data = new HashMap<>();
                        final String id = UUID.randomUUID().toString();

                        data.put("id", id);
                        data.put("lookid", Objects.requireNonNull(i.getExtras()).getInt("lookid"));
                        data.put("image", downloaded_url);
                        data.put("description", description_text.getText().toString());
                        data.put("timestamp", timestamp);
                        data.put("email", Utilities.loadPref(PostFeedActivity.this, "email", ""));
                        data.put("languages", "");
                        data.put("dbkey",My_DbKey);
                        //data.put("catagory",)

                        profCollection.collection("CommonFeed").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {

                                if (task.isSuccessful()){

                                   // profCollection.collection("CommonFeed").document(id).collection("Metadata").document().set(looks);


                                }
                            }
                        });

                        profCollection.collection("UsersList").document(My_DbKey).collection("Feed").add(data);

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
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);




                                } else {
                                    DeletePath();
                                    Utilities.hideLoading();
                                    Utilities.showToast(PostFeedActivity.this, "Try again Later");
                                }
                            }
                        });
                    }

                } else {
                    DeletePath();
                    Utilities.hideLoading();
                    Toast.makeText(PostFeedActivity.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}
