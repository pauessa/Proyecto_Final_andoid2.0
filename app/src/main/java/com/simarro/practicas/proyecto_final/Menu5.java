package com.simarro.practicas.proyecto_final;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simarro.practicas.proyecto_final.adapters.AdapterTodos;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import java.util.ArrayList;
import java.util.List;

public class Menu5 extends Fragment {


    RecyclerView rv;
    List<Libro> libros;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    AdapterTodos adapterTodos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu5, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CargarLibros cargar=new CargarLibros();
        cargar.execute();
        getActivity().setTitle("Lista de libros");
       // guardarPrueba();
        rv = getView().findViewById(R.id.Recycler);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        libros = new ArrayList<>();

        adapterTodos = new AdapterTodos(libros, new AdapterTodos.OnItemClickListener() {
            @Override
            public void onItemClick(Libro item) {
                int i=adapterTodos.getItemSelected();
                DialogoEditar dialogo = new DialogoEditar();
                dialogo.setLibro(item);
                dialogo.setpos(item.getPos());
                dialogo.show(getFragmentManager(), "ff");
            }
        });
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));
        rv.setAdapter(adapterTodos);

    }


    private void guardarPrueba() {

        DatabaseReference mDatabase;


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
        String userID = mAuth.getCurrentUser().getUid();

        Libro l = new Libro("hola");
        //l.setPortada(R.drawable.nocover);
        mDatabase.child("Usuarios").child(userID).child("LibrosDeseados").child(l.getTitulo()).setValue(l);
        l = new Libro("hola2");
        // l.setPortada(R.drawable.nocover);
        mDatabase.child("Usuarios").child(userID).child("LibrosDeseados").child(l.getTitulo()).setValue(l);
        l = new Libro("hola3");
        // l.setPortada(R.drawable.nocover);
        mDatabase.child("Usuarios").child(userID).child("LibrosDeseados").child(l.getTitulo()).setValue(l);


    }


    private class CargarLibros extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("Libros").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    libros.removeAll(libros);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Libro l = ds.getValue(Libro.class);
                        libros.add(l);
                        Log.e("EEEE", l.toString());
                    }
                    adapterTodos.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }


}
