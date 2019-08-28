package my.closet.fashion.style.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.FollowerAdapter;
import my.closet.fashion.style.modesl.FollowerFollowing;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {

    String key;
    RecyclerView listView;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());



    public FollowersFragment(String key) {

        this.key = key;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        listView = (RecyclerView) view.findViewById(R.id.followers_listview);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);

        FirebaseFirestore.getInstance().collection("UsersList").document(key).collection("FollowCollection").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e!=null){

                    return;
                }



                List<FollowerFollowing> followerFollowingList = Objects.requireNonNull(queryDocumentSnapshots).toObjects(FollowerFollowing.class);




                FollowerAdapter followerAdapter = new FollowerAdapter(getActivity(),followerFollowingList);
                listView.setAdapter(followerAdapter);
                followerAdapter.notifyDataSetChanged();


            }
        });



        return view;
    }

}
