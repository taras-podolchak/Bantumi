package es.upm.miw.bantumi.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BantumiEntity.class}, version = 1, exportSchema = false)
public abstract class BantumiRoomDatabase extends RoomDatabase {

    public static final String BASE_DATOS = BantumiEntity.TABLA + ".db";

    public abstract IBantumiDAO grupoDAO();

    private static volatile BantumiRoomDatabase INSTANCE;

    static BantumiRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BantumiRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BantumiRoomDatabase.class, BASE_DATOS)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
