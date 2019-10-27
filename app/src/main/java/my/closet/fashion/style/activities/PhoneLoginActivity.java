package my.closet.fashion.style.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;

public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText phone_no;
    EditText country_code;
    Button proceed;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress;
    private static final String TAG = PhoneLoginActivity.class.getSimpleName();
    CountryCodePicker countryCodePicker;
    private MixpanelAPI mixpanelAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        mixpanelAPI= MixpanelAPI.getInstance(PhoneLoginActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
//
        countryCodePicker.setNumberAutoFormattingEnabled(true);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                String code = credential.getSmsCode();

                if (code!=null){

                    phone_no.setText(code);

                    verifycode(code);



                }

                Log.d(TAG, "onVerificationCompleted:" + credential);

                mVerificationInProgress = false;


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent( String verificationId,
                                    PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId,token);
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                mixpanelAPI.track("OTP Verified");

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };


       // country_code = (EditText) findViewById(R.id.country_code_input);
        phone_no = (EditText) findViewById(R.id.number_input);
        proceed = (Button) findViewById(R.id.ph_auth_button);
        proceed.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ph_auth_button:

                mixpanelAPI.track("Phone Login Clicked");

                if (!validatePhoneNumber()) {
                    return;
                }

                Utilities.showLoading(PhoneLoginActivity.this, "Authenticating..");

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+" + countryCodePicker.getSelectedCountryCode().toString() + phone_no.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks

                mVerificationInProgress = true;
                break;


        }
    }

    private void verifycode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        mixpanelAPI.track("OTP Verified");

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                            if (user != null) {
                                user.getPhoneNumber();
                            }

                            if (user!=null){

                                Utilities.savePref(PhoneLoginActivity.this, "LoggedInWith", "Mobile");

                                Utilities.hideLoading();


                                Intent intent = new Intent(PhoneLoginActivity.this,UserProfileActivity.class);
                                //intent.putExtra("mob", Objects.requireNonNull(user.getPhoneNumber()).toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);


                            }
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private boolean validatePhoneNumber() {

        String phoneNumber = phone_no.getText().toString();
        String countrycode = phone_no.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(countrycode)) {
            phone_no.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

}
