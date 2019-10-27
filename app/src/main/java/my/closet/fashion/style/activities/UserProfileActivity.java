package my.closet.fashion.style.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import my.closet.fashion.style.BuildConfig;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.customs.IconizedMenu;
import my.closet.fashion.style.modesl.DefaultFollowers;
import my.closet.fashion.style.modesl.FBGmailData;

public class UserProfileActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    FirebaseFirestore profCollection;
    List<DefaultFollowers> defaultFollowers;


    private MixpanelAPI mixpanelAPI;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private RequestOptions requestOptions;
    private Intent i;
    private FBGmailData fBGmailData_obj;
    private Toolbar toolbar;
    private CircleImageView profile_image;
    private TextView username_txt;
    private EditText penname_edittext;
    private TextView email_edittext;
    private EditText link_edittext;
    private EditText bio_edittext;
    private ImageView menu;
    private Button login_btn;
    private RelativeLayout edit_profile_pic;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<String> langlist = new ArrayList<>();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    protected Location mLastLocation;
    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int GALLERY_PIC_REQUEST = 2;
    private static final int REQUEST_STORAGE_PERMISSION = 9;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private String location = "";
    private File destination;
    private Uri selectedImageUri;
    private String update_key = "";
    private UploadTask uploadTask;
    private String downloaded_url = "";
    private Dialog cam_dialog;
    private ImageButton close_btn;
    private Button camera_button;
    private Button gallery_button;
    private IconizedMenu popup;
    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profCollection = FirebaseFirestore.getInstance();

        mixpanelAPI = MixpanelAPI.getInstance(UserProfileActivity.this, "257c7d2e1c44d7d1ab6105af372f65a6 ");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://my-closet-fashion-style.appspot.com/");

        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.white_border);

        findView();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null){

            if (Utilities.loadPref(UserProfileActivity.this,"LoggedInWith","").equalsIgnoreCase("Gmail")){

                email_edittext.setText(firebaseUser.getEmail());
                LoginUsingEmail();



            }else if (Utilities.loadPref(UserProfileActivity.this,"LoggedInWith","").equalsIgnoreCase("Email")){

                email_edittext.setText(firebaseUser.getEmail());
                LoginUsingEmail();

            }else if (Utilities.loadPref(UserProfileActivity.this,"LoggedInWith","").equalsIgnoreCase("Mobile")){

                email_edittext.setText(firebaseUser.getPhoneNumber());
                LoginUsingEmail();

            }
        }


     /*  i = getIntent();
        Bundle bundle = i.getExtras();
        if (i != null && bundle != null) {

            if (bundle.containsKey("LoginData")) {

                fBGmailData_obj = (FBGmailData) i.getSerializableExtra("LoginData");
                fetchingData();


            } else if (bundle.containsKey("mob")) {

                String mob = i.getExtras().getString("mob");
                email_edittext.setText(mob.toString());
                LoginUsingEmail();


            }

        } else {

            String s = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail().toString();
            if (s != null) {

                email_edittext.setText(s);
                LoginUsingEmail();


            } else {

                 email_edittext.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                LoginUsingEmail();
            }


        } */
    }


    @Override
    public void onStart() {
        super.onStart();



/*
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }*/
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            getAddress(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            //showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(UserProfileActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();

            Log.v("IGA", "Address" + add);
            location=add;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }
    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(UserProfileActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    private void findView() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar avy = getSupportActionBar();
        avy.setTitle("Profile");

        toolbar.setNavigationIcon(R.drawable.ic_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);
            }
        });
        profile_image = findViewById(R.id.profile_image);
        username_txt = findViewById(R.id.username_txt);
        penname_edittext = findViewById(R.id.penname_edittext);
        email_edittext = (TextView) findViewById(R.id.email_edittext);
        link_edittext = findViewById(R.id.link_edittext);
        bio_edittext = findViewById(R.id.bio_edittext);




        edit_profile_pic = findViewById(R.id.edit_profile_pic);
        edit_profile_pic.setOnClickListener(this);

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(this);

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
    }

    private void fetchingData() {

        if (fBGmailData_obj!=null) {

            if (fBGmailData_obj.getEmail() != null && !fBGmailData_obj.getEmail().equalsIgnoreCase("")) {
                email_edittext.setText(fBGmailData_obj.getEmail());
                Utilities.savePref(UserProfileActivity.this, "email", fBGmailData_obj.getEmail());

                LoginUsingEmail();
            }

            if (fBGmailData_obj.getName() != null && !fBGmailData_obj.getName().equalsIgnoreCase("")) {
                username_txt.setText(fBGmailData_obj.getName());
            }

            if (fBGmailData_obj.getPicture() != null && !fBGmailData_obj.getPicture().equalsIgnoreCase("")) {

                if (Utilities.loadPref(UserProfileActivity.this, "LoggedInWith", "")
                        .equalsIgnoreCase("Facebook")) {
                    try {
                        URL profile_pic = new URL("http://graph.facebook.com/" + fBGmailData_obj.getId() + "/picture?type=large");

                        Glide.with(UserProfileActivity.this).asBitmap().load(profile_pic.toString()).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                String root = Environment.getExternalStorageDirectory().toString();
                                File myDir = new File(root + "/req_images");
                                myDir.mkdirs();
                                Random generator = new Random();
                                int n = 10000;
                                n = generator.nextInt(n);
                                String fname = "Image-" + n + ".jpg";
                                File file = new File(myDir, fname);
                                Log.i(TAG, "" + file);
                                deletePath(file);

                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                selectedImageUri = FileProvider.getUriForFile(UserProfileActivity.this,
                                        "my.closet.fashion.style.FileProvider",
                                        file);

                                Glide.with(UserProfileActivity.this)
                                        .load(selectedImageUri).apply(requestOptions)
                                        .into(profile_image);
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {

                    // profile_image.setBackgroundResource(R.drawable.ic_user_profile);


                    Glide.with(UserProfileActivity.this)
                            .asBitmap()
                            .load(R.drawable.ic_user_profile).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {


                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/req_images");
                            myDir.mkdirs();
                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);
                            String fname = "Image-" + n + ".jpg";
                            File file = new File(myDir, fname);
                            Log.i(TAG, "" + file);


                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                resource.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            selectedImageUri = FileProvider.getUriForFile(UserProfileActivity.this,
                                    "my.closet.fashion.style.FileProvider",
                                    file);
                            deletePath(file);

                        }
                    });

                }
            } else {

                //profile_image.setBackgroundResource(R.drawable.ic_user_profile);

            }

        } else {


        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.login_btn:
                mixpanelAPI.track("Login");
                if (selectedImageUri != null && !selectedImageUri.toString().equalsIgnoreCase("")) {
                    saveUserProfilepath();
                } else {
                    saveuserDetails("");
                }
                break;
            case R.id.edit_profile_pic:
                mixpanelAPI.track("Profile Picture");
                changepicPopup();

                break;

            case R.id.menu:
                Logout_Popup(menu);
                break;
        }

    }

    private void changepicPopup() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.change_profile_dialog, null);

        cam_dialog = new Dialog(UserProfileActivity.this);
        cam_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cam_dialog.setContentView(popupView);
        cam_dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(cam_dialog.getWindow().getAttributes());

        DisplayMetrics dm;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Utilities.screenWidth = dm.widthPixels;
            Utilities.screenHeight = dm.heightPixels;

            lp.width = (int) (Utilities.screenWidth * 0.90);


        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Utilities.screenWidth = dm.widthPixels;
            Utilities.screenHeight = dm.heightPixels;

            lp.width = (int) (Utilities.screenWidth * 0.90);

        }

        close_btn = cam_dialog.findViewById(R.id.close_btn);
        camera_button = cam_dialog.findViewById(R.id.camera_button);
        gallery_button = cam_dialog.findViewById(R.id.gallery_button);

        lp.gravity = Gravity.CENTER;
        cam_dialog.getWindow().setAttributes(lp);


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_dialog.dismiss();
                mixpanelAPI.track("Close Popup");
            }
        });

        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mixpanelAPI.track("Camera Popup");
                cam_dialog.dismiss();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mixpanelAPI.track("Gallery Popup");
                cam_dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent = Intent.createChooser(intent, "Open");
                startActivityForResult(intent, GALLERY_PIC_REQUEST);

            }
        });
        cam_dialog.show();

    }



    public void Logout_Popup(View v) {
        popup = new IconizedMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_edit, popup.getMenu());

        popup.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.profile_edit:
                        mixpanelAPI.track("Logout");
                        if (Utilities.loadPref(UserProfileActivity.this, "LoggedInWith", "").equalsIgnoreCase("Facebook")) {
                            disconnectFromFacebook();

                        } else if (Utilities.loadPref(UserProfileActivity.this, "LoggedInWith", "").equalsIgnoreCase("Gmail")) {

                            gmailLogoutCall();

                        } else if (Utilities.loadPref(UserProfileActivity.this,"LoggedInWith","").equalsIgnoreCase("Mobile")){

                            FirebaseAuth.getInstance().signOut();

                            Utilities.savebooleanPref(getApplicationContext(), "HasLogged_In", false);
                            Utilities.savePref(UserProfileActivity.this, "email", "");
                            Utilities.savePref(UserProfileActivity.this, "Profile_Pic", "");
                            Utilities.savePref(UserProfileActivity.this, "Display_Name", "");
                            Utilities.savePref(UserProfileActivity.this, "Pen_Name", "");

                            SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("dialog",true);
                            editor.apply();


                            Intent returnTOLogin = new Intent(getApplicationContext(), FbGmailActivity.class);
                            returnTOLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            returnTOLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            returnTOLogin.putExtra("position",2);
                            startActivity(returnTOLogin);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);


                        } else if (Utilities.loadPref(UserProfileActivity.this,"LoggedInWith","").equalsIgnoreCase("Email")){

                            FirebaseAuth.getInstance().signOut();

                            Utilities.savebooleanPref(getApplicationContext(), "HasLogged_In", false);
                            Utilities.savePref(UserProfileActivity.this, "email", "");
                            Utilities.savePref(UserProfileActivity.this, "Profile_Pic", "");
                            Utilities.savePref(UserProfileActivity.this, "Display_Name", "");
                            Utilities.savePref(UserProfileActivity.this, "Pen_Name", "");

                            SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("dialog",true);
                            editor.apply();


                            Intent returnTOLogin = new Intent(getApplicationContext(), FbGmailActivity.class);
                            returnTOLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            returnTOLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(returnTOLogin);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);



                        }
                        else {

                            Utilities.savePref(UserProfileActivity.this, "email", "");
                            Utilities.savePref(UserProfileActivity.this, "Profile_Pic", "");
                            Utilities.savePref(UserProfileActivity.this, "Display_Name", "");
                            Utilities.savelist(UserProfileActivity.this, "Languages", new ArrayList<Object>());
                            Utilities.savebooleanPref(UserProfileActivity.this, "HasLogged_In", false);

                            Intent returnTOLogin = new Intent(getApplicationContext(), HomeActivity.class);
                            returnTOLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            returnTOLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(returnTOLogin);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);
                        }
                        break;
                }

                return false;
            }
        });

        popup.show();
    }

    private void disconnectFromFacebook() {

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();


    }

    private void gmailLogoutCall() {

        FirebaseAuth.getInstance().signOut();

                                Utilities.savebooleanPref(getApplicationContext(), "HasLogged_In", false);
                                Utilities.savePref(UserProfileActivity.this, "email", "");
                                Utilities.savePref(UserProfileActivity.this, "Profile_Pic", "");
                                Utilities.savePref(UserProfileActivity.this, "Display_Name", "");
                                Utilities.savePref(UserProfileActivity.this, "Pen_Name", "");

        SharedPreferences preferences = getSharedPreferences("preference",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("dialog",true);
        editor.apply();


                                Intent returnTOLogin = new Intent(getApplicationContext(), FbGmailActivity.class);
                                returnTOLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                returnTOLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(returnTOLogin);
                                finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);
                            }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            Bitmap image = null;

            if (requestCode == 0) {

                selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri, "Photo");
                image = BitmapFactory.decodeFile(selectedImagePath);

            } else {
                image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            }
            Bitmap finalBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(finalBitmap);
            canvas.drawBitmap(image, 0f, 0f, null);

            FileOutputStream outStream = null;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArray = bytes.toByteArray();
            destination = new File(Utilities.picturePath, System.currentTimeMillis() + "_Profile_img" + ".jpg");

            try {
                destination.createNewFile();
                outStream = new FileOutputStream(destination);
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                outStream.write(bytes.toByteArray());
                outStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            selectedImageUri = Uri.fromFile(destination);

            Glide.with(UserProfileActivity.this)
                    .load(selectedImageUri).apply(requestOptions)
                    .into(profile_image);

        } else if (requestCode == GALLERY_PIC_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {

            selectedImageUri = data.getData();
            String selectedImagePath = "";
            if (requestCode == 2) {
                selectedImagePath = getPath(selectedImageUri, "Photo");
            } else {
                imageBitmap = (Bitmap) data.getExtras().get("data");
            }

            selectedImageUri = Uri.fromFile(new File(selectedImagePath));

            Glide.with(UserProfileActivity.this)
                    .load(selectedImagePath).apply(requestOptions)
                    .into(profile_image);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("", "onConnectionFailed:" + connectionResult);
    }

    private void LoginUsingEmail() {

        Utilities.showLoading(UserProfileActivity.this, "Loading...");

        if (fBGmailData_obj!=null){

            profCollection.collection("UsersList")
                    .whereEqualTo("Email", fBGmailData_obj.getEmail())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                            Utilities.hideLoading();
                            update_key = document.getId();

                            Utilities.savePref(UserProfileActivity.this, "My_DbKey",update_key);
                            // Utilities.savePref(UserProfileActivity.this,"email", Objects.requireNonNull(document.get("Email")).toString());
                            penname_edittext.setText(Objects.requireNonNull(document.get("Pen_Name")).toString());
                            email_edittext.setText(Objects.requireNonNull(document.get("Email")).toString());
                            link_edittext.setText(Objects.requireNonNull(document.get("Website")).toString());
                            bio_edittext.setText(Objects.requireNonNull(document.get("Bio")).toString());
                            username_txt.setText(Objects.requireNonNull(document.get("Display_Name")).toString());

                            // TODO for retriving languages .not require now

                       /* if (!document.get("Language").toString().equalsIgnoreCase("")) {
                            langlist= (ArrayList<String>) document.get("Language");
                            if (langlist != null && langlist.size() > 0) {
                                language_Text.setTags(langlist);
                            }
                        } else {
                            language_Text.setTags("Select Languages");
                        }*/
                            selectedImageUri = Uri.parse(Objects.requireNonNull(document.get("Profile_Pic")).toString());

                            if (selectedImageUri!=null && !selectedImageUri.toString().equalsIgnoreCase("")){

                                Glide.with(UserProfileActivity.this)
                                        .load(selectedImageUri).apply(requestOptions)
                                        .into(profile_image);


                            }


                        }
                        Utilities.hideLoading();
                    } else {
                        Utilities.hideLoading();
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Utilities.hideLoading();

                }
            });

        } else {


            profCollection.collection("UsersList")
                    .whereEqualTo("Email", email_edittext.getText().toString())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                            Utilities.hideLoading();
                            update_key = document.getId();

                            Utilities.savePref(UserProfileActivity.this, "My_DbKey",update_key);
                            // Utilities.savePref(UserProfileActivity.this,"email", Objects.requireNonNull(document.get("Email")).toString());
                            penname_edittext.setText(Objects.requireNonNull(document.get("Pen_Name")).toString());
                            email_edittext.setText(Objects.requireNonNull(document.get("Email")).toString());
                            link_edittext.setText(Objects.requireNonNull(document.get("Website")).toString());
                            bio_edittext.setText(Objects.requireNonNull(document.get("Bio")).toString());
                            username_txt.setText(Objects.requireNonNull(document.get("Display_Name")).toString());

                            // TODO for retriving languages .not require now

                       /* if (!document.get("Language").toString().equalsIgnoreCase("")) {
                            langlist= (ArrayList<String>) document.get("Language");
                            if (langlist != null && langlist.size() > 0) {
                                language_Text.setTags(langlist);
                            }
                        } else {
                            language_Text.setTags("Select Languages");
                        }*/
                            selectedImageUri = Uri.parse(Objects.requireNonNull(document.get("Profile_Pic")).toString());

                            if (selectedImageUri!=null && !selectedImageUri.toString().equalsIgnoreCase("")){

                                Glide.with(UserProfileActivity.this)
                                        .load(selectedImageUri).apply(requestOptions)
                                        .into(profile_image);


                            }


                        }
                        Utilities.hideLoading();
                    } else {
                        Utilities.hideLoading();
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Utilities.hideLoading();

                }
            });


        }


    }

    private void saveUserProfilepath() {
        Utilities.showLoading(UserProfileActivity.this, "Updating...");

        if (!email_edittext.getText().toString().equalsIgnoreCase("") &&
                !penname_edittext.getText().toString().equalsIgnoreCase("")) {


            if (selectedImageUri.toString().contains("https://firebasestorage.googleapis.com/")) {

                saveuserDetails(selectedImageUri.toString());

            } else {



                final StorageReference ref = storageRef.child("Profiles/" +
                        UUID.randomUUID().toString());

                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("image/jpg")
                        .build();
                uploadTask = ref.putFile(selectedImageUri,metadata);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        if (task.isComplete()) {
                            downloaded_url = task.getResult().toString();

                            saveuserDetails(downloaded_url);
                        }
                    }
                });
            }
        } else {
            Utilities.hideLoading();
            Utilities.showToast(UserProfileActivity.this, getResources().getString(R.string.addsuername));
        }
    }


    public void saveuserDetails(final String url) {

            if (!email_edittext.getText().toString().equalsIgnoreCase("") &&
                    !penname_edittext.getText().toString().equalsIgnoreCase("")) {


                Map<String, Object> Details = new HashMap<>();
                String id = UUID.randomUUID().toString();
                Details.put("Id", id);
                Details.put("Profile_Pic", url);
                Details.put("Display_Name", username_txt.getText().toString());
                Details.put("Pen_Name", penname_edittext.getText().toString());
                Details.put("Email", email_edittext.getText().toString());
                Details.put("Bio", bio_edittext.getText().toString());
                Details.put("Website", link_edittext.getText().toString());
                Details.put("Location", location);
                Details.put("Fcm_Token",Utilities.loadPref(UserProfileActivity.this,"Fcm_Token",""));


                if (update_key != null && !update_key.equalsIgnoreCase("")) {

                    profCollection.collection("UsersList")
                            .document(update_key)
                            .set(Details)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {

                                    if (task.isComplete()) {
                                        Utilities.hideLoading();

                                        Utilities.showToast(UserProfileActivity.this, getString(R.string.profile_updated));
                                        Utilities.savebooleanPref(UserProfileActivity.this, "HasLogged_In", true);
                                        Utilities.savePref(UserProfileActivity.this, "Profile_Pic", url);
                                        Utilities.savePref(UserProfileActivity.this, "Pen_Name", penname_edittext.getText().toString());
                                        Utilities.savePref(UserProfileActivity.this, "email", email_edittext.getText().toString());


                                        Intent login_Intent = new Intent(UserProfileActivity.this, HomeActivity.class);
                                       // login_Intent.putExtra("position",2);
                                        startActivity(login_Intent);
                                        finish();
                                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                                    } else {
                                        Utilities.showToast(UserProfileActivity.this, "Try again later");
                                        Utilities.hideLoading();
                                    }
                                }
                            });
                } else {

                    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    profCollection.collection("UsersList").document(uid).set(Details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            String dbid = UUID.randomUUID().toString();
                            Utilities.savebooleanPref(UserProfileActivity.this, "HasLogged_In", true);
                            Utilities.savePref(UserProfileActivity.this, "Profile_Pic", url);
                            Utilities.savePref(UserProfileActivity.this, "Pen_Name", penname_edittext.getText().toString());
                            Utilities.savePref(UserProfileActivity.this, "email", email_edittext.getText().toString());
                            Utilities.savePref(UserProfileActivity.this,"My_DbKey", uid);

                            Utilities.hideLoading();
                            Utilities.showToast(UserProfileActivity.this, getResources().getString(R.string.profilesaved));
                            Intent login_Intent = new Intent(UserProfileActivity.this, HomeActivity.class);
                           // login_Intent.putExtra("position",2);
                            startActivity(login_Intent);
                            finish();
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);



                        }
                    });

                }

            }else {
                Utilities.showToast(UserProfileActivity.this, getResources().getString(R.string.addsuername));
            }
    }


    public String getPath(Uri uri, String type) {
        String document_id = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        }

        String path = "";
        if (type.equalsIgnoreCase("Photo")) {

            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor != null && cursor.moveToFirst()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        } else {

            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor != null && cursor.moveToFirst()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                cursor.close();
            }
        }

        if (path.equalsIgnoreCase("")) {
            path = document_id;
        }

        return path;
    }

    private void deletePath(File file) {

        if (file != null ) {
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("file Deleted :" + file);
                } else {
                    System.out.println("file not Deleted :" + file);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
