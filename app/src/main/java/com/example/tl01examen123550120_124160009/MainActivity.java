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

    EditText etNombre, etTelefono, etNota;
    Spinner spinnerPais;
    Button btnSalvar, btnContactosSalvados;
    ContactoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes
        etNombre = findViewById(R.id.etNombreMain);
        etTelefono = findViewById(R.id.etTelefonoMain);
        etNota = findViewById(R.id.etNotaMain);
        spinnerPais = findViewById(R.id.spinnerPaisMain);
        btnSalvar = findViewById(R.id.btnSalvarMain);
        btnContactosSalvados = findViewById(R.id.btnContactosSalvadosMain);

        dao = new ContactoDAO(this);

        btnSalvar.setOnClickListener(v -> salvarContacto());

        btnContactosSalvados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactosGuardadosActivity.class);
            startActivity(intent);
        });
    }

    private void salvarContacto() {
        String nombre = etNombre.getText().toString().trim();
        String telefonoIngresado = etTelefono.getText().toString().trim();
        String nota = etNota.getText().toString().trim();
        String paisSeleccionado = spinnerPais.getSelectedItem().toString();

        // Validaciones con mensajes de alerta Toast (según imagen)
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

        // Extraer código de país
        String codigoPais = "";
        Pattern p = Pattern.compile("\\((.*?)\\)");
        Matcher m = p.matcher(paisSeleccionado);
        if (m.find()) {
            codigoPais = m.group(1).split(",")[0].trim();
        }

        String telefonoFinal = codigoPais + " " + telefonoIngresado;

        // Guardar contacto
        Contacto c = new Contacto(0, nombre, telefonoFinal, nota, "");
        dao.agregar(c);

        // Limpiar campos
        etNombre.setText("");
        etTelefono.setText("");
        etNota.setText("");
        spinnerPais.setSelection(0);

        Toast.makeText(this, "✅ Contacto salvado exitosamente", Toast.LENGTH_SHORT).show();
    }
}