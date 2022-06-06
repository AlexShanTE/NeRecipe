package com.shante.nerecipe.domain

class AddRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun addRecipe(recipe: Recipe) {
        recipeListRepository.addRecipe(recipe)
    }

}