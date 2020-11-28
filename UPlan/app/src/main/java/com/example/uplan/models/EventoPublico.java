package com.example.uplan.models;

public class EventoPublico extends Evento {

    private String venue;
    private int asistentes;

    public EventoPublico() {
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }
}
