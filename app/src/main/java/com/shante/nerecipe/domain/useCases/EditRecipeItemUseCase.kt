package com.shante.nerecipe.domain.useCases

import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository

class EditRecipeItemUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun editRecipe(recipe: Recipe) {
        recipeListRepository.editRecipe(recipe)
    }

}