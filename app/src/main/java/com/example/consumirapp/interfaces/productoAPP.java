package com.example.consumirapp.interfaces;

import com.example.consumirapp.modelo.producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface productoAPP {
    @GET("issues.php")
    Call<List<producto>> find(@Query("j_id") String id);
}
