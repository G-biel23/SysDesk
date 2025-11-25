package com.sysdesk.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Chamados")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "varchar(max)")
    private String descricao;

    @Column(nullable = false, length = 20)
    private String status = "ABERTO";

    @Column(name = "data_abertura", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime dataAbertura;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private com.sysdesk.api.model.Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private com.sysdesk.api.model.Usuario tecnico;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private com.sysdesk.api.model.Categoria categoria;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public com.sysdesk.api.model.Usuario getUsuario() { return usuario; }
    public void setUsuario(com.sysdesk.api.model.Usuario usuario) { this.usuario = usuario; }

    public com.sysdesk.api.model.Usuario getTecnico() { return tecnico; }
    public void setTecnico(com.sysdesk.api.model.Usuario tecnico) { this.tecnico = tecnico; }

    public com.sysdesk.api.model.Categoria getCategoria() { return categoria; }
    public void setCategoria(com.sysdesk.api.model.Categoria categoria) { this.categoria = categoria; }
}
