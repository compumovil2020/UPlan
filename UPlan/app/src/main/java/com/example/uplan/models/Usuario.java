package com.example.uplan.models;

import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class Usuario {

    private String nombre;
    private String apellido;
    private String email;
    private String imagen;
    private String username;
    private long fechaNacimiento;
    private long latitud;
    private long longitud;

    public Usuario(){

    }

    public long getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(long fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public long getLatitud() {
        return latitud;
    }

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public long getLongitud() {
        return longitud;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImagen() { return imagen; }

    public void setImagen(String imagen) { this.imagen = imagen; }
}
