package com.example.therecipe.Homepage

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.therecipe.CategoryActivity
import com.example.therecipe.JsonProcessor
import com.example.therecipe.JsonProcessor.Item
import com.example.therecipe.R
import com.example.therecipe.RecipeAdapter
import com.example.therecipe.RecipeApi.ImageSource
import com.example.therecipe.RecipeApi.Recipe
import com.example.therecipe.RecipeApi.RecipeApi
import com.example.therecipe.RecipeDataViewmodel
import com.example.therecipe.databinding.ActivityHomepageBinding
import com.google.android.material.navigation.NavigationView
import java.io.File

class HomepageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomepageBinding
    private var jsonProcessor = JsonProcessor()
    private lateinit var linearLayoutContainer: LinearLayout
    //val dataList = ArrayList<DataItem>()
    private var cuisineList = ArrayList<String>()
    private var cuisineCatImage = ArrayList<String>()
    private val itemList = ArrayList<Item>()
    private var recipeList = ArrayList<Item>()
    private lateinit var scrollView: ScrollView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

//    private lateinit var recipeviewModel: RecipeDataViewmodel
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeDataViewmodel: RecipeDataViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navButton)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set the navigation item listener
        navigationView.setNavigationItemSelectedListener(this)
//        recipeDataViewmodel = ViewModelProvider(this).get(RecipeDataViewmodel::class.java)

        linearLayoutContainer = findViewById(R.id.linearLayoutContainer)
        //recipeviewModel.fetchRecipes()

//        populateScrollview(cuisineList)
        checkFileDest()
        recipeList = jsonProcessor.callRecipeData(this)

        cuisineCategoriesRecyclerView()
        allCuisineRecyclerView()
        scrollView = findViewById(R.id.homepageScrollView)
        scrollView.post { scrollView.scrollTo(0, 0) } // Scroll to top after layout

//        binding.menuImage.setOnClickListener {
//            Toast.makeText(this, "Not functional but looks good to be there isn't it?", Toast.LENGTH_LONG).show()
//        }

        //fetchRecipes()
//        addRecipeExample()
    }

//
//    private fun createDataList(): ArrayList<String> {
//
////        dataList.add(DataItem("Local", R.drawable.local_category))
////        dataList.add(DataItem("Asian", R.drawable.asian_category))
////        dataList.add(DataItem("Western", R.drawable.western_category))
////        dataList.add(DataItem("Snacks", R.drawable.snacks_category))
////        dataList.add(DataItem("Beverage", R.drawable.beverage_category))
////        return dataList
//
//        return cuisineList
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle Home click
//                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
            }
            R.id.nav_categories -> {
                // Handle Settings click
//                Toast.makeText(this, "Categories", Toast.LENGTH_LONG).show()
                var intent = Intent(this@HomepageActivity, CategoryActivity::class.java)
                Log.d("Cardview onClick()", "Others")
                intent.putExtra("category", "Others")
                startActivity(intent)
            }
            // Add more cases for other menu items
        }

        // Close the drawer after item click
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun populateCategoryScrollview(dataList: ArrayList<String>, cuisineCatImage: ArrayList<String>) {
        for (item in dataList) {
            Log.d("populateScrollview", "item: "+cuisineCatImage.get(dataList.indexOf(item)))
            val cardView = createCardView(item, cuisineCatImage.get(dataList.indexOf(item)))
            linearLayoutContainer.addView(cardView)

        }
    }

    private fun createCardView(title: String, catImg: String): CardView{
        Log.d("createCardView", title)
        val cardView = CardView(this)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 8, 8, 8)
        cardView.layoutParams = layoutParams

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(16, 16, 16, 16)

        val imageView = ImageView(this)
        val imageLayoutParams = LinearLayout.LayoutParams(
            200,
            200
        )

        imageView.layoutParams = imageLayoutParams
//        val bitmap = loadScaledBitmap(resources, catImg, 200, 200)
        Glide.with(applicationContext)
            .load(catImg)
            .apply(
                RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_picture_available) // Optional: Placeholder image while loading
                .error(R.drawable.no_picture_available) // Optional: Error image if loading fails
            )
            .into(imageView)
        imageView.setBackgroundResource(R.drawable.category_border)



        val textView = TextView(this)
        textView.text = title
        textView.textSize = 16f
        textView.gravity = Gravity.CENTER

        linearLayout.addView(imageView)
        linearLayout.addView(textView)
        cardView.addView(linearLayout)
        cardView.setOnClickListener{
            var intent = Intent(this@HomepageActivity, CategoryActivity::class.java)
            Log.d("Cardview onClick()", title)
            intent.putExtra("category", title)
            startActivity(intent)
        }
        return  cardView
    }

//    data class DataItem(val title: String, val catImg: Int)


//    fun loadScaledBitmap(res: Resources, resId: String, reqWidth: Int, reqHeight: Int): Bitmap {
//        val options = BitmapFactory.Options()
//        options.inJustDecodeBounds = true
//        BitmapFactory.decodeResource(res, resId, options)
//
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
//        options.inJustDecodeBounds = false
//        return BitmapFactory.decodeResource(res, resId, options)
//    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun checkFileDest(){
        val fileDir = applicationContext.filesDir
        val filePath = fileDir.absolutePath
        Log.d("FileCheck", "Internal Storage: $filePath")
        val internalFileExists = checkInternalStorageFile(this, "upToDateRecipe.json")
        val externalPublicFileExists = checkExternalStoragePublicFile("upToDateRecipe.json")
        val externalPrivateFileExists = checkExternalStoragePrivateFile(this, "upToDateRecipe.json")


        Log.d("FileCheck", "Internal Storage: $internalFileExists")
        Log.d("FileCheck", "External Storage (Public): $externalPublicFileExists")
        Log.d("FileCheck", "External Storage (Private): $externalPrivateFileExists")
        Log.d("FileCheck", "spaghetti_carbonara: "+ R.drawable.spaghetti_carbonara)
        Log.d("FileCheck", "chicken_curry: "+ R.drawable.chicken_curry)
        Log.d("FileCheck", "vegetable_stir_fry: "+ R.drawable.vegetable_stir_fry)
        Log.d("FileCheck", "roti_jala: "+ R.drawable.roti_jala)
        Log.d("FileCheck", "mojito_cocktails_recipe: "+ R.drawable.mojito_cocktails_recipe)
        Log.d("FileCheck", "cherry_bakewell_cocktail: "+ R.drawable.cherry_bakewell_cocktail)
        Log.d("FileCheck", "banana_muffins: "+ R.drawable.banana_muffins)

    }
    fun checkInternalStorageFile(context: Context, fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists() && file.canRead()
    }
    fun checkExternalStoragePublicFile(fileName: String): Boolean {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
        return file.exists() && file.canRead()
    }
    fun checkExternalStoragePrivateFile(context: Context, fileName: String): Boolean {
        val file = File(context.getExternalFilesDir(null), fileName)
        return file.exists() && file.canRead()

    }

    private fun cuisineCategoriesRecyclerView() {
        val loadingAllRecipes = findViewById<ProgressBar>(R.id.loadingAllRecipes)
        loadingAllRecipes.visibility = ProgressBar.VISIBLE
        recipeDataViewmodel.recipes.observe(this, Observer { recipes ->


            val sortedRecipes = recipes.sortedBy { it.cuisine }
            sortedRecipes.forEach { recipe ->
                Log.d("RecipeDataViewmodel", "Recipe: ${recipe.cuisine}")
                if (!cuisineList.contains(recipe.cuisine)) {
                    cuisineList.add(recipe.cuisine)
                    cuisineCatImage.add(recipe.image)
                }
            }
            Log.d("RecipeDataViewmodel", "Recipe: ${cuisineList.size}")
            populateCategoryScrollview(cuisineList,cuisineCatImage)

            loadingAllRecipes.visibility = ProgressBar.GONE
        })
    }

    private fun allCuisineRecyclerView(){
        var recipeList = mutableListOf<Recipe>()
        val recyclerview = findViewById<RecyclerView>(R.id.recommendedRecyclerView)
        val loadingCategory = findViewById<ProgressBar>(R.id.loadingCategory)
        recyclerview.layoutManager = LinearLayoutManager(this)
        loadingCategory.visibility = ProgressBar.VISIBLE
//        Log.i("popularRecyclerView()", "Line: 296, Size: : " + popularList.size.toString())
        Log.i("popularRecyclerView()", "Line: 297, Size: : " + itemList.size.toString())

        recipeDataViewmodel.recipes.observe(this, Observer { recipes ->
            for (recipe in recipes) {
//                Log.i("popularRecyclerView()", "Line: 300, Size: " + popularList.size.toString())
                //if(item.recommended=="true"){
//                Log.i("popularRecyclerView()", "Line: 302, Size: " + popularList.size.toString())
                val newRecipe = Recipe(
                    id = recipe.id,
                    name = recipe.name,
                    ingredients = recipe.ingredients,
                    instructions = recipe.instructions,
                    prepTimeMinutes = recipe.prepTimeMinutes,
                    cookTimeMinutes = recipe.cookTimeMinutes,
                    servings = recipe.servings,
                    cuisine = recipe.cuisine,
                    image = recipe.image,
                    imageSource = recipe.imageSource
                )
                recipeList.add(newRecipe)
                Log.i(
                    "popularRecyclerView()", "Dish Name: ${recipe.name}, " +
                            "Image: ${recipe.image}, " +
                            "Category: ${recipe.cuisine}, " +
                            "Ingredients : ${recipe.ingredients}, " +
                            "Instructions: ${recipe.instructions}, "
                )
            }
            recipeAdapter = RecipeAdapter(recipeList)
            recyclerview.adapter = recipeAdapter

            loadingCategory.visibility = ProgressBar.GONE
        })
    }

    private fun addRecipeExample() {
        // 1. Create a new Recipe object
        var id = (recipeDataViewmodel.recipes.value?.size)?.plus(1)
        Log.d("RecipeDataViewmodel", "distinct(): ${id}")
        val newRecipe = Recipe(
            id = id,
            name = "Chocolate Chip Pookies", // Unique name
            ingredients = listOf(
                "Flour",
                "Sugar",
                "Chocolate Chips",
                "Butter",
                "Eggs",
                "Vanilla Extract",
                "Baking Soda",
                "Salt"
            ),
            instructions = listOf(
                "Preheat oven to 375°F (190°C).",
                "Cream together the butter and sugars until light and fluffy.",
                "Beat in the eggs one at a time, then stir in the vanilla.",
                "In a separate bowl, whisk together the flour, baking soda, and salt.",
                "Gradually add the dry ingredients to the wet ingredients, mixing until just combined.",
                "Stir in the chocolate chips.",
                "Drop by rounded tablespoons onto ungreased baking sheets.",
                "Bake for 10-12 minutes, or until golden brown.",
                "Let cool on baking sheets for a few minutes before transferring to a wire rack to cool completely."
            ),
            prepTimeMinutes = 15,
            cookTimeMinutes = 12,
            servings = 24,
            cuisine = "Dessert",
            image = "https://example.com/chocolate_",
            imageSource = ImageSource.LOCAL
        )
        recipeDataViewmodel.addRecipe(newRecipe)
        recipeDataViewmodel.recipes.observe(this, Observer { recipes ->
            for (recipe in recipes) {
                val newRecipe = Recipe(
                    id = recipe.id,
                    name = recipe.name,
                    ingredients = recipe.ingredients,
                    instructions = recipe.instructions,
                    prepTimeMinutes = recipe.prepTimeMinutes,
                    cookTimeMinutes = recipe.cookTimeMinutes,
                    servings = recipe.servings,
                    cuisine = recipe.cuisine,
                    image = recipe.image,
                    imageSource = recipe.imageSource
                )
                Log.i(
                    "addRecipeExample()", "Dish Name: ${recipe.name}, " +
                            "Image: ${recipe.image}, " +
                            "Category: ${recipe.cuisine}, " +
                            "Ingredients : ${recipe.ingredients}, " +
                            "Instructions: ${recipe.instructions}, "
                )
            }
        })
    }


//    private lateinit var  recipeText: TextView

//    private fun fetchRecipes(){
//        val api = Reftrofit.instance.create(RecipeApi::class.java)
//        api.getAllRecipes().enqueue(object : Callback<AllRecipeRes>{
//            override fun onResponse(call: Call<AllRecipeRes>, response: Response<AllRecipeRes>) {
//                if (response.isSuccessful) {
//                    val recipes = response.body()?.recipes
//                    if (!recipes.isNullOrEmpty()) {
//                        for (recipe in recipes) {
//                            Log.d("API_RESPONSE", """
//                            Name: ${recipe.cuisine}
//                           /* 🍽️ Name: ${recipe.name}
//                            🔹 Ingredients: ${recipe.ingredients.joinToString(", ")}
//                            📝 Instructions: ${recipe.instructions.joinToString(" ➜ ")}*/
//                        """.trimIndent())
//                        }
//                    } else {
//                        Log.d("API_RESPONSE", "No recipes found.")
//                    }
//                } else {
//                    Log.e("API_ERROR", "Response failed: ${response.errorBody()?.string()}")
//                }
//            }
//            override fun onFailure(call: Call<AllRecipeRes>, t: Throwable) {
//                Log.e("API_ERROR", "Failed to load recipes", t)
//            }
//        })
//    }
}
