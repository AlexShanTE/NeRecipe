package com.shante.nerecipe.domain

class GetRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun getRecipe(recipeId: Int): RecipeItem {
        return recipeListRepository.getRecipe(recipeId)
    }

}