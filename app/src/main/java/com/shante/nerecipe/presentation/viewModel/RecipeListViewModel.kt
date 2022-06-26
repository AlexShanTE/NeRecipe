package com.shante.nerecipe.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.shante.nerecipe.data.InMemoryRecipeListRepositoryImpl
import com.shante.nerecipe.data.RoomRepository
import com.shante.nerecipe.db.AppDb
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository
import com.shante.nerecipe.presentation.adapters.interactionListeners.RecipeListInteractionListener
import com.shante.nerecipe.utils.SingleLiveEvent

class RecipeListViewModel(
    application: Application
) : AndroidViewModel(application), RecipeListInteractionListener {

//    private val repository = InMemoryRecipeListRepositoryImpl
    private val repository: RoomRepository = RoomRepository(
        dao = AppDb.getInstance(context = application).recipeDao
    )

    val data by repository::data

    val navigateToRecipeDetailsScreen = SingleLiveEvent<Recipe>()
    val navigateToRecipeEditorScreen = SingleLiveEvent<Recipe?>()

    fun addRecipe(recipe: Recipe) {
        repository.addRecipe(recipe)
    }

    fun getRecipeById(recipeId : Int) : Recipe {
        return repository.getRecipe(recipeId)
    }

    // region RecipeListInteractionListener

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)

    override fun onRecipeItemClicked(recipe: Recipe) {
        navigateToRecipeDetailsScreen.value = recipe
    }

    override fun onAddClicked() {
        navigateToRecipeEditorScreen.value = null
//        navigateToRecipeEditorScreen.call()
    }


    // endregion RecipeInteractionListener


}