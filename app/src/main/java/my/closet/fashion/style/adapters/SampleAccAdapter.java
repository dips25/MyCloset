package my.closet.fashion.style.adapters;

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

import java.util.List;

import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.modesl.SamplePics;

import static android.content.Context.MODE_PRIVATE;

public class SampleAccAdapter extends ArrayAdapter {

    private List<SamplePics> sampleacc;
    private  Context context;
    private RequestOptions requestOptions;
    private MixpanelAPI mixpanelAPI;

    public SampleAccAdapter( Context context, int resource,  List<SamplePics> sampleacc) {
        super(context, resource, sampleacc);
        this.context = context;
        this.sampleacc = sampleacc;
    }


    @Override
    public View getView(final int position,  View convertView,  final ViewGroup parent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstsampletop = sharedPreferences.getBoolean("firstsampletop",true);
        mixpanelAPI= MixpanelAPI.getInstance(context,"257c7d2e1c44d7d1ab6105af372f65a6");





        View view = convertView;

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.card_accessories,parent,false);

        ImageView img = view.findViewById(R.id.img);



        Glide.with(context).load(sampleacc.get(position).getImgurl()).apply(requestOptions).into(img);






        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mixpanelAPI.track("SampleTops Clicked");

                SamplePics samplePics = (SamplePics) getItem(position);

                Intent intent = new Intent(getContext(), Pic_info.class);
                intent.putExtra("source","sample");
                intent.putExtra("sampleobject", samplePics);
                getContext().startActivity(intent);

                SharedPreferences preferences = context.getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstsampletop",false);
                editor.apply();


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
