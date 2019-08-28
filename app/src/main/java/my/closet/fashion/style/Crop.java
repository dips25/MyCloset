package my.closet.fashion.style;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by biswa on 1/11/2018.
 */

public class Crop extends AppCompatActivity {


    String path;
    String finaluri;
    Uri uri;
    Bitmap bitmap;
    byte[] bytes;
     Bitmap bmp;
    CropImageView view;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);





        Bundle bundle=getIntent().getExtras();

        if (bundle != null) {
             bytes=bundle.getByteArray("bytearray");
            assert bytes != null;
            bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);


        }






         view = (CropImageView) findViewById(R.id.cimg);

        view.setGuidelines(CropImageView.Guidelines.ON);


        view.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        view.setCropShape(CropImageView.CropShape.RECTANGLE);

        view.setCropRect(new Rect(100,100,800,500));
        view.setImageBitmap(bitmap);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cut_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.croptxt:

                Bitmap croppedbitmap=view.getCroppedImage(800,800);
                if (croppedbitmap!=null) {

                    ByteArrayOutputStream cropbyte=new ByteArrayOutputStream();
                    croppedbitmap.compress(Bitmap.CompressFormat.PNG,100,cropbyte);
                    byte[] bit=cropbyte.toByteArray();

                    Intent intent=new Intent(this,EraserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("source","add");
                    intent.putExtra("image","screenshot");
                    intent.putExtra("screenshots",bit);
                    startActivity(intent);
                    finish();
                    //view.setImageBitmap(croppedbitmap);
                    //break;

                }

        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap decodeSampledBitmap(int width, int height) {

        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        options.inSampleSize=calculateInSampleSize(options,width,height);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
    }

    private int calculateSampleSize(BitmapFactory.Options options, int width, int height) {

        final int fwidth=options.outWidth;
        final int fheight=options.outHeight;
        int insample = 1;

        if (fheight > height || fwidth > width){

            final int hheight=fheight/2;
            final int hwidth=fwidth/2;

            while ((hheight/insample)>=height && (hwidth/insample)>=width){

                insample*= 2;
            }
        }
        return insample;

    }

    public static float getScreenWidth(AppCompatActivity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float pxWidth = outMetrics.widthPixels;
        return pxWidth;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqwidth, int reqheight) {

        final int width=options.outWidth;
        final int height=options.outHeight;
        int samplesize=1;

        if (height>reqheight || width>reqwidth){

            final int heightratio=Math.round((float)height/(float)reqheight);
            final int widthratio=Math.round((float)width/(float)reqwidth);

            samplesize=heightratio<widthratio ? heightratio : widthratio;
        }
        return samplesize;
    }

}

