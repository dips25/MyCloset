package my.closet.fashion.style.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.activities.UserPersonalActivity;
import my.closet.fashion.style.modesl.FeedResponse;
import my.closet.fashion.style.modesl.FollowerFollowing;

public class FollowerFollowingAdapter extends RecyclerView.Adapter<FollowerFollowingAdapter.ViewHolder> {

    List<FollowerFollowing> followerFollowingList;
    Context context;
    private RequestOptions requestOptions;
    private String My_DbKey;
    private String key;
    private FirebaseFirestore userCollection = FirebaseFirestore.getInstance();
    private String myemail;


    public FollowerFollowingAdapter(Context context , List<FollowerFollowing> followerFollowingList) {

        this.followerFollowingList = followerFollowingList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        myemail = Utilities.loadPref(context, "email", "");
        My_DbKey = Utilities.loadPref(context, "My_DbKey", "");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_followers_following_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FollowerFollowingAdapter.ViewHolder holder, int position) {

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);


        final FollowerFollowing followerFollowing = followerFollowingList.get(position);

        FirebaseFirestore.getInstance().collection("UsersList").whereEqualTo("Email",followerFollowing.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {

                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()) {

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                        holder.name.setText(documentSnapshot.get("Pen_Name").toString());
                        Glide.with(context).load(documentSnapshot.get("Profile_Pic")).apply(requestOptions).into(holder.circleImageView);


                    }
                }



            }
        });


        FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Followee").document(followerFollowing.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (Objects.requireNonNull(documentSnapshot).exists()){

                    holder.follow_button.setText("Following");
                    holder.follow_button.setBackgroundResource(R.drawable.following_button_square);
                    holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));


                }else {

                    holder.follow_button.setText("Follow");
                    holder.follow_button.setBackgroundResource(R.drawable.follow_button_square);
                    holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.white));

                }

            }
        });


        String name = followerFollowing.getName();
        String imgname = followerFollowing.getImgname();

      /*  if (name!=null){

            holder.name.setText(name);

        }else {

            holder.name.setText("--");
        }

        if (imgname!=null){

            Glide.with(context).load(imgname).apply(requestOptions).into(holder.circleImageView);

        }else {

            holder.circleImageView.setBackgroundResource(R.drawable.ic_user_profile);


        } */

        if (followerFollowing.getEmail().equals(myemail)){

            holder.follow_button.setText("Following");
            holder.follow_button.setBackgroundResource(R.drawable.following_button_square);
            holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

        }else {

            holder.follow_button.setText("Follow");
            holder.follow_button.setBackgroundResource(R.drawable.follow_button_square);
            holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.white));


        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserPersonalActivity.class);
                intent.putExtra("followerkey",key);
                intent.putExtra("follower",(Serializable) followerFollowing);
                context.startActivity(intent);
            }
        });



        holder.follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("UsersList")
                        .whereEqualTo("Email", followerFollowing.getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                            key = documentSnapshot.getId();

                            if (key != null) {

                                if (!Utilities.loadPref(context, "email", "").equalsIgnoreCase(followerFollowing.getEmail())) {
                                    userCollection.collection("UsersList/" + key + "/FollowCollection").document(myemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (!task.getResult().exists()) {

                                                    Map<String, Object> data = new HashMap<>();
                                                    String id = UUID.randomUUID().toString();

                                                    data.put("id", id);
                                                    data.put("email", Utilities.loadPref(context, "email", ""));
                                                    data.put("imgname",Utilities.loadPref(context, "Profile_Pic", ""));
                                                    data.put("name",Utilities.loadPref(context, "Pen_Name","" ));

                                                    userCollection.collection("UsersList/" + key + "/FollowCollection").document(myemail).set(data);

                                                } else {
                                                    userCollection.collection("UsersList/" + key + "/FollowCollection").document(myemail).delete();
                                                }
                                            } else {
                                                Log.i("LikeError", task.getException().getMessage());
                                                Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }

                                userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(followerFollowing.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()){

                                            if (!Objects.requireNonNull(task.getResult()).exists()){

                                                String id = UUID.randomUUID().toString();

                                                HashMap<String,Object> hashMap = new HashMap<>();

                                                hashMap.put("id",id);
                                                hashMap.put("email",followerFollowing.getEmail());
                                                hashMap.put("name",followerFollowing.getName());
                                                hashMap.put("imgname",followerFollowing.getImgname());

                                                userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(followerFollowing.getEmail()).set(hashMap);
                                                getPosts(key);



                                            }else {

                                                userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(followerFollowing.getEmail()).delete();

                                            }
                                        }

                                    }
                                });


                            }

                        }
                    }

                });
            }
        });





    }

    private void getPosts(String key) {

        userCollection.collection("UsersList").document(key).collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){

                        FeedResponse feedResponse = documentSnapshot.toObject(FeedResponse.class);
                        if (feedResponse != null) {
                            userCollection.collection("UsersList").document(My_DbKey).collection("Feed").add(feedResponse);
                        }
                    }




                }else {

                    Toast.makeText(context,"Check your connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return followerFollowingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircleImageView circleImageView;
        Button follow_button;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.list_user_name);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.follower_following_image);
            follow_button = (Button) itemView.findViewById(R.id.list_followers_button);
        }
    }
}
