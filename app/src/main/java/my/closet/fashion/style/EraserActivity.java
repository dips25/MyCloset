package my.closet.fashion.style;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import my.closet.fashion.style.customs.HoverView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class EraserActivity extends AppCompatActivity implements OnClickListener {
    private String imagePath;
    private Intent intent;
    private ContentResolver mContentResolver;
    Bitmap mBitmap;

    HoverView hoverView;
    double mDensity;

    int viewWidth;
    int viewHeight;
    int bmWidth;
    int bmHeight;

    int actionBarHeight;
    int bottombarHeight;
    double bmRatio;
    double viewRatio;

    ImageButton eraserMainButton;
    ImageButton magicWandMainButton;
    ImageButton mirrorButton;
    ImageButton positionButton;
    ImageView eraserSubButton, unEraserSubButton;
    ImageView brushSize1Button, brushSize2Button, brushSize3Button, brushSize4Button;
    ImageView magicRemoveButton, magicRestoreButton;
    ImageView  undoButton, redoButton;
    ImageView nextButton;
    ImageView colorButton;

    SeekBar magicSeekbar;
    RelativeLayout eraserLayout, magicLayout;
    RelativeLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eraser);
        mContentResolver = getContentResolver();

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null && bundle.containsKey("source")){

            Uri uri = (Uri) bundle.get("path");
            mBitmap = getBitmapfromUri(uri);
        }else {

            Uri uri = (Uri) getIntent().getExtras().get("path");
            mBitmap = getBitmapfromUri(uri);
        }




        if (Build.VERSION.SDK_INT >= 23 ) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }



        mLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mDensity = getResources().getDisplayMetrics().density;
        actionBarHeight = (int)(110*mDensity);
        bottombarHeight = (int)(60*mDensity);


        viewWidth = getResources().getDisplayMetrics().widthPixels;
        viewHeight = getResources().getDisplayMetrics().heightPixels - actionBarHeight - bottombarHeight;
        viewRatio = (double) viewHeight/ (double) viewWidth;


        if (mBitmap!=null) {
            bmRatio = (double) mBitmap.getHeight() / (double) mBitmap.getWidth();
            if (bmRatio < viewRatio) {
                bmWidth = viewWidth;
                bmHeight = (int) (((double) viewWidth) * ((double) (mBitmap.getHeight()) / (double) (mBitmap.getWidth())));
            } else {
                bmHeight = viewHeight;
                bmWidth = (int) (((double) viewHeight) * ((double) (mBitmap.getWidth()) / (double) (mBitmap.getHeight())));
            }
        }



            mBitmap = Bitmap.createScaledBitmap(mBitmap, bmWidth, bmHeight, false);




            hoverView = new HoverView(this, mBitmap, bmWidth, bmHeight, viewWidth, viewHeight);


        if (hoverView!=null) {

            hoverView.setLayoutParams(new LayoutParams(viewWidth, viewHeight));



            mLayout.addView(hoverView);
        }

        initButton();

    }

    public void initButton() {
        eraserMainButton = (ImageButton) findViewById(R.id.eraseButton);
        eraserMainButton.setOnClickListener(this);
        magicWandMainButton = (ImageButton) findViewById(R.id.magicButton);
        magicWandMainButton.setOnClickListener(this);
        mirrorButton = (ImageButton) findViewById(R.id.mirrorButton);
        mirrorButton.setOnClickListener(this);
        positionButton = (ImageButton) findViewById(R.id.positionButton);
        positionButton.setOnClickListener(this);
        nextButton = (ImageView) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
        boolean first = preferences.getBoolean("first",true);


        if (first){

            showtutorial();
        }


        findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
        findViewById(R.id.eraser_layout).setVisibility(View.GONE);
        resetMainButtonState();
        positionButton.setSelected(true);
        hoverView.switchMode(hoverView.MOVING_MODE);

        eraserSubButton = (ImageView) findViewById(R.id.erase_sub_button);
        eraserSubButton.setOnClickListener(this);
        unEraserSubButton = (ImageView) findViewById(R.id.unerase_sub_button);
        unEraserSubButton.setOnClickListener(this);

        brushSize1Button = (ImageView) findViewById(R.id.brush_size_1_button);
        brushSize1Button.setOnClickListener(this);

        brushSize2Button = (ImageView) findViewById(R.id.brush_size_2_button);
        brushSize2Button.setOnClickListener(this);

        brushSize3Button = (ImageView) findViewById(R.id.brush_size_3_button);
        brushSize3Button.setOnClickListener(this);

        brushSize4Button = (ImageView) findViewById(R.id.brush_size_4_button);
        brushSize4Button.setOnClickListener(this);


        magicSeekbar = (SeekBar) findViewById(R.id.magic_seekbar);
        magicSeekbar.setProgress(15);
        magicSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hoverView.setMagicThreshold(seekBar.getProgress());
                if(hoverView.getMode() == hoverView.MAGIC_MODE)
                    hoverView.magicEraseBitmap();
                else if(hoverView.getMode() == hoverView.MAGIC_MODE_RESTORE)
                    hoverView.magicRestoreBitmap();
                hoverView.invalidateView();
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					/*mHoverView.setMagicThreshold(progress);
					if(mHoverView.getMode() == mHoverView.MAGIC_MODE)
						mHoverView.magicEraseBitmap();
					else if(mHoverView.getMode() == mHoverView.MAGIC_MODE_RESTORE)
						mHoverView.magicRestoreBitmap();
					mHoverView.invalidateView();*/
            }
        });

        magicRemoveButton = (ImageView) findViewById(R.id.magic_remove_button);
        magicRemoveButton.setOnClickListener(this);
        magicRestoreButton = (ImageView) findViewById(R.id.magic_restore_button);
        magicRestoreButton.setOnClickListener(this);

       // nextButton = (Button) findViewById(R.id.nextButton);
       // nextButton.setOnClickListener(this);

        undoButton = (ImageView) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(this);

        redoButton = (ImageView) findViewById(R.id.redoButton);
        redoButton.setOnClickListener(this);
        updateRedoButton();

        eraserLayout = (RelativeLayout) findViewById(R.id.eraser_layout);
        magicLayout = (RelativeLayout) findViewById(R.id.magicWand_layout);
       // eraserMainButton.setSelected(true);

        colorButton = (ImageView) findViewById(R.id.colorButton);
        colorButton.setOnClickListener(this);
    }

    private Bitmap getBitmap(String path) {
        Uri uri = getImageUri(path);
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 500;
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math
                        .round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            b = Bitmap.createBitmap(b, 0, 0, o2.outWidth, o2.outHeight, getOrientationMatrix(path), true);

            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getBitmapfromUri(Uri uri) {
        //Uri uri = getImageUri(path);
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 500;
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math
                        .round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            b = Bitmap.createBitmap(b, 0, 0, o2.outWidth, o2.outHeight, getOrientationMatrix(uri.getPath()), true);

            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void resetSeekBar() {
        magicSeekbar.setProgress(0);
        hoverView.setMagicThreshold(0);
    }

    private Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    private Matrix getOrientationMatrix(String path) {
        Matrix matrix = new Matrix();
        ExifInterface exif;
        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return matrix;
    }

    int currentColor = 0;

    public void setBackGroundColor(int color) {
        switch (color) {
            case 0:
                mLayout.setBackgroundResource(R.drawable.bg);
                colorButton.setBackgroundResource(R.drawable.white_drawable);
                break;
            case 1:
                mLayout.setBackgroundColor(Color.WHITE);
                colorButton.setBackgroundResource(R.drawable.black_drawable);
                break;
            case 2:
                mLayout.setBackgroundColor(Color.BLACK);
                colorButton.setBackgroundResource(R.drawable.transparent_drawable);
                break;

            default:
                break;
        }

        currentColor = color;
    }

    public void resetMainButtonState() {
        eraserMainButton.setSelected(false);
        magicWandMainButton.setSelected(false);
        mirrorButton.setSelected(false);
        positionButton.setSelected(false);
    }

    public void resetSubEraserButtonState() {
        eraserSubButton.setSelected(false);
        unEraserSubButton.setSelected(false);
    }

    public void resetSubMagicButtonState() {
        magicRemoveButton.setSelected(false);
        magicRestoreButton.setSelected(false);
    }

    public void resetBrushButtonState() {
        brushSize1Button.setSelected(false);
        brushSize2Button.setSelected(false);
        brushSize3Button.setSelected(false);
        brushSize4Button.setSelected(false);
    }

    public void updateUndoButton() {
        if (hoverView != null) {
            if (hoverView.checkUndoEnable()) {
                undoButton.setEnabled(true);
                undoButton.setAlpha(1.0f);
            } else {
                undoButton.setEnabled(false);
                undoButton.setAlpha(0.3f);
            }
        }
    }

    public void updateRedoButton() {

        if (hoverView != null) {
            if (hoverView.checkRedoEnable()) {
                redoButton.setEnabled(true);
                redoButton.setAlpha(1.0f);
            } else {
                redoButton.setEnabled(false);
                redoButton.setAlpha(0.3f);
            }
        }
    }

    @Override
    public void onClick(View v) {
        updateUndoButton();
        updateRedoButton();

        switch (v.getId()) {
            case R.id.eraseButton:
                hoverView.switchMode(hoverView.ERASE_MODE);
                if(eraserLayout.getVisibility() == View.VISIBLE) {
                    eraserLayout.setVisibility(View.GONE);
                } else {
                    eraserLayout.setVisibility(View.VISIBLE);
                }
                magicLayout.setVisibility(View.GONE);
                resetMainButtonState();
                resetSubEraserButtonState();
                eraserSubButton.setSelected(true);
                eraserMainButton.setSelected(true);
                break;
            case R.id.magicButton:
                hoverView.switchMode(HoverView.MAGIC_MODE);
                if(magicLayout.getVisibility() == View.VISIBLE) {
                    magicLayout.setVisibility(View.GONE);
                } else {
                    magicLayout.setVisibility(View.VISIBLE);
                }
                eraserLayout.setVisibility(View.GONE);
                resetMainButtonState();
                resetSubMagicButtonState();
                resetSeekBar();
                magicRemoveButton.setSelected(true);
                magicWandMainButton.setSelected(true);

                break;

            case R.id.mirrorButton:
                findViewById(R.id.eraser_layout).setVisibility(View.GONE);
                findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
                hoverView.mirrorImage();
                break;
            case R.id.positionButton:
                hoverView.switchMode(hoverView.MOVING_MODE);
                findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
                findViewById(R.id.eraser_layout).setVisibility(View.GONE);
                resetMainButtonState();
                positionButton.setSelected(true);
                break;

            case R.id.erase_sub_button:
                hoverView.switchMode(HoverView.ERASE_MODE);
                resetSubEraserButtonState();
                eraserSubButton.setSelected(true);
                break;
            case R.id.unerase_sub_button:
                hoverView.switchMode(HoverView.UNERASE_MODE);
                resetSubEraserButtonState();
                unEraserSubButton.setSelected(true);
                break;

            case R.id.brush_size_1_button:
                resetBrushButtonState();
                hoverView.setEraseOffset(40);
                brushSize1Button.setSelected(true);
                break;

            case R.id.brush_size_2_button:
                resetBrushButtonState();
                hoverView.setEraseOffset(60);
                brushSize2Button.setSelected(true);
                break;

            case R.id.brush_size_3_button:
                resetBrushButtonState();
                hoverView.setEraseOffset(80);
                brushSize3Button.setSelected(true);
                break;

            case R.id.brush_size_4_button:
                resetBrushButtonState();
                hoverView.setEraseOffset(100);
                brushSize4Button.setSelected(true);
                break;

            case R.id.magic_remove_button:
                resetSubMagicButtonState();
                magicRemoveButton.setSelected(true);
                hoverView.switchMode(HoverView.MAGIC_MODE);
                resetSeekBar();
                break;

            case R.id.magic_restore_button:
                resetSubMagicButtonState();
                magicRestoreButton.setSelected(true);
                hoverView.switchMode(HoverView.MAGIC_MODE_RESTORE);
                resetSeekBar();
                break;

            case R.id.nextButton:
                //Intent intent = new Intent(getApplicationContext(), Pic_info.class)
                if (hoverView!=null){

                    hoverView.save();

                }

                finish();
                break;

            case R.id.colorButton:
                setBackGroundColor((currentColor+1)%3);
                break;

            case R.id.undoButton:
                findViewById(R.id.eraser_layout).setVisibility(View.GONE);
                findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
                hoverView.undo();
                if(hoverView.checkUndoEnable()) {
                    undoButton.setEnabled(true);
                    undoButton.setAlpha(1.0f);
                }
                else {
                    undoButton.setEnabled(false);
                    undoButton.setAlpha(0.3f);
                }
                updateRedoButton();
                break;
            case R.id.redoButton:
                findViewById(R.id.eraser_layout).setVisibility(View.GONE);
                findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
                hoverView.redo();
                updateUndoButton();
                updateRedoButton();
                break;
        }

    }

    private void showtutorial() {

        new GuideView.Builder(EraserActivity.this)
                .setContentText(getString(R.string.eraser_hint))
                .setTargetView(eraserMainButton)
                .setDismissType(DismissType.anywhere)
                .setContentTextSize(16)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {

                        new GuideView.Builder(EraserActivity.this)
                                .setContentText(getString(R.string.magicwand_hint))
                                .setTargetView(magicWandMainButton)
                                .setDismissType(DismissType.anywhere)
                                .setContentTextSize(16)
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {


                                        new GuideView.Builder(EraserActivity.this)
                                                .setContentText(getString(R.string.position_hint))
                                                .setTargetView(positionButton)
                                                .setDismissType(DismissType.anywhere)
                                                .setContentTextSize(16)
                                                .build()
                                                .show();


                                    }
                                })
                                .build()
                                .show();

                    }
                })
                .build()
                .show();



        SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first",false);
        editor.apply();

    }


}
