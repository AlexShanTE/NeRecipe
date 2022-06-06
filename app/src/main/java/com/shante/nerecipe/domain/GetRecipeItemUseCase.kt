package com.shante.nerecipe.domain

class GetRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun getRecipe(recipeId: Int): Recipe {
        return recipeListRepository.getRecipe(recipeId)
    }

}