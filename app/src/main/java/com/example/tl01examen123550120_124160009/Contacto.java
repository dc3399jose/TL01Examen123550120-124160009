package com.example.tl01examen123550120_124160009;

public class Contacto {

    private int id;
    private String nombre;
    private String telefono;
    private String nota;
    private String imagen;

    public Contacto(int id, String nombre, String telefono, String nota, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.imagen = imagen;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getNota() { return nota; }
    public String getImagen() { return imagen; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setNota(String nota) { this.nota = nota; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}