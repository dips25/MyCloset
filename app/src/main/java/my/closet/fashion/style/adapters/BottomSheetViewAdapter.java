package my.closet.fashion.style.adapters;

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

    private final int resource;
    // Dresses dresses;
    Context context;
    ArrayList<Dresses> dressesArrayList;



    public BottomSheetViewAdapter(Context context, int resource, ArrayList<Dresses> dresses) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.dressesArrayList = dresses;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;


        if (view==null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.card_accessories,parent,false);


        }

            ImageView img = view.findViewById(R.id.img);

        Bitmap bitmap = new ImageSaver(context).setDirectoryName("mycloset").setFileName(dressesArrayList.get(position).getImagename()).load();

        img.setImageBitmap(bitmap);





        return view;


    }


    @Override
    public int getCount() {

        return dressesArrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }




}
