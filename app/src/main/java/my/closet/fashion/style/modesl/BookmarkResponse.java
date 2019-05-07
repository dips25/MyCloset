package my.closet.fashion.style.modesl;

public class BookmarkResponse {

    String id;
    String email;
    String image;
    String timestamp;

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


}
