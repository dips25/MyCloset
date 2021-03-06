package my.closet.fashion.style.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import my.closet.fashion.style.R;
import my.closet.fashion.style.ZoomImage;
import my.closet.fashion.style.adapters.Lookbookadapter;
import my.closet.fashion.style.modesl.Looks;

/**
 * A simple {@link Fragment} subclass.
 */
public class LookbookFragment extends Fragment {

    View view;
   public static GridView gridView;
    Realm realm;
    ArrayList<Looks> dresses = new ArrayList<>();
    my.closet.fashion.style.adapters.Lookbookadapter Lookbookadapter;
    private TextView toolbar_title;
    public TextView lookbook_tutorial_text;

    public LookbookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_lookbook, container, false);

        Realm.init(getContext());
       // Realm.getDefaultInstance().getConfiguration();
       // final RealmConfiguration configuration = new RealmConfiguration.Builder().name("sample.realm").schemaVersion(1).migration(new MyMigration()).build();
        realm = Realm.getDefaultInstance();
        findView(view);
        return view;
    }

    private void findView(View v) {



        lookbook_tutorial_text = (TextView)v.findViewById(R.id.lookbook_tutorial_text);

        gridView = (GridView) v.findViewById(R.id.grid);


        refresh();

        Lookbookadapter = new Lookbookadapter(Objects.requireNonNull(getContext()), R.layout.card_accessories, dresses);
        gridView.setAdapter(Lookbookadapter);
        Lookbookadapter.notifyDataSetChanged();

        if (Lookbookadapter.isEmpty()){

            lookbook_tutorial_text.setVisibility(View.VISIBLE);

        }else {

            lookbook_tutorial_text.setVisibility(View.GONE);
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Looks looks = (Looks) parent.getItemAtPosition(position);
                String imgname = looks.getImage_name();
                String lookname = looks.getStyle_name();
                id = looks.getId();


                Intent intent = new Intent(getActivity(), ZoomImage.class);
                intent.putExtra("imgname",dresses.get(position).getImage_name());
                intent.putExtra("stylename", dresses.get(position).getStyle_name());
                intent.putExtra("id", dresses.get(position).getId());
                intent.putExtra("lookid",dresses.get(position).getLookid());
                startActivity(intent);

                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    public void refresh() {
        RealmResults<Looks> dressesRealmList = realm.where(Looks.class).findAllSorted("id", Sort.DESCENDING);
        for (Looks looks : dressesRealmList) {
            dresses.add(looks);

        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dresses.clear();
    }




}
