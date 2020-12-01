package com.example.uplan;

import java.util.ArrayList;

public class Concierto {
    String publicadoPor;
    String nombre;
    String descripcion;
    String venue;
    ArrayList<String> artistas;
    ArrayList<String> links;
    int asistentes;
    String fecha;
    String fechaPublicacion;
    double latitud;
    double longitud;
    String imagen;

    public Concierto() {
    }

    public String getPublicadoPor() {
        return publicadoPor;
    }

    public void setPublicadoPor(String publicadoPor) {
        this.publicadoPor = publicadoPor;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public ArrayList<String> getArtistas() {
        return artistas;
    }

    public void setArtistas(ArrayList<String> artistas) {
        this.artistas = artistas;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
