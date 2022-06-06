package com.shante.nerecipe.data

import com.shante.nerecipe.domain.RecipeItem
import com.shante.nerecipe.domain.RecipeListRepository
import java.lang.RuntimeException

object InMemoryRecipeListRepositoryImpl : RecipeListRepository {

    private val recipeList = mutableListOf<RecipeItem>()
    private var id = 0

    override fun addRecipe(recipe: RecipeItem) {
        if (recipe.id == RecipeItem.UNDEFINED_ID) {
            val recipeId = id++
            recipeList.add(recipe.copy(id = recipeId)) //add new recipe
        } else recipeList.add(recipe) //edit
    }

    override fun deleteRecipe(recipe: RecipeItem) {
        recipeList.remove(recipe)
    }

    override fun editRecipe(recipe: RecipeItem) {
        val oldRecipe = getRecipe(recipe.id)
        recipeList.remove(oldRecipe)
        addRecipe(recipe)
    }

    override fun getRecipe(recipeId: Int): RecipeItem {
        return recipeList.find {
            it.id == recipeId
        } ?: throw RuntimeException("Recipe with $recipeId not found")
    }

    override fun getRecipeList(): List<RecipeItem> {
        //return copy of recipeList
        return recipeList.toList()
    }
}