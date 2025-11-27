package com.example.sysdesk.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Sugestao implements Serializable {

    private Integer id;
    private String descricao;
    private Usuario tecnico;
    private LocalDateTime dataHora;

    public Sugestao() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
