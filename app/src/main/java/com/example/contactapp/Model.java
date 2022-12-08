package com.example.contactapp;

public class Model{
    private int id;
    private byte[] imagen;
    private String nombre;
    private String telefono;

    //constructor


    public Model(int id, byte[] imagen, String nombre, String telefono) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
//getter and setter method


