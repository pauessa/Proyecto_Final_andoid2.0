package com.simarro.practicas.proyecto_final;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simarro.practicas.proyecto_final.pojo.Autor;
import com.simarro.practicas.proyecto_final.pojo.Libro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Menu5 extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu5, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Menu 5");
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        guardarPrueba();

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


}
