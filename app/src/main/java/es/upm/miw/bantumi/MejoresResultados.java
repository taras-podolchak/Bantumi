package es.upm.miw.bantumi;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderGetKt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.model.BantumiEntity;
import es.upm.miw.bantumi.model.BantumiViewModel;

public class MejoresResultados extends AppCompatActivity {


    BantumiViewModel bantumiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mejores_resultados_list);


        final RecyclerView recyclerView = findViewById(R.id.list);
        final MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Use ViewModelProviders to associate the ViewModel with the UI controller
        // Get a new or existing ViewModel from the ViewModelProvider.
        // bantumiViewModel = ViewModelProviders.of(this).get(BantumiEntity.class);
        //bantumiViewModel = ViewModelProviders.of(this).get(BantumiViewModel.class);
        bantumiViewModel= new ViewModelProvider(this).get(BantumiViewModel.class);

       // ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(BantumiViewModel.class);
        // Add an observer on the LiveData returned by getAlphabetizedUsers.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        bantumiViewModel.getAll().observe(this, new Observer<List<BantumiEntity>>() {
            @Override
            public void onChanged(@Nullable final List<BantumiEntity> listBantumiEntity) {
                // Update the cached copy of the users in the adapter.
                adapter.setItems(listBantumiEntity);
            }
        });

    }
}