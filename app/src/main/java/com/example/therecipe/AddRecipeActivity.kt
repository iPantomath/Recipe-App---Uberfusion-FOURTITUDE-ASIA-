package com.example.therecipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.therecipe.JsonProcessor.Item
import com.example.therecipe.RecipeApi.ImageSource
import com.example.therecipe.RecipeApi.Recipe
import com.example.therecipe.databinding.ActivityAddRecipeBinding
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding
    private var jsonProcessor = JsonProcessor()
    private var recipeList = ArrayList<Item>()
    private var selectedImageUri: Uri? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var category = intent.getStringExtra("category")
        val repository = SharedPreferences(application)

        binding.addRecipeImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.category.text = "Recipe style: $category"

        binding.recipeActivitygoBack.setOnClickListener {
            onBackPressed()
        }

        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }
        binding.saveButton.setOnClickListener {
            val dishname = binding.recipeNameEditTextView.text.toString()
            val cuisine = category
            val imagePath = selectedImageUri?.let {
                saveImageToInternalStorage(this, it, "$dishname.jpg")
            }
            if (imagePath != null) {
                // Store imagePath in your recipe data
            }
            val image = imagePath
            val servings = binding.servingEditTextView.text.toString()
            val cooktime = binding.cookTimeEditTextView.text.toString()
            val ingredients = binding.ingredientsEditTextView.text.toString()
            val instructions = binding.stepsData.text.toString()
            val imageSource = ImageSource.LOCAL
//            var latestList: ArrayList<Item> = ArrayList()

            Log.i("AddRecipeActivity", binding.recipeNameEditTextView.text.toString())
//            recipeList = jsonProcessor.callRecipeData(this)
//            val updatedItem = Item(dishname, dishname, dishname, dishname, dishname, dishname)
//            recipeList.add(updatedItem)
            Log.i("AddRecipeActivity", recipeList.toString())
//            Log.i("AddRecipeActivity", updatedItem.toString())

            if (image != null) {
                addRecipeExample(dishname, cuisine, image, servings, cooktime, ingredients, instructions, imageSource)
            }
            finish()
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                selectedImageUri = uri
                binding.addRecipeImage.setImageURI(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

//            val sourceJsonString = jsonProcessor.loadJSONFromAsset(this, "recipetypes.json")
//        if (sourceJsonString != null) {
//            val ingredients = sourceJsonString?.let { it1 -> extractIngredients(it1) }
//            val recipe = JsonProcessor.Recipe(
//                dishname,
//                dishname,
//                dishname,
//                ingredients!!,
//                dishname,
//                false
//            )
//            val recipeData = JsonProcessor.RecipeData(listOf(recipe))
//            val jsonString = jsonProcessor.convertToJsonString(recipeData)
//            val fileName = "testSaving0.json"
//            jsonProcessor.saveJSONToInternalStorage(this, fileName, jsonString)
//            Log.i("AddRecipeActivity", jsonString)
//        }
//
//
//
//            jsonProcessor.saveJSONToInternalStorage(this, "testSaving0.json", recipeList.toString())
//        }
//
//        Log.i("AddRecipeActivity", binding.recipeNameEditTextView.text.toString())
//        //binding.recipeNameEditTextView.text.
//        recipeList = jsonProcessor.callRecipeData(this)

    }

    fun saveImageToInternalStorage(context: Context, imageUri: Uri, filename: String): String? {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val directory = context.filesDir
            val file = File(directory, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
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

    private fun addRecipeExample(
        dishname: String,
        cuisine: String?,
        image: String,
        servings: String,
        cooktime: String,
        ingredients: String,
        instructions: String,
        imageSource: ImageSource
    ) {
        // 1. Create a new Recipe object
        val repository = SharedPreferences(application)
        var id = (repository.getAllRecipes().size).plus(1)
        Log.d("RecipeDataViewmodel", "distinct(): ${id}")
        val newRecipe = cuisine?.let {
            Recipe(
                id = id,
                name = dishname, // Unique name
                ingredients = splitParagraphBySentences(ingredients),
                instructions = splitParagraphBySentences(instructions),
                prepTimeMinutes = 15,
                cookTimeMinutes = cooktime.toInt(),
                servings = servings.toInt(),
                cuisine = cuisine.toString(),
                image = image,
                imageSource = imageSource
            )
        }
        if (newRecipe != null) {
            repository.addRecipe(newRecipe)
        }
//        recipeDataViewmodel.recipes.observe(this, Observer { recipes ->
//            for (recipe in recipes) {
//                val newRecipe = Recipe(
//                    id = recipe.id,
//                    name = recipe.name,
//                    ingredients = recipe.ingredients,
//                    instructions = recipe.instructions,
//                    prepTimeMinutes = recipe.prepTimeMinutes,
//                    cookTimeMinutes = recipe.cookTimeMinutes,
//                    servings = recipe.servings,
//                    cuisine = recipe.cuisine,
//                    image = recipe.image,
//                )
                Log.i(
                    "addRecipeExample()", "Dish Name: ${dishname}, " +
                            "Image: ${image}, " +
                            "Category: ${cuisine}, " +
                            "Ingredients : ${splitParagraphBySentences(ingredients)}, " +
                            "Instructions: ${splitParagraphBySentences(instructions)}, "
                )
//            }
//        })
    }

    fun splitParagraphBySentences(paragraph: String): List<String> {
        return paragraph.split(Regex("(?<=[.!?])\\s+"))
    }
}
