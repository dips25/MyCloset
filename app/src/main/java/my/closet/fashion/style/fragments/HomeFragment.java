package my.closet.fashion.style.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.activities.FbGmailActivity;
import my.closet.fashion.style.activities.FullScreenViewActivity;
import my.closet.fashion.style.adapters.FeedsAdapter;
import my.closet.fashion.style.adapters.FeedsViewHolder;
import my.closet.fashion.style.customs.SpacesItemDecoration;
import my.closet.fashion.style.modesl.FeedResponse;

import static my.closet.fashion.style.activities.HomeActivity.bottomnav;
import static my.closet.fashion.style.activities.HomeActivity.look_tab;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private static final int REQUEST_CHOOSE_PHOTO = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 9;
    FeedsAdapter feedsAdapter;
    View view;
    private TextView toolbar_title;
    private RequestOptions requestOptions;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String myemail="";
    private RecyclerView homerecyleview;
    private ProgressBar prbLoading;
    private Query query;
    private StaggeredGridLayoutManager mLayoutManager;
    private FirestorePagingAdapter<FeedResponse, FeedsViewHolder> adapter;
    private MixpanelAPI mixpanelAPI;
    private String My_DbKey;
    private List<FeedResponse> feedResponses = new ArrayList<>();
    List<String> followeenameslist = new ArrayList<>();
    private DocumentSnapshot lastvisible;
    private boolean isScrolling;
    private int[] into;
    private int lastvisibleitem;
    private boolean isLastItemReached;
    List<FeedResponse> items = new ArrayList<>();
    FirebaseFirestore ref = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mixpanelAPI= MixpanelAPI.getInstance(getActivity(),"257c7d2e1c44d7d1ab6105af372f65a6");
         view = inflater.inflate(R.layout.fragment_home, container, false);

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);




        My_DbKey = Utilities.loadPref(getActivity(), "My_DbKey", "");
        myemail = Utilities.loadPref(getActivity(), "email", "");

       /* CollectionReference feedRef = FirebaseFirestore.getInstance()
                .collection("UsersList")
                .document(My_DbKey)
                .collection("Feed"); */

        if (myemail==null && Objects.requireNonNull(myemail).equalsIgnoreCase("")) {

            Intent intent = new Intent(getActivity(),FbGmailActivity.class);
            startActivity(intent);
            getActivity().finish();

        }else {

            Toast.makeText(getActivity(), "You are now signed in." + My_DbKey, Toast.LENGTH_SHORT).show();
        }








        toolbar_title = (TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.title);
        if (toolbar_title!=null) {
            toolbar_title.setText(R.string.home);
        }

        homerecyleview = (RecyclerView) view.findViewById(R.id.homerecyleview_layout);
        prbLoading = (ProgressBar) view.findViewById(R.id.prbLoading);

        homerecyleview.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        homerecyleview.setLayoutManager(mLayoutManager);
        homerecyleview.addItemDecoration(new SpacesItemDecoration(20));

        query = ref.collection("UsersList").document(My_DbKey).collection("Feed").orderBy("timestamp", Query.Direction.DESCENDING);
        SetUpRecycleView(query);

        homerecyleview.addOnScrollListener(new RecyclerView.OnScrollListener()
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


        return view;
    }



    private void SetUpRecycleView(Query query) {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(8)
                .build();

        FirestorePagingOptions<FeedResponse> options = new FirestorePagingOptions.Builder<FeedResponse>()
                .setQuery(query, config, new SnapshotParser<FeedResponse>() {

                    @Override
                    public FeedResponse parseSnapshot( DocumentSnapshot snapshot) {
                        FeedResponse chat = snapshot.toObject(FeedResponse.class);
                        Objects.requireNonNull(chat).setDocumentId(snapshot.getId());
                        return chat;
                    }
                }).setLifecycleOwner(this)
                .build();


        adapter = new FirestorePagingAdapter<FeedResponse, FeedsViewHolder>(options) {

            @Override
            public FeedsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_item, parent, false);

                return new FeedsViewHolder(view);
            }


            @Override
            public void onBindViewHolder(final FeedsViewHolder holder, int position, final FeedResponse model) {

                holder.bind(getActivity(), model);



                holder.picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mixpanelAPI.track("Full Screen");
                        Intent textint = new Intent(getActivity(), FullScreenViewActivity.class);
                        textint.putExtra("position", model);
                        Objects.requireNonNull(getActivity()).startActivity(textint);

                    }
                });

                //Getting likes count

                CollectionReference likeCollectionRef = db.collection("CommonFeed")
                        .document(model.getDocumentId()).collection("Likes");
                likeCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if (documentSnapshots.isEmpty()) {
                            holder.no_of_likes.setVisibility(View.GONE);
                        }

                        else {
                            holder.no_of_likes.setVisibility(View.VISIBLE);
                            int likeCount = documentSnapshots.size();
                            holder.no_of_likes.setText(String.valueOf(likeCount)+ " likes");
                        }

                    }
                });
                if (myemail != null && !myemail.equalsIgnoreCase("")) {

                    //Getting if post like or not

                    DocumentReference likereference = db.collection("CommonFeed")
                            .document(model.getDocumentId()).collection("Likes")
                            .document(myemail);

                    likereference.addSnapshotListener(new EventListener<DocumentSnapshot>() {

                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }
                            if (documentSnapshot.exists()) {
                                holder.like_text.setSelected(true);
                            } else {
                                holder.like_text.setSelected(false);
                            }
                        }
                    });
                }

                holder.like_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mixpanelAPI.track("Likes");
                        if (holder.like_text.isSelected()) {
                            holder.like_text.setSelected(false);
                        } else {
                            holder.like_text.setSelected(true);
                            holder.like_text.likeAnimation();
                        }
                        if (!Utilities.loadPref(Objects.requireNonNull(getActivity()), "email", "").equalsIgnoreCase("")) {
                            Likepost(model);
                        } else {
                            Intent ii = new Intent(getActivity(), FbGmailActivity.class);
                            startActivity(ii);
                            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
                        }
                    }
                });


                if (model.getEmail() != null) {

                    db.collection("UsersList")
                            .whereEqualTo("Email", model.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.get("Profile_Pic") != null && !Objects.requireNonNull(document.get("Profile_Pic")).toString().equalsIgnoreCase("")) {
                                        Glide.with(Objects.requireNonNull(getContext())).load(document.get("Profile_Pic")).apply(requestOptions).into(holder.profile_pic);
                                    } else {
                                        holder.profile_pic.setBackgroundResource(R.drawable.ic_user_profile);
                                    }
                                    holder.username_txt.setText(Objects.requireNonNull(document.get("Pen_Name")).toString());

                                }
                            }
                        }
                    });
                }



            }


            protected void onLoadingStateChanged(LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        prbLoading.setVisibility(View.VISIBLE);
                        break;
                    case LOADED:
                        prbLoading.setVisibility(View.GONE);
                        break;
                    case FINISHED:
                        prbLoading.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        prbLoading.setVisibility(View.GONE);
                        break;
                }
            }
        };

        homerecyleview.setAdapter(adapter);
    }

    private void Likepost(final FeedResponse model) {
        db.collection("CommonFeed/" + model.getDocumentId() + "/Likes").document(myemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!Objects.requireNonNull(task.getResult()).exists()) {

                        Map<String, Object> data = new HashMap<>();
                        String id = UUID.randomUUID().toString();

                        data.put("id", id);
                        data.put("email", Utilities.loadPref(getActivity(), "email", ""));

                        db.collection("CommonFeed/" + model.getDocumentId() + "/Likes").document(myemail).set(data);

                    } else {
                        db.collection("CommonFeed/" + model.getDocumentId() + "/Likes").document(myemail).delete();
                    }
                } else {
                    Log.i("LikeError", task.getException().getMessage());
                    Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onStart() {

        super.onStart();
        if(adapter!=null) {
            adapter.startListening();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null) {
            adapter.stopListening();
        }

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.upload_menu).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:

                return false;

            default:
                break;
        }

        return false;
    }






}
