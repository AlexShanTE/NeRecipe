package com.shante.nerecipe.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shante.nerecipe.data.InMemoryRecipeListRepositoryImpl
import com.shante.nerecipe.domain.*
import com.shante.nerecipe.domain.useCases.*
import com.shante.nerecipe.presentation.adapters.iInteractionListeners.RecipeDetailsInteractionListener
import com.shante.nerecipe.utils.SingleLiveEvent

class RecipeDetailsViewModel(
    application: Application
) : AndroidViewModel(application), RecipeDetailsInteractionListener {

    private val repository = InMemoryRecipeListRepositoryImpl

    private val deleteRecipeItemUseCase = DeleteRecipeItemUseCase(repository)
    private val editRecipeItemUseCase = EditRecipeItemUseCase(repository)
    private val getRecipeListUseCase = GetRecipeListUseCase(repository)


    val recipeList = getRecipeListUseCase.getRecipeList()

    val navigateToRecipeEditorScreen = SingleLiveEvent<Recipe?>()

     fun deleteRecipe(recipe: Recipe) {
        deleteRecipeItemUseCase.deleteRecipe(recipe)
    }

     fun editRecipe(recipe: Recipe) {
        editRecipeItemUseCase.editRecipe(recipe)
    }

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)

    override fun onIngredientsShowClicked(recipe: Recipe) = repository.showIngredients(recipe)

    override fun onCookStepsShowClicked(recipe: Recipe) = repository.showCookSteps(recipe)

    override fun onDeleteClicked(recipe: Recipe) {
        deleteRecipe(recipe)
    }

    override fun onEditClicked(recipe: Recipe) {
        navigateToRecipeEditorScreen.value = recipe
    }

}