package com.example.sysdesk.api;

import com.example.sysdesk.model.Categoria;
import com.example.sysdesk.model.Chamado;
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
    Call<Void> deletarChamado(@Path("id") int id);

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

}
