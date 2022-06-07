package com.shante.nerecipe.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipeBinding
import com.shante.nerecipe.domain.Recipe

class RecipeListAdapter(
    private val recipeInteractionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipeListAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, recipeInteractionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RecipeBinding,
        private val listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        init {
            itemView.setOnClickListener {
                listener.onRecipeItemClicked(recipe)
            }
            binding.favoriteButton.setOnClickListener{listener.onFavoriteClicked(recipe)}

        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                author.text = recipe.author
                title.text = recipe.title
                category.text = recipe.category
                cookingTime.text = recipe.cookingTime
                recipePreview.setImageResource(R.mipmap.ic_food)
                when (recipe.isFavorite) {
                    true -> favoriteButton.setImageResource(R.drawable.ic_star_24)
                    false -> favoriteButton.setImageResource(R.drawable.ic_star_border_24)
                }
            }
        }

    }
}

private object DiffCallBack : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }

}