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

public class ContactosGuardadosActivity extends AppCompatActivity {

    EditText etBuscar;
    ListView lvContactos;
    Button btnBuscar, btnVolver;
    ContactoDAO dao;
    ContactoAdapter adapter;
    List<Contacto> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_guardados);

        etBuscar = findViewById(R.id.etBuscar);
        lvContactos = findViewById(R.id.lvContactos);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnVolver = findViewById(R.id.btnVolverPrincipal);

        dao = new ContactoDAO(this);
        cargarLista();

        btnBuscar.setOnClickListener(v -> {
            String texto = etBuscar.getText().toString();
            lista = dao.buscar(texto);
            adapter = new ContactoAdapter(this, lista);
            lvContactos.setAdapter(adapter);
            if(lista.isEmpty()){
                Toast.makeText(this, "No se encontraron contactos", Toast.LENGTH_SHORT).show();
            }
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lista = dao.buscar(s.toString());
                adapter = new ContactoAdapter(ContactosGuardadosActivity.this, lista);
                lvContactos.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        lvContactos.setOnItemClickListener((parent, view, position, id) -> {
            Contacto c = lista.get(position);
            Intent intent = new Intent(this, DetalleActivity.class);
            intent.putExtra("id", c.getId());
            intent.putExtra("nombre", c.getNombre());
            intent.putExtra("telefono", c.getTelefono());
            intent.putExtra("nota", c.getNota());
            startActivity(intent);
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista(); // Refrescar por si se borró o editó en detalle
    }

    private void cargarLista() {
        lista = dao.obtenerTodos();
        adapter = new ContactoAdapter(this, lista);
        lvContactos.setAdapter(adapter);
    }
}