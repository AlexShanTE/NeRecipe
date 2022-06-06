package com.shante.nerecipe.domain

class DeleteRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun deleteRecipe(recipe: RecipeItem) {
        recipeListRepository.deleteRecipe(recipe)
    }

}