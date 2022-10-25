package es.upm.miw.bantumi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import es.upm.miw.bantumi.model.BantumiEntity;
import es.upm.miw.bantumi.model.BantumiViewModel;
import es.upm.miw.bantumi.preferencias.AjustesActivity;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    JuegoBantumi juegoBantumi;
    private BantumiViewModel bantumiVM;
    int numInicialSemillas;
    private SharedPreferences preferencias;
    private TextView tvJugador1;
    private TextView tvJugador2;
    private TextView almacenJugador1;
    private TextView almacenJugador2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        crearObservadores();

        tvJugador1 = findViewById(R.id.tvPlayer1);
        tvJugador2 = findViewById(R.id.tvPlayer2);
        almacenJugador1 = findViewById(R.id.casilla_06);
        almacenJugador2 = findViewById(R.id.casilla_13);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        recuperarNombres();
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    integer -> mostrarValor(finalI, juegoBantumi.getSemillas(finalI)));
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                turno -> marcarTurno(juegoBantumi.turnoActual())
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.white));
                tvJugador1.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                tvJugador2.setTextColor(getColor(R.color.black));
                tvJugador2.setBackgroundColor(getColor(R.color.white));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador1.setBackgroundColor(getColor(R.color.white));
                tvJugador2.setTextColor(getColor(R.color.white));
                tvJugador2.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos   posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcReiniciarPartida:
                new FinalAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
                return true;
            case R.id.opcGuardarPartida:
                guardarPartida();
                return true;
            case R.id.opcRecuperarPartida:
                recuperarPartida();
                return true;
            case R.id.opcMejoresResultados:
                return true;
            case R.id.opcAjustes: // @todo Preferencias
                startActivity(new Intent(this, AjustesActivity.class));
                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;
            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }


    private void guardarPartida() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("BantumiGame", MODE_PRIVATE);
            fileOutputStream.write(juegoBantumi.serializa().getBytes());
            fileOutputStream.close();
            Snackbar.make(getWindow().getDecorView().getRootView(), "Partida guardada con exito ", Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.i(LOG_TAG, e.toString());
        }
    }

    private void recuperarPartida() {
        try {
            FileInputStream fileInputStream = openFileInput("BantumiGame");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String cadenaRecuperada;
            while ((cadenaRecuperada = bufferedReader.readLine()) != null) {
                stringBuilder.append(cadenaRecuperada).append("\n");
            }
            String fileData = stringBuilder.toString();
            fileInputStream.close();

            if (!fileData.equals("")) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Recuperando la partida...", Snackbar.LENGTH_SHORT).show();
                juegoBantumi.deserializa(fileData);
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), "No se encuentra la partida guardada", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i(LOG_TAG, e.toString());
        }
    }

    /**
     * Acción que se ejecuta al pulsar sobre un hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * Elige una posición aleatoria del campo del jugador2 y realiza la siembra
     * Si mantiene turno -> vuelve a jugar
     */
    void juegaComputador() {
        while (juegoBantumi.turnoActual() == JuegoBantumi.Turno.turnoJ2) {
            int pos = 7 + (int) (Math.random() * 6);    // posición aleatoria
            Log.i(LOG_TAG, "juegaComputador(), pos=" + pos);
            if (juegoBantumi.getSemillas(pos) != 0 && (pos < 13)) {
                juegoBantumi.jugar(pos);
            } else {
                Log.i(LOG_TAG, "\t posición vacía");
            }
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana Jugador 1"
                : "Gana Jugador 2";
        Snackbar.make(
                        findViewById(android.R.id.content),
                        texto,
                        Snackbar.LENGTH_LONG
                )
                .show();

        // @TODO guardar puntuación
        new FinalAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
        guardarPuntuacion();
    }

    private void recuperarNombres() {
        tvJugador1.setText(obtenerNombreDelJugador(R.string.key_jugador01, R.string.default_value_Nombre01));
        tvJugador2.setText(obtenerNombreDelJugador(R.string.key_jugador02, R.string.default_value_Nombre02));
    }

    private String obtenerNombreDelJugador(int key, int default_value) {
        return preferencias.getString(getString(key), getString(default_value));
    }

    @SuppressLint("NewApi")
    void guardarPuntuacion() {
        Intent replyIntent = new Intent();
        String nombreJugador01 = tvJugador1.getText().toString();
        String nombreJugador02 = tvJugador2.getText().toString();
        String almacenJugador1 = this.almacenJugador1.getText().toString();
        String almacenJugador2 = this.almacenJugador2.getText().toString();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        BantumiEntity bantumi = new BantumiEntity(nombreJugador01, nombreJugador02, almacenJugador1, almacenJugador2, fecha);
        bantumiVM.insert(bantumi);

        setResult(RESULT_OK, replyIntent);
    }
}