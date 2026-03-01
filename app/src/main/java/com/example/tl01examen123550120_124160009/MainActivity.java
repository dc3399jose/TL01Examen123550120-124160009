package com.example.tl01examen123550120_124160009;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etBuscar;
    ListView lvContactos;
    Button btnAgregar, btnEliminar, btnBuscar;
    ContactoDAO dao;
    ContactoAdapter adapter;
    List<Contacto> lista;
    Contacto contactoSeleccionado = null;
    String telefonoLlamar = null;
    static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBuscar = findViewById(R.id.etBuscar);
        lvContactos = findViewById(R.id.lvContactos);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnBuscar = findViewById(R.id.btnBuscar);

        // Inicializar el DAO y cargar la lista al iniciar
        dao = new ContactoDAO(this);
        cargarLista();

        // Botón Buscar (Manual)
        btnBuscar.setOnClickListener(v -> {
            String texto = etBuscar.getText().toString();
            lista = dao.buscar(texto);
            adapter = new ContactoAdapter(MainActivity.this, lista);
            lvContactos.setAdapter(adapter);
            if(lista.isEmpty()){
                Toast.makeText(this, "No se encontraron contactos", Toast.LENGTH_SHORT).show();
            }
        });

        // Buscador automático (Opcional, se mantiene por comodidad)
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lista = dao.buscar(s.toString());
                adapter = new ContactoAdapter(MainActivity.this, lista);
                lvContactos.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Seleccionar contacto
        lvContactos.setOnItemClickListener((parent, view, position, id) -> {
            contactoSeleccionado = lista.get(position);
            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
            intent.putExtra("nombre", contactoSeleccionado.getNombre());
            intent.putExtra("telefono", contactoSeleccionado.getTelefono());
            intent.putExtra("nota", contactoSeleccionado.getNota());
            startActivity(intent);
        });

        // Mantener presionado para llamar
        lvContactos.setOnItemLongClickListener((parent, view, position, id) -> {
            Contacto c = lista.get(position);
            telefonoLlamar = c.getTelefono();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                hacerLlamada(telefonoLlamar);
            }
            return true;
        });

        // Agregar contacto
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        // Eliminar contacto
        btnEliminar.setOnClickListener(v -> {
            if (contactoSeleccionado != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar")
                        .setMessage("¿Desea eliminar a " + contactoSeleccionado.getNombre() + "?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            dao.eliminar(contactoSeleccionado.getId());
                            contactoSeleccionado = null;
                            cargarLista();
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(this, "Seleccione un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hacerLlamada(String telefono) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + telefono));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hacerLlamada(telefonoLlamar);
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cargarLista() {
        lista = dao.obtenerTodos();
        adapter = new ContactoAdapter(this, lista);
        lvContactos.setAdapter(adapter);
    }

    private void mostrarDialogoAgregar() {
        View view = getLayoutInflater().inflate(R.layout.dialog_agregar, null);
        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etNota = view.findViewById(R.id.etNota);

        new AlertDialog.Builder(this)
                .setTitle("Agregar Contacto")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString();
                    String telefono = etTelefono.getText().toString();
                    String nota = etNota.getText().toString();

                    if (nombre.isEmpty() || telefono.isEmpty()) {
                        Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!telefono.matches("\\+?[0-9]{8,15}")) {
                        Toast.makeText(this, "Teléfono inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Contacto c = new Contacto(0, nombre, telefono, nota, "");
                    dao.agregar(c);
                    cargarLista();
                    Toast.makeText(MainActivity.this, "✅ Contacto agregado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}