package my.closet.fashion.style.adapters;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import my.closet.fashion.style.R;
import my.closet.fashion.style.customs.ZoomImageView;
import my.closet.fashion.style.modesl.FeedResponse;
import xyz.hanks.library.bang.SmallBangView;


public class FeedsViewHolder extends RecyclerView.ViewHolder{
    private final RequestOptions requestOptions;
    public SmallBangView like_text;
    public ZoomImageView picture;
    public ImageView profile_pic;
    public TextView username_txt,no_of_likes;
    public CheckBox favorite_button;
    public LinearLayout profile_layout;
    public ImageView blog_like_btn;
    public FeedsViewHolder(View itemView) {
        super(itemView);

        requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.white_border);

        picture = (ZoomImageView) itemView.findViewById(R.id.picture);
        profile_pic=(ImageView)itemView.findViewById(R.id.profile_pic);
        username_txt=(TextView)itemView.findViewById(R.id.username_txt);
        no_of_likes=(TextView)itemView.findViewById(R.id.no_of_likes);
        profile_layout=(LinearLayout)itemView.findViewById(R.id.profile_layout);
        blog_like_btn=(ImageView)itemView.findViewById(R.id.blog_like_btn);
        like_text = itemView.findViewById(R.id.like_heart);
    }
    public void bind(Context context, FeedResponse model) {
        if(model.getImage()!=null && !model.getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(model.getImage()).apply(requestOptions).into(picture);
        }else {
        }

        // GlideLoadImage.loadImage(context,picture,model.getImage(),model.getImage());
    }
}
