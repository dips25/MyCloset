package my.closet.fashion.style;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {
    public static int screenWidth, screenHeight;
    public static String picturePath = "";
    private static ProgressDialog mProgressDialog;
    public static boolean Following;
    public static boolean SavedTab ,MyTab;
    public static boolean bookmarked;
    public static String currentItem="";

    public static void savebooleanPref(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanPref(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Login_Preferences", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }

    public static void savePref(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref-Values", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadPref(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Pref-Values", Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public static void savelist(Context context, String key, ArrayList<Object> value) {
        // FOr saving list of static data

        SharedPreferences sharedPref = context.getSharedPreferences("Pref-Values", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            sb.append(value.get(i)).append(",");
        }
        editor.putString(key, sb.toString());
        editor.commit();
    }



    public static void showToast(Context context, String ToastMessage) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.custom_toasr_view, null);

        TextView txt = (TextView) child.findViewById(R.id.toast_txt);
        txt.setBackgroundResource(R.drawable.round_toast_bg);
        txt.setText(ToastMessage);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 250);
        toast.setView(child);
        toast.show();
    }


    public static void showLoading(Context context,String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    public static void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public static void hideKeyboard(View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static int getRandomList(List<Integer> list) {
        int index = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            index = ThreadLocalRandom.current().nextInt(list.size());
        }
        System.out.println("\nIndex :" + index);
        return list.get(index);

    }


    public static Bitmap StringToBitMap(String encodedString) {
        Bitmap bitmap = null;
        try {
            byte[] encodeByte = Base64.decode(encodedString.getBytes(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return bitmap;
        }
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private static ColorDrawable[] vibrantLightColorList = {
                    new ColorDrawable(Color.parseColor("#76A599")),
                    new ColorDrawable(Color.parseColor("#7D9A96")),
                    new ColorDrawable(Color.parseColor("#6F8888")),
                    new ColorDrawable(Color.parseColor("#82A1AB")),
                    new ColorDrawable(Color.parseColor("#8094A1")),
                    new ColorDrawable(Color.parseColor("#74838C")),
                    new ColorDrawable(Color.parseColor("#B6A688")),
                    new ColorDrawable(Color.parseColor("#9E9481")),
                    new ColorDrawable(Color.parseColor("#8D8F9F")),
                    new ColorDrawable(Color.parseColor("#A68C93")),
                    new ColorDrawable(Color.parseColor("#968388")),
                    new ColorDrawable(Color.parseColor("#7E808B")),
            };

    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public static Bitmap getBitmap(Drawable drawableRes) {
        Bitmap mutableBitmap = null;
        mutableBitmap = Bitmap.createBitmap(320, 420, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawableRes.setBounds(0, 0, 320, 420);
        drawableRes.draw(canvas);

        return mutableBitmap;

    }

    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }



}



