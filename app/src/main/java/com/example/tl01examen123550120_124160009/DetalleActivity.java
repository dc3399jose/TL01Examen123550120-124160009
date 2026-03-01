package com.example.tl01examen123550120_124160009;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetalleActivity extends AppCompatActivity {

    TextView tvNombre, tvTelefono, tvNota;
    Button btnLlamar, btnActualizar, btnCompartir, btnEliminar, btnVolver;
    String nombre, telefono, nota;
    int id;
    ContactoDAO dao;
    static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actyvity_detalle);

        dao = new ContactoDAO(this);

        tvNombre = findViewById(R.id.tvDetalleNombre);
        tvTelefono = findViewById(R.id.tvDetalleTelefono);
        tvNota = findViewById(R.id.tvDetalleNota);
        
        btnLlamar = findViewById(R.id.btnDetalleLlamar);
        btnActualizar = findViewById(R.id.btnDetalleActualizar);
        btnCompartir = findViewById(R.id.btnDetalleCompartir);
        btnEliminar = findViewById(R.id.btnDetalleEliminar);
        btnVolver = findViewById(R.id.btnDetalleAtras);

        // Recibir datos
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nombre = intent.getStringExtra("nombre");
        telefono = intent.getStringExtra("telefono");
        nota = intent.getStringExtra("nota");

        actualizarUI();

        btnLlamar.setOnClickListener(v -> mostrarConfirmacionLlamada());

        btnActualizar.setOnClickListener(v -> mostrarDialogoActualizar());

        btnCompartir.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String texto = "Contacto: " + nombre + "\nTel: " + telefono;
            shareIntent.putExtra(Intent.EXTRA_TEXT, texto);
            startActivity(Intent.createChooser(shareIntent, "Compartir vía"));
        });

        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Contacto")
                    .setMessage("¿Estás seguro de que deseas eliminar a " + nombre + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        dao.eliminar(id);
                        Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                        finish(); // Cerrar detalle y volver a la lista
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    private void actualizarUI() {
        tvNombre.setText(nombre);
        tvTelefono.setText(telefono);
        tvNota.setText(nota);
    }

    private void mostrarConfirmacionLlamada() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Llamada")
                .setMessage("¿Desea llamar a " + nombre + "?")
                .setPositiveButton("Llamar", (dialog, which) -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    } else {
                        hacerLlamada();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void hacerLlamada() {
        String numeroLimpio = telefono.replaceAll("[\\s\\-]", "");
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + numeroLimpio));
        startActivity(intent);
    }

    private void mostrarDialogoActualizar() {
        View view = getLayoutInflater().inflate(R.layout.dialog_agregar, null);
        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etNota = view.findViewById(R.id.etNota);
        Spinner spinnerPais = view.findViewById(R.id.spinnerPais);

        etNombre.setText(nombre);
        etNota.setText(nota);
        
        // Limpiar código de país para el EditText
        if (telefono.contains(" ")) {
            String[] partes = telefono.split(" ", 2);
            etTelefono.setText(partes[1]);
        } else {
            etTelefono.setText(telefono);
        }

        new AlertDialog.Builder(this)
                .setTitle("Actualizar Contacto")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoNombre = etNombre.getText().toString().trim();
                    String nuevoTelSimple = etTelefono.getText().toString().trim();
                    String nuevaNota = etNota.getText().toString();
                    String paisSeleccionado = spinnerPais.getSelectedItem().toString();

                    if (nuevoNombre.isEmpty() || nuevoTelSimple.isEmpty()) {
                        Toast.makeText(this, "Campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String codigoPais = "";
                    Pattern p = Pattern.compile("\\((.*?)\\)");
                    Matcher m = p.matcher(paisSeleccionado);
                    if (m.find()) {
                        codigoPais = m.group(1).split(",")[0].trim();
                    }

                    String nuevoTelefonoFinal = codigoPais + " " + nuevoTelSimple;

                    Contacto c = new Contacto(id, nuevoNombre, nuevoTelefonoFinal, nuevaNota, "");
                    dao.actualizar(c);
                    
                    // Actualizar variables locales y UI
                    this.nombre = nuevoNombre;
                    this.telefono = nuevoTelefonoFinal;
                    this.nota = nuevaNota;
                    actualizarUI();
                    
                    Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hacerLlamada();
            }
        }
    }
}