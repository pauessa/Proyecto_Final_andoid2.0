package com.simarro.practicas.proyecto_final;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import com.squareup.picasso.Picasso;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class DialogoLeidos extends DialogFragment {
    Libro l;
    DatabaseReference mDatabase;

    ZXingScannerView v;
    int pos;
    public void setLibro(Libro l){
        this.l=l;
    }
    public void setpos(int pos){
        this.pos=pos;
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
        valoracion.setText(l.getValoracionMedia()+"/5");
        builder.setView(view)

                .setTitle("Libro: "+l.getTitulo())
                .setPositiveButton("Puntuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        final EditText input = new EditText(getContext());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setHint("(MAX=5)");
                        alertDialog.setTitle(l.getTitulo());
                        alertDialog.setMessage("Introduce Putuacion del libro ");
                        alertDialog.setView(input);
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aplicar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int j=Integer.parseInt(input.getText().toString());
                                if(j>=5){
                                    j=5;
                                }
                               l.setValoracionMedia(j);
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                String userID = mAuth.getCurrentUser().getUid();
                                mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
                                mDatabase.child("Libros").child(l.getIsbn()).setValue(l);
                                mDatabase.child("Usuarios").child(userID).child("LibrosLeidos").child(l.getTitulo()).setValue(l);

                                alertDialog.cancel();

                            }
                        });
                        alertDialog.show();
                        dialog.cancel();

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                    }});

        return builder.create();
    }


}
