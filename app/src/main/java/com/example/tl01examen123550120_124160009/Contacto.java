package com.example.tl01examen123550120_124160009;

// Esta clase es el "Modelo" o la "Ficha" de un contacto
// Define qué información vamos a guardar de cada persona
public class Contacto {


    private int id;        // Un número único para identificar al contacto en la base de datos
    private String nombre; // El nombre de la persona
    private String telefono; // El número de teléfono con su código de país
    private String nota;   // Una nota breve o descripción
    private String imagen; // La ruta o referencia a una foto (si aplica)

    //constructor: creamos una ficha de contacto nueva
    public Contacto(int id, String nombre, String telefono, String nota, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.imagen = imagen;
    }

    //metodo getter Para leer la información
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getNota() { return nota; }
    public String getImagen() { return imagen; }

    //metodo setter Para cambiar la información
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setNota(String nota) { this.nota = nota; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}