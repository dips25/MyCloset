package my.closet.fashion.style.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import my.closet.fashion.style.BuildConfig;
import my.closet.fashion.style.MainActivity;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.modesl.FBGmailData;

public class FbGmailActivity extends AppCompatActivity implements View.OnClickListener , GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_ALL = 1111;
    private CallbackManager callbackManager;
    private FrameLayout google_lyt;
    private FrameLayout facebook_lyt;
    private SignInButton gmail_login;
    private LoginButton FBloginButton;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_gmail);

       /* callbackManager = CallbackManager.Factory.create();
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }*/

        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA,
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }

       findViews();
    }

    private void findViews() {

        google_lyt = (FrameLayout) findViewById(R.id.google_lyt);
        google_lyt.setOnClickListener(this);

       /* facebook_lyt = (FrameLayout) findViewById(R.id.facebook_lyt);
        facebook_lyt.setOnClickListener(this);*/

        gmail_login = (SignInButton) findViewById(R.id.google_login);
       // FBloginButton = (LoginButton) findViewById(R.id.facebook_login);
    /*    FBloginButton.setReadPermissions("email");
        FBloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("onError", "onError");
                Log.d("FBandGoogleActivity", exception.getCause().getMessage());
            }

        });*/


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, FbGmailActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        gmail_login.setSize(SignInButton.SIZE_STANDARD);
        gmail_login.setBackgroundResource(R.drawable.ic_google_plus);
        gmail_login.setScopes(gso.getScopeArray());
    }

  /*  private void getUserDetails(LoginResult loginResult) {

        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json_object, GraphResponse response) {

                        if (json_object != null) {

                            FBGmailData fbGmailData = new FBGmailData();
                            try {
                                fbGmailData.setEmail(json_object.get("email").toString());
                                fbGmailData.setName(json_object.get("name").toString());
                                fbGmailData.setPicture(json_object.get("picture").toString());
                                fbGmailData.setId(json_object.get("id").toString());

                                Utilities.savePref(FbGmailActivity.this, "LoggedInWith", "Facebook");

                                Intent fromFacebook_Intent = new Intent(FbGmailActivity.this, UserProfileActivity.class);
                                fromFacebook_Intent.putExtra("LoginData", fbGmailData);
                                startActivity(fromFacebook_Intent);
                                finish();
                                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email, picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }
*/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.google_lyt:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

            case R.id.facebook:

                break;


        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            updateUI(acct, true);
        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            Utilities.showLoading(FbGmailActivity.this,"Loading...");
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    Utilities.hideLoading();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void updateUI(GoogleSignInAccount accdetails, boolean isSignedIn) {
        if (isSignedIn) {


            if (accdetails != null) {

                //will get retrive all the details from gmail

              //  Utilities.showToast(FbGmailActivity.this,accdetails.getDisplayName().toString());

                FBGmailData fbGmailData=new FBGmailData();

                if(accdetails.getEmail()!= null && !accdetails.getEmail().toString().equalsIgnoreCase("")) {
                    fbGmailData.setEmail(accdetails.getEmail().toString());
                }
                if(accdetails.getDisplayName()!=null  && !accdetails.getDisplayName().toString().equalsIgnoreCase("")) {
                    fbGmailData.setName(accdetails.getDisplayName().toString());
                }

                if(accdetails.getId()!=null   && !accdetails.getId().toString().equalsIgnoreCase("")) {
                    fbGmailData.setId(accdetails.getId().toString());
                }

                if(accdetails.getPhotoUrl()!=null ) {
                    fbGmailData.setPicture(accdetails.getPhotoUrl().toString());
                }

                Utilities.savePref(FbGmailActivity.this, "LoggedInWith", "Gmail");

                Intent fromFacebook_Intent = new Intent(FbGmailActivity.this, UserProfileActivity.class);
                fromFacebook_Intent.putExtra("LoginData", fbGmailData);
                startActivity(fromFacebook_Intent);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
            }

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                } else {

                }
                return false;
            }
        }
        return true;
    }
}
