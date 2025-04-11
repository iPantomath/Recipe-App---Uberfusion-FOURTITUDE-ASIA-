package com.example.therecipe

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.therecipe.RecipeApi.AllRecipeRes
import com.example.therecipe.RecipeApi.Recipe
import com.example.therecipe.RecipeApi.RecipeApi
import com.example.therecipe.RecipeApi.Reftrofit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val api = Reftrofit.instance.create(RecipeApi::class.java)


    fun getRecipesFromApi(callback: (List<Recipe>) -> Unit){
        val localRecipes = getAllRecipes()
        if (localRecipes.isNotEmpty()) {
            callback(localRecipes)
        } else {
            fetchRecipesFromApi { apiRecipes ->
                if (apiRecipes.isNotEmpty()) {
                    saveRecipes(apiRecipes)
                    callback(apiRecipes)
                } else {
                    callback(emptyList())
                }

            }
        }
    }

    fun getAllRecipes(): List<Recipe> {
        val json = sharedPreferences.getString("recipes", null)
        return if (json != null) {
            val type = object : TypeToken<List<Recipe>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun fetchRecipesFromApi(callback: (List<Recipe>) -> Unit){
        Log.d("API_RESPONSE", " Repository fetching")
        api.getAllRecipes().enqueue(object : Callback<AllRecipeRes> {
            override fun onResponse(call: Call<AllRecipeRes>, response: Response<AllRecipeRes>) {
                Log.d("API_RESPONSE", "onResponse")
                if (response.isSuccessful) {
                    Log.d("API_RESPONSE", "Success: ${response.body()}")
                    val apiRecipes = response.body()?.recipes ?: emptyList()
                    Log.d("API_DATA", "API Recipes Count: ${apiRecipes.size}")
                    apiRecipes.forEachIndexed { index, recipe ->
                        Log.d("API_DATA", "Recipe $index: $recipe")
                    }
                    callback(apiRecipes)
                } else {
                    Log.e("API_ERROR", "Response Failed: ${response.errorBody()?.string()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<AllRecipeRes>, t: Throwable) {
                // Handle failure
                Log.d("API_RESPONSE", "onFailure")
                callback(emptyList())
            }
        })
    }

    fun addRecipe(recipe: Recipe) {
        val recipes = getAllRecipes().toMutableList()
        recipes.add(recipe)
        saveRecipes(recipes)
    }

    fun updateRecipe(recipe: Recipe) {
        val recipes = getAllRecipes().toMutableList()
        val index = recipes.indexOfFirst { it.id == recipe.id }
        if (index != -1) {
            recipes[index] = recipe
            saveRecipes(recipes)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        val recipes = getAllRecipes().toMutableList()
        recipes.remove(recipe)
        saveRecipes(recipes)
    }

    private fun saveRecipes(recipes: List<Recipe>) {
        val json = gson.toJson(recipes)
        sharedPreferences.edit().putString("recipes", json).apply()

    }

}