package my.closet.fashion.style.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import my.closet.fashion.style.R;
import my.closet.fashion.style.adapters.FollowerFollowingAdapter;
import my.closet.fashion.style.modesl.FollowerFollowing;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    String key;
    RecyclerView following_recycler_view;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());




    public FollowingFragment(String key) {
        this.key = key;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_following, container, false);

        following_recycler_view = (RecyclerView) view.findViewById(R.id.following_listview);
        following_recycler_view.setLayoutManager(linearLayoutManager);
        following_recycler_view.setHasFixedSize(true);

        FirebaseFirestore.getInstance().collection("UsersList").document(key).collection("Followee").addSnapshotListener((queryDocumentSnapshots, e) -> {

            if (e!=null){

                return;
            }

            List<FollowerFollowing> mfollowerFollowingList = Objects.requireNonNull(queryDocumentSnapshots).toObjects(FollowerFollowing.class);


            FollowerFollowingAdapter followerFollowingAdapter = new FollowerFollowingAdapter(getActivity(),mfollowerFollowingList);
            following_recycler_view.setAdapter(followerFollowingAdapter);
            followerFollowingAdapter.notifyDataSetChanged();


        });


        return  view;
    }

}
