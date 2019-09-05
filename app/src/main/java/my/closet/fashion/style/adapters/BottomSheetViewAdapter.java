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
import my.closet.fashion.style.modesl.Dresses;

public class BottomSheetViewAdapter extends ArrayAdapter {

    Dresses dresses;
    Context context;
    ArrayList<Dresses> dressesArrayList = new ArrayList<>();


    @Override
    public int getCount() {
        return super.getCount();
    }

    public BottomSheetViewAdapter(Context context, int resource, ArrayList<Dresses> dressesArrayList) {

        super(context,resource,dressesArrayList);
        this.context = context;
        this.dressesArrayList = dressesArrayList;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.card_accessories,parent,false);

        ImageView imageView = (ImageView) v.findViewById(R.id.img);

        ImageView tut_clicker = (ImageView) v.findViewById(R.id.tut_clicker);
        tut_clicker.setVisibility(View.GONE);

        Bitmap bitmap = new ImageSaver(context).setFileName(dressesArrayList.get(position).getImagename()).setDirectoryName("mycloset").load();
        imageView.setImageBitmap(bitmap);

        return v;


    }
}
