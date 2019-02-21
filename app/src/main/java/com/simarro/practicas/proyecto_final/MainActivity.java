package com.simarro.practicas.proyecto_final;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simarro.practicas.proyecto_final.pojo.Autor;
import com.simarro.practicas.proyecto_final.pojo.Editorial;
import com.simarro.practicas.proyecto_final.pojo.Libro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    DatabaseReference mDatabase;
    String userID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        comprobarIdioma();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Fragment fragment;
            String data = extras.getString("CAMBIO");
            switch (data) {
                case "DESEADOS":
                    fragment = new ListaDeseos();
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                    break;
                case "LEYENDO":
                    fragment = new ListaLeyendo();
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                    break;

                default:
                    fragment = new ListaTodos();
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                    break;
            }


        } else {
            Fragment fragment = new ListaTodos();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_desconectar) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_mapa) {

            Uri location = Uri.parse(getString(R.string.coordenades));

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

// Verify it resolves
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
            boolean isIntentSafe = activities.size() > 0;

// Start an activity if it's safe
            if (isIntentSafe) {
                startActivity(mapIntent);
            }
            return true;
        }
        if (id == R.id.action_preferencias) {
            Intent i=new Intent(this,OpcionesActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_menu1:
                fragment = new ListaLeidos();

                break;
            case R.id.nav_menu2:
                fragment = new ListaLeyendo();
                break;
            case R.id.nav_menu3:
                fragment = new ListaDeseos();

                break;
            case R.id.nav_menu4:
                fragment = new ListaTodos();

                break;

        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    public void anadir(View v) {
        if (checkPermission()) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnLeyendo:
                     intent = new Intent(this, Scanner.class);
                    intent.putExtra("TIPO","LEYENDO");
                    startActivity(intent);
                    break;
                case R.id.btnLibros:
                    intent = new Intent(this, Scanner.class);
                    intent.putExtra("TIPO","LIBROS");
                    startActivity(intent);
                    break;

                    default:
                         intent = new Intent(this, Scanner.class);
                        intent.putExtra("TIPO","otro");
                        startActivity(intent);
                        break;

            }

        }

    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(this, "This version is not Android 6 or later " + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
        } else {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                Toast.makeText(this, "Requesting permissions", Toast.LENGTH_LONG).show();
            } else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "OK Permissions granted ! ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permissions are not granted ! " + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }





    private void comprobarIdioma(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String idioma = prefs.getString("Idioma", "error");

        if(!idioma.equals("error")){
            if(idioma.equals("ESP")){
                Locale localizacion = new Locale("es", "ES");
                Locale.setDefault(localizacion);
                Configuration config = new Configuration();
                config.locale = localizacion;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }else{
                Locale localizacion = new Locale("en", "US");
                Locale.setDefault(localizacion);
                Configuration config = new Configuration();
                config.locale = localizacion;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }
        }

    }

}
