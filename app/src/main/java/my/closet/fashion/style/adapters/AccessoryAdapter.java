package my.closet.fashion.style.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.fragments.ClosetFragment;
import my.closet.fashion.style.modesl.Colors;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.Styles;

import static my.closet.fashion.style.adapters.BottomAdapter.mbcontext;
import static my.closet.fashion.style.adapters.FootWearAdapter.mfcontext;
import static my.closet.fashion.style.fragments.ClosetFragment.accrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.bottomrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.footrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.madapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mbottomadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mfootwearadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mtopadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.toprecycler;

/**
 * Created by biswa on 6/24/2017.
 */

public class AccessoryAdapter extends RecyclerView.Adapter<AccessoryAdapter.ViewHolder>  {

    public static Context mcontext;
    Bitmap bitmap;
    MixpanelAPI mixpanel;

    private static ArrayList<Dresses> mdresses;
    public Realm realm;
    CheckBox checkBox;







    //Boolean isMultiSelectionEnabled=false;



    public AccessoryAdapter(Context context, ArrayList<Dresses> dresses) {

        this.mdresses = dresses;
        this.mcontext=context;


    }


    public ArrayList<Integer> getItems(){

        ArrayList<Integer> dressname= new ArrayList<>();
        ArrayList<Integer> code = new ArrayList<>();

        for (Dresses dresses : mdresses){

            if (dresses.isSelected()){

                dressname.add(dresses.getId());
               // dressname.add(String.valueOf(dresses.getId()));
            }
        }
        return dressname;


    }





    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        CardView crd = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_accessories, parent, false);
        return new ViewHolder(crd);
    }


    public void onBindViewHolder( final ViewHolder holder, int position) {


        final Dresses dresses1=mdresses.get(position);

        bitmap = new ImageSaver(mcontext).setFileName(dresses1.getImagename()).setDirectoryName("mycloset").load();
        holder.imageview.setImageBitmap(bitmap);


        realm=Realm.getDefaultInstance();
        Realm.init(mcontext);


            realm.beginTransaction();
            dresses1.setSelected(false);
            realm.commitTransaction();

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm=Realm.getDefaultInstance();
                Realm.init(mcontext);

                realm.beginTransaction();
                dresses1.setSelected(!dresses1.isSelected());
                holder.cardView.setBackgroundColor(dresses1.isSelected() ? Color.LTGRAY : Color.WHITE);
                realm.commitTransaction();

                (ClosetFragment.instance).onClickcalled();


            }
        });


    }





    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return ViewHolder.MULTISELECTION;
    }



    public int getItemCount() {
        return mdresses.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public static final int MULTISELECTION = 2 ;
        CardView cardView;
        ImageView imageview;




       public ViewHolder(final CardView card) {
            super(card);

            cardView = card;


            imageview = (ImageView) card.findViewById(R.id.img);

            mcontext = card.getContext();
            imageview.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {

                                               // itemClicked.onItemClick(getAdapterPosition());



                                                PopupMenu menu = new PopupMenu(mcontext, v);
                                                MenuInflater inflater = menu.getMenuInflater();
                                                inflater.inflate(R.menu.popup_editdelete, menu.getMenu());
                                                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                                                    public boolean onMenuItemClick(MenuItem menuItem){

                                                        switch (menuItem.getItemId()){

                                                            case R.id.edit:
                                                                final Dresses dresses=mdresses.get(getPosition());

                                                                if (mixpanel!=null) {

                                                                    mixpanel.track("Edit Clicked");
                                                                }

                                                                realm=Realm.getDefaultInstance();
                                                                realm.init(mcontext);


                                                                Colors colors=realm.where(Colors.class).equalTo("colorset",dresses.getId()).findFirst();
                                                                Styles styles=realm.where(Styles.class).equalTo("styleset",dresses.getId()).findFirst();

                                                                Intent intent=new Intent(mcontext, Pic_info.class);
                                                                Bundle bundle=new Bundle();

                                                                bundle.putString("source","edit");

                                                                bundle.putInt("id",dresses.getId());
                                                                bundle.putString("category",dresses.getCategory());

                                                                if (colors!=null) {
                                                                    bundle.putString("black",colors.getBlack());
                                                                    bundle.putString("white", colors.getWhite());
                                                                    bundle.putString("grey", colors.getGrey());
                                                                    bundle.putString("beige", colors.getBeige());
                                                                    bundle.putString("red", colors.getRed());
                                                                    bundle.putString("pink", colors.getPink());
                                                                    bundle.putString("green", colors.getGreen());
                                                                    bundle.putString("blue", colors.getBlue());
                                                                    bundle.putString("yellow", colors.getYellow());
                                                                    bundle.putString("orange", colors.getOrange());
                                                                    bundle.putString("brown", colors.getBrown());
                                                                    bundle.putString("purple", colors.getPurple());
                                                                    bundle.putString("silver",colors.getSilver());
                                                                    bundle.putString("gold",colors.getGold());

                                                                }
                                                                if (styles!=null){

                                                                    bundle.putString("formal",styles.getFormal());
                                                                    bundle.putString("casual",styles.getCasual());
                                                                    bundle.putString("party",styles.getParty());
                                                                    bundle.putString("special",styles.getSpecial());
                                                                }
                                                                bundle.putString("imagepath",dresses.getImagename());
                                                                intent.putExtras(bundle);
                                                                mcontext.startActivity(intent);

                                                                return true;

                                                            case R.id.delete:

                                                                if (mixpanel!=null) {

                                                                    mixpanel.track("Delete Clicked");
                                                                }

                                                                realm=Realm.getDefaultInstance();
                                                                realm.init(mcontext);

                                                                AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
                                                                builder.setTitle(R.string.deletequery)
                                                                        .setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {


                                                                                Dresses dressess=mdresses.get(getPosition());

                                                                                Colors colors1=realm.where(Colors.class).equalTo("colorset",dressess.getId()).findFirst();
                                                                                Styles styles1=realm.where(Styles.class).equalTo("styleset",dressess.getId()).findFirst();
                                                                                new ImageSaver(mcontext).setFileName(dressess.getImagename()).setDirectoryName("mycloset").delete();
                                                                                realm.beginTransaction();
                                                                                colors1.deleteFromRealm();
                                                                                styles1.deleteFromRealm();
                                                                                dressess.deleteFromRealm();
                                                                                Toast.makeText(mcontext,"Picture Deleted", Toast.LENGTH_SHORT).show();
                                                                                realm.commitTransaction();
                                                                                refresh();

                                                                            }
                                                                        })
                                                                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.cancel();
                                                                            }
                                                                        });
                                                                AlertDialog alertDialog=builder.create();
                                                                alertDialog.show();

                                                                return true;

                                                        }
                                                        return true;
                                                    }
                                                });
                                                menu.show();

                                                return false;
                                            }
                                        });


            }


        private void refresh() {

            ArrayList<Dresses> accdresss = new ArrayList<>();
            RealmResults<Dresses> accdressesresults = realm.where(Dresses.class).contains("category", "Accessories").findAllSorted("id", Sort.DESCENDING);
            for (Dresses dressesss : accdressesresults) {
                accdresss.add(dressesss);
            }
            madapter = new AccessoryAdapter(mcontext, accdresss);
            accrecycler.setAdapter(madapter);
            madapter.notifyDataSetChanged();

            ArrayList<Dresses> bottomdresss = new ArrayList<>();
            RealmResults<Dresses> bottomdressesresults = realm.where(Dresses.class).contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);
            for (Dresses dressesss : bottomdressesresults) {
                bottomdresss.add(dressesss);
            }
            mbottomadapter = new BottomAdapter(mbcontext, bottomdresss);
            bottomrecycler.setAdapter(mbottomadapter);
            mbottomadapter.notifyDataSetChanged();

            ArrayList<Dresses> footdresss = new ArrayList<>();
            RealmResults<Dresses> footdressesresults = realm.where(Dresses.class).contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);
            for (Dresses dressesss : footdressesresults) {
                footdresss.add(dressesss);
            }
            mfootwearadapter = new FootWearAdapter(mfcontext, footdresss);
            footrecycler.setAdapter(mfootwearadapter);
            mfootwearadapter.notifyDataSetChanged();

            ArrayList<Dresses> topdresss = new ArrayList<>();
            RealmResults<Dresses> topdressesresults = realm.where(Dresses.class).contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);
            for (Dresses dressesss : topdressesresults) {
                topdresss.add(dressesss);
            }
            mtopadapter = new TopAdapter(TopAdapter.ccontext, topdresss);
            toprecycler.setAdapter(mtopadapter);
            mtopadapter.notifyDataSetChanged();

        }



     }


}




