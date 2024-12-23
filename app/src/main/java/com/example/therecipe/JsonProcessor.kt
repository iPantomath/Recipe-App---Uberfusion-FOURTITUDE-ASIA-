package com.example.therecipe

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class JsonProcessor {

    private val recipeList = ArrayList<Item>()
    private val updatedRecipeList = ArrayList<Item>()
    private lateinit var itemsArray: JSONArray



    fun callRecipeData(context: Context) : ArrayList<Item> {
        val sourceJsonString = loadJSONFromAsset(context, "recipetypes.json")
        if (sourceJsonString != null) {
            val jsonObject = parseJsonData(sourceJsonString)
            if (jsonObject != null) {
                Log.i("HomepageActivity onCreate", jsonObject.toString())
                //val recipeDataString = convertToJsonString(jsonObject)
                itemsArray = jsonObject.getJSONArray("recipes")
                for (i in 0 until itemsArray.length()) {
                    val itemObject = itemsArray.getJSONObject(i)
                    val dishName = itemObject.getString("dishName")
                    val image = itemObject.getString("image")
                    val category = itemObject.getString("category")
                    val ingredients = itemObject.getString("ingredients")
                    val steps = itemObject.getString("steps")
                    val recommended = itemObject.getString("recommended")
                    recipeList.add(Item(dishName, image, category, ingredients, steps, recommended))
                    Log.i("callRecipeData", "Dish Name: $dishName, Image: $image, Category: $category, Recommended: $recommended")
                }
                return recipeList
            }
        }
        return TODO("Provide the return value")
    }

    fun loadJSONFromAsset(context: Context, filename: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(filename)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun parseJsonData(jsonString: String): JSONObject? {
        return try {
            val jsonObject = JSONObject(jsonString)
            JSONObject(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun modifyJsonData(jsonObject: JSONObject): ArrayList<Item> {
        val newRecipeArray = jsonObject.getJSONArray("recipes")
        val newItem = JSONObject()
        newItem.put("dishName", "3")
        newItem.put("image", "2131165427")
        newItem.put("category", "Value 3")
        newRecipeArray.put(newItem)
        for (i in 0 until newRecipeArray.length()) {
            val itemObject = newRecipeArray.getJSONObject(i)
            val id = itemObject.getString("dishName")
            val name = itemObject.getString("image")
            val value = itemObject.getString("category")
            updatedRecipeList.add(JsonProcessor.Item(id, name, value, "", "", ""))
            Log.i("callRecipeData", "Dish Name: $id, Image: $name, Category: $value")
        }
        return updatedRecipeList
    }

    fun convertToJsonString(recipeData: RecipeData): String {
        val jsonObject = JSONObject()
        val recipesArray = JSONArray()
        for (recipe in recipeData.recipes) {
            val recipeObject = JSONObject()
            recipeObject.put("dishName", recipe.dishName)
            recipeObject.put("image", recipe.image)
            recipeObject.put("category", recipe.category)
            recipeObject.put("steps", recipe.steps)
            recipeObject.put("recommended", recipe.recommended)

            val ingredientsArray = JSONArray()
            for (ingredient in recipe.ingredients) {
                val ingredientObject = JSONObject()
                ingredientObject.put("name", ingredient.name)
                ingredientObject.put("quantity", ingredient.quantity)
                ingredientsArray.put(ingredientObject)
            }
            recipeObject.put("ingredients", ingredientsArray)
            recipesArray.put(recipeObject)
        }
        jsonObject.put("recipes", recipesArray)
        return jsonObject.toString()
    }

    fun saveJSONToInternalStorage(context: Context, fileName: String, jsonString: String) {
        try {
            val fileOutputStream: FileOutputStream? = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream?.write(jsonString.toByteArray())
            fileOutputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    data class Item(val dishName: String, val image: String, val category: String, val  ingredients: String, val steps: String, val recommended: String)

    data class Ingredient(val name: String, val quantity: String)
    data class Recipe(
        val dishName: String,
        val image: String,
        val category: String,
        val ingredients: List<Ingredient>,
        val steps: String,
        val recommended: Boolean
    )
    data class RecipeData(val recipes: List<Recipe>)

}