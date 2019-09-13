package my.closet.fashion.style;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.shashi.mysticker.BitmapStickerIcon;
import com.shashi.mysticker.DeleteIconEvent;
import com.shashi.mysticker.DrawableSticker;
import com.shashi.mysticker.Sticker;
import com.shashi.mysticker.StickerView;
import com.shashi.mysticker.ZoomIconEvent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.activities.HomeActivity;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.customs.StickerImageView;
import my.closet.fashion.style.modesl.Colors;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.Looks;
import nl.dionsegijn.konfetti.KonfettiView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


/**
 * Created by biswa on 10/3/2017.
 */

public class CustomView extends AppCompatActivity {



    int i;
    Realm realm;
    int id;
    int[] primitive;
    CheckBox checkBox;

    Bitmap bitmaparray[]=new Bitmap[10];

    ArrayList<Integer> imagearray=new ArrayList<>();
    ArrayList<Integer> toparray=new ArrayList<>();
    ArrayList<Integer> bottomarray=new ArrayList<>();
    ArrayList<Integer> footarray=new ArrayList<>();

    ArrayList<Dresses> dresses = new ArrayList<>();

    StickerView frameLayout;
    StickerImageView stickerImageView1;
    StickerImageView stickerImageView2;
    StickerImageView stickerImageView3;
    StickerImageView stickerImageView4;
    ImageView img;
    EditText look_name;
    String imagename;
    int n;
    private MixpanelAPI mixpanelAPI;
    StickerImageView stickerImageView5;
    StickerImageView stickerImageView6;
    HashMap<String,Object> valuemap = new HashMap<>();
    KonfettiView konfettiView;


    ArrayList<Integer> finaldress=new ArrayList<>();

    int metaid;
    UploadTask uploadTask;



    private String My_DbKey;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    SharedPreferences sharedPreferences;


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_look);
        mixpanelAPI= MixpanelAPI.getInstance(CustomView.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://my-closet-fashion-style.appspot.com/");
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);

        Realm.init(this);
        realm=Realm.getDefaultInstance();

        My_DbKey=Utilities.loadPref(CustomView.this, "My_DbKey", "");

        frameLayout=(StickerView) findViewById(R.id.frame);
        checkBox = (CheckBox) findViewById(R.id.chk_post);
        konfettiView = (KonfettiView) findViewById(R.id.custom_konfetti);
        konfettiView.setVisibility(View.GONE);


        checkBox.setChecked(true);

        //SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean createlooktut = sharedPreferences.getBoolean("createlooktut",true);



        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,

                R.drawable.ic_close_black_24dp),

                BitmapStickerIcon.LEFT_TOP);

        deleteIcon.setIconEvent(new DeleteIconEvent());



        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,

                R.drawable.ic_zoom_in_black_24dp),

                BitmapStickerIcon.RIGHT_BOTOM);

        zoomIcon.setIconEvent(new ZoomIconEvent());



       /* BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,

                R.drawable.ic_flip),

                BitmapStickerIcon.RIGHT_TOP);

        flipIcon.setIconEvent(new FlipHorizontallyEvent()); */


        frameLayout.setIcons(Arrays.asList(deleteIcon,zoomIcon));
        frameLayout.setLocked(false);
        frameLayout.setConstrained(true);

        frameLayout.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(Sticker sticker) {

            }

            @Override
            public void onStickerClicked(Sticker sticker) {

                frameLayout.replace(sticker);
                frameLayout.invalidate();
            }

            @Override
            public void onStickerDeleted(Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(Sticker sticker) {

            }
        });

        //img=(ImageView) findViewById(R.id.testimage);

        look_name=(EditText) findViewById(R.id.look_name);

        final Intent intent=getIntent();

        if(intent!=null) {

            imagearray = intent.getIntegerArrayListExtra("acckey");
            toparray = intent.getIntegerArrayListExtra("topkey");
            bottomarray = intent.getIntegerArrayListExtra("bottomkey");
            footarray = intent.getIntegerArrayListExtra("footkey");
        }


        finaldress.addAll(imagearray);
        finaldress.addAll(toparray);
        finaldress.addAll(bottomarray);
        finaldress.addAll(footarray);

        for (i=0;i<finaldress.size();i++){

            Colors colors = realm.where(Colors.class).equalTo("colorset",finaldress.get(i)).findFirst();
            Dresses dresses = realm.where(Dresses.class).equalTo("id",finaldress.get(i)).findFirst();
            if (colors!=null && dresses!=null) {
                checkcolors(colors,dresses);

            }


        }

        if (createlooktut) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {

                   new MaterialTapTargetPrompt.Builder(CustomView.this,R.style.MaterialTapTargetPromptTheme)
                           .setSecondaryText("Click Here to Save")
                           .setTarget(findViewById(R.id.save))
                           .setIcon(R.drawable.ic_check_black_24dp)
                           .show();


                }
            });
        }












        try {

            for (i = 0; i < finaldress.size(); i++) {

                Dresses img = realm.where(Dresses.class).equalTo("id",finaldress.get(i)).findFirst();
                assert img!=null;

                //assert bitmaparray[i] != null;
                bitmaparray[i] = new ImageSaver(this).setFileName(img.getImagename()).setDirectoryName("mycloset").load();
                //assert bmp != null;

            }
        }catch (ArrayIndexOutOfBoundsException e){

            e.printStackTrace();
        }

        if (bitmaparray[0]!=null) {

            Drawable drawable = new BitmapDrawable(getResources(),bitmaparray[0]);
            frameLayout.addSticker(new DrawableSticker(drawable));


        }

        if (bitmaparray[1]!=null){

            Drawable drawable = new BitmapDrawable(getResources(),bitmaparray[1]);
            frameLayout.addSticker(new DrawableSticker(drawable));

        }

        if (bitmaparray[2]!=null){

            Drawable drawable = new BitmapDrawable(getResources(),bitmaparray[2]);
            frameLayout.addSticker(new DrawableSticker(drawable));

        }

        if (bitmaparray[3]!=null){

            Drawable drawable = new BitmapDrawable(getResources(),bitmaparray[3]);
            frameLayout.addSticker(new DrawableSticker(drawable));

        }

        if (bitmaparray[4]!=null){

            Drawable drawable = new BitmapDrawable(getResources(),bitmaparray[4]);
            frameLayout.addSticker(new DrawableSticker(drawable));

        }

        if (bitmaparray[5]!=null){

            Drawable drawable = new BitmapDrawable(getResources(),bitmaparray[5]);
            frameLayout.addSticker(new DrawableSticker(drawable));

        }



        finaldress.clear();

        Realm.init(this);
        realm=Realm.getDefaultInstance();


    }

    private void checkcolors(Colors colors, Dresses dresses) {

        ArrayList<String> metacoclors = new ArrayList<>();

        if (colors.getBlack()!=null){

            metacoclors.add(colors.getBlack());

        }

        if (colors.getWhite()!=null){

            metacoclors.add(colors.getWhite());

        }

        if (colors.getGrey()!=null) {

            metacoclors.add(colors.getGrey());

        }

        if (colors.getBeige()!=null){

            metacoclors.add(colors.getBeige());

        }

        if (colors.getRed()!=null){

            metacoclors.add(colors.getRed());

        }

        if (colors.getPink()!=null){

            metacoclors.add(colors.getPink());

        }

        if (colors.getSilver()!=null){

            metacoclors.add(colors.getSilver());

        }

        if (colors.getGreen()!=null){

            metacoclors.add(colors.getGreen());

        }

        if (colors.getBlue()!=null){

            metacoclors.add(colors.getBlue());

        }

        if (colors.getYellow()!=null){

            metacoclors.add(colors.getYellow());

        }

        if (colors.getOrange()!=null){

            metacoclors.add(colors.getOrange());

        }

        if (colors.getBrown()!=null){

            metacoclors.add(colors.getBrown());

        }

        if (colors.getPurple()!=null){

            metacoclors.add(colors.getPurple());

        }

        if (colors.getGold()!=null){

            metacoclors.add(colors.getGold());

        }

        if (dresses.getCategory()!=null && dresses.getCategory().equals("Accessories")){

            valuemap.put("Accessories",metacoclors);


        }else if (dresses.getCategory()!=null && dresses.getCategory().equals("Tops")){

            valuemap.put("Tops",metacoclors);

        }else if (dresses.getCategory()!=null && dresses.getCategory().equals("Bottoms")){

            valuemap.put("Bottoms",metacoclors);

        }else if (dresses.getCategory()!=null && dresses.getCategory().equals("Footwear")){

            valuemap.put("Footwear",metacoclors);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        switch (item.getItemId()) {

            case R.id.save:

                mixpanelAPI.track("Create A Look Save");

                if (checkBox.isChecked()){



                    getImagename();

                    frameLayout.setDrawingCacheEnabled(true);
                    frameLayout.buildDrawingCache(true);
                    Bitmap bitmap = Bitmap.createBitmap(frameLayout.getDrawingCache(true));
                    frameLayout.setDrawingCacheEnabled(false);

                    final String s = look_name.getText().toString();


                        Utilities.showLoading(CustomView.this, "Uploading...");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                        byte[] b = baos.toByteArray();
                    new ImageSaver(CustomView.this).setFileName(imagename).setDirectoryName("mycloset").saveImage(bitmap);


                        RealmResults<Looks> lookid = realm.where(Looks.class).findAll();

                        final int newid = lookid.size();

                        if (newid == 0) {

                            id = newid + 1;

                        } else {

                            RealmResults<Looks> finlook = realm.where(Looks.class).findAllSorted("id", Sort.DESCENDING);
                            id = finlook.get(0).getId() + 1;
                        }


                        realm.beginTransaction();

                        Looks looks = new Looks();

                        looks.setId(id);
                        looks.setImage_name(imagename);
                        looks.setStyle_name(s);
                        looks.setLookid(n);


                        Looks finallooks = realm.copyToRealm(looks);
                        realm.commitTransaction();

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {

                                updatepost(b);
                            }
                        });





                        return true;






                }else {

                    final String s = look_name.getText().toString();



                        getImagename();

                        frameLayout.setDrawingCacheEnabled(true);
                        frameLayout.buildDrawingCache(true);
                        Bitmap bitmap = Bitmap.createBitmap(frameLayout.getDrawingCache(true));
                        frameLayout.setDrawingCacheEnabled(false);

                        new ImageSaver(CustomView.this).setFileName(imagename).setDirectoryName("mycloset").saveImage(bitmap);


                        RealmResults<Looks> lookid = realm.where(Looks.class).findAll();

                        final int newid = lookid.size();

                        if (newid == 0) {

                            id = newid + 1;

                        } else {

                            RealmResults<Looks> finlook = realm.where(Looks.class).findAllSorted("id", Sort.DESCENDING);
                            id = finlook.get(0).getId() + 1;
                        }


                        realm.beginTransaction();

                        Looks looks = new Looks();

                        looks.setId(id);
                        looks.setImage_name(imagename);

                        if (s!=null) {
                            looks.setStyle_name(s);
                        } else {

                            looks.setStyle_name("");
                        }
                        looks.setLookid(n);


                        Looks finallooks = realm.copyToRealm(looks);
                        realm.commitTransaction();

                    valuemap.put("lookid", n);
                    valuemap.put("stylename",s);


                    FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("MetaData").add(valuemap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(Task<DocumentReference> task) {

                            if (task.isSuccessful()) {



                                Utilities.hideLoading();


                                Intent intent = new Intent(CustomView.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("position",3);
                                startActivity(intent);
                                finish();


                                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);


                            }


                        }


                    });







                    return true;



                    }




                }





        return super.onOptionsItemSelected(item);
    }

    private void updatepost(byte[] b) {

        Utilities.showLoading(CustomView.this,"Posting");


        final StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

        uploadTask = ref.putBytes(b);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {

                if (!task.isSuccessful()){

                    throw Objects.requireNonNull(task.getException());
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {

                if (task.isSuccessful()){

                    String download_url = task.getResult().toString();

                    if (download_url!=null){

                        Long tsLong = System.currentTimeMillis() / 1000;
                        String timestamp = tsLong.toString();

                        final Map<String, Object> data = new HashMap<>();
                        final String id = UUID.randomUUID().toString();

                        data.put("id", id);
                        data.put("lookid", n);
                        data.put("image", download_url);
                        data.put("description", look_name.getText().toString());
                        data.put("timestamp", timestamp);
                        data.put("email", Utilities.loadPref(CustomView.this, "email", ""));
                        data.put("languages", "");
                        data.put("dbkey",My_DbKey);

                        FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Feed").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(Task<DocumentReference> task) {

                                if (task.isSuccessful()){

                                    FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("FollowCollection").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()){

                                                if (!Objects.requireNonNull(task.getResult()).isEmpty()){

                                                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                                        FirebaseFirestore.getInstance().collection("UsersList").whereEqualTo("Email",documentSnapshot.get("email")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(Task<QuerySnapshot> task) {

                                                                if (task.isSuccessful()){

                                                                    for (DocumentSnapshot documentSnapshot1 : Objects.requireNonNull(task.getResult())){

                                                                        FirebaseFirestore.getInstance().collection("UsersList").document(documentSnapshot1.getId()).collection("Feed").add(data);
                                                                    }




                                                                }


                                                            }
                                                        });

                                                    }


                                                }

                                                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Posts").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(Task<DocumentReference> task) {

                                                        if (task.isSuccessful()){

                                                            valuemap.put("lookid", n);
                                                            valuemap.put("stylename",look_name.getText().toString());

                                                            FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("MetaData").add(valuemap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                @Override
                                                                public void onComplete(Task<DocumentReference> task) {

                                                                    if (task.isSuccessful()) {

                                                                        boolean post_tut = sharedPreferences.getBoolean("post",true);

                                                                        Utilities.hideLoading();


                                                                        Intent intent = new Intent(CustomView.this, HomeActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        if(post_tut){

                                                                            intent.putExtra("celebration","celeb");
                                                                        }
                                                                        startActivity(intent);
                                                                        finish();

                                                                        Utilities.showToast(CustomView.this,"Posted");
                                                                        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);


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
                        });







                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

                Utilities.hideLoading();
                Toast.makeText(CustomView.this, "Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        Utilities.hideLoading();




    }

    public void getImagename() {

        Date date=new Date();
        Random random=new Random();
        n=random.nextInt(2134499);
        imagename="lookimage" + date.getTime() + n + ".png";

    }


}



