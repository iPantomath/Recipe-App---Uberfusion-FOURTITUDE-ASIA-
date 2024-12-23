package com.example.therecipe

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.therecipe.JsonProcessor.Item
import com.example.therecipe.databinding.ActivityAddRecipeBinding
import com.example.therecipe.databinding.ActivityCategoryBinding
import org.json.JSONObject

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding
    private var jsonProcessor = JsonProcessor()
    private var recipeList = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var category = intent.getStringExtra("category")

        binding.category.text = "Recipe style: $category"

        binding.recipeActivitygoBack.setOnClickListener {
            onBackPressed()
        }

        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }
        binding.saveButton.setOnClickListener {
            val dishname = binding.recipeNameEditTextView.text.toString()
            var latestList: ArrayList<Item> = ArrayList()

            Log.i("AddRecipeActivity", binding.recipeNameEditTextView.text.toString())
            recipeList = jsonProcessor.callRecipeData(this)
            val updatedItem = Item(dishname,dishname,dishname,dishname,dishname,dishname)
            recipeList.add(updatedItem)
            Log.i("AddRecipeActivity", recipeList.toString())
            Log.i("AddRecipeActivity", updatedItem.toString())


            val sourceJsonString = jsonProcessor.loadJSONFromAsset(this, "recipetypes.json")
        if (sourceJsonString != null) {
            val ingredients = sourceJsonString?.let { it1 -> extractIngredients(it1) }
            val recipe = JsonProcessor.Recipe(
                dishname,
                dishname,
                dishname,
                ingredients!!,
                dishname,
                false
            )
            val recipeData = JsonProcessor.RecipeData(listOf(recipe))
            val jsonString = jsonProcessor.convertToJsonString(recipeData)
            val fileName = "testSaving0.json"
            jsonProcessor.saveJSONToInternalStorage(this, fileName, jsonString)
            Log.i("AddRecipeActivity", jsonString)
        }



            jsonProcessor.saveJSONToInternalStorage(this, "testSaving0.json", recipeList.toString())
        }

        Log.i("AddRecipeActivity", binding.recipeNameEditTextView.text.toString())
        //binding.recipeNameEditTextView.text.
        recipeList = jsonProcessor.callRecipeData(this)

    }

    override fun onBackPressed() {
        finish()
    }

    private fun extractIngredients(jsonString: String): List<JsonProcessor.Ingredient> {
        val ingredientsList = mutableListOf<JsonProcessor.Ingredient>()
        try {
            val jsonObject = JSONObject(jsonString)
            val recipesArray = jsonObject.getJSONArray("recipes")

            if (recipesArray.length() > 0) {
                val recipeObject = recipesArray.getJSONObject(0) // Assuming you want ingredients from the first recipe
                val ingredientsArray = recipeObject.getJSONArray("ingredients")

                for (i in 0 until ingredientsArray.length()) {
                    val ingredientObject = ingredientsArray.getJSONObject(i)
                    val name = ingredientObject.getString("name")
                    val quantity = ingredientObject.getString("quantity")
                    ingredientsList.add(JsonProcessor.Ingredient(name, quantity))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ingredientsList
    }
}
