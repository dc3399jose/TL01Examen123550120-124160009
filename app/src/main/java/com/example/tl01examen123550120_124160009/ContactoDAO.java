package com.example.tl01examen123550120_124160009;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

// DAO "Data Access Object" (Objeto de Acceso a Datos) Es el encargado de todas las operaciones con la base de datos.
public class ContactoDAO {

    private DatabaseHelper dbHelper; // Asistente que crea la base de datos

    // Constructor: Prepara el asistente para usarlo
    public ContactoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    // Recibe un contacto y lo guarda en la base de datos
    public void agregar(Contacto contacto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Abrimos para escribir
        ContentValues values = new ContentValues(); // Preparamos los datos
        values.put("nombre", contacto.getNombre());
        values.put("telefono", contacto.getTelefono());
        values.put("nota", contacto.getNota());
        values.put("imagen", contacto.getImagen());

        db.insert(DatabaseHelper.TABLE_NAME, null, values); // Insertamos la fila
        db.close(); // Cerramos la conexión
    }


    // Busca un contacto por su ID y cambia sus datos por unos nuevos
    public void actualizar(Contacto contacto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Abrimos para escribir
        ContentValues values = new ContentValues();
        values.put("nombre", contacto.getNombre());
        values.put("telefono", contacto.getTelefono());
        values.put("nota", contacto.getNota());
        values.put("imagen", contacto.getImagen());

        // Actualizamos donde el ID coincida
        db.update(DatabaseHelper.TABLE_NAME, values, "id=?", new String[]{String.valueOf(contacto.getId())});
        db.close();
    }


    // Borra un contacto definitivamente usando su ID único
    public void eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Borramos donde el ID coincida
        db.delete(DatabaseHelper.TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }


    // Trae todos los contactos guardados para mostrarlos en la lista
    public List<Contacto> obtenerTodos() {
        List<Contacto> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Abrimos solo para leer

        // Ejecutamos la consulta SQL para traer todo
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);

        // Leemos fila por fila los resultados
        if (cursor.moveToFirst()) {
            do {
                // Convertimos la fila de la base de datos en un objeto Contacto
                lista.add(new Contacto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close(); // Cerramos el cursor
        db.close();
        return lista; // Devolvemos la lista de contactos
    }

    // Busca contactos cuyo nombre coincida parcialmente con lo que el usuario escribió
    public List<Contacto> buscar(String texto) {
        List<Contacto> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta SQL con filtro LIKE (busca coincidencias)
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