package com.shante.nerecipe.presentation.adapters.recipeListScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipePreviewItemBinding
import com.shante.nerecipe.domain.Kitchen
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.interactionListeners.RecipeListInteractionListener

class RecipeListAdapter(
    private val recipeInteractionListener: RecipeListInteractionListener
) : ListAdapter<Recipe, RecipeListAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipePreviewItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, recipeInteractionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RecipePreviewItemBinding,
        private val listener: RecipeListInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        init {
            itemView.setOnClickListener { listener.onRecipeItemClicked(recipe) }
            binding.favoriteButton.setOnClickListener { listener.onFavoriteClicked(recipe) }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                author.text = recipe.author
                title.text = recipe.title
                if (recipe.kitchenCategory == Kitchen.selectedKitchenList.last().title) {
                    kitchenCategory.visibility = View.GONE
                } else kitchenCategory.text = recipe.kitchenCategory
                if (recipe.cookingTime == null) {
                    cookingTime.visibility = View.GONE
                } else cookingTime.text = recipe.cookingTime
                if (recipe.previewUri !== null) {
                    Glide.with(recipePreview)
                        .asDrawable()
                        .load(recipe.previewUri)
                        .error(R.drawable.ic_no_image)
                        .into(recipePreview)
                } else {
                    recipePreview.setImageResource(R.drawable.ic_no_image)
                }
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