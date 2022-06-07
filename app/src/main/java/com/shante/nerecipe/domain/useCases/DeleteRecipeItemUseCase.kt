package com.shante.nerecipe.domain.useCases

import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository

class DeleteRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun deleteRecipe(recipe: Recipe) {
        recipeListRepository.deleteRecipe(recipe)
    }

}