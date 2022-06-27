package com.shante.nerecipe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository
import com.shante.nerecipe.utils.RecipeFilling
import kotlin.random.Random

object InMemoryRecipeListRepositoryImpl : RecipeListRepository {

    private const val GENERATED_RECIPE_AMOUNT = 10
    private const val GENERATED_INGREDIENTS_AMOUNT = 3
    private const val GENERATED_COOKING_STEPS_AMOUNT = 3
    private var nextId = GENERATED_RECIPE_AMOUNT

    override val data: MutableLiveData<List<Recipe>> = MutableLiveData(
        List(GENERATED_RECIPE_AMOUNT) { index ->
            Recipe(
                id = index + 1,
                title = "Recipe №${index + 1}",
                author = "Me",
                authorId = Random.nextInt(1, 5),
                kitchenCategory = RecipeFilling.getRandomKitchenCategory(),
                cookingTime = "1h",
                ingredientsList =
                List(GENERATED_INGREDIENTS_AMOUNT) { ingredientIndex ->
                    Ingredient(
                        "Ingredient $ingredientIndex",
                        "${Random.nextInt(10, 100)} шт.",
                        ingredientIndex
                    )
                },
                cookingInstructionList =
                List(GENERATED_COOKING_STEPS_AMOUNT) { cookingStepIndex ->
                    CookingStep(
                        description = "Description $cookingStepIndex",
                        stepImageUri = RecipeFilling.setRandomCookingStepImage(),
                        id = cookingStepIndex
                    )
                },
                previewUri = RecipeFilling.setRandomImagePreview()
            )
        }
    )

    private val recipeList
        get() = checkNotNull(data.value) {
            "data should be not null"
        }

    override fun addRecipe(recipe: Recipe) {
        if (recipe.id == Recipe.UNDEFINED_ID) {
            data.value = listOf(recipe.copy(id = ++nextId)) + recipeList //add new recipe
        } else editRecipe(recipe) //edit
    }

    override fun deleteRecipe(recipe: Recipe) {
        data.value = recipeList.filterNot { it.id == recipe.id }
    }

    override fun editRecipe(recipe: Recipe) {
        data.value = recipeList.map {
            if (it.id == recipe.id) recipe else it
        }
    }

    override fun getRecipe(recipeId: Int): Recipe {
        return recipeList.find {
            it.id == recipeId
        } ?: throw RuntimeException("Recipe with $recipeId not found")
    }

    override fun getAllRecipes(): List<Recipe> {
        return recipeList
    }

    override fun favorite(recipeId: Int) {
        data.value = recipeList.map { if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite) else it }
    }



}