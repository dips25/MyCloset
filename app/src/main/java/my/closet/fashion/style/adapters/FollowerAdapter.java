package my.closet.fashion.style.adapters;

import android.content.Context;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.modesl.FeedResponse;
import my.closet.fashion.style.modesl.FollowerFollowing;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    Context context;
    List<FollowerFollowing> followerFollowings;
    private RequestOptions requestOptions;
    private String My_DbKey;
    private String key;
    private String myemail;
    private FirebaseFirestore userCollection = FirebaseFirestore.getInstance();


    public FollowerAdapter(Context context, List<FollowerFollowing> followerFollowingArrayList) {

        this.context = context;
        this.followerFollowings = followerFollowingArrayList;
    }

    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        My_DbKey = Utilities.loadPref(context, "My_DbKey", "");
        myemail = Utilities.loadPref(context, "email", "");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_followers_following_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FollowerAdapter.ViewHolder holder, int position) {

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);


        final FollowerFollowing following = followerFollowings.get(position);

        FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Followee").document(following.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){

                    return;
                }

                if (Objects.requireNonNull(documentSnapshot).exists()){

                    holder.follow_button.setText("Following");
                    holder.follow_button.setBackgroundResource(R.drawable.following_btn);
                    holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));


                }else {

                    holder.follow_button.setText("Follow");
                    holder.follow_button.setBackgroundResource(R.drawable.follow_btn);
                    holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.white));

                }

            }
        });

        String username = following.getName();
        String imgname = following.getImgname();

        if (username!=null) {

            holder.textView.setText(username);
        }else {

            holder.textView.setText("--");

        }

        if (imgname!=null){

            Glide.with(context).load(imgname).apply(requestOptions).into(holder.imageView);
        }else {

            holder.imageView.setBackgroundResource(R.drawable.ic_user_profile);


        }

        if (following.getEmail().equals(myemail)){

            holder.follow_button.setText("Following");
            holder.follow_button.setBackgroundResource(R.drawable.following_btn);
            holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

        }else {

            holder.follow_button.setText("Follow");
            holder.follow_button.setBackgroundResource(R.drawable.follow_btn);
            holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.white));


        }


        holder.follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("UsersList").whereEqualTo("Email", following.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                            key = documentSnapshot.getId();

                            if (key != null) {

                                if (!Utilities.loadPref(context, "email", "").equalsIgnoreCase(following.getEmail())) {
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

                                userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(following.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()){

                                            if (!Objects.requireNonNull(task.getResult()).exists()){

                                                String id = UUID.randomUUID().toString();

                                                HashMap<String,Object> hashMap = new HashMap<>();

                                                hashMap.put("id",id);
                                                hashMap.put("email",following.getEmail());
                                                hashMap.put("name",following.getName());
                                                hashMap.put("imgname",following.getImgname());

                                                userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(following.getEmail()).set(hashMap);
                                                getPosts(key);



                                            }else {

                                                userCollection.collection("UsersList").document(My_DbKey).collection("Followee").document(following.getEmail()).delete();

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
        return followerFollowings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView textView;
        Button follow_button;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (CircleImageView) itemView.findViewById(R.id.follower_following_image);
            textView = (TextView) itemView.findViewById(R.id.list_user_name);
            follow_button = (Button) itemView.findViewById(R.id.list_followers_button);
        }
    }
}
