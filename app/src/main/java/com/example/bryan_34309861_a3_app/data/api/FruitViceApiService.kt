package com.example.bryan_34309861_a3_app.data.api

import com.example.bryan_34309861_a3_app.data.model.Fruit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface FruitViceApiService {
    @GET("api/fruit/all")
    suspend fun getAllFruits(): Response<List<Fruit>>

    @GET("api/fruit/{name}")
    suspend fun getFruitByName(@Path("name") name: String): Response<Fruit>
}