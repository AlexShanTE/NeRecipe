package com.shante.nerecipe.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shante.nerecipe.data.InMemoryRecipeListRepositoryImpl
import com.shante.nerecipe.data.RoomRepository
import com.shante.nerecipe.db.AppDb
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.interactionListeners.RecipeDetailsInteractionListener
import com.shante.nerecipe.utils.SingleLiveEvent

class RecipeDetailsViewModel(
    application: Application
) : AndroidViewModel(application), RecipeDetailsInteractionListener {

//        private val repository = InMemoryRecipeListRepositoryImpl
    private val repository: RoomRepository = RoomRepository(
        dao = AppDb.getInstance(context = application).recipeDao
    )

    val data by repository::data

    val navigateToRecipeEditorScreen = SingleLiveEvent<Recipe?>()

    fun deleteRecipe(recipe: Recipe) {
        repository.deleteRecipe(recipe)
    }

    fun editRecipe(recipe: Recipe) {
        repository.editRecipe(recipe)
    }

    fun getRecipeById(recipeId : Int) : Recipe {
        return repository.getRecipe(recipeId)
    }

    override fun onFavoriteClicked(recipe: Recipe) = repository.favorite(recipe.id)

    override fun onDeleteClicked(recipe: Recipe) {
        deleteRecipe(recipe)
    }

    override fun onEditClicked(recipe: Recipe) {
        navigateToRecipeEditorScreen.value = recipe
    }

}