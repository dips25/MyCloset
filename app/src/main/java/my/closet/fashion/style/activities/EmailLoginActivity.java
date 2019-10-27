package my.closet.fashion.style.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Objects;

import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;

public class EmailLoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    TextView forgot_password;

    Button login;
    private ProgressDialog rdialog;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private MixpanelAPI mixpanelAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        mixpanelAPI= MixpanelAPI.getInstance(EmailLoginActivity.this,"257c7d2e1c44d7d1ab6105af372f65a6");

        email = (EditText) findViewById(R.id.email_edittext);
        password = (EditText) findViewById(R.id.password_edittext);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        login = (Button) findViewById(R.id.emailloginbutton);


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mixpanelAPI.track("Forgot Password Clicked");

                sendResetLink();

            }
        });

        rdialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mixpanelAPI.track("Email Login Clicked");

                signin();
            }
        });


    }

    private void sendResetLink() {

        String s = email.getText().toString();

        if (TextUtils.isEmpty(s)){

            Toast.makeText(this, R.string.invalidemailtext, Toast.LENGTH_SHORT).show();

        } else {

            FirebaseAuth.getInstance().sendPasswordResetEmail(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {

                    if (task.isSuccessful()){

                        Toast.makeText(EmailLoginActivity.this, R.string.passwordresetlink, Toast.LENGTH_LONG).show();


                    }else {

                        Toast.makeText(EmailLoginActivity.this, "Error:" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();


                    }

                }
            });


        }
    }

    private void signin() {

        String remail = email.getText().toString();
        String rpassword = password.getText().toString();


       // rdialog.setTitle("Registering Account");
        rdialog.setMessage("Logging In...");
        rdialog.show();
        rdialog.setCanceledOnTouchOutside(false);

        if (TextUtils.isEmpty(remail)) {

            Toast.makeText(EmailLoginActivity.this, R.string.enteremail, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(rpassword)) {

            Toast.makeText(EmailLoginActivity.this,R.string.enterpassword, Toast.LENGTH_SHORT).show();


        } else {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(remail, rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Utilities.savePref(EmailLoginActivity.this, "LoggedInWith", "Email");

                        FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                        if (user!=null){


                            if (user.isEmailVerified()) {



                                Intent intent = new Intent(EmailLoginActivity.this, UserProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);


                            }else {

                                Toast.makeText(EmailLoginActivity.this, R.string.verifyemail, Toast.LENGTH_SHORT).show();
                            }





                        }
                        rdialog.dismiss();

                    } else {

                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(EmailLoginActivity.this, "Error:" + error, Toast.LENGTH_LONG).show();
                        rdialog.dismiss();
                    }

                }
            });

        }
    }
}
