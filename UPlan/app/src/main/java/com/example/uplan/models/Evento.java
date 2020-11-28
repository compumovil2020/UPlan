package com.example.uplan.models;

public class Evento {

    private String tipo;
    private String nombreEv;
    private String descripcion;
    private String nombrePerf;
    private String imgperfil;
    private String imgevento;
    private Double latitud;
    private Double longitud;
    private long fechaEv;
    private long fechaPub;

    public Evento(){

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreEv() {
        return nombreEv;
    }

    public void setNombreEv(String nombreEv) {
        this.nombreEv = nombreEv;
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

    public long getFechaEv() {
        return fechaEv;
    }

    public void setFechaEv(long fechaEv) {
        this.fechaEv = fechaEv;
    }

    public String getNombrePerf() {
        return nombrePerf;
    }

    public void setNombrePerf(String nombrePerf) {
        this.nombrePerf = nombrePerf;
    }

    public long getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(long fechaPub) {
        this.fechaPub = fechaPub;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
