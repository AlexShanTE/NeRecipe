package com.shante.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shante.nerecipe.data.InMemoryRecipeListRepositoryImpl
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.domain.repository.RecipeListRepository
import com.shante.nerecipe.utils.DeleteTypeConverter

@Dao
interface RecipeDao {

    @Insert
    fun addRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes WHERE id= :recipe")
    @TypeConverters (DeleteTypeConverter::class)
    fun deleteRecipe(recipe: RecipeEntity)

    @Update
    fun editRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE id= :recipeId")
    fun getRecipe(recipeId: Int): RecipeEntity

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAllRecipes(): LiveData<List<RecipeEntity>>

    @Query(
        """
        UPDATE recipes SET
            isFavorite = CASE WHEN isFavorite THEN 0 ELSE 1 END
        WHERE id = :recipeId;
            """
    )
    fun favorite(recipeId: Int)

}