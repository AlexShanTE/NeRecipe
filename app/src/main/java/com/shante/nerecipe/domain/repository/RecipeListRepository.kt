package com.shante.nerecipe.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shante.nerecipe.domain.Recipe

interface RecipeListRepository {

    val data: LiveData<List<Recipe>>

    fun addRecipe(recipe: Recipe)

    fun deleteRecipe(recipe: Recipe)

    fun editRecipe(recipe: Recipe)

    fun getRecipe(recipeId: Int): Recipe

    fun getAllRecipes(): List<Recipe>

    fun favorite(recipeId: Int)

    companion object {
        const val CANCEL_SEARCH_REQUEST = "cancel request"
    }

}