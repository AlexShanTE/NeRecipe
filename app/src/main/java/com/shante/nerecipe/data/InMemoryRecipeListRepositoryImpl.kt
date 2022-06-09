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
    private val data = MutableLiveData<List<Recipe>>()
    private val recipeList = mutableListOf<Recipe>()
    private var sortedRecipeList = mutableListOf<Recipe>()


    init {
        for (i in 0..GENERATED_RECIPE_AMOUNT) {
            val recipe = Recipe(
                title = "Recipe №$i",
                author = "Me",
                category = "Russian food",
                cookingTime = "1h\n45min",
                ingredientsList =
                List(GENERATED_INGREDIENTS_AMOUNT) {
                    Ingredient(
                        "Ingredient $it",
                        "${Random.nextInt(10, 100)} шт.",
                        it
                    )
                },
                cookingStepsList =
                List(GENERATED_COOKING_STEPS_AMOUNT) {
                    CookingStep("Description $it", it)
                }
            )
            addRecipe(recipe)
        }
    }

    override fun addRecipe(recipe: Recipe) {
        if (recipe.id == Recipe.UNDEFINED_ID) {
            val recipeId = id++
            recipeList.add(recipe.copy(id = recipeId)) //add new recipe
        } else editRecipe(recipe) //edit
        updateList(isSorted = false)
    }

    override fun deleteRecipe(recipe: Recipe) {
        recipeList.remove(recipe)
        updateList(isSorted = false)
    }

    override fun editRecipe(recipe: Recipe) {
        recipeList.replaceAll {
            if (it.id == recipe.id) recipe else it
        }
        updateList(isSorted = false)
    }

    override fun getRecipe(recipeId: Int): Recipe {
        return recipeList.find {
            it.id == recipeId
        } ?: throw RuntimeException("Recipe with $recipeId not found")
    }

    override fun getRecipeList(): LiveData<List<Recipe>> {
        return data
    }

    override fun favorite(recipeId: Int) {
        recipeList.replaceAll { if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite) else it }
        if (sortedRecipeList.isEmpty()) {
            updateList(isSorted = false)
        } else {
            sortedRecipeList.replaceAll { if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite) else it }
            updateList(isSorted = true)
        }
    }

    override fun showIngredients(recipe: Recipe) {
        recipeList.replaceAll {
            if (it.id == recipe.id) it.copy(isIngredientsShowed = !it.isIngredientsShowed) else it
        }
        updateList(isSorted = false)
    }

    override fun showCookSteps(recipe: Recipe) {
        recipeList.replaceAll {
            if (it.id == recipe.id) it.copy(isCookingStepsShowed = !it.isCookingStepsShowed) else it
        }
        updateList(isSorted = false)
    }

    override fun findRecipeByRequest(request: String) {
        if (request == RecipeListRepository.CANCEL_SEARCH_REQUEST) {
            sortedRecipeList.clear()
            updateList(isSorted = false)
        } else {
            sortedRecipeList = recipeList.filter {
                it.title.contains(request, ignoreCase = true) || it.author.contains(
                    request,
                    ignoreCase = true
                )
            } as MutableList<Recipe>
            updateList(isSorted = true)
        }
    }

    private fun updateList(isSorted: Boolean) {
        if (isSorted)
            data.value = sortedRecipeList.toList()
        else
            data.value = recipeList.toList()
    }


}