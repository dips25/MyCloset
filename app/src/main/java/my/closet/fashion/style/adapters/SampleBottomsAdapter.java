package my.closet.fashion.style.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.modesl.SamplePics;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static android.content.Context.MODE_PRIVATE;

public class SampleBottomsAdapter extends ArrayAdapter {

    private ArrayList<SamplePics> samplebottoms;
    private Context context;
    private RequestOptions requestOptions;
    private MixpanelAPI mixpanelAPI;


    public SampleBottomsAdapter( Context context, int resource, ArrayList<SamplePics> samplebottoms) {
        super(context, resource,samplebottoms);
        this.context = context;
        this.samplebottoms = samplebottoms;
    }

    @SuppressLint("ViewHolder")

    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstsamplebottom = sharedPreferences.getBoolean("firstsamplebottom",true);
        mixpanelAPI= MixpanelAPI.getInstance(context,"257c7d2e1c44d7d1ab6105af372f65a6");

        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.card_accessories,parent,false);

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        ImageView img = (ImageView) view.findViewById(R.id.img);
        ImageView tut_clicker = (ImageView) view.findViewById(R.id.tut_clicker);
        tut_clicker.setVisibility(View.GONE);

        Glide.with(context).load(samplebottoms.get(position).getImgurl()).apply(requestOptions).into(img);

        if (firstsamplebottom && !sharedPreferences.getBoolean("firstsampletop",true)) {


            View v = parent.getChildAt(0);

            new MaterialTapTargetPrompt.Builder((Activity) getContext(),R.style.MaterialTapTargetPromptTheme_MaterialTapTargetSimple)
                    .setTarget(v)
                    .setSecondaryText("")
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPromptFocal(new RectanglePromptFocal())
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {

                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){

                                if (mixpanelAPI!=null){

                                    mixpanelAPI.track("SampleBottomTutorialClicked");
                                }

                                SharedPreferences preferences = context.getSharedPreferences("prefs",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("firstsamplebottom",false);
                                editor.apply();

                                prompt.finish();


                            }
                        }
                    })
                    .show();
        }


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SamplePics samplePics = (SamplePics) getItem(position);

                Intent intent = new Intent(getContext(), Pic_info.class);
                intent.putExtra("source","sample");
                intent.putExtra("sampleobject", samplePics);
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return samplebottoms.size();
    }
}
