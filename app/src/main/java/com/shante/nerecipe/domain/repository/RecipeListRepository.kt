package com.shante.nerecipe.domain.repository

import androidx.lifecycle.LiveData
import com.shante.nerecipe.domain.Recipe

interface RecipeListRepository {

    fun addRecipe(recipe: Recipe)

    fun deleteRecipe(recipe: Recipe)

    fun editRecipe(recipe: Recipe)

    fun getRecipe(recipeId: Int): Recipe

    fun getRecipeList(): LiveData<List<Recipe>>

    fun favorite(recipeId: Int)

    fun findRecipeByRequest(request:String)

    companion object {
        const val CANCEL_SEARCH_REQUEST = "cancel request"
    }

}