package my.closet.fashion.style.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import my.closet.fashion.style.R;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.modesl.Looks;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static android.content.Context.MODE_PRIVATE;

public class Lookbookadapter extends ArrayAdapter {

    ArrayList<Looks> dresses;
    Context context;
    ImageView grid_image;



    public Lookbookadapter( Context context, int resource, ArrayList<Looks> dressesArrayList) {


        super(context, resource,dressesArrayList);
        this.dresses=dressesArrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @SuppressLint({"ViewHolder", "InflateParams"})

    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstsamplebottom = sharedPreferences.getBoolean("firstlookbook",true);

        View v=convertView;
        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v=inflater.inflate(R.layout.single_gridview,null);

        grid_image=(ImageView) v.findViewById(R.id.grid_image);

        if (firstsamplebottom) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    View tutview = parent.getChildAt(0);

                    new MaterialTapTargetPrompt.Builder((Activity) context,R.style.MaterialTapTargetPromptTheme)
                            .setTarget(tutview)
                            .setSecondaryText("Click to Post")
                            .setPromptBackground(new RectanglePromptBackground())
                            .setPromptFocal(new RectanglePromptFocal())
                            .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                                @Override
                                public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {

                                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {

                                        SharedPreferences preferences = context.getSharedPreferences("prefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean("firstlookbook", false);
                                        editor.apply();

                                        prompt.finish();
                                    }
                                }
                            })
                            .show();


                }
            });
        }




        //grid_image.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Bitmap bmp=new ImageSaver(context).setFileName(dresses.get(position).getImage_name()).setDirectoryName("mycloset").load();
        grid_image.setImageBitmap(bmp);


        return v;


    }
}
