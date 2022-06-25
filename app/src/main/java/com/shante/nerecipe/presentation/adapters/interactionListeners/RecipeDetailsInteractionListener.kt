package com.shante.nerecipe.presentation.adapters.interactionListeners

import com.shante.nerecipe.domain.Recipe

interface RecipeDetailsInteractionListener {

    fun onFavoriteClicked(recipe: Recipe)

    fun onDeleteClicked(recipe: Recipe)

    fun onEditClicked(recipe: Recipe)

}