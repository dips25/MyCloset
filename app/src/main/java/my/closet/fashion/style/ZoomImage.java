package my.closet.fashion.style;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.adapters.Lookbookadapter;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.modesl.Looks;

public class ZoomImage extends AppCompatActivity {

    PhotoView photoView;
    String finalname;
    String edtlook;
    EditText lookedit;
    private static String STORE_DIRECTORY;
    Bitmap bitmap;
    //Button bttn;
    int newid;
    Realm realm;
    ImageView lookupdt;
    ImageView share;
    RelativeLayout looksvbttn;
    ArrayList<Looks> looksArrayList=new ArrayList<>();



    ImageView edit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_activity);

        Realm.init(this);
        realm=Realm.getDefaultInstance();

        photoView=(PhotoView) findViewById(R.id.zoom_image);
        share=(ImageView) findViewById(R.id.share);

        looksvbttn=(RelativeLayout) findViewById(R.id.looksvbttn);
        looksvbttn.setVisibility(View.INVISIBLE);

        lookedit=(EditText) findViewById(R.id.look_edit);
        lookedit.setVisibility(View.INVISIBLE);

        lookupdt=(ImageView) findViewById(R.id.look_updt);
        edit=(ImageView) findViewById(R.id.toogle_edit);
        finalname= Objects.requireNonNull(getIntent().getExtras()).getString("imgname");

        edtlook=getIntent().getExtras().getString("stylename");
        newid=  getIntent().getExtras().getInt("id");
        bitmap=new ImageSaver(this).setFileName(finalname).setDirectoryName("mycloset").load();

        photoView.setImageBitmap(bitmap);

        share.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         Intent shareintent=new Intent();
                                         shareintent.setAction(Intent.ACTION_SEND);
                                         shareintent.putExtra(Intent.EXTRA_STREAM,getImgFile());
                                         shareintent.setType("image/*");
                                         startActivity(Intent.createChooser(shareintent,"Share image via"));


                                     }
                                 });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        edit.setVisibility(View.INVISIBLE);
                        lookedit.setVisibility(View.VISIBLE);
                        lookedit.setText(edtlook);
                        looksvbttn.setVisibility(View.VISIBLE);


                    }
                });

                lookupdt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Looks looks = new Looks();

                            realm.beginTransaction();
                        Looks looks=realm.where(Looks.class).equalTo("id",newid).findFirst();
                       // realmObject.deleteFromRealm();
                        if (looks!=null) {
                          //  looks.setId(newid);
                            looks.setStyle_name(lookedit.getText().toString());
                            looks.setImage_name(finalname);
                            realm.copyToRealmOrUpdate(looks);
                            realm.commitTransaction();
                        }

                        refreshlayout();

                        Intent intent=new Intent(getApplicationContext(),Lookbook.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                            //realm.close();


                       // finish();

                    }
                });




    }

    private void refreshlayout() {

        RealmResults<Looks> results=realm.where(Looks.class).findAll().sort("id", Sort.DESCENDING);
        for (Looks looks1 : results){
            looksArrayList.add(looks1);
        }

        Lookbook lookbook=new Lookbook();


        Lookbookadapter lookbookadapter=new Lookbookadapter(getApplicationContext(),R.layout.single_gridview,looksArrayList);
        lookbook.gridView.setAdapter(lookbookadapter);
        lookbookadapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zoom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.deleteii:
                realm=Realm.getDefaultInstance();
                realm.beginTransaction();
                Looks looke = realm.where(Looks.class).equalTo("id", newid).findFirst();
                looke.deleteFromRealm();
                realm.commitTransaction();
                refreshlayout();

               Intent intent=new Intent(getApplicationContext(),Lookbook.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private Uri getImgFile() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File externaldirectory=getExternalFilesDir(null);
        assert externaldirectory != null;
        STORE_DIRECTORY=externaldirectory.getAbsolutePath() + "/screenshots/";
        File file1=  new File(STORE_DIRECTORY);
        // file1 =

        if (!file1.exists()){

            file1.mkdirs();
        }
        File finalfile=new File(file1,imageFileName);
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(finalfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(finalfile);


    }



}
