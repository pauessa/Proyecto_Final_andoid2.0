package com.simarro.practicas.proyecto_final;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SqlLite extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Imagen (codigo INTEGER, url TEXT)";
    String sqlInsert = "INSERT INTO Imagen (codigo, url) VALUES (0, \"https://firebasestorage.googleapis.com/v0/b/proyectofinal-3872c.appspot.com/o/Books%2Fnocover.jpg?alt=media&token=feda68b7-36e4-4a25-8eff-af649183a369\") ";

    public SqlLite(Context contexto, String nombre,
                   SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
        db.execSQL(sqlInsert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS Imagen");
        db.execSQL(sqlCreate);
        db.execSQL(sqlInsert);
    }
    public String getimage(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT url FROM Imagen WHERE codigo='0' ", null);
        c.moveToFirst();
        return c.getString(0);

    }
}