package com.simarro.practicas.proyecto_final;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simarro.practicas.proyecto_final.adapters.AdapterSpinner;
import com.simarro.practicas.proyecto_final.adapters.AdapterSpinnerEditorial;
import com.simarro.practicas.proyecto_final.pojo.Autor;
import com.simarro.practicas.proyecto_final.pojo.Editorial;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrearLibroActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView imgFoto;
    private static final int COD_SELECIONADA = 10;
    private static final int COD_FOTO = 20;
    FirebaseStorage storage;
    String titulo="";
    List<Autor> autores;
    List<Editorial> editoriales;

    Spinner spiner,spinerEditorial;
    private AdapterSpinner adapter;
    AdapterSpinnerEditorial adapterEditorial;
    EditText txtTitulo;
    EditText saga;
    EditText sinopsis;
    EditText npag;
    EditText lengua;
    EditText genero;
    TextView fecha;
String portada;


    Autor autorSelecionado;
    Editorial editorialSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_libro);

        this.setTitle("ISBN: "+getIntent().getStringExtra("ISBN"));
        autores = new ArrayList<>();
        editoriales =new ArrayList<>();
        spiner=findViewById(R.id.spinner);
        spinerEditorial=findViewById(R.id.spinner2);
        storage = FirebaseStorage.getInstance();
        txtTitulo=findViewById(R.id.editTextTiulo);
        saga=findViewById(R.id.editTextSaga);
        sinopsis=findViewById(R.id.editText3);
        npag=findViewById(R.id.editText4);
        lengua=findViewById(R.id.editText);
        genero=findViewById(R.id.editText2);
        fecha=findViewById(R.id.textView5);
        adapter = new AdapterSpinner(this,android.R.layout.simple_spinner_item, autores);
        adapterEditorial = new AdapterSpinnerEditorial(this,android.R.layout.simple_spinner_item, editoriales);
        spiner.setAdapter(adapter);
        spinerEditorial.setAdapter(adapterEditorial);
        mostrarAutores();
        mostrarEditoriales();


        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
                Autor a = adapter.getItem(position);
                autorSelecionado=a;
                Log.e("ASDASD",a.getNombre());
                Toast.makeText(getBaseContext(), a.getNombre(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                Log.e("ASDASD","nada");

            }
        });


        spinerEditorial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
                Editorial e = adapterEditorial.getItem(position);
                editorialSelecionada=e;
                Log.e("ASDASD",e.getNombre());
                Toast.makeText(getBaseContext(), e.getNombre(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                Log.e("ASDASD","nada");

            }
        });
    }

    private void mostrarEditoriales() {
        DatabaseReference mDatabase;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
        String userID = mAuth.getCurrentUser().getUid();
        mDatabase.child("Libros").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editoriales.removeAll(editoriales);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Libro l = ds.getValue(Libro.class);
                    editoriales.add(l.getEditorial());

                }
                adapterEditorial.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cancelar(View v){
        finish();
    }

    public void subir_imagen(View v) {
        if (!txtTitulo.getText().equals("")){
            titulo=txtTitulo.getText().toString();
        }

        if (!titulo.equals("")) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, COD_FOTO);
        }else{
            Toast.makeText(getBaseContext(), "tienes que introducir un titulo primero",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case COD_FOTO:
                imgFoto = findViewById(R.id.imageView2);
                bitmap = (Bitmap) data.getExtras().get("data");
                //imgFoto.setImageBitmap(bitmap);
                Uri uri = data.getData();
                uploadImage(bitmap,titulo);
                break;
        }
    }


    public void uploadImage(Bitmap bitmap, final String titulo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://proyectofinal-3872c.appspot.com/Books");
        final StorageReference imagesRef = storageRef.child(titulo+".jpg");

        final UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Bitmap hochladen
                        Picasso.get().load(uri.toString()).into(imgFoto);
                        portada=uri.toString();
                        //imgFoto.setImageURI(uri);
                    }
                });

            }
        });
    }
    private void mostrarAutores() {
        DatabaseReference mDatabase;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
        String userID = mAuth.getCurrentUser().getUid();
        mDatabase.child("Libros").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                autores.removeAll(autores);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Libro l = ds.getValue(Libro.class);
                    autores.add(l.getAutor());
                }
                 adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void Crear(View v){
        Autor a= (Autor) spiner.getSelectedItem();
        Editorial ed= (Editorial) spinerEditorial.getSelectedItem();
        DatabaseReference mDatabase;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Creamos una referencia al root de la base de datos
        Date date1 = null;
        try {
            date1= new SimpleDateFormat("dd/MM/yyyy").parse(fecha.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Libro l = new Libro(titulo,a,Integer.parseInt(npag.getText().toString()), getIntent().getStringExtra("ISBN"), ed, date1, sinopsis.getText().toString()   , saga.getText().toString(), lengua.getText().toString(), genero.getText().toString(), 10);
        l.setPortada(portada);



        mDatabase.child("Libros").child(l.getIsbn()).setValue(l);

    }

    public void fecha(View v){
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
                                String date = month + "/" + day + "/" + year;
                                fecha.setText(date);
                            }
                },year, month, day);

        dialog.show();

    }

    public void crearAutor(View v){
        Intent i=new Intent(this,CrearAutorActivity.class);
        i.putExtra("ISBN",getIntent().getStringExtra("ISBN"));
        startActivity(i);
    }
    public void crearEditorial(View v){
        Intent i=new Intent(this,CrearEditorialActivity.class);
        i.putExtra("ISBN",getIntent().getStringExtra("ISBN"));
        i.putExtra("AUTOR",autorSelecionado);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
