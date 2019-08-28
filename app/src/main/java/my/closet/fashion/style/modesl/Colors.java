package my.closet.fashion.style.modesl;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by biswa on 7/7/2017.
 */

public class Colors extends RealmObject implements Parcelable {

    public Colors() {

        //Empty constructor
    }

    String black;
    String white;
    String grey;
    String beige;
    String red;
    String pink;
    String silver;
    String blue;
    String green;
    String yellow;
    String orange;
    String brown;
    String purple;
    String gold;



    int colorset;


    protected Colors(Parcel in) {
        black = in.readString();
        white = in.readString();
        grey = in.readString();
        beige = in.readString();
        red = in.readString();
        pink = in.readString();
        silver = in.readString();
        blue = in.readString();
        green = in.readString();
        yellow = in.readString();
        orange = in.readString();
        brown = in.readString();
        purple = in.readString();
        gold = in.readString();
        colorset = in.readInt();
    }

    public static final Creator<Colors> CREATOR = new Creator<Colors>() {
        @Override
        public Colors createFromParcel(Parcel in) {
            return new Colors(in);
        }

        @Override
        public Colors[] newArray(int size) {
            return new Colors[size];
        }
    };

    public int getColorset() {
        return colorset;
    }

    public void setColorset(int colorset) {
        this.colorset = colorset;
    }





    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getGrey() {
        return grey;
    }

    public void setGrey(String grey) {
        this.grey = grey;
    }

    public String getBeige() {
        return beige;
    }

    public void setBeige(String beige) {
        this.beige = beige;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getPink() {
        return pink;
    }

    public void setPink(String pink) {
        this.pink = pink;
    }

    public String getSilver() {
        return silver;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public String getGreen() {
        return green;
    }

    public void setGreen(String green) {
        this.green = green;
    }

    public String getYellow() {
        return yellow;
    }

    public void setYellow(String yellow) {
        this.yellow = yellow;
    }

    public String getOrange() {
        return orange;
    }

    public void setOrange(String orange) {
        this.orange = orange;
    }

    public String getBrown() {
        return brown;
    }

    public void setBrown(String brown) {
        this.brown = brown;
    }

    public String getPurple() {
        return purple;
    }

    public void setPurple(String purple) {
        this.purple = purple;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(black);
        dest.writeString(white);
        dest.writeString(grey);
        dest.writeString(beige);
        dest.writeString(red);
        dest.writeString(pink);
        dest.writeString(silver);
        dest.writeString(blue);
        dest.writeString(green);
        dest.writeString(yellow);
        dest.writeString(orange);
        dest.writeString(brown);
        dest.writeString(purple);
        dest.writeString(gold);
        dest.writeInt(colorset);
    }
}

