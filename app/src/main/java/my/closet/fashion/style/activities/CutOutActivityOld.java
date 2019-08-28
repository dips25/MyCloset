package my.closet.fashion.style.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.customs.CutOut;

public class CutOutActivityOld extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_out);

        Uri uri = (Uri) Objects.requireNonNull(getIntent().getExtras()).get("imguri");

        CutOut.activity().src(uri).noCrop().start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CutOut.CUTOUT_ACTIVITY_REQUEST_CODE){

            switch (resultCode){

                case Activity.RESULT_OK:

                    Uri imageUri = CutOut.getUri(data);

                    Intent intent = new Intent(this, Pic_info.class);
                    intent.putExtra("source","add");
                    intent.putExtra("imguri",imageUri);
                    startActivity(intent);

                    break;

                case CutOut.CUTOUT_ACTIVITY_RESULT_ERROR_CODE:

                    Exception ex = CutOut.getError(data);
                    break;


            }


        }
    }
}
