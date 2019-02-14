package com.simarro.practicas.proyecto_final;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.simarro.practicas.proyecto_final.pojo.Autor;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CrearLibroActivity extends AppCompatActivity {
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;
    File fileImagen;
    Bitmap bitmap;
    ImageView imgFoto;
    private static final int COD_SELECIONADA = 10;
    private static final int COD_FOTO = 20;
    FirebaseStorage storage;
    String titulo;
    List<Autor> autores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_libro);
        autores = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        titulo="test";
        TextView isbn=findViewById(R.id.isbn);
        isbn.setText(getIntent().getStringExtra("ISBN"));
        mostrarAutores();
    }

    public void cancelar(View v){
        finish();
    }

    public void subir_imagen(View v) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, COD_FOTO);

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
                    Log.e("EEEE", l.getAutor().toString());
                }
                // adapterLeidos.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
