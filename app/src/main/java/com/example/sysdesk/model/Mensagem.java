package com.example.sysdesk.model;

public class Mensagem {
    private String texto;
    private boolean isUsuario; // true se a mensagem for do usuário, false se for do técnico ou bot

    public Mensagem(String texto, boolean isUsuario) {
        this.texto = texto;
        this.isUsuario = isUsuario;
    }

    public String getTexto() { return texto; }
    public boolean isUsuario() { return isUsuario; }
}
