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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import my.closet.fashion.style.R;
import my.closet.fashion.style.Utilities;
import my.closet.fashion.style.modesl.DefaultFollowers;
import my.closet.fashion.style.modesl.FeedResponse;

public class DefaultFollowersAdapter extends RecyclerView.Adapter<DefaultFollowersAdapter.ViewHolder> {

    private List<DefaultFollowers> defaultFollowers = new ArrayList<>();
    Context context;

    private String My_DbKey;
    private String key;
    private String myemail;


    public DefaultFollowersAdapter(Context context, List<DefaultFollowers> defaultFollowers) {

        this.context = context;
        this.defaultFollowers = defaultFollowers;


    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_followers_following_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        myemail = Utilities.loadPref(context, "email", "");
        My_DbKey = Utilities.loadPref(context, "My_DbKey", "");

        final DefaultFollowers defaultFollower = defaultFollowers.get(position);

        holder.name.setText(defaultFollower.getName());
        String imgname = defaultFollower.getProfile_pic();

        if (imgname!=null){

            Glide.with(context).load(imgname).into(holder.profile_pic);


        }else {

            holder.profile_pic.setBackgroundResource(R.drawable.ic_user_profile);
        }

        FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Followee").document(defaultFollower.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (Objects.requireNonNull(documentSnapshot).exists()){

                    holder.follow_button.setText("Following");
                    holder.follow_button.setBackgroundResource(R.drawable.following_button_square_dialog);
                    holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));



                }else {

                    holder.follow_button.setText("Follow");
                    holder.follow_button.setBackgroundResource(R.drawable.follow_button_square_dialog);
                    holder.follow_button.setTextColor(ContextCompat.getColor(context,R.color.white));

                }

            }
        });

        holder.follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("UsersList").whereEqualTo("Email", defaultFollower.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                            key = documentSnapshot.getId();

                            if (key != null) {

                                if (!Utilities.loadPref(context, "email", "").equalsIgnoreCase(defaultFollower.getEmail())) {
                                    FirebaseFirestore.getInstance().collection("UsersList/" + key + "/FollowCollection").document(myemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                                                    FirebaseFirestore.getInstance().collection("UsersList/" + key + "/FollowCollection").document(myemail).set(data);

                                                } else {
                                                    FirebaseFirestore.getInstance().collection("UsersList/" + key + "/FollowCollection").document(myemail).delete();
                                                }
                                            } else {
                                                Log.i("LikeError", task.getException().getMessage());
                                                Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }

                                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Followee").document(defaultFollower.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()){

                                            if (!Objects.requireNonNull(task.getResult()).exists()){

                                                String id = UUID.randomUUID().toString();

                                                HashMap<String,Object> hashMap = new HashMap<>();

                                                hashMap.put("id",id);
                                                hashMap.put("email",defaultFollower.getEmail());
                                                hashMap.put("name",defaultFollower.getName());
                                                hashMap.put("imgname",defaultFollower.getProfile_pic());

                                                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Followee").document(defaultFollower.getEmail()).set(hashMap);
                                                getPosts(key);



                                            }else {

                                                FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Followee").document(defaultFollower.getEmail()).delete();

                                            }
                                        }else {

                                            Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                            }

                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {

                        Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return defaultFollowers.size();
    }



    private void getPosts(String key) {

        FirebaseFirestore.getInstance().collection("UsersList").document(key).collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){

                        FeedResponse feedResponse = documentSnapshot.toObject(FeedResponse.class);
                        if (feedResponse != null) {
                            FirebaseFirestore.getInstance().collection("UsersList").document(My_DbKey).collection("Feed").add(feedResponse);
                        }
                    }




                }else {

                    Toast.makeText(context,"Check your connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircleImageView profile_pic;
        Button follow_button;


        public ViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.list_user_name);
            profile_pic = (CircleImageView) v.findViewById(R.id.follower_following_image);
            follow_button = (Button) v.findViewById(R.id.list_followers_button);


        }
    }
}
