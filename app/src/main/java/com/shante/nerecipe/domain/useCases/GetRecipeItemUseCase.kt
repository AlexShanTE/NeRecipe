package com.shante.nerecipe.domain.useCases

import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository

class GetRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun getRecipe(recipeId: Int): Recipe {
        return recipeListRepository.getRecipe(recipeId)
    }

}