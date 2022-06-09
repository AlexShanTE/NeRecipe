package com.shante.nerecipe.presentation.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.shante.nerecipe.data.InMemoryRecipeListRepositoryImpl
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository
import com.shante.nerecipe.domain.useCases.*
import com.shante.nerecipe.presentation.adapters.iInteractionListeners.RecipeListInteractionListener
import com.shante.nerecipe.utils.SingleLiveEvent

class RecipeListViewModel(
    application: Application
) : AndroidViewModel(application), RecipeListInteractionListener {

    private val repository = InMemoryRecipeListRepositoryImpl

    private val addRecipeItemUseCase = AddRecipeItemUseCase(repository)
    private val deleteRecipeItemUseCase = DeleteRecipeItemUseCase(repository)
    private val editRecipeItemUseCase = EditRecipeItemUseCase(repository)
    private val getRecipeItemUseCase = GetRecipeItemUseCase(repository)
    private val getRecipeListUseCase = GetRecipeListUseCase(repository)


    val recipeList = getRecipeListUseCase.getRecipeList()

    val navigateToRecipeDetailsScreen = SingleLiveEvent<Recipe>()


    fun deleteRecipe(recipe: Recipe) {
        deleteRecipeItemUseCase.deleteRecipe(recipe)
    }

    fun editRecipe(recipe: Recipe) {
        editRecipeItemUseCase.editRecipe(recipe)
    }

    fun addRecipe(recipe: Recipe) {
        addRecipeItemUseCase.addRecipe(recipe)
    }

    fun getRecipe(recipeId: Int): Recipe {
        return getRecipeItemUseCase.getRecipe(recipeId)
    }


    // region RecipeListInteractionListener

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)

    override fun onRecipeItemClicked(recipe: Recipe) {
        Toast.makeText(
            getApplication<Application>().applicationContext,
            "Clicked on recipe ${recipe.id}",
            Toast.LENGTH_SHORT
        ).show()
        navigateToRecipeDetailsScreen.value = recipe
    }

    override fun onSearchClicked(request: String) = repository.findRecipeByRequest(request)

    override fun onCancelClicked() = repository.findRecipeByRequest(RecipeListRepository.CANCEL_SEARCH_REQUEST)

    // endregion RecipeInteractionListener

}