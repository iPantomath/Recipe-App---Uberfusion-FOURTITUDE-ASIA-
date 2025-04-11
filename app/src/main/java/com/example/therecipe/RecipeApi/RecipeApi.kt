package com.example.therecipe.RecipeApi

import retrofit2.Call
import retrofit2.http.GET

interface RecipeApi {

    @GET("recipes")
    fun getAllRecipes(): Call<AllRecipeRes>

}