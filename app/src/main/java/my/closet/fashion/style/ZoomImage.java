package my.closet.fashion.style;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

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
import my.closet.fashion.style.activities.PostFeedActivity;
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

    RelativeLayout looksvbttn;
    ArrayList<Looks> looksArrayList=new ArrayList<>();
    MixpanelAPI mixpanelAPI;
    private int lookid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_activity);

        mixpanelAPI= MixpanelAPI.getInstance(ZoomImage.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        Realm.init(this);
        realm=Realm.getDefaultInstance();

        photoView=(PhotoView) findViewById(R.id.zoom_image);


        looksvbttn=(RelativeLayout) findViewById(R.id.looksvbttn);
        looksvbttn.setVisibility(View.INVISIBLE);

        lookedit=(EditText) findViewById(R.id.look_edit);
       // lookedit.setVisibility(View.INVISIBLE);

        lookupdt=(ImageView) findViewById(R.id.look_updt);
        finalname= Objects.requireNonNull(getIntent().getExtras()).getString("imgname");

        edtlook=getIntent().getExtras().getString("stylename");
        newid=  getIntent().getExtras().getInt("id");
        lookid = getIntent().getExtras().getInt("lookid");
        bitmap=new ImageSaver(this).setFileName(finalname).setDirectoryName("mycloset").load();

        lookedit.setText(edtlook);

        photoView.setImageBitmap(bitmap);



                lookupdt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Looks looks = new Looks();

                        mixpanelAPI.track("ZoomActivity Update");

                            realm.beginTransaction();
                        Looks looks=realm.where(Looks.class).equalTo("id",newid).findFirst();

                        if (looks!=null) {
                           // looks.setId(newid);
                            looks.setStyle_name(lookedit.getText().toString());
                            looks.setImage_name(finalname);
                            looks.setLookid(lookid);
                            realm.copyToRealmOrUpdate(looks);
                            realm.commitTransaction();
                        }

                       // refreshlayout();




                            //realm.close();


                        finish();

                    }
                });




    }

    private void refreshlayout() {

        RealmResults<Looks> results=realm.where(Looks.class).findAll().sort("id", Sort.DESCENDING);
        for (Looks looks1 : results){
            looksArrayList.add(looks1);
        }


        Lookbookadapter lookbookadapter=new Lookbookadapter(getApplicationContext(),R.layout.single_gridview,looksArrayList);
       // LookbookFragment.gridView.setAdapter(lookbookadapter);
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

                mixpanelAPI.track("ZoomActivity Delete");
                realm=Realm.getDefaultInstance();
                realm.beginTransaction();
                Looks looke = realm.where(Looks.class).equalTo("id", newid).findFirst();
                looke.deleteFromRealm();
                realm.commitTransaction();
               // refreshlayout();

              // Intent intent=new Intent(getApplicationContext(),Lookbook.class);
              // startActivity(intent);
                finish();
                return true;

            case R.id.edit_lookbook:

                mixpanelAPI.track("ZoomActivity Edit");

                //lookedit.setVisibility(View.VISIBLE);
                //lookedit.setText(edtlook);
                looksvbttn.setVisibility(View.VISIBLE);
                return true;


            case R.id.post:

                Intent intent1 = new Intent(getApplicationContext(),PostFeedActivity.class);
                intent1.putExtra("Lookbook",finalname);
                intent1.putExtra("lookid",lookid);
                intent1.putExtra("stylename",edtlook);
                startActivity(intent1);
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
