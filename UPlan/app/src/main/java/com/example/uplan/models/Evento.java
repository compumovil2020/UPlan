package com.example.uplan.models;

import java.util.Date;

public class Publicacion {

    private String nombre;
    private String descripcion;
    private String imgperfil;
    private String imgevento;
    private long timestamp;

    public Publicacion(){

    }

    public Publicacion(String nombre, String descripcion, String imgperfil, String imgevento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imgperfil = imgperfil;
        this.imgevento = imgevento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImgperfil() {
        return imgperfil;
    }

    public void setImgperfil(String imgperfil) {
        this.imgperfil = imgperfil;
    }

    public String getImgevento() {
        return imgevento;
    }

    public void setImgevento(String imgevento) {
        this.imgevento = imgevento;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
