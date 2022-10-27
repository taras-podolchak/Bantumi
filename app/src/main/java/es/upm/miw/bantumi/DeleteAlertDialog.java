package es.upm.miw.bantumi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import es.upm.miw.bantumi.model.BantumiEntity;
import es.upm.miw.bantumi.model.BantumiViewModel;

public class DeleteAlertDialog extends AppCompatDialogFragment {
    private BantumiEntity bantumi;

    public DeleteAlertDialog() {

    }

    public DeleteAlertDialog(BantumiEntity bantumi) {
        this.bantumi = bantumi;
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Eliminando")
                .setMessage("Estas seguro que quieres eliminar?")
                .setPositiveButton(
                        getString(R.string.txtDialogoFinalAfirmativo),
                        (dialog, which) -> {
                            if (bantumi == null)
                                new ViewModelProvider(this).get(BantumiViewModel.class).deleteAll();
                            else
                                new ViewModelProvider(this).get(BantumiViewModel.class).delete(bantumi);
                        }
                ).setNegativeButton(
                        getString(R.string.txtDialogoFinalNegativo),
                        (dialog, which) -> dialog.dismiss()
                );
        return builder.create();
    }
}
