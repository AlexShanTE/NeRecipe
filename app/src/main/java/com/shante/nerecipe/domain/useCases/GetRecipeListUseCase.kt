package com.shante.nerecipe.domain.useCases

import androidx.lifecycle.LiveData
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository

class GetRecipeListUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun getRecipeList(): LiveData<List<Recipe>> {
        return recipeListRepository.getRecipeList()
    }

}