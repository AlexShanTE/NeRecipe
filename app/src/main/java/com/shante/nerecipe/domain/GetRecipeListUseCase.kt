package com.shante.nerecipe.domain

import androidx.lifecycle.LiveData

class GetRecipeListUseCase(
    private val recipeListRepository: RecipeListRepository
) {

    fun getRecipeList(): LiveData<List<Recipe>> {
        return recipeListRepository.getRecipeList()
    }

}