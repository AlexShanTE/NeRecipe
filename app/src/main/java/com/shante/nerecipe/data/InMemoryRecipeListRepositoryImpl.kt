package com.shante.nerecipe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.RecipeListRepository

object InMemoryRecipeListRepositoryImpl : RecipeListRepository {

    private const val GENERATED_RECIPE_AMOUNT = 5

    private var id = 0
    private val recipeListLD = MutableLiveData<List<Recipe>>()
    private val recipeList = mutableListOf<Recipe>()


    init {
        for (i in 0..GENERATED_RECIPE_AMOUNT) {
            val newRecipe = Recipe(
                title = "Recipe №$i",
                author = "Me",
                category = "Russian food"
            )
            addRecipe(newRecipe)
        }
    }

    override fun addRecipe(recipe: Recipe) {
        if (recipe.id == Recipe.UNDEFINED_ID) {
            val recipeId = id++
            recipeList.add(recipe.copy(id = recipeId)) //add new recipe
        } else editRecipe(recipe) //edit
        updateList()
    }

    override fun deleteRecipe(recipe: Recipe) {
        recipeList.remove(recipe)
        updateList()
    }

    override fun editRecipe(recipe: Recipe) {
        recipeList.replaceAll {
            if (it.id == recipe.id) recipe else it
        }
        updateList()
    }

    override fun getRecipe(recipeId: Int): Recipe {
        return recipeList.find {
            it.id == recipeId
        } ?: throw RuntimeException("Recipe with $recipeId not found")
    }

    override fun getRecipeList(): LiveData<List<Recipe>> {
        return recipeListLD
    }

    private fun updateList() {
        recipeListLD.value = recipeList.toList()
    }

}