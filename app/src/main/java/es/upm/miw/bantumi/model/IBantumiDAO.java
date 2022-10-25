package es.upm.miw.bantumi.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IBantumiDAO {
    @Query("SELECT * FROM " + BantumiEntity.TABLA)
    LiveData<List<BantumiEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(BantumiEntity bantumi);

    @Query("DELETE FROM " + BantumiEntity.TABLA)
    void deleteAll();

 /*   @Delete
    void delete(BantumiEntity grupo);*/
}
