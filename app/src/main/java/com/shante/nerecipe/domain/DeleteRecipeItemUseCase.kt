package com.shante.nerecipe.domain

class DeleteRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun deleteRecipe(recipe: Recipe) {
        recipeListRepository.deleteRecipe(recipe)
    }

}