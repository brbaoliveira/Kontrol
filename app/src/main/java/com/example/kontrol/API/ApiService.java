package com.example.kontrol.API;

import com.example.kontrol.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("usuarios")
    Call<Usuario> criarUsuario(@Body Usuario usuario);
}
