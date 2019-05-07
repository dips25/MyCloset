package my.closet.fashion.style;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.customs.StickerImageView;
import my.closet.fashion.style.modesl.Looks;

/**
 * Created by biswa on 10/3/2017.
 */

public class CustomView extends AppCompatActivity {



    int i;
    Realm realm;
    int id;

    Bitmap bitmaparray[]=new Bitmap[10];

    ArrayList<String> imagearray=new ArrayList<>();
    ArrayList<String> toparray=new ArrayList<>();
    ArrayList<String> bottomarray=new ArrayList<>();
    ArrayList<String> footarray=new ArrayList<>();

    FrameLayout frameLayout;
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


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_look);
        mixpanelAPI= MixpanelAPI.getInstance(CustomView.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        frameLayout=(FrameLayout) findViewById(R.id.frame);
        img=(ImageView) findViewById(R.id.testimage);

        look_name=(EditText) findViewById(R.id.look_name);

        final Intent intent=getIntent();

         imagearray=(ArrayList<String>) intent.getSerializableExtra("acckey");
         toparray=(ArrayList<String>)intent.getSerializableExtra("topkey");
         bottomarray=(ArrayList<String>) intent.getSerializableExtra("bottomkey");
         footarray=(ArrayList<String>) intent.getSerializableExtra("footkey");

        ArrayList<String> finaldress=new ArrayList<>();

        finaldress.addAll(imagearray);
        finaldress.addAll(toparray);
        finaldress.addAll(bottomarray);
        finaldress.addAll(footarray);

        stickerImageView1=new StickerImageView(this);
        stickerImageView1.setVisibility(View.GONE);
        stickerImageView2=new StickerImageView(this);
        stickerImageView2.setVisibility(View.GONE);
        stickerImageView3=new StickerImageView(this);
        stickerImageView3.setVisibility(View.GONE);
        stickerImageView4=new StickerImageView(this);
        stickerImageView4.setVisibility(View.GONE);
        stickerImageView5=new StickerImageView(this);
        stickerImageView5.setVisibility(View.GONE);
        stickerImageView6=new StickerImageView(this);
        stickerImageView6.setVisibility(View.GONE);





        stickerImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stickerImageView1.setControlItemsHidden(true);
            }
        });

        stickerImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stickerImageView2.setControlItemsHidden(true);
            }
        });

        stickerImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stickerImageView3.setControlItemsHidden(true);
            }
        });

        stickerImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stickerImageView4.setControlItemsHidden(true);
            }
        });

        try {

            for (i = 0; i < finaldress.size(); i++) {

                //assert bitmaparray[i] != null;
                bitmaparray[i] = new ImageSaver(this).setFileName(finaldress.get(i)).setDirectoryName("mycloset").load();
                //assert bmp != null;

            }
        }catch (ArrayIndexOutOfBoundsException e){

            e.printStackTrace();
        }

        if (bitmaparray[0]!=null) {

            stickerImageView1.setVisibility(View.VISIBLE);
            stickerImageView1.setImageBitmap(bitmaparray[0]);
            frameLayout.addView(stickerImageView1);
        }

        if (bitmaparray[1]!=null){

            stickerImageView2.setVisibility(View.VISIBLE);
            stickerImageView2.setImageBitmap(bitmaparray[1]);
            frameLayout.addView(stickerImageView2);
        }

        if (bitmaparray[2]!=null){

            stickerImageView3.setVisibility(View.VISIBLE);
            stickerImageView3.setImageBitmap(bitmaparray[2]);
            frameLayout.addView(stickerImageView3);
        }

        if (bitmaparray[3]!=null){

            stickerImageView4.setVisibility(View.VISIBLE);
            stickerImageView4.setImageBitmap(bitmaparray[3]);
            frameLayout.addView(stickerImageView4);
        }

        if (bitmaparray[4]!=null){

            stickerImageView5.setVisibility(View.VISIBLE);
            stickerImageView5.setImageBitmap(bitmaparray[4]);
            frameLayout.addView(stickerImageView5);
        }

        if (bitmaparray[5]!=null){

            stickerImageView6.setVisibility(View.VISIBLE);
            stickerImageView6.setImageBitmap(bitmaparray[5]);
            frameLayout.addView(stickerImageView6);
        }



        finaldress.clear();

        Realm.init(this);
        realm=Realm.getDefaultInstance();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Realm.init(this);
        realm=Realm.getDefaultInstance();

        switch (item.getItemId()){

            case R.id.save:

                mixpanelAPI.track("Create A Look Save");

                stickerImageView1.setControlItemsHidden(true);
                stickerImageView2.setControlItemsHidden(true);
                stickerImageView3.setControlItemsHidden(true);
                stickerImageView4.setControlItemsHidden(true);

                getImagename();

                frameLayout.setDrawingCacheEnabled(true);
                frameLayout.buildDrawingCache(true);
                Bitmap bitmap=Bitmap.createBitmap(frameLayout.getDrawingCache(true));
                frameLayout.setDrawingCacheEnabled(false);
                new ImageSaver(CustomView.this).setFileName(imagename).setDirectoryName("mycloset").saveImage(bitmap);

                String s=look_name.getText().toString();
                if (s.length()>0) {

                    RealmResults<Looks> lookid=realm.where(Looks.class).findAll();
                    int newid=lookid.size();

                    if(newid==0) {

                        id=newid+1;

                    }else {

                        RealmResults<Looks> finlook=realm.where(Looks.class).findAllSorted("id", Sort.DESCENDING);
                        id=finlook.get(0).getId()+1;
                    }

                    Looks looks = new Looks();
                    looks.setId(id);
                    looks.setImage_name(imagename);
                    looks.setStyle_name(s);

                    realm.beginTransaction();
                    Looks finallooks=realm.copyToRealm(looks);
                    realm.commitTransaction();

                    stickerImageView1.setVisibility(View.INVISIBLE);
                    stickerImageView2.setVisibility(View.INVISIBLE);
                    stickerImageView3.setVisibility(View.INVISIBLE);
                    stickerImageView4.setVisibility(View.INVISIBLE);


                    finish();


                }else {

                    Toast.makeText(CustomView.this,"Give a name to look created",Toast.LENGTH_LONG).show();

                    }

                    return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void getImagename() {

        Date date=new Date();
        Random random=new Random();
        n=random.nextInt(21344);
        imagename="lookimage" + date.getTime() + n + ".png";

    }


}



