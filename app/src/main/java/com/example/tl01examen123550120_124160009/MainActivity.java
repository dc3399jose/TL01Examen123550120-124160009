package com.example.tl01examen123550120_124160009;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button btnAgregar, btnContactosSalvados;
    ContactoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnContactosSalvados = findViewById(R.id.btnContactosSalvados);

        dao = new ContactoDAO(this);

        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        btnContactosSalvados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactosGuardadosActivity.class);
            startActivity(intent);
        });
    }

    private void mostrarDialogoAgregar() {
        View view = getLayoutInflater().inflate(R.layout.dialog_agregar, null);
        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etNota = view.findViewById(R.id.etNota);
        Spinner spinnerPais = view.findViewById(R.id.spinnerPais);

        new AlertDialog.Builder(this)
                .setTitle("Agregar Contacto")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String telefonoIngresado = etTelefono.getText().toString().trim();
                    String nota = etNota.getText().toString();
                    String paisSeleccionado = spinnerPais.getSelectedItem().toString();

                    if (nombre.isEmpty() || telefonoIngresado.isEmpty()) {
                        Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String codigoPais = "";
                    Pattern p = Pattern.compile("\\((.*?)\\)");
                    Matcher m = p.matcher(paisSeleccionado);
                    if (m.find()) {
                        codigoPais = m.group(1).split(",")[0].trim();
                    }

                    String telefonoFinal = codigoPais + " " + telefonoIngresado;

                    if (!telefonoIngresado.matches("[0-9\\s\\-]+")) {
                        Toast.makeText(this, "Formato de teléfono inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Contacto c = new Contacto(0, nombre, telefonoFinal, nota, "");
                    dao.agregar(c);
                    Toast.makeText(this, "Contacto agregado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}