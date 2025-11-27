package com.example.sysdesk.api;

import com.example.sysdesk.model.Categoria;
import com.example.sysdesk.model.Chamado;
import com.example.sysdesk.model.Sugestao;
import com.example.sysdesk.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SysDeskApi {

    // === CATEGORIAS ===
    @GET("categorias")
    Call<List<Categoria>> getCategorias();

    @GET("categorias/{id}")
    Call<Categoria> getCategoriaById(@Path("id") int id);

    @POST("categorias")
    Call<Categoria> criarCategoria(@Body Categoria categoria);

    @PUT("categorias/{id}")
    Call<Categoria> atualizarCategoria(@Path("id") int id, @Body Categoria categoria);

    @DELETE("categorias/{id}")
    Call<Void> deletarCategoria(@Path("id") int id);

    // === CHAMADOS ===
    @GET("chamados")
    Call<List<Chamado>> getChamados();

    @GET("chamados/{id}")
    Call<Chamado> getChamadoById(@Path("id") int id);

    @POST("chamados")
    Call<Chamado> criarChamado(@Body Chamado chamado);

    @PUT("chamados/{id}")
    Call<Chamado> atualizarChamado(@Path("id") int id, @Body Chamado chamado);

    @DELETE("chamados/{id}")
    Call<Void> excluirChamado(@Path("id") int id);

    // Listar chamados abertos de um usuário
    @GET("chamados/usuario/{idUsuario}/abertos")
    Call<List<Chamado>> getChamadosAbertosDoUsuario(@Path("idUsuario") int idUsuario);

    @GET("chamados/abertos")
    Call<List<Chamado>> getChamadosAbertos();



    // === USUÁRIOS ===
    @GET("usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("usuarios/{id}")
    Call<Usuario> getUsuarioById(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @POST("/usuarios")
    Call<Usuario> criarUsuario(@Body Usuario usuario);

    @PUT("usuarios/{id}")
    Call<Usuario> atualizarUsuario(@Path("id") int id, @Body Usuario usuario);

    @DELETE("usuarios/{id}")
    Call<Void> deletarUsuario(@Path("id") int id);

    @POST("/usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    // Aceitar chamado
    @PUT("/chamados/{id}/aceitar")
    Call<Chamado> aceitarChamado(@Path("id") Integer chamadoId, @Body Usuario tecnico);

    // Enviar sugestão
    @POST("/chamados/{id}/sugestoes")
    Call<Sugestao> sugerirSolucao(@Path("id") int chamadoId, @Body Sugestao sugestao);

    // Buscar todas sugestões de um chamado
    @GET("/chamados/{id}/sugestoes")
    Call<List<Sugestao>> getSugestoes(@Path("id") int chamadoId);

    @GET("chamados/tecnico/{id}")
    Call<List<Chamado>> getChamadosPorTecnico(@Path("id") int tecnicoId);

    @GET("chamados/tecnico/{id}/status/{status}")
    Call<List<Chamado>> getChamadosPorTecnicoEStatus(
            @Path("id") int tecnicoId,
            @Path("status") String status
    );

    // Fechar chamado
    @PUT("chamados/{id}/fechar")
    Call<Chamado> fecharChamado(@Path("id") int id);

    @GET("chamados/disponiveis")
    Call<List<Chamado>> getChamadosDisponiveis();



}

