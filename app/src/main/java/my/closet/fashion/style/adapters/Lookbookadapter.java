package my.closet.fashion.style.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import my.closet.fashion.style.R;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.modesl.Looks;

public class Lookbookadapter extends ArrayAdapter {

    ArrayList<Looks> dresses;
    Context context;
    ImageView grid_image;
    LayoutInflater inflater;



    public Lookbookadapter( Context context, int resource, ArrayList<Looks> dressesArrayList) {


        super(context, resource,dressesArrayList);
        this.dresses=dressesArrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return dresses.size();
    }

    @SuppressLint({"ViewHolder", "InflateParams"})

    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {

        inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){

            convertView = inflater.inflate(R.layout.single_gridview,null);
        }


        grid_image=(ImageView) convertView.findViewById(R.id.grid_image);


        Bitmap bmp=new ImageSaver(context).setFileName(dresses.get(position).getImage_name()).setDirectoryName("mycloset").load();
        grid_image.setImageBitmap(bmp);


        return convertView;


    }


    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
