package com.shante.nerecipe.domain

import androidx.lifecycle.LiveData

interface RecipeListRepository {

    fun addRecipe(recipe: Recipe)

    fun deleteRecipe(recipe: Recipe)

    fun editRecipe(recipe: Recipe)

    fun getRecipe(recipeId: Int): Recipe

    fun getRecipeList(): LiveData<List<Recipe>>

}