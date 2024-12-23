package com.example.therecipe

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.therecipe.JsonProcessor.Item
import com.example.therecipe.databinding.ActivityHomepageBinding
import org.json.JSONArray
import java.io.File

class HomepageActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHomepageBinding
    private var jsonProcessor = JsonProcessor()
    private lateinit var linearLayoutContainer: LinearLayout
    val dataList = ArrayList<DataItem>()
    val itemList = ArrayList<JsonProcessor.Item>()
    //val list = ArrayList<RecipeDataInterface>()
    //lateinit var rcpList: List<RecipeDataInterface>
    lateinit var itemsArray:JSONArray
    private var recipeList = ArrayList<Item>()
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        linearLayoutContainer = findViewById(R.id.linearLayoutContainer)

        val dataList = createDataList()
        populateScrollview(dataList)
        //loadRecipeData()
        checkFileDest()
        recipeList = jsonProcessor.callRecipeData(this)

//        val sourceJsonString = jsonProcessor.loadJSONFromAsset(this, "recipetypes.json")
//        if (sourceJsonString != null) {
//            val jsonObject = jsonProcessor.parseJsonData(sourceJsonString)
//            if (jsonObject != null) {
//                Log.i("HomepageActivity onCreate", jsonObject.toString())
//                val modifiedJsonObject = jsonProcessor.modifyJsonData(jsonObject)         //To modify data
//                Log.i("HomepageActivity onCreate", modifiedJsonObject.toString())         //To modify data
//                val destinationJsonString = jsonProcessor.convertToJsonString(modifiedJsonObject)
//                jsonProcessor.saveJSONToInternalStoereryeryrage(this, "upToDateRecipe.json", destinationJsonString)     //To modify data
//                val textView = findViewById<TextView>(R.id.json)
//                textView.text = destinationJsonString
//                Log.i("HomepageActivity onCreate", destinationJsonString)       //To modify data
//                displayItems(recipeList)
                //val jsonObject = JSONObject(js)
//                itemsArray = modifiedJsonObject.getJSONArray("recipes")
//                for (i in 0 until itemsArray.length()) {
//                    val itemObject = itemsArray.getJSONObject(i)
//                    val id = itemObject.getString("dishName")
//                    val name = itemObject.getString("image")
//                    val value = itemObject.getString("category")
//                        itemList.add(JsonProcessor.Item(id, name, value, "", "", ""))
//                    Log.i("latestItemList", "Dish Name: $id, Image: $name, Category: $value")
//                }
//            }
//        }
        popularRecyclerView()
        //Log.i("Interface onCreate", rcpList.toString())
        scrollView = findViewById(R.id.homepageScrollView) // Replace with your ScrollView ID
        scrollView.post { scrollView.scrollTo(0, 0) } // Scroll to top after layout
    }

//    private fun loadRecipeData(modifiedJsonObject: JSONObject) {
//        val jsonString = loadJSONFromAsset(this, "recipetypes.json")
//        if (jsonString != null) {
//            val itemList = modifiedJsonObject
//            displayItems(itemList)
//        }
//    }
//    private fun displayItems(itemList: ArrayList<JsonProcessor.Item>) {
//       // val textView = findViewById<TextView>(R.id.textView)
//        var text = ""
//        for (item in itemList) {
//            Log.i("displayItems()", "Dish Name: ${item.dishName}, Image: ${item.image}, Category: ${item.category}, Ingredients : ${item.ingredients}, Steps: {${item.steps}}, Recommended: ${item.recommended}")
//            //text += "ID: ${item.dishName}, Name: ${item.dishName}, Value: ${item.value}\n"
//        }
//        //textView.text = text
//    }

    private fun createDataList(): ArrayList<DataItem> {

        dataList.add(DataItem("Local", R.drawable.local_category))
        dataList.add(DataItem("Asian", R.drawable.asian_category))
        dataList.add(DataItem("Western", R.drawable.western_category))
        dataList.add(DataItem("Snacks", R.drawable.snacks_category))
        dataList.add(DataItem("Beverage", R.drawable.beverage_category))
        return dataList
    }

    private fun populateScrollview(dataList: ArrayList<DataItem>) {
        for (item in dataList) {
//            val textView = TextView(this)
//            textView.text = item.title
//            linearLayoutContainer.addView(textView)
            val cardView = createCardView(item.title, item.catImg)
            linearLayoutContainer.addView(cardView)

        }
    }

    private fun createCardView(title: String, catImg: Int): CardView{
        Log.d("createCardView", title)
        val cardView = CardView(this)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 8, 8, 8)
        cardView.layoutParams = layoutParams
//        cardView.radius = 8f
//        cardView.cardElevation = 4f

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding(16, 16, 16, 16)

        val imageView = ImageView(this)
        val imageLayoutParams = LinearLayout.LayoutParams(
            200,
            200
        )

        imageView.layoutParams = imageLayoutParams
//        val drawable = ContextCompat.getDrawable(this, catImg)
//        imageView.setImageDrawable(drawable)
        val bitmap = loadScaledBitmap(resources, catImg, 200, 200)
        imageView.setImageBitmap(bitmap)
        imageView.setBackgroundResource(R.drawable.category_border)

        val textView = TextView(this)
        textView.text = title
        textView.textSize = 16f
        textView.gravity = android.view.Gravity.CENTER

        linearLayout.addView(imageView)
        linearLayout.addView(textView)
        cardView.addView(linearLayout)
        cardView.setOnClickListener{
            // Handle click event for the CardView
            var intent = Intent(this@HomepageActivity, CategoryActivity::class.java)
            Log.d("Cardview onClick()", title)
            intent.putExtra("category", title)
            startActivity(intent)
        }
//        linearLayoutContainer.addView(cardView)
        return  cardView
    }

    data class DataItem(val title: String, val catImg: Int)


    fun loadScaledBitmap(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

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

   /* fun loadJSONFromAsset(context: Context, filename: String): String? {
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
    }*/

    /*data class Item(val dishName: String, val image: String, val category: String, val  ingredients: String, val steps: String, val recommended: String)*/

    /*fun parseJsonData(jsonString: String): JSONObject? {
        return try {
            val jsonObject = JSONObject(jsonString)
            val itemsArray = jsonObject.getJSONArray("recipes")
            for (i in 0 until itemsArray.length()) {
                val itemObject = itemsArray.getJSONObject(i)
                val dishName = itemObject.getString("dishName")
                val image = itemObject.getString("image")
                val category = itemObject.getString("category")
                val ingredients = itemObject.getString("ingredients")
                val steps = itemObject.getString("steps")
                val recommended = itemObject.getString("recommended")
                itemList.add(Item(dishName, image, category, ingredients, steps, recommended))
            }
            JSONObject(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }*/

    /*fun modifyJsonData(jsonObject: JSONObject): JSONObject {
        val itemsArray = jsonObject.getJSONArray("recipes")
        val newItem = JSONObject()
        newItem.put("dishName", "3")
        newItem.put("image", "2131165427")
        newItem.put("category", "Value 3")
        itemsArray.put(newItem)
        return jsonObject
    }*/

    /*fun convertToJsonString(jsonObject: JSONObject): String {
        return jsonObject.toString()
    }*/

    /*fun saveJSONToInternalStorage(context: Context, fileName: String, jsonString: String) {
        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/

    fun checkFileDest(){
        val fileDir = applicationContext.filesDir
        val filePath = fileDir.absolutePath
        Log.d("FileCheck", "Internal Storage: $filePath")
        val internalFileExists = checkInternalStorageFile(this, "upToDateRecipe.json")
        val externalPublicFileExists = checkExternalStoragePublicFile("upToDateRecipe.json")
        val externalPrivateFileExists = checkExternalStoragePrivateFile(this, "upToDateRecipe.json")

//        //val textView = findViewById<TextView>(R.id.textView)
//        var text = "Internal Storage: $internalFileExists\n"
//        text += "External Storage (Public): $externalPublicFileExists\n"
//        text += "External Storage (Private): $externalPrivateFileExists"
//        //textView.text = text

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

    private fun popularRecyclerView(){
        var popularList: ArrayList<JsonProcessor.Item> = ArrayList()
        val recyclerview = findViewById<RecyclerView>(R.id.recommendedRecyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        Log.i("popularRecyclerView()", "Line: 296, Size: : " + popularList.size.toString())
        Log.i("popularRecyclerView()", "Line: 297, Size: : " + itemList.size.toString())


        for (item in recipeList) {
            Log.i("popularRecyclerView()", "Line: 300, Size: "+ popularList.size.toString())
            //if(item.recommended=="true"){
                Log.i("popularRecyclerView()", "Line: 302, Size: " +popularList.size.toString())
                val dishname = item.dishName
                val image = item.image
                val category = item.category
                val ing = item.ingredients
                val steps = item.steps
                val recommended = item.recommended
                val popularItem = JsonProcessor.Item(dishname,image,category,ing,steps,recommended)
                popularList.add(popularItem)
                Log.i("popularRecyclerView()", "Dish Name: $dishname, " +
                            "Image: $image, " +
                            "Category: $category, " +
                            "Ingredients : $ing, " +
                            "Steps: $steps, " +
                            "Recommended: $recommended")

           // }
        }
       // Log.i("popularRecyclerView()", "Line: 316, Size: " + list.size.toString())
        recyclerview.adapter = RecipeAdapter(popularList)
    }

    //data class HomePageItem(override val dishName: String, override val image: String, override val category: String, override val  ingredients: String, override val steps: String, override val recommended: String): RecipeDataInterface
}
