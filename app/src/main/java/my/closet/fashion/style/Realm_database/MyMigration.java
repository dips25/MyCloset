package my.closet.fashion.style.Realm_database;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            schema.get("Looks")
                    .addField("lookid", int.class);

            oldVersion++;
        }

    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MyMigration);
    }
}
