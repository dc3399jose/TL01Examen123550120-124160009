package com.example.tl01examen123550120_124160009;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DetalleActivity extends AppCompatActivity {

    TextView tvNombre, tvTelefono, tvNota;
    Button btnLlamar;
    String telefono;
    static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actyvity_detalle);

        tvNombre = findViewById(R.id.tvDetalleNombre);
        tvTelefono = findViewById(R.id.tvDetalleTelefono);
        tvNota = findViewById(R.id.tvDetalleNota);
        btnLlamar = findViewById(R.id.btnLlamar);

        // Recibir datos de la pantalla anterior
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        telefono = intent.getStringExtra("telefono");
        String nota = intent.getStringExtra("nota");

        tvNombre.setText(nombre);
        tvTelefono.setText(telefono);
        tvNota.setText(nota);

        btnLlamar.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                hacerLlamada();
            }
        });
    }

    private void hacerLlamada() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + telefono));
        startActivity(intent);
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