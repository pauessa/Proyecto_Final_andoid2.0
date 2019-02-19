package com.simarro.practicas.proyecto_final;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import com.squareup.picasso.Picasso;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class DialogoBorrar extends DialogFragment {
    Libro l;
    DatabaseReference mDatabase;

    ZXingScannerView v;
    public void setLibro(Libro l){
        this.l=l;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_borrar,null);

        TextView saga=view.findViewById(R.id.txtSaga);
        saga.setText(l.getNombreSaga());
        ImageView portada=view.findViewById(R.id.imgPortada);
        Picasso.get().load(l.getPortada()).into(portada);

        TextView autor=view.findViewById(R.id.txtAutor);
        autor.setText(l.getAutor().getNombre());
        TextView npag=view.findViewById(R.id.Npag);
        npag.setText(Integer.toString(l.getnPaginas()));
        TextView valoracion=view.findViewById(R.id.Valoracion);
        valoracion.setText(l.getValoracionMedia()+"/10");
        builder.setView(view)

                .setTitle("Libro: "+l.getTitulo())
                .setPositiveButton("Borrar de mi lista", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
                        String userID = mAuth.getCurrentUser().getUid();
                        mDatabase.child("Usuarios").child(userID).child("LibrosDeseados").child(l.getTitulo()).setValue(null);
                        dialog.cancel();

                    }
                }).setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        })
                .setNegativeButton("Mover a leyendo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
                        String userID = mAuth.getCurrentUser().getUid();
                        mDatabase.child("Usuarios").child(userID).child("LibrosLeyendo").child(l.getTitulo()).setValue(l);
                        mDatabase.child("Usuarios").child(userID).child("LibrosDeseados").child(l.getTitulo()).setValue(null);

                        dialog.cancel();


                    }});

        return builder.create();
    }


}
