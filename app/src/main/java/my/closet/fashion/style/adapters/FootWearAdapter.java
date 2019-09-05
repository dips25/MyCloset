package my.closet.fashion.style.adapters;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.Pic_info;
import my.closet.fashion.style.R;
import my.closet.fashion.style.customs.ImageSaver;
import my.closet.fashion.style.fragments.ClosetFragment;
import my.closet.fashion.style.modesl.Colors;
import my.closet.fashion.style.modesl.Dresses;
import my.closet.fashion.style.modesl.Styles;

import static my.closet.fashion.style.adapters.AccessoryAdapter.mcontext;
import static my.closet.fashion.style.adapters.BottomAdapter.mbcontext;
import static my.closet.fashion.style.adapters.TopAdapter.ccontext;
import static my.closet.fashion.style.fragments.ClosetFragment.accrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.bottomrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.footrecycler;
import static my.closet.fashion.style.fragments.ClosetFragment.madapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mbottomadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mfootwearadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.mtopadapter;
import static my.closet.fashion.style.fragments.ClosetFragment.toprecycler;

/**
 * Created by biswa on 7/4/2017.
 */

public class FootWearAdapter extends RecyclerView.Adapter<FootWearAdapter.ViewHolder> {

    public static Context mfcontext;
    public static ArrayList<Dresses> mdresses;
    Bitmap bitmap;

    public static RealmList<Colors>mcolors;
    Realm realm;
    MixpanelAPI mixpanelAPI;

    public FootWearAdapter(Context context, ArrayList<Dresses> dresses) {

        this.mfcontext = context;
        this.mdresses = dresses;
    }

    public ArrayList<Integer> getItems(){

        ArrayList<Integer> footdressname=new ArrayList<>();

        for (Dresses dresses : mdresses){

            if (dresses.isSelected()){

                footdressname.add(dresses.getId());
            }
        }
//        Toast.makeText(mfcontext, footdressname.toString(), Toast.LENGTH_SHORT).show();
        return footdressname;
    }

    public ViewHolder onCreateViewHolder(ViewGroup group, int i) {

        CardView crd = (CardView) LayoutInflater.from(group.getContext()).inflate(R.layout.card_accessories, group, false);
        return new ViewHolder(crd);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Dresses dresses = mdresses.get(position);
        bitmap=new ImageSaver(mfcontext).setFileName(dresses.getImagename()).setDirectoryName("mycloset").load();
        holder.imageview.setImageBitmap(bitmap);
        holder.tut_clicker.setVisibility(View.GONE);

        realm=Realm.getDefaultInstance();
        Realm.init(mfcontext);


        realm.beginTransaction();
        dresses.setSelected(false);
        realm.commitTransaction();
//        holder.cardView.setBackgroundColor(dresses.isSelected() ? Color.CYAN : Color.WHITE);


        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm=Realm.getDefaultInstance();
                Realm.init(mfcontext);


                realm.beginTransaction();
                dresses.setSelected(!dresses.isSelected());
                holder.cardView.setBackgroundColor(dresses.isSelected() ? Color.LTGRAY : Color.WHITE);
                realm.commitTransaction();

                (ClosetFragment.instance).onClickcalled();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemCount() {
        return mdresses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        public ImageView tut_clicker;
        CardView cardView;
        ImageView imageview;

        ViewHolder(CardView card) {

            super(card);
            cardView=card;
            imageview=(ImageView)card.findViewById(R.id.img);
            tut_clicker = (ImageView) card.findViewById(R.id.tut_clicker);
            mfcontext = card.getContext();

            imageview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu menu = new PopupMenu(mfcontext, v);
                    MenuInflater inflater = menu.getMenuInflater();
                    inflater.inflate(R.menu.popup_editdelete, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                        public boolean onMenuItemClick(MenuItem menuItem){

                            switch (menuItem.getItemId()){

                                case R.id.edit:

                                    if (mixpanelAPI!=null) {

                                        mixpanelAPI.track("Edit Clicked");
                                    }


                                    final Dresses dresses=mdresses.get(getPosition());
                                    realm=Realm.getDefaultInstance();
                                    realm.init(mfcontext);


                                    Colors colors=realm.where(Colors.class).equalTo("colorset",dresses.getId()).findFirst();
                                    Styles styles=realm.where(Styles.class).equalTo("styleset",dresses.getId()).findFirst();


                                    Intent intent=new Intent(mfcontext, Pic_info.class);
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
                                    mfcontext.startActivity(intent);

                                    return true;

                                case R.id.delete:

                                    if (mixpanelAPI!=null) {

                                        mixpanelAPI.track("Delete Clicked");
                                    }


                                    realm=Realm.getDefaultInstance();
                                    realm.init(mfcontext);

                                    AlertDialog.Builder builder=new AlertDialog.Builder(mfcontext);
                                    builder.setTitle(R.string.deletequery)
                                            .setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    realm.beginTransaction();
                                                    Dresses dressess=mdresses.get(getPosition());
                                                    Colors colors1=realm.where(Colors.class).equalTo("colorset",dressess.getId()).findFirst();
                                                    Styles styles1=realm.where(Styles.class).equalTo("styleset",dressess.getId()).findFirst();

                                                    colors1.deleteFromRealm();
                                                    styles1.deleteFromRealm();
                                                    dressess.deleteFromRealm();
                                                    Toast.makeText(mfcontext,"Picture Deleted",Toast.LENGTH_SHORT).show();

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

                                    AlertDialog alert1=builder.create();
                                    alert1.show();





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

            //Dresses dresses=new Dresses();
            for (Dresses dressesss : topdressesresults) {
                topdresss.add(dressesss);
            }

            mtopadapter = new TopAdapter(ccontext, topdresss);
            toprecycler.setAdapter(mtopadapter);
            mtopadapter.notifyDataSetChanged();

        }


    }
}




