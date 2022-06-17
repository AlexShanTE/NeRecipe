package com.shante.nerecipe.utils

import android.content.Context
import android.widget.Toast
import com.shante.nerecipe.domain.Recipe

object RecipeFieldsChecker {

    fun checkRecipeForEmptyFields(context: Context, recipe: Recipe): Boolean {
        return when {
            recipe.title.isBlank() -> {
                toastMaker(context, "title shouldn't be empty")
                false
            }
            recipe.ingredientsList.isEmpty() -> {
                toastMaker(context, "Please add at least one ingredient")
                false
            }
            recipe.cookingInstructionList.isEmpty() -> {
                toastMaker(context, "Please add at least one cooking instruction step")
                false
            }
            else -> true
        }
    }

    private fun toastMaker(context: Context, value: String) {
        Toast.makeText(
            context,
            value,
            Toast.LENGTH_SHORT
        ).show()
    }

}