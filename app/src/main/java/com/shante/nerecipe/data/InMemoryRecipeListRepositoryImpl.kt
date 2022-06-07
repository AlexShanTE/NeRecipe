package com.shante.nerecipe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository
import kotlin.random.Random

object InMemoryRecipeListRepositoryImpl : RecipeListRepository {

    private const val GENERATED_RECIPE_AMOUNT = 5
    private const val GENERATED_INGREDIENTS_AMOUNT = 5
    private const val GENERATED_COOKING_STEPS_AMOUNT = 5

    private var id = 0
    private val recipeListLD = MutableLiveData<List<Recipe>>()
    private val recipeList = mutableListOf<Recipe>()


    init {
        for (i in 0..GENERATED_RECIPE_AMOUNT) {
            val newRecipe = Recipe(
                title = "Recipe №$i",
                author = "Me",
                category = "Russian food",
                cookingTime = "1h\n45min",
                ingredientsList =
                List(GENERATED_INGREDIENTS_AMOUNT) {
                    Ingredient(
                        "Ingredient $it",
                        "${Random.nextInt(10, 100)} шт.",
                        it)
                },
                cookingStepsList =
                List(GENERATED_COOKING_STEPS_AMOUNT) {
                    CookingStep("Description $it", it)
                }
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

    override fun favorite(recipeId: Int) {
        recipeList.replaceAll {
            if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite) else it
        }
        updateList()
    }

    override fun showIngredients(recipe: Recipe) {
        recipeList.replaceAll {
            if (it.id == recipe.id) it.copy(isIngredientsShowed = !it.isIngredientsShowed) else it
        }
        updateList()
    }

    override fun showCookSteps(recipe: Recipe) {
        recipeList.replaceAll {
            if (it.id == recipe.id) it.copy(isCookingStepsShowed = !it.isCookingStepsShowed) else it
        }
        updateList()
    }

    private fun updateList() {
        recipeListLD.value = recipeList.toList()
    }

}