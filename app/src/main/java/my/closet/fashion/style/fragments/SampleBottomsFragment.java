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
import my.closet.fashion.style.adapters.SampleBottomsAdapter;
import my.closet.fashion.style.modesl.SamplePics;

/**
 * A simple {@link Fragment} subclass.
 */
public class SampleBottomsFragment extends Fragment {




    private GridView gridView;
    private Query query;
    private List<SamplePics> samplePics;

    public SampleBottomsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_sample_bottoms, container, false);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstsampletop = sharedPreferences.getBoolean("firstsampletop",true);
        boolean firstsamplebottom = sharedPreferences.getBoolean("firstsamplebottom",true);
        gridView = (GridView) v.findViewById(R.id.skirts_gridview);


        if (firstsamplebottom && !firstsampletop){

            Utilities.showToast(getActivity(),"Select a Bottom");
            
        }

        query = FirebaseFirestore.getInstance().collection("Sample Pics").whereEqualTo("category","Bottoms");
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
                    samplePics =  queryDocumentSnapshots.toObjects(SamplePics.class);

                    SampleBottomsAdapter sampleAccAdapter = new SampleBottomsAdapter(Objects.requireNonNull(getContext()), R.layout.card_accessories, (ArrayList<SamplePics>) samplePics);
                    gridView.setAdapter(sampleAccAdapter);




                }
            }
        });

    }




}
