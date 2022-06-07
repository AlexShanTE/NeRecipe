package com.shante.nerecipe.presentation.adapters

import com.shante.nerecipe.domain.Recipe

interface RecipeInteractionListener {

    fun onFavoriteClicked(recipe: Recipe)

    fun onRecipeItemClicked(recipe: Recipe)

}