package my.closet.fashion.style.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.modesl.Looks;
import my.closet.fashion.style.R;

public class Lookbookadapter extends ArrayAdapter {

    ArrayList<Looks> dresses;
    Context context;
    ImageView grid_image;
    TextView look_name;


    public Lookbookadapter(@NonNull Context context, int resource,ArrayList<Looks> dressesArrayList) {


        super(context, resource,dressesArrayList);
        this.dresses=dressesArrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v=convertView;
        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v=inflater.inflate(R.layout.single_gridview,null );
        grid_image=(ImageView) v.findViewById(R.id.grid_image);
        look_name=(TextView) v.findViewById(R.id.look_name);

        //grid_image.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Bitmap bmp=new ImageSaver(context).setFileName(dresses.get(position).getImage_name()).setDirectoryName("mycloset").load();
        grid_image.setImageBitmap(bmp);
        look_name.setText(" " + dresses.get(position).getStyle_name());

        return v;


    }
}
