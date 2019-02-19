package com.simarro.practicas.proyecto_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.simarro.practicas.proyecto_final.pojo.Libro;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView escanerView;
    DatabaseReference mDatabase;
    String userID;
    Libro l;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    String prueba="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scanner);
        escanerView=new ZXingScannerView(this);
        setContentView(escanerView);
        escanerView.setResultHandler(this);
        escanerView.startCamera();
    }


    @Override
    public void handleResult(Result result) {
        prueba=result.getText();
        mostrarLibro(result.getText());
    }

    private void mostrarLibro(final String text) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
        mDatabase.child("Libros").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(text)) {
                    mDatabase.child("Libros").orderByKey().equalTo(text).addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                                Libro l = dataSnapshot.getValue(Libro.class);
                                DialogoScanner dialogo = new DialogoScanner();
                                dialogo.setLibro(l);
                                dialogo.setTipo(getIntent().getStringExtra("TIPO"));
                                dialogo.show(getSupportFragmentManager(), "ff");

                        }

                        @Override
                        public void onChildChanged( DataSnapshot dataSnapshot,  String s) {
                        }

                        @Override
                        public void onChildRemoved( DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved( DataSnapshot dataSnapshot,  String s) {
                        }

                        @Override
                        public void onCancelled( DatabaseError databaseError) {

                        }
                    });
                }else{
                    createSimpleDialog().show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"cancelado",Toast.LENGTH_SHORT).show();
            }
        });


    }





    @Override
    protected void onPause() {
        super.onPause();
        escanerView.startCamera();

    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
        } else if (prueba.equals("")){
            Toast.makeText(getBaseContext(), "Pulsa dos veces para salir", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
        escanerView.resumeCameraPreview(this);
        prueba="";


    }

    public AlertDialog createSimpleDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error")
                .setMessage("Libro no encontrado \n Quieres añadirlo a la base de datos?")
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i =new Intent(getApplicationContext(),CrearLibroActivity.class);
                                i.putExtra("ISBN",prueba);
                                startActivity(i);
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetCamara();
                            }
                        });

        return builder.create();
    }
    public void resetCamara(){
        escanerView.resumeCameraPreview(this);
    }

}
