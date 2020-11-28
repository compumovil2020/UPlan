package com.example.uplan.models;

import java.util.List;

public class EventoGamer extends Evento {

    private String venue;
    private List<String> videojuegos;
    private int asistentes;

    public EventoGamer() {
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public List<String> getVideojuegos() {
        return videojuegos;
    }

    public void setVideojuegos(List<String> videojuegos) {
        this.videojuegos = videojuegos;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }
}
