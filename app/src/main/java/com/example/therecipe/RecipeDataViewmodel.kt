package com.example.therecipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.therecipe.RecipeApi.AllRecipeRes
import com.example.therecipe.RecipeApi.Recipe
import com.example.therecipe.RecipeApi.RecipeApi
import com.example.therecipe.RecipeApi.Reftrofit
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDataViewmodel(application: Application) : AndroidViewModel(application){

    private val repository : SharedPreferences = SharedPreferences(application)
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes : LiveData<List<Recipe>> = _recipes
//    private val api = Reftrofit.instance.create(RecipeApi::class.java)

//    fun fetchRecipes(){
//        Log.d("API_RESPONSE", " ViewModel fetching")
//        api.getAllRecipes().enqueue(object : Callback<AllRecipeRes>{
//            override fun onResponse(call: Call<AllRecipeRes>, response: Response<AllRecipeRes>) {
//                Log.d("API_RESPONSE", "onResponse")
//                if (response.isSuccessful) {
//                    Log.d("API_RESPONSE", "Success: ${response.body()}")
//                    _recipes.value = response.body()?.recipes ?: emptyList()
//                } else {
//                    Log.e("API_ERROR", "Response Failed: ${response.errorBody()?.string()}")
//                }
//            }
//            override fun onFailure(call: Call<AllRecipeRes>, t: Throwable) {
//                // Handle failure
//                Log.d("API_RESPONSE", "onFailure")
//                _recipes.value = emptyList()
//            }
//        })
//        }

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        repository.getRecipesFromApi { apiRecipes ->
            Log.d("VIEWMODEL_DATA", "Recipes received in ViewModel: ${apiRecipes.size}")
            apiRecipes.forEachIndexed { index, recipe ->
                Log.d("VIEWMODEL_DATA", "Recipe $index: $recipe")
            }
            _recipes.value = apiRecipes
        }
        Log.d("loadRecipes", recipes.value.toString())
    }

    fun addRecipe(recipe: Recipe) {
        repository.addRecipe(recipe)
        loadRecipes()
    }

    fun updateRecipe(recipe: Recipe) {
        repository.updateRecipe(recipe)
        loadRecipes() // Refresh the list
    }

    fun deleteRecipe(recipe: Recipe) {
        repository.deleteRecipe(recipe)
        loadRecipes() // Refresh the list
    }

}