package my.closet.fashion.style.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.Objects;

import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.activities.PictureDeletingActivity;
import my.closet.fashion.style.adapters.UserFeedsViewHolder;
import my.closet.fashion.style.customs.SpacesItemDecoration;
import my.closet.fashion.style.modesl.FeedResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPicsFragment extends Fragment {


    private View view;
    private TextView toolbar_title;
    private RecyclerView mypicsrecyleview;
    private ProgressBar prbLoading;
    private StaggeredGridLayoutManager mLayoutManager;
    private String My_DbKey="";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference feedRef;
    private Query query;
    private FirestorePagingAdapter<FeedResponse, UserFeedsViewHolder> adapter;
    private RelativeLayout look_tab;
    private BottomNavigationView bottomnav;

    public MyPicsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_my_pics, container, false);

        My_DbKey = Utilities.loadPref(Objects.requireNonNull(getActivity()), "My_DbKey", "");

        feedRef = db.collection("UsersList").document(My_DbKey).collection("Posts");
        findViews(view);

        return view;
    }

    private void findViews(View view) {


        mypicsrecyleview = (RecyclerView) view.findViewById(R.id.mypicsrecyleview_layout);
        prbLoading = (ProgressBar) view.findViewById(R.id.prbLoading);

        mypicsrecyleview.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mypicsrecyleview.setLayoutManager(mLayoutManager);
        mypicsrecyleview.addItemDecoration(new SpacesItemDecoration(20));

        look_tab = getActivity().findViewById(R.id.linear);
        bottomnav = (BottomNavigationView) getActivity().findViewById(R.id.bnve_icon_selector);


        mypicsrecyleview.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 && bottomnav.isShown() && look_tab.isShown())
                {
                    bottomnav.setVisibility(View.GONE);
                    look_tab.setVisibility(View.GONE);

                }else if (dy<0){

                    look_tab.setVisibility(View.VISIBLE);
                    bottomnav.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });



        SetUpRecycleView();
    }

    private void SetUpRecycleView() {


        Query query = feedRef.orderBy("timestamp", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<FeedResponse> options = new FirestorePagingOptions.Builder<FeedResponse>()
                .setLifecycleOwner(this)
                .setQuery(query, config, FeedResponse.class)
                .build();

        adapter = new FirestorePagingAdapter<FeedResponse, UserFeedsViewHolder>(options) {

            @Override
            public UserFeedsViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_feed_item, parent, false);
                return new UserFeedsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final UserFeedsViewHolder holder,
                                            int position,
                                            final FeedResponse model) {
                holder.bind(getActivity(), model);

                holder.picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent textint = new Intent(getActivity(), PictureDeletingActivity.class);
                        Utilities.MyTab=true;
                        textint.putExtra("picture", (Serializable) model);
                        getActivity().startActivity(textint);
                        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                    }
                });



            }

            @Override
            protected void onLoadingStateChanged(LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        //   prbLoading.setVisibility(View.VISIBLE);
                        break;
                    case LOADED:
                           prbLoading.setVisibility(View.GONE);
                        break;
                    case FINISHED:
                          prbLoading.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        retry();
                        //  prbLoading.setVisibility(View.GONE);
                        break;
                }
            }
        };

        mypicsrecyleview.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}
