package com.shante.nerecipe.presentation.adapters.interactionListeners

import com.shante.nerecipe.domain.Recipe

interface RecipeListInteractionListener {

    fun onFavoriteClicked(recipe: Recipe)

    fun onRecipeItemClicked(recipe: Recipe)

    fun onSearchClicked(request: String)

    fun onAddClicked()

    fun onCancelClicked()

}