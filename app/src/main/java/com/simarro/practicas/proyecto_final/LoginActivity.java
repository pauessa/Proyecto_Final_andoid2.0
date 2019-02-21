package com.simarro.practicas.proyecto_final;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnEntrar, btnRegistrar;
    private EditText editCorreo, editContra;
    private String correo, contra;
    private Intent i;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        btnEntrar = findViewById(R.id.btSignIn);
        btnRegistrar = findViewById(R.id.btSignUp);
        editCorreo = findViewById(R.id.emailinput);
        editContra = findViewById(R.id.passwordinput);

        btnEntrar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);

        i = new Intent(this, MainActivity.class);
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("SESION", "Sesion abierta " + user.getEmail());
                    startActivity(i);
                } else {
                    Log.i("SESION", "Sesion cerrada");
                }
            }
        };

    }

    public void login(String correo, String contra) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("SESION", "sesion iniciada");
                }else{
                    Toast.makeText(getBaseContext(), "datos incorrectos o usuario inesistente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registro(String correo, String contra) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("SESION", "Usuario creado");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSignIn:

                correo = editCorreo.getText().toString();
                contra = editContra.getText().toString();
                if (correo.equals("") || contra.equals("")) {
                    Toast.makeText(this, "El campo de correo o contraseña no puede ser vacio", Toast.LENGTH_SHORT).show();
                }else{
                    login(correo, contra);
                }
                break;

            case R.id.btSignUp:
                correo = editCorreo.getText().toString();
                contra = editContra.getText().toString();
                if (correo.equals("") || contra.equals("")) {
                    Toast.makeText(this, "El campo de correo o contraseña no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (contra.length() < 6) {
                    Toast.makeText(this, "La contraseña tiene que ser mas minimo 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    if (validarEmail(correo)){
                        registro(correo, contra);
                    }else {
                        Toast.makeText(this, "Tienes que introducir un correo valido", Toast.LENGTH_SHORT).show();
                    }
                }
                break;








        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
