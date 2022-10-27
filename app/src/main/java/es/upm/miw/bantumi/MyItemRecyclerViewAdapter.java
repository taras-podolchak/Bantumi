package es.upm.miw.bantumi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.model.BantumiEntity;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button botonEliminarItem;


        public ViewHolder(View itemView) {
            super(itemView);
            mIdView = itemView.findViewById(R.id.item_number);
            mContentView = itemView.findViewById(R.id.item_content);
            botonEliminarItem = itemView.findViewById(R.id.botonEliminarItem);

        }
    }

    private final LayoutInflater mInflater;
    private List<BantumiEntity> itemsList;
    private Context mContex;

    public MyItemRecyclerViewAdapter(Context context) {
        this.mContex = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.mejores_resultados_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (itemsList != null) {
            BantumiEntity current = itemsList.get(position);
            holder.mIdView.setText(Integer.toString(position + 1));
            holder.mContentView.setText(current.getNombreJugador01() + ": " + current.getNumSemillasAlmacenJugador1()
                    + "\n" + current.getNombreJugador02() + ": " + current.getNumSemillasAlmacenJugador2()
                    + "\nFecha: " + current.getFecha());
        } else {
            holder.mContentView.setText("No item");
        }
        holder.botonEliminarItem.setOnClickListener(view -> {
            FragmentManager manager = ((AppCompatActivity) mContex).getSupportFragmentManager();
            new DeleteAlertDialog(itemsList.get(position)).show(manager, "Eliminar registro");
        });
    }

    public void setItems(List<BantumiEntity> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (itemsList == null)
                ? 0
                : itemsList.size();
    }
}