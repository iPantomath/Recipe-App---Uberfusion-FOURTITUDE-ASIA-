package com.example.therecipe

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RecipeAdapter(itemList: ArrayList<JsonProcessor.Item>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    val itemList = itemList

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishName: TextView = itemView.findViewById(R.id.dishname)
        val category: TextView = itemView.findViewById(R.id.category)
        val image: ImageView = itemView.findViewById(R.id.recipeImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        // Inflate your layout here
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_recyclerview, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.dishName.text = currentItem.dishName
        holder.category.text = currentItem.category.replaceFirstChar { it.uppercase()}


        val defaultOptions = RequestOptions().placeholder(R.drawable.no_picture_available)
            .error(R.drawable.no_picture_available)

        Glide.with(holder.itemView.context)
            .applyDefaultRequestOptions(defaultOptions)
            .load(currentItem.image)
            .into(holder.image)

        holder.itemView.setOnClickListener {
        val context = holder.itemView.context
        var intent = Intent(context, RecipeActivity::class.java)
        Log.d("onBindViewHolder", "Calling RecipeActivity")
        intent.putExtra("dishName", currentItem.dishName)
        intent.putExtra("category", currentItem.category)
        intent.putExtra("image", currentItem.image)
        intent.putExtra("ingredients", currentItem.ingredients)
        intent.putExtra("steps", currentItem.steps)
        intent.putExtra("recommended", currentItem.recommended)
        context.startActivity(intent)
        }
//
    }

    override fun getItemCount(): Int {
        Log.i("getItemCount()", "Recipe Adapter Size: " + itemList.size.toString())
        return itemList.size
    }

}
