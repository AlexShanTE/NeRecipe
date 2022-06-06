package com.shante.nerecipe.domain

interface RecipeListRepository {

    fun addRecipe(recipe: RecipeItem)

    fun deleteRecipe(recipe: RecipeItem)

    fun editRecipe(recipe: RecipeItem)

    fun getRecipe(recipeId: Int): RecipeItem

    fun getRecipeList(): List<RecipeItem>

}