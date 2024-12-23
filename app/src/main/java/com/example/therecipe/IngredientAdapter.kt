package com.example.therecipe

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * TODO: document your custom view class.
 */
class IngredientAdapter( private val ingredients: List<RecipeActivity.Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.ingredientName)
            val qty: TextView = itemView.findViewById(R.id.ingredientQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientAdapter.ViewHolder, position: Int) {
        val ingredients = ingredients[position]
        holder.name.text = ingredients.name
        holder.qty.text = ingredients.quantity
        Log.i("IngredientAdapter onBindViewHolder()", "Ing Name & Qty: "+ ingredients.name+" "+ingredients.quantity)

    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

}