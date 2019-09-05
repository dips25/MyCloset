package my.closet.fashion.style.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;



import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.modesl.SamplePics;

public class SampleFootwearAdapter extends ArrayAdapter {

    private ArrayList<SamplePics> samplefootwear;
    Context context;
    private RequestOptions requestOptions;

    public SampleFootwearAdapter( Context context, int resource, ArrayList<SamplePics> samplefootwear) {
        super(context, resource, samplefootwear);
        this.samplefootwear = samplefootwear;
        this.context = context;
    }

    @SuppressLint("ViewHolder")

    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {
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

        Glide.with(context).load(samplefootwear.get(position).getImgurl()).apply(requestOptions).into(img);

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
        return samplefootwear.size();
    }
}
