package com.shante.nerecipe.data

import androidx.lifecycle.map
import com.shante.nerecipe.db.RecipeDao
import com.shante.nerecipe.db.toEntity
import com.shante.nerecipe.db.toModel
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository

class RoomRepository(
    private val dao: RecipeDao
) : RecipeListRepository {

    override val data = dao.getAllRecipes().map { entities ->
        entities.map { it.toModel() }
    }

    override fun addRecipe(recipe: Recipe) {
        dao.addRecipe(recipe.toEntity())
    }

    override fun deleteRecipe(recipe: Recipe) {
        dao.deleteRecipe(recipe.toEntity())
    }

    override fun editRecipe(recipe: Recipe) {
        dao.editRecipe(recipe.toEntity())
    }

    override fun getRecipe(recipeId: Int): Recipe {
        return dao.getRecipe(recipeId).toModel()
    }

    override fun getAllRecipes(): List<Recipe> {
        return dao.getAllRecipes().value?.map { it.toModel() }  ?: emptyList()
    }

    override fun favorite(recipeId: Int) {
        dao.favorite(recipeId)
    }

}