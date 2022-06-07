package com.shante.nerecipe.domain.useCases

import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository

class AddRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun addRecipe(recipe: Recipe) {
        recipeListRepository.addRecipe(recipe)
    }

}