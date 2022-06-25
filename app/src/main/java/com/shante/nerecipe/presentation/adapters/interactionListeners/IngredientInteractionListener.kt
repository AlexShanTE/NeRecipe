package com.shante.nerecipe.presentation.adapters.interactionListeners

import com.shante.nerecipe.domain.Ingredient

interface IngredientInteractionListener {

    fun onIngredientUp(ingredient: Ingredient, moveBy: Int)

    fun onIngredientDown(ingredient: Ingredient, moveBy: Int)

    fun onIngredientEdit(ingredient: Ingredient)

    fun onIngredientDelete(ingredient: Ingredient)

}