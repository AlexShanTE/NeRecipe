package com.shante.nerecipe.domain

class EditRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun editRecipe(recipe: RecipeItem) {
        recipeListRepository.editRecipe(recipe)
    }

}