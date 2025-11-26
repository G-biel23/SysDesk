package com.example.sysdesk.event;

import com.example.sysdesk.model.Chamado;

public class ChamadoAtualizadoEvent {
    private Chamado chamado;

    public ChamadoAtualizadoEvent(Chamado chamado) {
        this.chamado = chamado;
    }

    public Chamado getChamado() {
        return chamado;
    }
}
