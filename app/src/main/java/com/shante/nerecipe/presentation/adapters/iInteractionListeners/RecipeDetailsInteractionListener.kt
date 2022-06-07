package com.shante.nerecipe.presentation.adapters.iInteractionListeners

import com.shante.nerecipe.domain.Recipe

interface RecipeDetailsInteractionListener {

    fun onFavoriteClicked(recipe: Recipe)

    fun onIngredientsShowClicked(recipe: Recipe)

    fun onCookStepsShowClicked(recipe: Recipe)

}