package my.closet.fashion.style.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.adapters.SampleAccAdapter;
import my.closet.fashion.style.modesl.SamplePics;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeesFragment extends Fragment {


    private GridView gridview;
    Query query;
    List<SamplePics> samplePicsArrayList;

    public TeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tees,container,false);
        gridview = (GridView) v.findViewById(R.id.tees_gridview);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstsampletop = sharedPreferences.getBoolean("firstsampletop",true);

        if(firstsampletop){

            Utilities.showToast(getActivity(),"Select a Top");

        }

        query = FirebaseFirestore.getInstance().collection("Sample Pics").whereEqualTo("subcategory","Tees");
        getclothes();

        return v;
    }

    private void getclothes() {

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e!=null){

                    return;
                }

                if (queryDocumentSnapshots != null) {
                    samplePicsArrayList =  queryDocumentSnapshots.toObjects(SamplePics.class);

                    SampleAccAdapter sampleAccAdapter = new SampleAccAdapter(Objects.requireNonNull(getContext()), R.layout.card_accessories, (ArrayList<SamplePics>) samplePicsArrayList);
                    gridview.setAdapter(sampleAccAdapter);

                }
            }
        });

    }

}
