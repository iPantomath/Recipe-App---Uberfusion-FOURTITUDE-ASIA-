package com.example.therecipe.RecipeApi

import java.io.Serializable

enum class ImageSource {
    NETWORK, LOCAL
}

data class Recipe(
    val id: Int? = null,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val cuisine	: String,
    val image: String,
    val imageSource: ImageSource?
) : Serializable

data class AllRecipeRes(
    val recipes: List<Recipe>
)
