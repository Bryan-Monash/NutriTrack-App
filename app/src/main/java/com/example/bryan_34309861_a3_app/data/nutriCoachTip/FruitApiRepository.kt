package com.example.bryan_34309861_a3_app.data.nutriCoachTip

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FruitApiRepository(context: Context): ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.fruityvice.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(FruitViceApiService::class.java)

    suspend fun getFruitDetailsByName(fruitName: String): Fruit {
        val dummyFruit = Fruit(
            id = 0,
            name = "",
            family = "",
            order = "",
            genus = "",
            nutritions = Nutrition(
                calories = 0f,
                fat = 0f,
                sugar = 0f,
                carbohydrates = 0f,
                protein = 0f
            )
        )
        return try {
            val response =
                apiService.getFruitByName(fruitName)

            if (response.isSuccessful) {
                val apiFruit = response.body()!!
                Log.d("THE FRUIT", "$apiFruit")
                return apiFruit
            }
            dummyFruit
        } catch (e: Exception) {
            e.printStackTrace()
            dummyFruit
        }
    }
}