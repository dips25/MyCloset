package my.closet.fashion.style.modesl;

import com.google.firebase.firestore.Exclude;

public class BlogPostId {

    @Exclude
    public String BlogPostId;

    public  <T extends  BlogPostId> T withId( final String id){
        this.BlogPostId = id;
        return (T) this;
    }

}
