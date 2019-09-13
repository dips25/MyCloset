package my.closet.fashion.style.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Objects;

import io.fabric.sdk.android.Fabric;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.modesl.FBGmailData;

public class FbGmailActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_ALL = 1111;
    private CallbackManager callbackManager;
    private FrameLayout google_lyt;
    private FrameLayout facebook_lyt;
    private SignInButton gmail_login;
    private LoginButton loginButton;
    private GoogleSignInClient mGoogleApiClient;
    private static final String TAG = FbGmailActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private MixpanelAPI mixpanelAPI;
    FirebaseAuth mauth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    private String idToken;
    private String name;
    private String email;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this,new Crashlytics());
        setContentView(R.layout.activity_fb_gmail);
        mixpanelAPI= MixpanelAPI.getInstance(FbGmailActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.facebook_lyt);
        loginButton.setReadPermissions("email", "public_profile");

        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                // Get signedIn user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //if user is signed in, we call a helper method to save the user details to Firebase
                if (user != null) {

                    Intent intent = new Intent(FbGmailActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    // User is signed in
                    // you could place other firebase code
                    //logic to save the user details to Firebase
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


     /*   if (Build.VERSION.SDK_INT >= 23 ) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } */

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


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
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
                mixpanelAPI.track("Gmail Login");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

            case R.id.facebook_lyt:

                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        handleFacebookAccessToken(loginResult.getAccessToken());


                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

                break;


        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {


        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mauth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FbGmailActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }

    private void handleSignInResult(GoogleSignInResult task) {

       /* try {

            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            updateUI(googleSignInAccount,true);

        } catch (ApiException e) {
            e.printStackTrace();
        } */

        if (task.isSuccess()) {
            GoogleSignInAccount user = task.getSignInAccount();

            idToken = Objects.requireNonNull(user).getIdToken();
            name = user.getDisplayName();
            email = user.getEmail();
           // prof = Objects.requireNonNull(acct.getPhotoUrl()).toString();

            FBGmailData fbGmailData=new FBGmailData();

            if(Objects.requireNonNull(user).getEmail()!= null && !user.getEmail().toString().equalsIgnoreCase("")) {
                fbGmailData.setEmail(user.getEmail().toString());
            }
            if(user.getDisplayName()!=null  && !user.getDisplayName().toString().equalsIgnoreCase("")) {
                fbGmailData.setName(user.getDisplayName().toString());
            }

            if(user.getId()!=null   && !user.getId().toString().equalsIgnoreCase("")) {
                fbGmailData.setId(user.getId().toString());
            }

            if(user.getPhotoUrl()!=null ) {
                fbGmailData.setPicture(user.getPhotoUrl().toString());
            }

            Utilities.savePref(FbGmailActivity.this, "LoggedInWith", "Gmail");


            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

            mauth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mauth.getCurrentUser();

                                Intent fromFacebook_Intent = new Intent(FbGmailActivity.this, UserProfileActivity.class);
                                fromFacebook_Intent.putExtra("LoginData", fbGmailData);
                                startActivity(fromFacebook_Intent);
                                finish();
                                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });



        } else {

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);


         /*   Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            } */

            //handleSignInResult(task);

        }
    }

    private void firebaseAuthWithGoogle(AuthCredential account) {



       // AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);


    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser cuser = mauth.getCurrentUser();

        if (cuser!=null){

            Intent intent = new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }




   /*     GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount!=null){

            updateUI(googleSignInAccount,true);
        }
       /* mGoogleApiClient.connect();
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
        } */
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null){
            mauth.removeAuthStateListener(authStateListener);
        }
    }

    private void updateUI(FirebaseUser user) {



            if (user != null) {

                //will get retrive all the details from gmail

              //  Utilities.showToast(FbGmailActivity.this,accdetails.getDisplayName().toString());

                FBGmailData fbGmailData=new FBGmailData();

                if(user.getEmail()!= null && !user.getEmail().toString().equalsIgnoreCase("")) {
                    fbGmailData.setEmail(user.getEmail().toString());
                }
                if(user.getDisplayName()!=null  && !user.getDisplayName().toString().equalsIgnoreCase("")) {
                    fbGmailData.setName(user.getDisplayName().toString());
                }

                user.getUid();
                if(!user.getUid().toString().equalsIgnoreCase("")) {
                    fbGmailData.setId(user.getUid().toString());
                }

                if(user.getPhotoUrl()!=null ) {
                    fbGmailData.setPicture(user.getPhotoUrl().toString());
                }

                Utilities.savePref(FbGmailActivity.this, "LoggedInWith", "Facebook");

                Intent fromFacebook_Intent = new Intent(FbGmailActivity.this, UserProfileActivity.class);
                fromFacebook_Intent.putExtra("LoginData", fbGmailData);
                startActivity(fromFacebook_Intent);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);


            }

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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
