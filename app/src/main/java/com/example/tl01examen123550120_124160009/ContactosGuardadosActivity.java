package com.example.tl01examen123550120_124160009;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

// Esta pantalla muestra la lista de todos los contactos guardados
public class ContactosGuardadosActivity extends AppCompatActivity {

    //COMPONENTES DE LA PANTALLA
    EditText etBuscar;
    ListView lvContactos;
    Button btnBuscar, btnVolver;

    //Herramientas necesarias
    ContactoDAO dao;       // Asistente de base de datos
    ContactoAdapter adapter; // Organizador de la lista
    List<Contacto> lista;   // La lista de datos reales

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_guardados);

        //unir componentes
        etBuscar = findViewById(R.id.etBuscar);
        lvContactos = findViewById(R.id.lvContactos);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnVolver = findViewById(R.id.btnVolverPrincipal);

        dao = new ContactoDAO(this);
        cargarLista(); // Cargamos los contactos al abrir la pantalla

        //accion buscar
        btnBuscar.setOnClickListener(v -> {
            String texto = etBuscar.getText().toString();
            lista = dao.buscar(texto); // Buscamos en la base de datos
            adapter = new ContactoAdapter(this, lista); // Actualizamos el organizador
            lvContactos.setAdapter(adapter); // Mostramos los resultados
            if(lista.isEmpty()){
                Toast.makeText(this, "No se encontraron contactos", Toast.LENGTH_SHORT).show();
            }
        });

        //accion buscar (tiempo real)
        // Se ejecuta automáticamente cada vez que el usuario escribe una letra
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lista = dao.buscar(s.toString()); // Busca mientras escribe
                adapter = new ContactoAdapter(ContactosGuardadosActivity.this, lista);
                lvContactos.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        // Al tocar un contacto de la lista, abre la pantalla de detalle
        lvContactos.setOnItemClickListener((parent, view, position, id) -> {
            Contacto c = lista.get(position);
            Intent intent = new Intent(this, DetalleActivity.class);
            // Pasamos los datos del contacto seleccionado a la pantalla de detalle
            intent.putExtra("id", c.getId());
            intent.putExtra("nombre", c.getNombre());
            intent.putExtra("telefono", c.getTelefono());
            intent.putExtra("nota", c.getNota());
            startActivity(intent);
        });

        //boton de volver
        btnVolver.setOnClickListener(v -> finish());
    }


    @Override
    protected void onResume() {
        super.onResume();
        cargarLista(); // Recarga la lista por si se editó o borró algo en el detalle
    }

    //funcion helper: cargar lista
    private void cargarLista() {
        lista = dao.obtenerTodos(); // Trae todos los contactos de la base de datos
        adapter = new ContactoAdapter(this, lista);
        lvContactos.setAdapter(adapter); // Dibuja la lista
    }
}