package my.closet.fashion.style.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;
import java.util.List;

import my.closet.fashion.style.R;
import my.closet.fashion.style.activities.FullScreenViewActivity;
import my.closet.fashion.style.modesl.FeedResponse;



public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

     ImageView picture;
     android.content.Context context;
    List<FeedResponse> feedResponses;
    private RequestOptions requestOptions;

    public FeedsAdapter(Context context, List<FeedResponse> feedResponses){

        this.context = context;
        this.feedResponses = feedResponses;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

         FeedResponse feedResponse = feedResponses.get(position);

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

            String img = feedResponse.getImage();

            if (img!=null){

                Glide.with(context).load(img).apply(requestOptions).into(picture);
            }

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mixpanelAPI.track("Full Screen");
                Intent textint = new Intent(context, FullScreenViewActivity.class);
                textint.putExtra("position", (Serializable) feedResponse);
                context.startActivity(textint);

            }
        });


    }

    @Override
    public int getItemCount() {
        return feedResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View view;



        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            picture = (ImageView) itemView.findViewById(R.id.picture);

        }




    }
}
