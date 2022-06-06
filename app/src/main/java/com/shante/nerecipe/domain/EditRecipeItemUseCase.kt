package com.shante.nerecipe.domain

class EditRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun editRecipe(recipe: Recipe) {
        recipeListRepository.editRecipe(recipe)
    }

}