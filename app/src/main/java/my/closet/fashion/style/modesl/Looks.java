package my.closet.fashion.style.modesl;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Looks extends RealmObject {
@PrimaryKey
    int id;
    String image_name;
    String style_name;

    public Looks(){


    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getStyle_name() {
        return style_name;
    }

    public void setStyle_name(String style_name) {
        this.style_name = style_name;
    }

}
