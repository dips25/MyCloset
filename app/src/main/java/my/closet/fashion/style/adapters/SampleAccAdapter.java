package my.closet.fashion.style.adapters;

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

import java.util.List;

import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.modesl.SamplePics;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static android.content.Context.MODE_PRIVATE;

public class SampleAccAdapter extends ArrayAdapter {

    private List<SamplePics> sampleacc;
    private  Context context;
    private RequestOptions requestOptions;

    public SampleAccAdapter( Context context, int resource,  List<SamplePics> sampleacc) {
        super(context, resource, sampleacc);
        this.context = context;
        this.sampleacc = sampleacc;
    }


    @Override
    public View getView(final int position,  View convertView,  final ViewGroup parent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstsampletop = sharedPreferences.getBoolean("firstsampletop",true);



        View view = convertView;

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.card_accessories,parent,false);

        ImageView img = view.findViewById(R.id.img);

        Glide.with(context).load(sampleacc.get(position).getImgurl()).apply(requestOptions).into(img);

        if (firstsampletop) {

            View v = parent.getChildAt(0);

            new MaterialTapTargetPrompt.Builder((Activity) getContext(),R.style.MaterialTapTargetPromptTheme)
                    .setTarget(v)
                    .setSecondaryText("Click Here")
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPromptFocal(new RectanglePromptFocal())
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {

                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){

                                SharedPreferences preferences = context.getSharedPreferences("prefs",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("firstsampletop",false);
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
        return sampleacc.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
