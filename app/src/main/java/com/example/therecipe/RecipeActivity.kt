package com.example.therecipe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.therecipe.databinding.ActivityRecipeBinding
import org.json.JSONArray

class RecipeActivity :AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private var dishName = String()
    private var category = String()
    private var image = String()
    private var ingredients = String()
    private var steps = String()
    private var recommended = String()
    private lateinit var recyclerView: RecyclerView
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intiView()
    }

    private fun intiView() {
        dishName = intent.getStringExtra("dishName").toString()
        category = intent.getStringExtra("category").toString().replaceFirstChar { it.uppercase() }
        image = intent.getStringExtra("image").toString()
        ingredients = intent.getStringExtra("ingredients").toString()
        steps = intent.getStringExtra("steps").toString()
        recommended = intent.getStringExtra("recommended").toString()
        Log.d("RecipeActivity", "Dish Name: $dishName, " +
                "Category: $category, "+
                "Image: $image, " +
                "Category: $category, " +
                "Ingredients : $ingredients, " +
                "Steps: $steps, " +
                "Recommended: $recommended")

        binding.dishName.text = dishName
        binding.category.text = category
        Glide.with(applicationContext)
            .load(image)
            .into(binding.recipeImage)
//        binding.ingredientsData.text = ingredients


        binding.stepsData.text = steps
        Log.d("RecipeActivity", "Ingredients : $ingredients")
        ingredientsView(ingredients)

        binding.recipeActivitygoBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun ingredientsView(ingredients: String) {
        recyclerView = findViewById(R.id.ingredientsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
        val ingredient = extractIngredients(ingredients)
        ingredientAdapter = IngredientAdapter(ingredient)
        recyclerView.adapter = ingredientAdapter
    }

    private fun extractIngredients(jsonString: String): List<Ingredient>{

        val result = mutableListOf<Ingredient>()
        val jsonArray = JSONArray(ingredients)
        for(item in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(item)
            val name = jsonObject.getString("name")
            val quantity = jsonObject.getString("quantity")
            result.add(Ingredient(name, quantity))
        }
        return result
    }

    data class Ingredient(val name: String, val quantity: String)

    override fun onBackPressed() {
        super.onBackPressed()
    }
}