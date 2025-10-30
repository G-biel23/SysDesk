package com.example.sysdesk.model;

public class Chamado {
    private int id;
    private String titulo;
    private String descricao;
    private String status;
    private String data_abertura;
    private int id_usuario;
    private int id_tecnico;
    private int id_categoria;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getData_abertura() { return data_abertura; }
    public void setData_abertura(String data_abertura) { this.data_abertura = data_abertura; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public int getId_tecnico() { return id_tecnico; }
    public void setId_tecnico(int id_tecnico) { this.id_tecnico = id_tecnico; }

    public int getId_categoria() { return id_categoria; }
    public void setId_categoria(int id_categoria) { this.id_categoria = id_categoria; }
}
