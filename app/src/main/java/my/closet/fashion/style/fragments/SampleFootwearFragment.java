package my.closet.fashion.style.fragments;


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
import java.util.Objects;

import javax.annotation.Nullable;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.SampleFootwearAdapter;
import my.closet.fashion.style.modesl.SamplePics;

/**
 * A simple {@link Fragment} subclass.
 */
public class SampleFootwearFragment extends Fragment {


    private GridView gridview;
    private Query query;
    private ArrayList<SamplePics> samplePicsArrayList;

    public SampleFootwearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View v =  inflater.inflate(R.layout.fragment_sample_footwear, container, false);
        gridview = (GridView) v.findViewById(R.id.samplefootwear_gridview);
        query = FirebaseFirestore.getInstance().collection("Sample Pics").whereEqualTo("category","Footwear");
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
                    samplePicsArrayList = (ArrayList<SamplePics>) queryDocumentSnapshots.toObjects(SamplePics.class);
                    SampleFootwearAdapter sampleFootwearAdapter = new SampleFootwearAdapter(Objects.requireNonNull(getContext()), R.layout.card_accessories, samplePicsArrayList);
                    gridview.setAdapter(sampleFootwearAdapter);
                }
            }
        });





    }

}


