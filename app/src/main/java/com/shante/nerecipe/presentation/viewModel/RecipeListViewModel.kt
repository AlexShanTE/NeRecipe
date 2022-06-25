package com.shante.nerecipe.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shante.nerecipe.data.InMemoryRecipeListRepositoryImpl
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository
import com.shante.nerecipe.domain.useCases.*
import com.shante.nerecipe.presentation.adapters.interactionListeners.RecipeListInteractionListener
import com.shante.nerecipe.utils.SingleLiveEvent

class RecipeListViewModel(
    application: Application
) : AndroidViewModel(application), RecipeListInteractionListener {

    private val repository = InMemoryRecipeListRepositoryImpl

    private val addRecipeItemUseCase = AddRecipeItemUseCase(repository)
    private val getRecipeListUseCase = GetRecipeListUseCase(repository)

    val recipeList = getRecipeListUseCase.getRecipeList()

    val navigateToRecipeDetailsScreen = SingleLiveEvent<Recipe>()
    val navigateToRecipeEditorScreen = SingleLiveEvent<Recipe?>()

    fun addRecipe(recipe: Recipe) {
        addRecipeItemUseCase.addRecipe(recipe)
    }

    // region RecipeListInteractionListener

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)

    override fun onRecipeItemClicked(recipe: Recipe) {
        navigateToRecipeDetailsScreen.value = recipe
    }

    override fun onSearchClicked(request: String) = repository.findRecipeByRequest(request)

    override fun onAddClicked() {
        navigateToRecipeEditorScreen.value = null
//        navigateToRecipeEditorScreen.call()
    }
    override fun onCancelClicked() = repository.findRecipeByRequest(RecipeListRepository.CANCEL_SEARCH_REQUEST)

    // endregion RecipeInteractionListener

}