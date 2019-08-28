package my.closet.fashion.style.modesl;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by biswa on 6/22/2017.
 */

public class Dresses extends RealmObject implements Parcelable {



    @PrimaryKey
    int id;
    String category;
    String styles;

    boolean isSelected=false;

    RealmList<Styles> stylesRealmList=new RealmList<>();
    String imagename;

    RealmList<Colors> colorsRealmList=new RealmList<>();

    public Dresses() {

        //Empty Constructor
    }

    protected Dresses(Parcel in) {
        id = in.readInt();
        category = in.readString();
        styles = in.readString();
        isSelected = in.readByte() != 0;
        imagename = in.readString();
    }

    public static final Creator<Dresses> CREATOR = new Creator<Dresses>() {
        @Override
        public Dresses createFromParcel(Parcel in) {
            return new Dresses(in);
        }

        @Override
        public Dresses[] newArray(int size) {
            return new Dresses[size];
        }
    };

    public String getCategory(){
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getStyles(){
        return styles;
    }
    public void setStyles(String styles){
        this.styles=styles;
    }





    public String getImagename(){
        return imagename;
    }
    public void setImagename(String imagename){

        this.imagename=imagename;
    }


    public RealmList<Colors> getColorsRealmList() {
        return colorsRealmList;
    }

    public void setColorsRealmList(RealmList<Colors> colorsRealmList) {
        this.colorsRealmList = colorsRealmList;
    }

    public RealmList<Styles> getStylesRealmList() {
        return stylesRealmList;
    }

    public void setStylesRealmList(RealmList<Styles> stylesRealmList) {
        this.stylesRealmList = stylesRealmList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected=selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
        dest.writeString(styles);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(imagename);
    }
}

