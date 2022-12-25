package com.deriska.apkmagang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class UpdateDataDialog extends AppCompatDialogFragment {

    private EditText updateNik, updateNama, updateAlamat;
    private UpdateDataDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_data_dialog, null);

        builder.setView(view)
                .setTitle("Update Data")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nik = updateNik.getText().toString();
                        String nama = updateNama.getText().toString();
                        String alamat = updateAlamat.getText().toString();
                        listener.update(nik, nama, alamat);
                    }
                });

        updateNik = view.findViewById(R.id.edt_updateId);
        updateNama = view.findViewById(R.id.edt_updateNama);
        updateAlamat = view.findViewById(R.id.edt_updateAlamat);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (UpdateDataDialogListener) context;
        } catch (ClassCastException e) {

        }
    }

    public interface UpdateDataDialogListener {
        void update(String nik, String nama, String alamat);
    }
}
