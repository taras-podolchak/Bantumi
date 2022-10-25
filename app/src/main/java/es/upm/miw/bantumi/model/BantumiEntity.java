package es.upm.miw.bantumi.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = BantumiEntity.TABLA)
public class BantumiEntity {
    static public final String TABLA = "bantumi";

    @PrimaryKey(autoGenerate = true)
    protected int uid;
    protected String nombreJugador01;
    protected String nombreJugador02;
    protected String numSemillasAlmacenJugador1;
    protected String numSemillasAlmacenJugador2;
    protected String fecha;

    public BantumiEntity(String nombreJugador01, String nombreJugador02, String numSemillasAlmacenJugador1, String numSemillasAlmacenJugador2, String fecha) {
        this.nombreJugador01 = nombreJugador01;
        this.nombreJugador02 = nombreJugador02;
        this.numSemillasAlmacenJugador1 = numSemillasAlmacenJugador1;
        this.numSemillasAlmacenJugador2 = numSemillasAlmacenJugador2;
        this.fecha = fecha;
    }

    public int getUid() {
        return uid;
    }

    public String getNombreJugador01() {
        return nombreJugador01;
    }

    public void setNombreJugador01(String nombreJugador01) {
        this.nombreJugador01 = nombreJugador01;
    }

    public String getNombreJugador02() {
        return nombreJugador02;
    }

    public void setNombreJugador02(String nombreJugador02) {
        this.nombreJugador02 = nombreJugador02;
    }

    public String getNumSemillasAlmacenJugador1() {
        return numSemillasAlmacenJugador1;
    }

    public void setNumSemillasAlmacenJugador1(String numSemillasAlmacenJugador1) {
        this.numSemillasAlmacenJugador1 = numSemillasAlmacenJugador1;
    }

    public String getNumSemillasAlmacenJugador2() {
        return numSemillasAlmacenJugador2;
    }

    public void setNumSemillasAlmacenJugador2(String numSemillasAlmacenJugador2) {
        this.numSemillasAlmacenJugador2 = numSemillasAlmacenJugador2;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
