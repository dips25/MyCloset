package my.closet.fashion.style.modesl;

import java.io.Serializable;

public class BookmarkResponse implements Serializable {

    String id;
    String email;
    String image;
    String timestamp;
    String description;
    private int lookid;
    String profileimage;

    public BookmarkResponse() {

    }

    public BookmarkResponse(String id, String email, String image,String timestamp) {
        this.id = id;
        this.email = email;
        this.image = image;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLookid() {
        return lookid;
    }

    public void setLookid(int lookid) {
        this.lookid = lookid;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
