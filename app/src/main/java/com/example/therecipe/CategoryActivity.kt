package com.example.therecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.therecipe.JsonProcessor.Item
import com.example.therecipe.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private var jsonProcessor = JsonProcessor()
    private var recipeList = ArrayList<Item>()
    var categoryRecipes: ArrayList<Item> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getCategory = intent.getStringExtra("category")
        Log.i("CategoryActivity()", " Category$getCategory getCategory$getCategory")
        binding.categoryTitle.text = getCategory

        binding.goBack.setOnClickListener { onBackPressed() }

        displayRecipeByCategory(getCategory)
        onSelectRecipe()
        binding.addImageView.setOnClickListener { onAddRecipe(getCategory) }

    }

    private fun onAddRecipe(getCategory: String?) {
        var intent = Intent(this@CategoryActivity, AddRecipeActivity::class.java)
        Log.d("CatergotyActivity onAddRecipe()", "Here now")
        intent.putExtra("category", getCategory)
        startActivity(intent)
    }

    private fun onSelectRecipe() {

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount>0){
            supportFragmentManager.popBackStack()
            findViewById<FrameLayout>(R.id.recipe_fragment_container).visibility = View.GONE
        }else{
            super.onBackPressed()
        }
    }

    private fun displayRecipeByCategory(category: String?) {

        val recyclerview = findViewById<RecyclerView>(R.id.categoryRecyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recipeList = jsonProcessor.callRecipeData(this)
        var lowercaseCategory = category?.lowercase()

        for (item in recipeList) {
            Log.i("displayRecipeByCategory()", "Line: 45, Size: "+ item.category+" "+lowercaseCategory+" "+recipeList.size.toString())
            if(item.category==lowercaseCategory){
                Log.i("popularRecyclerView()", "Line: 47, Size: " +categoryRecipes.size.toString())
                val dishname = item.dishName
                val image = item.image
                val category = item.category
                val ingredient = item.ingredients
                val steps = item.steps
                val recommended = item.recommended
                val popularItem = Item(dishname,image,category,ingredient,steps,recommended)
                categoryRecipes.add(popularItem)
                Log.i("popularRecyclerView()", "Dish Name: $dishname, " +
                        "Image: $image, " +
                        "Category: $category, " +
                        "Ingredients : $ingredient, " +
                        "Steps: $steps, " +
                        "Recommended: $recommended")

            }
        }
        Log.i("displayRecipeByCategory()", "Line: 65, Size: " + categoryRecipes.size.toString())
        recyclerview.adapter = RecipeAdapter(categoryRecipes)
    }
}
