package com.example.uplan.models;

import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class Usuario {

    private String nombre;
    private String apellido;
    private String email;
    private String identificacion;
    private String imagen;

    public Usuario(){

    }

    public Usuario(String nombre, String apellido, String email, String identificacion,String imagen) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.identificacion = identificacion;
        this.imagen = imagen;
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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getImagen() { return imagen; }

    public void setImagen(String imagen) { this.imagen = imagen; }
}
