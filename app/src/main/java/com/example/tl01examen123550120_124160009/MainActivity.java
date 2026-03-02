package com.example.tl01examen123550120_124160009;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Componentes de la interfaz de usuario
    EditText etNombre, etTelefono, etNota;
    Spinner spinnerPais;
    Button btnSalvar, btnContactosSalvados;
    // Asistente para manejar la base de datos
    ContactoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes: Conectamos las variables de Java con los elementos visuales (XML)
        etNombre = findViewById(R.id.etNombreMain);
        etTelefono = findViewById(R.id.etTelefonoMain);
        etNota = findViewById(R.id.etNotaMain);
        spinnerPais = findViewById(R.id.spinnerPaisMain);
        btnSalvar = findViewById(R.id.btnSalvarMain);
        btnContactosSalvados = findViewById(R.id.btnContactosSalvadosMain);

        // Preparamos el asistente de base de datos
        dao = new ContactoDAO(this);

        // Acción del botón guardar: Al tocarlo, ejecuta la función para guardar
        btnSalvar.setOnClickListener(v -> salvarContacto());

        // Acción del botón para ver contactos: Al tocarlo, abre la pantalla de lista
        btnContactosSalvados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactosGuardadosActivity.class);
            startActivity(intent);
        });
    }

    private void salvarContacto() {
        // Obtenemos el texto que el usuario escribió
        String nombre = etNombre.getText().toString().trim();
        String telefonoIngresado = etTelefono.getText().toString().trim();
        String nota = etNota.getText().toString().trim();
        String paisSeleccionado = spinnerPais.getSelectedItem().toString();

        // Validaciones: Comprobamos que ningún campo obligatorio esté vacío
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Debe escribir un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (telefonoIngresado.isEmpty()) {
            Toast.makeText(this, "Debe escribir un teléfono", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nota.isEmpty()) {
            Toast.makeText(this, "Debe escribir una nota", Toast.LENGTH_SHORT).show();
            return;
        }

        // Procesamiento del teléfono: Extraemos el código de país (ej: +504) del menú desplegable
        String codigoPais = "";
        Pattern p = Pattern.compile("\\((.*?)\\)");
        Matcher m = p.matcher(paisSeleccionado);
        if (m.find()) {
            codigoPais = m.group(1).split(",")[0].trim();
        }

        // Unimos el código de país con el número telefónico
        String telefonoFinal = codigoPais + " " + telefonoIngresado;

        // Guardado: Creamos la "ficha" del contacto y le pedimos al asistente (DAO) que lo guarde
        Contacto c = new Contacto(0, nombre, telefonoFinal, nota, "");
        dao.agregar(c);

        // Limpieza: Borramos los campos de texto para que estén listos para otro registro
        etNombre.setText("");
        etTelefono.setText("");
        etNota.setText("");
        spinnerPais.setSelection(0);

        // Mensaje de éxito para el usuario
        Toast.makeText(this, "Contacto salvado exitosamente", Toast.LENGTH_SHORT).show();
    }
}