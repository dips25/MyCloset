package my.closet.fashion.style.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Objects;

import my.closet.fashion.style.R;

public class EmailSignUpActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText cnfpassword;
    private Button login;
    private ProgressDialog rdialog;
    private MixpanelAPI mixpanelAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);

        email = findViewById(R.id.lemail_edittext);
        password = findViewById(R.id.lpassword_edittext);
        cnfpassword = findViewById(R.id.lcnf_password_edittext);

        mixpanelAPI= MixpanelAPI.getInstance(EmailSignUpActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        login = findViewById(R.id.lemailloginbutton);

        rdialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mixpanelAPI.track("SignUp Clicked");

                signup();
            }
        });
    }

    private void signup() {

        String remail = email.getText().toString();
        String rpassword = password.getText().toString();
        String rcnfpass = cnfpassword.getText().toString();

        rdialog.setTitle("Registering Account");
        rdialog.setMessage("Please wait...");
        rdialog.show();
        rdialog.setCanceledOnTouchOutside(false);

        if (TextUtils.isEmpty(remail)) {

            Toast.makeText(EmailSignUpActivity.this, R.string.enteremail, Toast.LENGTH_SHORT).show();
            rdialog.dismiss();

        } else if (TextUtils.isEmpty(rpassword)) {

            Toast.makeText(EmailSignUpActivity.this, R.string.enterpassword, Toast.LENGTH_SHORT).show();
            rdialog.dismiss();

        }else if (TextUtils.isEmpty(rcnfpass)) {


            Toast.makeText(EmailSignUpActivity.this, R.string.cnfpassword, Toast.LENGTH_SHORT).show();
            rdialog.dismiss();

        }else if (!rcnfpass.equalsIgnoreCase(rpassword)){

            Toast.makeText(EmailSignUpActivity.this, R.string.passwordmismatch, Toast.LENGTH_SHORT).show();
            rdialog.dismiss();


        } else {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(remail, rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                       // Utilities.savePref(EmailSignUpActivity.this, "LoggedInWith", "Email");

                        FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                        if (user != null) {

                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {

                                    Intent intent = new Intent(EmailSignUpActivity.this, EmailLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    //Toast.makeText(EmailSignUpActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);

                                    Toast.makeText(EmailSignUpActivity.this, R.string.verificationlinksent, Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {

                                    Toast.makeText(EmailSignUpActivity.this, "Error:"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });



                        }
                        rdialog.dismiss();

                    } else {

                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(EmailSignUpActivity.this, "Error:" + error, Toast.LENGTH_LONG).show();
                        rdialog.dismiss();
                    }

                }
            });

        }
    }
}
