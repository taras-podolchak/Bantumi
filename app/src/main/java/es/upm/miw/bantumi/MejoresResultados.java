package es.upm.miw.bantumi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import es.upm.miw.bantumi.model.BantumiEntity;
import es.upm.miw.bantumi.model.BantumiViewModel;

public class MejoresResultados extends AppCompatActivity {


    BantumiViewModel bantumiViewModel;
    Button botonEliminarItems;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mejores_resultados_list);

        final RecyclerView recyclerView = findViewById(R.id.list);
        final MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bantumiViewModel = new ViewModelProvider(this).get(BantumiViewModel.class);
        bantumiViewModel.getAll().observe(this, listBantumiEntity -> adapter.setItems(getTopTen(listBantumiEntity)));

        botonEliminarItems = findViewById(R.id.botonEliminarItems);
        botonEliminarItems.setOnClickListener(v -> new DeleteAlertDialog().show(getSupportFragmentManager(), "Eliminar registros"));
    }

    @SuppressLint("NewApi")
    public List<BantumiEntity> getTopTen(List<BantumiEntity> ldList) {
        return ldList
                .stream()
                .sorted(Comparator.<BantumiEntity>comparingInt(bantumi -> bantumi.almacenMasGrande(bantumi)).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}