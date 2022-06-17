package com.shante.nerecipe.presentation.adapters.detailsScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shante.nerecipe.databinding.IngredientsItemBinding
import com.shante.nerecipe.domain.Ingredient

class RecipeDetailsIngredientsAdapter :
    ListAdapter<Ingredient, RecipeDetailsIngredientsAdapter.IngredientViewHolder>(
        DiffCallBackIngredient
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IngredientsItemBinding.inflate(inflater, parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IngredientViewHolder(
        private val binding: IngredientsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var ingredient: Ingredient

        fun bind(ingredient: Ingredient) {
            this.ingredient = ingredient
            with(binding) {
                ingredientTitle.text = ingredient.title
                ingredientValue.text = ingredient.value
                ingredientOptionsButton.visibility = View.GONE
            }
        }
    }
}

private object DiffCallBackIngredient : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }

}
