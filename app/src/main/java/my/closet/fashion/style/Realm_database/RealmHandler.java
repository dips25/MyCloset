package my.closet.fashion.style.Realm_database;

import my.closet.fashion.style.modesl.Dresses;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by biswa on 6/27/2017.
 */

public class RealmHandler {

    Realm realm;

    public RealmHandler(Realm realm){
        this.realm=realm;
    }

    public void save(final Dresses dresses){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Dresses d=realm.copyToRealmOrUpdate(dresses);

            }
        });

    }

    public ArrayList<Dresses> retreive(){

        ArrayList<Dresses> dressesArrayList=new ArrayList<>();

        realm=Realm.getDefaultInstance();


        RealmResults<Dresses>dressesRealmResults=realm.where(Dresses.class).findAll();

        for (Dresses d:dressesRealmResults){

            dressesArrayList.add(d);
        }
        return dressesArrayList;


    }

    public void delete(){


    }
}
