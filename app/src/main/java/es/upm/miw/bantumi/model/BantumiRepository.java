package es.upm.miw.bantumi.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BantumiRepository {
    private IBantumiDAO iBantumiDAO;
    private LiveData<List<BantumiEntity>> ldList;

    /**
     * Constructor
     *
     * @param application app
     */
    public BantumiRepository(Application application) {
        BantumiRoomDatabase db = BantumiRoomDatabase.getDatabase(application);
        iBantumiDAO = db.grupoDAO();
        ldList = iBantumiDAO.getAll();
    }

    public LiveData<List<BantumiEntity>> getAll() {
        return ldList;
    }

    public long insert(BantumiEntity bantumi) {
        return iBantumiDAO.insert(bantumi);
    }

 /*   public void deleteAll() {
        iItemDAO.deleteAll();
    }

    public void delete(BantumiEntity item)  {
        iItemDAO.delete(item);
    }*/
}
