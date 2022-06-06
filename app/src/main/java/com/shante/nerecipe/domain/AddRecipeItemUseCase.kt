package com.shante.nerecipe.domain

class AddRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun addRecipe(recipe: RecipeItem) {
        recipeListRepository.addRecipe(recipe)
    }

}