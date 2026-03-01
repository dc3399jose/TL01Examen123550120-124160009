package com.example.tl01examen123550120_124160009;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Contacto> {

    private Context context;
    private List<Contacto> lista;
    private int[] colores = {
            Color.parseColor("#E53935"),
            Color.parseColor("#8E24AA"),
            Color.parseColor("#1E88E5"),
            Color.parseColor("#00897B"),
            Color.parseColor("#F4511E"),
            Color.parseColor("#6D4C41"),
            Color.parseColor("#039BE5"),
            Color.parseColor("#43A047")
    };

    public ContactoAdapter(Context context, List<Contacto> lista) {
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.itemcontacto, parent, false);
        }

        Contacto contacto = lista.get(position);

        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvTelefono = convertView.findViewById(R.id.tvTelefono);
        TextView tvNota = convertView.findViewById(R.id.tvNota);
        ImageView ivContacto = convertView.findViewById(R.id.ivContacto);

        tvNombre.setText(contacto.getNombre());
        tvTelefono.setText(contacto.getTelefono());
        tvNota.setText(contacto.getNota());

        // Color diferente por contacto
        int color = colores[position % colores.length];
        ivContacto.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(color));

        return convertView;
    }
}