package com.example.therecipe.RecipeApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Reftrofit {

    private const val BASE_URL = "https://dummyjson.com/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}