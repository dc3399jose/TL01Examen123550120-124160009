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

// Esta pantalla muestra la información detallada de un contacto
// permite editarlo, eliminarlo o llamarlo
public class DetalleActivity extends AppCompatActivity {

//elementos de la pantalla
    TextView tvNombre, tvTelefono, tvNota;
    Button btnLlamar, btnActualizar, btnCompartir, btnEliminar, btnVolver;

//datos del contacto
    String nombre, telefono, nota;
    int id;

//herramientas
    ContactoDAO dao; // Asistente de base de datos
    static final int REQUEST_CALL = 1; // cod para solicitar permiso de llamada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actyvity_detalle);

        dao = new ContactoDAO(this);
        //enlazar componentes
        tvNombre = findViewById(R.id.tvDetalleNombre);
        tvTelefono = findViewById(R.id.tvDetalleTelefono);
        tvNota = findViewById(R.id.tvDetalleNota);

        btnLlamar = findViewById(R.id.btnDetalleLlamar);
        btnActualizar = findViewById(R.id.btnDetalleActualizar);
        btnCompartir = findViewById(R.id.btnDetalleCompartir);
        btnEliminar = findViewById(R.id.btnDetalleEliminar);
        btnVolver = findViewById(R.id.btnDetalleAtras);

        //recibir datos del intent
        // Obtenemos la información que nos dio la pantalla anterior
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nombre = intent.getStringExtra("nombre");
        telefono = intent.getStringExtra("telefono");
        nota = intent.getStringExtra("nota");

        actualizarUI(); // Mostramos los datos en pantalla

        //ACCIÓN: BOTON LLAMAR
        btnLlamar.setOnClickListener(v -> mostrarConfirmacionLlamada());

        //ACCIÓN: BOTON ACTUALIZAR
        btnActualizar.setOnClickListener(v -> mostrarDialogoActualizar());

        //ACCIÓN: BOTON COMPARTIR
        btnCompartir.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String texto = "Contacto: " + nombre + "\nTel: " + telefono;
            shareIntent.putExtra(Intent.EXTRA_TEXT, texto);
            startActivity(Intent.createChooser(shareIntent, "Compartir vía"));
        });

        //BOTON ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            // Mostramos una alerta para confirmar antes de borrar
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Contacto")
                    .setMessage("¿Estás seguro de que deseas eliminar a " + nombre + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        dao.eliminar(id); // Borramos de la base de datos
                        Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                        finish(); // Cerramos esta pantalla y volvemos a la lista
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        //BOTON VOLVER
        btnVolver.setOnClickListener(v -> finish());
    }

    //ACTUALIZAR PANTALLA
    private void actualizarUI() {
        tvNombre.setText(nombre);
        tvTelefono.setText(telefono);
        tvNota.setText(nota);
    }

    //CONFIRMACION DE LLAMADA
    private void mostrarConfirmacionLlamada() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Llamada")
                .setMessage("¿Desea llamar a " + nombre + "?")
                .setPositiveButton("Llamar", (dialog, which) -> {
                    // Verificamos si tenemos permiso para llamar
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Solicitamos el permiso
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    } else {
                        hacerLlamada(); // Si ya tenemos permiso, llamamos
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    //HACER LLAMADA
    private void hacerLlamada() {
        // Limpiamos el número de espacios o guiones antes de llamar
        String numeroLimpio = telefono.replaceAll("[\\s\\-]", "");
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + numeroLimpio));
        startActivity(intent);
    }

    // FUNCION: DIÁLOGO ACTUALIZAR
    private void mostrarDialogoActualizar() {
        // Inflamos un diseño de diálogo (ventana flotante)
        View view = getLayoutInflater().inflate(R.layout.dialog_agregar, null);
        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etNota = view.findViewById(R.id.etNota);
        Spinner spinnerPais = view.findViewById(R.id.spinnerPais);

        // Rellenamos la ventana con los datos actuales
        etNombre.setText(nombre);
        etNota.setText(nota);

        // Separamos el código de país del número para editarlo
        if (telefono.contains(" ")) {
            String[] partes = telefono.split(" ", 2);
            etTelefono.setText(partes[1]);
        } else {
            etTelefono.setText(telefono);
        }

        // Creamos la alerta de actualización
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

                    // Re-formateamos el número con el código de país
                    String codigoPais = "";
                    Pattern p = Pattern.compile("\\((.*?)\\)");
                    Matcher m = p.matcher(paisSeleccionado);
                    if (m.find()) {
                        codigoPais = m.group(1).split(",")[0].trim();
                    }

                    String nuevoTelefonoFinal = codigoPais + " " + nuevoTelSimple;

                    // Actualizamos en la base de datos
                    Contacto c = new Contacto(id, nuevoNombre, nuevoTelefonoFinal, nuevaNota, "");
                    dao.actualizar(c);

                    // Actualizamos los datos mostrados en pantalla
                    this.nombre = nuevoNombre;
                    this.telefono = nuevoTelefonoFinal;
                    this.nota = nuevaNota;
                    actualizarUI();

                    Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    //gestion de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            // Si el usuario aceptó el permiso, hacemos la llamada
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hacerLlamada();
            }
        }
    }
}