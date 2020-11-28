package com.example.uplan.models;

public class EventoFiesta extends Evento {

    private String venue;
    private String genero;
    private int asistentes;

    public EventoFiesta() {
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }
}
