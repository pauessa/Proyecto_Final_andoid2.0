package com.simarro.practicas.proyecto_final;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simarro.practicas.proyecto_final.adapters.AdapterLeidos;
import com.simarro.practicas.proyecto_final.adapters.Adapterleyendo;
import com.simarro.practicas.proyecto_final.pojo.Libro;

import java.util.ArrayList;
import java.util.List;

public class Menu3 extends Fragment {

    RecyclerView rv;
    List<Libro> libros;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Adapterleyendo adapterleyendo;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu3,container,false);

    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Lista de leyendo");

        rv=getView().findViewById(R.id.Recycler);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        libros=new ArrayList<>();

        FirebaseDatabase db=FirebaseDatabase.getInstance();

        adapterleyendo=new Adapterleyendo(libros, new Adapterleyendo.OnItemClickListener() {
            @Override
            public void onItemClick(final Libro item) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("hint");
                alertDialog.setTitle(item.getTitulo());
                alertDialog.setMessage("Introduce numero de paginas leidas (MAX="+item.getnPaginas()+")");
                alertDialog.setView(input);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aplicar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Integer.parseInt(input.getText().toString())>item.getnPaginas()){
                            item.setHojasleidas(item.getnPaginas());
                        }else{
                            item.setHojasleidas(Integer.parseInt(input.getText().toString()));
                        }
                        DatabaseReference mDatabase;
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
                        String userID = mAuth.getCurrentUser().getUid();
                        mDatabase.child("Usuarios").child(userID).child("LibrosLeyendo").child(item.getTitulo()).setValue(item);
                        adapterleyendo.notifyDataSetChanged();
                    }
                });
                if(item.getHojasleidas()==item.getnPaginas()){
                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Marcar como leido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference mDatabase;
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
                            String userID = mAuth.getCurrentUser().getUid();
                            mDatabase.child("Usuarios").child(userID).child("LibrosLeidos").child(item.getTitulo()).setValue(item);
                            mDatabase.child("Usuarios").child(userID).child("LibrosLeyendo").child(item.getTitulo()).setValue(null);

                        }
                    }) ;
                }
                alertDialog.show();

               // DialogoBorrar dialogo = new DialogoBorrar();
                //dialogo.setLibro(item);
                //dialogo.show(getFragmentManager(), "ff");
            }
        });
        rv.setAdapter(adapterleyendo);

        //Leer BBDD
        String userID = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference().child("Usuarios").child(userID).child("LibrosLeyendo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                libros.removeAll(libros);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Libro l=ds.getValue(Libro.class);
                    libros.add(l);
                    Log.e("EEEE",l.toString());
                }
                adapterleyendo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

