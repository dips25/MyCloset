package my.closet.fashion.style.modesl;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import my.closet.fashion.style.modesl.Colors;
import my.closet.fashion.style.modesl.Styles;

/**
 * Created by biswa on 6/22/2017.
 */

public class Dresses extends RealmObject {



    @PrimaryKey
    int id;
    String category;
    String styles;

    boolean isSelected=false;



    RealmList<Styles> stylesRealmList=new RealmList<>();
    String imagename;



    RealmList<Colors> colorsRealmList=new RealmList<>();








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


    }

