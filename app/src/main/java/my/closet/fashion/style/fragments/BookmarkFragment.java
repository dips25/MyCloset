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

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.activities.PictureDeletingActivity;
import my.closet.fashion.style.adapters.BookmarkViewHolder;
import my.closet.fashion.style.customs.SpacesItemDecoration;
import my.closet.fashion.style.modesl.BookmarkResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment {


    private View view;
    private TextView toolbar_title;
    private String My_DbKey;
    private CollectionReference feedRef;
    private RecyclerView bookmarkrecyleview;
    private ProgressBar prbLoading;
    private StaggeredGridLayoutManager mLayoutManager;
    private FirestorePagingAdapter<BookmarkResponse, BookmarkViewHolder> adapter;
    private RelativeLayout look_tab;
    private BottomNavigationView bottomnav;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_bookmark, container, false);

        My_DbKey = Utilities.loadPref(Objects.requireNonNull(getActivity()), "My_DbKey", "");
        feedRef = FirebaseFirestore.getInstance().collection("UsersList/" + My_DbKey + "/Bookmarks");


        findViews(view);
        return view;
    }

    private void findViews(View view) {

        bookmarkrecyleview = (RecyclerView) view.findViewById(R.id.bookmarkrecyleview_layout);
        prbLoading = (ProgressBar) view.findViewById(R.id.prbookLoading);

        bookmarkrecyleview.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        bookmarkrecyleview.setLayoutManager(mLayoutManager);
        bookmarkrecyleview.addItemDecoration(new SpacesItemDecoration(20));

        look_tab = getActivity().findViewById(R.id.linear);
        bottomnav = (BottomNavigationView) getActivity().findViewById(R.id.bnve_icon_selector);

        bookmarkrecyleview.addOnScrollListener(new RecyclerView.OnScrollListener()
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

            FirestorePagingOptions<BookmarkResponse> options = new FirestorePagingOptions.Builder<BookmarkResponse>()
                    .setLifecycleOwner(this)
                    .setQuery(query, config, new SnapshotParser<BookmarkResponse>() {

                        @Override
                        public BookmarkResponse parseSnapshot( DocumentSnapshot snapshot) {

                            return snapshot.toObject(BookmarkResponse.class);
                        }
                    })
                    .build();

            adapter = new FirestorePagingAdapter<BookmarkResponse, BookmarkViewHolder>(options) {

                @Override
                public BookmarkViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.user_feed_item, parent, false);
                    return new BookmarkViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(final BookmarkViewHolder holder,
                                                int position,
                                                final BookmarkResponse model) {
                    holder.bind(getActivity(), model);

                    holder.picture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent textint = new Intent(getActivity(), PictureDeletingActivity.class);
                           // ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()),holder.picture,"image");
                            Utilities.MyTab=false;
                            textint.putExtra("bookmark", model);
                            startActivity(textint);

                            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.fade_in,R.anim.fade_out);



                          //  Objects.requireNonNull(getActivity()).startActivity(textint,activityOptionsCompat.toBundle());

                        }
                    });



                }

                @Override
                protected void onLoadingStateChanged( LoadingState state) {
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

            bookmarkrecyleview.setAdapter(adapter);
        }

    }




