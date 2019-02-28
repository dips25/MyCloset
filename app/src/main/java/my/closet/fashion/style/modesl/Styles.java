package my.closet.fashion.style.modesl;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by biswa on 8/12/2017.
 */

public class Styles extends RealmObject {

    @PrimaryKey

    int styleset;
    String formal;
    String casual;
    String party;
    String special;


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


}
