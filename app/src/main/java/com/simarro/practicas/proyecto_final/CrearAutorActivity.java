package com.simarro.practicas.proyecto_final;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simarro.practicas.proyecto_final.pojo.Autor;
import com.simarro.practicas.proyecto_final.pojo.Editorial;
import com.simarro.practicas.proyecto_final.pojo.Libro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearAutorActivity extends AppCompatActivity {
    TextView fecha;
    EditText editTextNombre, nlibros, pais, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_autor);
        fecha = findViewById(R.id.textView5);
        editTextNombre = findViewById(R.id.editTextNombre);
        nlibros = findViewById(R.id.editText4);
        pais = findViewById(R.id.editTextPais);
        bio = findViewById(R.id.editText3);
    }


    public void fecha(View v) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int year = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d("ggg", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                        String date = day + "/" + month + "/" + year;
                        fecha.setText(date);
                    }
                }, year, month, day);

        dialog.show();

    }


    public void Crear(View v) {
        DatabaseReference mDatabase;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
        Date date1 = null;
        try {
            date1= new SimpleDateFormat("dd/MM/yyyy").parse(fecha.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Libro l = new Libro("", new Autor(editTextNombre.getText().toString(), date1, Integer.parseInt(nlibros.getText().toString()), pais.getText().toString(), bio.getText().toString()), 0, getIntent().getStringExtra("ISBN"), new Editorial("",date1), date1, "", "", "", "", 0);
        //l.setPortada(portada);


        mDatabase.child("Libros").child(l.getIsbn()).setValue(l);
        finish();

    }
}
