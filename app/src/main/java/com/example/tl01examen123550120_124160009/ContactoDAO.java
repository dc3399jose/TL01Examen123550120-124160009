package com.example.tl01examen123550120_124160009;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class ContactoDAO {

    private DatabaseHelper dbHelper;

    public ContactoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void agregar(Contacto contacto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", contacto.getNombre());
        values.put("telefono", contacto.getTelefono());
        values.put("nota", contacto.getNota());
        values.put("imagen", contacto.getImagen());
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    public void eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Contacto> obtenerTodos() {
        List<Contacto> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Contacto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public List<Contacto> buscar(String texto) {
        List<Contacto> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE nombre LIKE ?", new String[]{"%" + texto + "%"});
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Contacto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}
