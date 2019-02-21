package com.simarro.practicas.proyecto_final;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simarro.practicas.proyecto_final.Dialogos.DialogoBorrar;
import com.simarro.practicas.proyecto_final.adapters.AdapterLeidos;
import com.simarro.practicas.proyecto_final.pojo.Libro;

import java.util.ArrayList;
import java.util.List;

public class ListaDeseos extends Fragment {
    RecyclerView rv;
    List<Libro> libros;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    AdapterLeidos adapterLeidos;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lista_deseos,container,false);

    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Lista de deseos");

        rv=getView().findViewById(R.id.Recycler);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        libros=new ArrayList<>();

        FirebaseDatabase db=FirebaseDatabase.getInstance();

        adapterLeidos=new AdapterLeidos(libros, new AdapterLeidos.OnItemClickListener() {
            @Override
            public void onItemClick(Libro item) {
                DialogoBorrar dialogo = new DialogoBorrar();
                dialogo.setLibro(item);
                dialogo.show(getFragmentManager(), "ff");
            }
        });
        rv.setAdapter(adapterLeidos);

        //Leer BBDD
        String userID = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference().child("Usuarios").child(userID).child("LibrosDeseados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                libros.removeAll(libros);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Libro l=ds.getValue(Libro.class);
                    libros.add(l);
                    Log.e("EEEE",l.toString());
                }
                adapterLeidos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
