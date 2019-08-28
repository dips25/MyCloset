package my.closet.fashion.style.modesl;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by biswa on 8/12/2017.
 */

public class Styles extends RealmObject implements Parcelable {
    public Styles() {

        //Empty constructor
    }

    @PrimaryKey

    int styleset;
    String formal;
    String casual;
    String party;
    String special;


    protected Styles(Parcel in) {
        styleset = in.readInt();
        formal = in.readString();
        casual = in.readString();
        party = in.readString();
        special = in.readString();
    }

    public static final Creator<Styles> CREATOR = new Creator<Styles>() {
        @Override
        public Styles createFromParcel(Parcel in) {
            return new Styles(in);
        }

        @Override
        public Styles[] newArray(int size) {
            return new Styles[size];
        }
    };

    public int getStyleset() {
        return styleset;
    }

    public void setStyleset(int styleset) {
        this.styleset = styleset;
    }


    public String getFormal() {
        return formal;
    }

    public void setFormal(String formal) {
        this.formal = formal;
    }

    public String getCasual() {
        return casual;
    }

    public void setCasual(String casual) {
        this.casual = casual;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(styleset);
        dest.writeString(formal);
        dest.writeString(casual);
        dest.writeString(party);
        dest.writeString(special);
    }
}
