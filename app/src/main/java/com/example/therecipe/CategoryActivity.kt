package com.example.therecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.therecipe.JsonProcessor.Item
import com.example.therecipe.RecipeApi.Recipe
import com.example.therecipe.databinding.ActivityCategoryBinding
import kotlin.text.lowercase

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
//    private var jsonProcessor = JsonProcessor()
    private var recipeList = ArrayList<Item>()
    private var categoryRecipes: ArrayList<Recipe> = ArrayList()
    private var categories: ArrayList<Recipe> = ArrayList()
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var getCategory = String()
    private var cuisineList = ArrayList<String>()
    var cuisineCatImage = ArrayList<String>()
//    private val repository : SharedPreferences = SharedPreferences(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategory = intent.getStringExtra("category").toString()
        Log.i("CategoryActivity()", " Category$getCategory getCategory$getCategory")
        binding.categoryTitle.text = getCategory

        binding.goBack.setOnClickListener { onBackPressed() }

        if(getCategory.equals("Others")){
            Log.i("CategoryActivity()", " loadCategories(if)")
          loadCategories()
        } else {
            Log.i("CategoryActivity()", " loadCategories(else)")
            loadRecipes()
        }

        binding.addImageView.setOnClickListener { onAddRecipe(getCategory) }

    }

    private fun loadRecipes() {
        Log.i("loadRecipes()", " loadRecipes()")
        val repository = SharedPreferences(application)
        repository.getRecipesFromApi { recipes ->
                displayRecipeByCategory(getCategory, recipes)
        }
    }

    private fun loadCategories() {
        Log.i("loadCategories()", "loadCategories()")
        val repository = SharedPreferences(application)
        repository.getRecipesFromApi { recipes ->
            displayAllCategories(getCategory, recipes)
        }

    }

    override fun onResume() {
        super.onResume()
        if(!getCategory.equals("Others")) {
            loadRecipes()
        }
    }

    private fun onAddRecipe(getCategory: String?) {
        var intent = Intent(this@CategoryActivity, AddRecipeActivity::class.java)
        Log.d("CatergotyActivity onAddRecipe()", "Here now")
        intent.putExtra("category", getCategory)
        startActivity(intent)
    }


    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount>0){
            supportFragmentManager.popBackStack()
            findViewById<FrameLayout>(R.id.recipe_fragment_container).visibility = View.GONE
        }else{
            super.onBackPressed()
        }
    }

    private fun displayRecipeByCategory(recipeCategory: String?, allRecipes: List<Recipe>) {
        val recyclerview = findViewById<RecyclerView>(R.id.categoryRecyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Clear the previous list
        categoryRecipes.clear()


        // Filter the recipes
        //val lowercaseCategory = category?.lowercase()
        for (recipe in allRecipes) {
            Log.i("displayRecipeByCategory()", "Dish Name: ${recipe.cuisine} | Category: $recipeCategory")

            /*if(category.equals("all")){
                categoryRecipes.add(recipe)
            } else */if (recipe.cuisine.equals(recipeCategory)) {
                Log.i("displayRecipeByCategory()", "Dish Name: ${recipe.name}, " +
                        "Image: $recipe.image, " +
                        "Category: ${recipe.cuisine}, " +
                        "Ingredients : ${recipe.ingredients}, " +
                        "Steps: ${recipe.instructions}, " +
                        "Recommended: ${recipe.cookTimeMinutes}")
                categoryRecipes.add(recipe)
            }
        }

        // Update the adapter with the filtered list
        recipeAdapter = RecipeAdapter(categoryRecipes)
        recyclerview.adapter = recipeAdapter
    }

    private fun displayAllCategories(category: String, allRecipes: List<Recipe>) {
        val recyclerview = findViewById<RecyclerView>(R.id.categoryRecyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Clear the previous list
        categories.clear()


        // Filter the recipes
        //val lowercaseCategory = category?.lowercase()
        for (recipe in allRecipes) {
            Log.i("displayAllCategories()", "Dish Name: ${recipe.cuisine} | Category: $category | ${cuisineList.size}")

            if (!cuisineList.contains(recipe.cuisine)) {
                cuisineList.add(recipe.cuisine)
                Log.i("cuisineList", "Cuisine Name: ${recipe.cuisine} | Category: ${recipe.name}")
                categories.add(recipe)
                Log.i("cuisineList", "Categories Size: ${categories.size}")
            }
//            if(category.equals("all")){
//                categoryRecipes.add(recipe)
//            } else if (recipe.cuisine.equals(category)) {
//                Log.i("displayRecipeByCategory()", "Dish Name: ${recipe.name}, " +
//                        "Image: $recipe.image, " +
//                        "Category: ${recipe.cuisine}, " +
//                        "Ingredients : ${recipe.ingredients}, " +
//                        "Steps: ${recipe.instructions}, " +
//                        "Recommended: ${recipe.cookTimeMinutes}")
//                categoryRecipes.add(recipe)
//            }
        }

        // Update the adapter with the filtered list
        categoriesAdapter = CategoriesAdapter(categories)
        recyclerview.adapter = categoriesAdapter
    }

//    private fun displayRecipeByCategory(category: String?) {
//
//        val recyclerview = findViewById<RecyclerView>(R.id.categoryRecyclerView)
//        recyclerview.layoutManager = LinearLayoutManager(this)
//        recipeList = jsonProcessor.callRecipeData(this)
//        var lowercaseCategory = category?.lowercase()
//
//        for (item in recipeList) {
//            Log.i("displayRecipeByCategory()", "Line: 45, Size: "+ item.category+" "+lowercaseCategory+" "+recipeList.size.toString())
//            if(item.category==lowercaseCategory){
//                Log.i("popularRecyclerView()", "Line: 47, Size: " +categoryRecipes.size.toString())
//                val dishname = item.dishName
//                val image = item.image
//                val category = item.category
//                val ingredient = item.ingredients
//                val steps = item.steps
//                val recommended = item.recommended
//                val popularItem = Item(dishname,image,category,ingredient,steps,recommended)
//                categoryRecipes.add(popularItem)
//                Log.i("popularRecyclerView()", "Dish Name: $dishname, " +
//                        "Image: $image, " +
//                        "Category: $category, " +
//                        "Ingredients : $ingredient, " +
//                        "Steps: $steps, " +
//                        "Recommended: $recommended")
//
//            }
//        }
//        Log.i("displayRecipeByCategory()", "Line: 65, Size: " + categoryRecipes.size.toString())
////        recyclerview.adapter = RecipeAdapter(categoryRecipes)
//    }
}
