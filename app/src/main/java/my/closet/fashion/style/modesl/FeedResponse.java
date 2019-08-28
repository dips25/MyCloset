package my.closet.fashion.style.modesl;

import java.io.Serializable;
import java.util.ArrayList;

public class FeedResponse extends BlogPostId implements Serializable{

    private String DocumentId;
    private String id;



    private int lookid;
    private String image;
    private ArrayList<String> text;
    private String timestamp;
    private String profile;
    private String penname;
    private String email;
    private String caption;
    private String bywhom;
    private String languages;
    private String like;
    String dbkey;


    public FeedResponse() {

    }

    public FeedResponse(String id,String image, ArrayList<String> text, String timestamp,String profile,
                        String penname,String email,String caption,String bywhom,String languages,String like) {
        this.image = image;
        this.text = text;
        this.timestamp = timestamp;
        this.profile=profile;
        this.penname=penname;
        this.email=email;
        this.caption=caption;
        this.bywhom=bywhom;
        this.languages=languages;
        this.like=like;

    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getBywhom() {
        return bywhom;
    }

    public void setBywhom(String bywhom) {
        this.bywhom = bywhom;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPenname() {
        return penname;
    }

    public void setPenname(String penname) {
        this.penname = penname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLookid() {
        return lookid;
    }

    public void setLookid(int lookid) {
        this.lookid = lookid;
    }

    public String getDbkey() {
        return dbkey;
    }

    public void setDbkey(String dbkey) {
        this.dbkey = dbkey;
    }


}
