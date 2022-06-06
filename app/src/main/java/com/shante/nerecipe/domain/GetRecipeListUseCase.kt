package com.shante.nerecipe.domain

class GetRecipeListUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun getRecipeList(): List<RecipeItem> {
        return recipeListRepository.getRecipeList()
    }

}