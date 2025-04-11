package com.example.therecipe

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.therecipe.RecipeApi.ImageSource
import com.example.therecipe.RecipeApi.Recipe

class CategoriesAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<CategoriesAdapter.RecipeViewHolder>() {

//    val itemList = itemList

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       /* fun bind(recipe: Recipe) {*/
//            val cuisineName: TextView = itemView.findViewById(R.id.dishname)
            val cuisineCategory: TextView = itemView.findViewById(R.id.categories)
//            val cuisineImage: ImageView = itemView.findViewById(R.id.recipeImage)

//            cuisineName.text = recipe.name
//            cuisine.text = recipe.cuisine
//            Glide.with(itemView.context)
//                .load(recipe.image)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.no_picture_available)
//                .into(image)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Log.i("onCreateViewHolder", "Size: ${recipes.size}")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_recyclerview, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.i("getItemCount()", "Size: ${recipes.size}")
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Log.i("onBindViewHolder", "Size: ${recipes.size}")
        val recipe = recipes[position]
//        holder.cuisineName.text = recipe.name
        holder.cuisineCategory.text = recipe.cuisine
//        if(recipe.imageSource?.equals(ImageSource.LOCAL) == true){
//
//            val bitmap = BitmapFactory.decodeFile(recipe.image) // recipe.image is a local path
//            holder.cuisineImage.setImageBitmap(bitmap)
//
//        }else{
//            Glide.with(holder.itemView.context)
//                .load(recipe.image)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.no_picture_available)
//                .into(holder.cuisineImage)
//        }


        holder.itemView.setOnClickListener {
            // Handle item click here
            val context = holder.itemView.context
            val intent = Intent(context, CategoryActivity::class.java)
            // Add extras to the intent (e.g., recipe details)
            Log.d("RecipeAdapter", "Dish Name: ${recipe.name}, "+
            "Cuisine: ${recipe.cuisine}, "+ "Recipe Ingredients: ${recipe.ingredients}" +
            "Instruction: ${recipe.instructions}" + "Cook Time: ${recipe.cookTimeMinutes}" +
            "Servings: ${recipe.servings}")
            intent.putExtra("category", recipe.cuisine)
            context.startActivity(intent)
        }
    }
}

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        // Inflate your layout here
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_recyclerview, parent, false)
//        return RecipeViewHolder(view)
//    }

//    override fun getItemCount(): Int {
//        Log.i("getItemCount()", "Size: ${recipes.size}")
//        return recipes.size
//    }

//    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
//        val recipe = recipes[position]
////        holder.bind(recipe)
//
////        val currentItem = itemList[position]
////        holder.name.text = recipe.name
////        holder.category.text = currentItem.category.replaceFirstChar { it.uppercase()}
////
////
////        val defaultOptions = RequestOptions().placeholder(R.drawable.no_picture_available)
////            .error(R.drawable.no_picture_available)
////
////        Glide.with(holder.itemView.context)
////            .applyDefaultRequestOptions(defaultOptions)
//////            .load(currentItem.image)
//////            .into(holder.image)
////
//        holder.itemView.setOnClickListener {
//            val context = holder.itemView.context
//            var intent = Intent(context, RecipeActivity::class.java)
//            Log.d("onBindViewHolder", "Calling RecipeActivity")
//            intent.putExtra("dishName", currentItem.dishName)
//            intent.putExtra("category", currentItem.category)
//            intent.putExtra("image", currentItem.image)
//            intent.putExtra("ingredients", currentItem.ingredients)
//            intent.putExtra("steps", currentItem.steps)
//            intent.putExtra("recommended", currentItem.recommended)
//            context.startActivity(intent)
//        }
//    }
//

//}
