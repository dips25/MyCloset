package my.closet.fashion.style.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.activities.HomeActivity;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.modesl.Colors;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.Styles;

import static my.closet.fashion.style.fragments.ClosetFragment.madapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mbottomadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mfootwearadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mtopadapter;
import static my.closet.fashion.style.adapters.AccessoryAdapter.mcontext;
import static my.closet.fashion.style.adapters.FootWearAdapter.mfcontext;
import static my.closet.fashion.style.fragments.ClosetFragment.accrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.bottomrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.footrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.toprecycler;

/**
 * Created by biswa on 7/4/2017.
 */

public class BottomAdapter extends RecyclerView.Adapter<BottomAdapter.ViewHolder> {

    public static Context mbcontext;
    Bitmap bitmap;

    ArrayList<Dresses>lookdresses;

    public static  ArrayList<Dresses> mdresses;
    MixpanelAPI mixpanel;


    Realm realm;

    public BottomAdapter(Context context,ArrayList<Dresses> dresses){

        this.mbcontext=context;
        this.mdresses=dresses;

    }

    public ArrayList<String> getItems(){

        ArrayList<String> bottdressname=new ArrayList<>();

        for (Dresses dresses : mdresses){

            if (dresses.isSelected()){

                bottdressname.add(dresses.getImagename());
            }
        }
        return bottdressname;
    }

    public ViewHolder onCreateViewHolder(ViewGroup group, int i){

        CardView crd=(CardView) LayoutInflater.from(group.getContext()).inflate(R.layout.card_accessories,group,false);
        return new ViewHolder(crd);



    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Dresses dresses=mdresses.get(position);
        bitmap=new ImageSaver(mbcontext).setFileName(dresses.getImagename()).setDirectoryName("mycloset").load();
        holder.imageview.setImageBitmap(bitmap);

        realm=Realm.getDefaultInstance();
        Realm.init(mbcontext);
        realm.beginTransaction();
        dresses.setSelected(false);
        realm.commitTransaction();

//        holder.cardView.setCardBackgroundColor(dresses.isSelected() ? Color.CYAN : Color.WHITE);

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm=Realm.getDefaultInstance();
                Realm.init(mbcontext);


                realm.beginTransaction();
                dresses.setSelected(!dresses.isSelected());
                holder.cardView.setBackgroundColor(dresses.isSelected() ? Color.LTGRAY : Color.WHITE);
                realm.commitTransaction();

                ((HomeActivity)mbcontext).onClickcalled();

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public int getItemCount(){
        return mdresses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageview;

        ViewHolder(CardView card){

            super(card);
            cardView=card;
            imageview=(ImageView)card.findViewById(R.id.img);
            mbcontext=card.getContext();

            imageview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu menu = new PopupMenu(mbcontext, v);
                    MenuInflater inflater = menu.getMenuInflater();
                    inflater.inflate(R.menu.popup_editdelete, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                        public boolean onMenuItemClick(MenuItem menuItem){

                            switch (menuItem.getItemId()){

                                case R.id.edit:

                                    if (mixpanel!=null) {

                                        mixpanel.track("Edit Clicked");
                                    }


                                    final Dresses dresses=mdresses.get(getPosition());
                                    realm=Realm.getDefaultInstance();
                                    realm.init(mbcontext);

                                    Colors colors=realm.where(Colors.class).equalTo("colorset",dresses.getId()).findFirst();
                                    Styles styles=realm.where(Styles.class).equalTo("styleset",dresses.getId()).findFirst();


                                    Intent intent=new Intent(mbcontext, Pic_info.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("source","edit");

                                    bundle.putInt("id",dresses.getId());
                                    bundle.putString("category",dresses.getCategory());

                                    bundle.putString("black",colors.getBlack());
                                    bundle.putString("white",colors.getWhite());
                                    bundle.putString("grey",colors.getGrey());
                                    bundle.putString("beige",colors.getBeige());
                                    bundle.putString("red",colors.getRed());
                                    bundle.putString("pink",colors.getPink());
                                    bundle.putString("green",colors.getGreen());
                                    bundle.putString("blue",colors.getBlue());
                                    bundle.putString("yellow",colors.getYellow());
                                    bundle.putString("orange",colors.getOrange());
                                    bundle.putString("brown",colors.getBrown());
                                    bundle.putString("purple",colors.getPurple());
                                    bundle.putString("silver",colors.getSilver());
                                    bundle.putString("gold",colors.getGold());

                                    bundle.putString("formal",styles.getFormal());
                                    bundle.putString("casual",styles.getCasual());
                                    bundle.putString("party",styles.getParty());
                                    bundle.putString("special",styles.getSpecial());


                                    bundle.putString("imagepath",dresses.getImagename());
                                    intent.putExtras(bundle);
                                    mbcontext.startActivity(intent);

                                    return true;

                                case R.id.delete:

                                    if (mixpanel!=null) {

                                        mixpanel.track("Delete Clicked");
                                    }


                                    realm=Realm.getDefaultInstance();
                                    realm.init(mbcontext);
                                    AlertDialog.Builder builder=new AlertDialog.Builder(mbcontext);
                                    builder.setTitle(R.string.deletequery)

                                            .setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    realm.beginTransaction();
                                                    final Dresses dressess=mdresses.get(getPosition());


                                                    Colors colors1=realm.where(Colors.class).equalTo("colorset",dressess.getId()).findFirst();
                                                    Styles styles1=realm.where(Styles.class).equalTo("styleset",dressess.getId()).findFirst();

                                                    colors1.deleteFromRealm();
                                                    styles1.deleteFromRealm();

                                                    dressess.deleteFromRealm();

                                                    realm.commitTransaction();
                                                    Toast.makeText(mbcontext,"Picture Deleted",Toast.LENGTH_SHORT).show();
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

            //Dresses dresses=new Dresses();
            for (Dresses dressesss : accdressesresults) {
                accdresss.add(dressesss);
            }

            madapter = new AccessoryAdapter(mcontext,accdresss);
            accrecycler.setAdapter(madapter);
            madapter.notifyDataSetChanged();

            ArrayList<Dresses> bottomdresss = new ArrayList<>();
            RealmResults<Dresses> bottomdressesresults = realm.where(Dresses.class).contains("category", "Bottoms").findAllSorted("id", Sort.DESCENDING);

            //Dresses dresses=new Dresses();
            for (Dresses dressesss : bottomdressesresults) {
                bottomdresss.add(dressesss);
            }

            mbottomadapter = new BottomAdapter(mbcontext, bottomdresss);
            bottomrecycler.setAdapter(mbottomadapter);
            mbottomadapter.notifyDataSetChanged();

            ArrayList<Dresses> footdresss = new ArrayList<>();
            RealmResults<Dresses> footdressesresults = realm.where(Dresses.class).contains("category", "Footwear").findAllSorted("id", Sort.DESCENDING);

            //Dresses dresses=new Dresses();
            for (Dresses dressesss : footdressesresults) {
                footdresss.add(dressesss);
            }

            mfootwearadapter = new FootWearAdapter(mfcontext, footdresss);
            footrecycler.setAdapter(mfootwearadapter);
            mfootwearadapter.notifyDataSetChanged();

            ArrayList<Dresses> topdresss = new ArrayList<>();
            RealmResults<Dresses> topdressesresults = realm.where(Dresses.class).contains("category", "Tops").findAllSorted("id", Sort.DESCENDING);

            //Dresses dresses=new Dresses();
            for (Dresses dressesss : topdressesresults) {
                topdresss.add(dressesss);
            }

            mtopadapter = new TopAdapter(TopAdapter.ccontext, topdresss);
            toprecycler.setAdapter(mtopadapter);
            mtopadapter.notifyDataSetChanged();

        }


    }

}




