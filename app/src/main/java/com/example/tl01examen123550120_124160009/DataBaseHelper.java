package com.example.tl01examen123550120_124160009;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// base de datos donde guardamos la info de nuestros contactos
class DatabaseHelper extends SQLiteOpenHelper {

    // Nombres de la base de datos y tabla
    public static final String DATABASE_NAME = "contactos.db"; // Nombre del archivo físico
    public static final String TABLE_NAME = "contactos";// Nombre de la tabla dentro del archivo
    public static final int DATABASE_VERSION = 1;  // Versión para gestionar cambios futuros

    // Constructor: Prepara el ayudante con la configuración
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creacion de tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecutamos el comando SQL para crear la tabla con sus campos
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID unico automatico
                "nombre TEXT, " +                     // Campo de texto para nombre
                "telefono TEXT, " +                  // Campo de texto para teléfono
                "nota TEXT, " +                    // Campo de texto para nota
                "imagen TEXT)");                 // Campo de texto para la ruta de imagen
    }

    // actualizar tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borramos la tabla vieja si existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creamos la tabla nueva de nuevo
        onCreate(db);
    }
}