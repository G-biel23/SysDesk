package com.example.sysdesk.event;

public class ChamadoExcluidoEvent {
    private final int idChamado;
    public ChamadoExcluidoEvent(int idChamado) { this.idChamado = idChamado; }
    public int getIdChamado() { return idChamado; }
}
