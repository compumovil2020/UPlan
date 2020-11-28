package com.example.uplan.models;

import java.util.List;

public class EventoDeportivo extends Evento {

    private String deporte;
    private List<String> implementos;
    private String modalidad;
    private int asistentes;

    public EventoDeportivo() {
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public List<String> getImplementos() {
        return implementos;
    }

    public void setImplementos(List<String> implementos) {
        this.implementos = implementos;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }
}
