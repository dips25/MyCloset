package my.closet.fashion.style.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import my.closet.fashion.style.R;
import my.closet.fashion.style.modesl.FeedResponse;


public class UserFeedsViewHolder extends RecyclerView.ViewHolder{
    private final RequestOptions requestOptions;
    public ImageView picture;

    public UserFeedsViewHolder(View itemView) {
        super(itemView);

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        picture = (ImageView) itemView.findViewById(R.id.picture);

    }
    public void bind(Context context, FeedResponse model) {
        Glide.with(context).load(model.getImage()).apply(requestOptions).into(picture);

    }

}
