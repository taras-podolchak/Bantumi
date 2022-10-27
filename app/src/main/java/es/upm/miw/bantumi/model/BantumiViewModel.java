package es.upm.miw.bantumi.model;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.upm.miw.bantumi.JuegoBantumi;

public class BantumiViewModel extends AndroidViewModel {

    private final ArrayList<MutableLiveData<Integer>> tablero;

    private final MutableLiveData<JuegoBantumi.Turno> turno;

    private final BantumiRepository bantumiRepository;

    private LiveData<List<BantumiEntity>> ldList;

    public BantumiViewModel(Application application) {
        super(application);
        turno = new MutableLiveData<>(JuegoBantumi.Turno.turnoJ1);
        tablero = new ArrayList<>(JuegoBantumi.NUM_POSICIONES);
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            tablero.add(i, new MutableLiveData<>(0));
        }
        bantumiRepository = new BantumiRepository(application);
        ldList = bantumiRepository.getAll();
    }

    /**
     * @return Devuelve el turno actual
     */
    public LiveData<JuegoBantumi.Turno> getTurno() {
        return turno;
    }

    /**
     * Establece el valor para turno
     *
     * @param turno valor
     */
    public void setTurno(JuegoBantumi.Turno turno) {
        this.turno.setValue(turno);
    }

    /**
     * Recupera el valor de una determinada posición
     *
     * @param pos posición
     * @return contenido de la posición <i>pos</i>
     */
    @NonNull
    public LiveData<Integer> getNumSemillas(int pos) {
        if (pos < 0 || pos >= JuegoBantumi.NUM_POSICIONES) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return tablero.get(pos);
    }

    /**
     * Asigna el valor v a la posición pos del tablero
     *
     * @param pos índice
     * @param v   valor
     */
    public void setNumSemillas(int pos, int v) {
        if (pos < 0 || pos >= JuegoBantumi.NUM_POSICIONES) {
            throw new ArrayIndexOutOfBoundsException();
        }
        tablero.get(pos).setValue(v);
    }

    @SuppressLint("NewApi")
    public String datosDeTablero() {
        return tablero.stream()
                .map(MutableLiveData::getValue)
                .map(Object::toString)
                .collect(Collectors.joining(",")) + ",";
    }
    public LiveData<List<BantumiEntity>> getAll() {
        return ldList;
    }

    public void insert(BantumiEntity bantumi) {
        bantumiRepository.insert(bantumi);
    }

    public void deleteAll() {
        bantumiRepository.deleteAll();
    }
    public void delete(BantumiEntity bantumi) {
        bantumiRepository.delete(bantumi);
    }
}