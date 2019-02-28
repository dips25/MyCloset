package my.closet.fashion.style;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.adapters.Lookbookadapter;
import my.closet.fashion.style.modesl.Looks;

public class Lookbook extends AppCompatActivity {

   static GridView gridView;
    Realm realm;
    ArrayList<Looks> dresses=new ArrayList<>();
    my.closet.fashion.style.adapters.Lookbookadapter Lookbookadapter;

    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookbook);

        gridView=(GridView) findViewById(R.id.grid);

        Realm.init(this);
        realm=Realm.getDefaultInstance();

        refresh();




        Lookbookadapter=new Lookbookadapter(this,R.layout.single_gridview,dresses);
        gridView.setAdapter(Lookbookadapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


             Looks looks = (Looks) parent.getItemAtPosition(position);
             String imgname=looks.getImage_name();
             String lookname=looks.getStyle_name();
             id=looks.getId();


                Intent intent=new Intent(Lookbook.this,ZoomImage.class);
                intent.putExtra("imgname",dresses.get(position).getImage_name());
                intent.putExtra("stylename",dresses.get(position).getStyle_name());
                intent.putExtra("id",dresses.get(position).getId());
                startActivity(intent);

            }
        });


    }

    public void refresh() {


        RealmResults<Looks> dressesRealmList=realm.where(Looks.class).findAllSorted("id", Sort.DESCENDING);

        for (Looks looks : dressesRealmList){

            dresses.add(looks);


        }
    }
}
