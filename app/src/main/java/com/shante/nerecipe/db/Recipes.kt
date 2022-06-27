package com.shante.nerecipe.db

import android.net.Uri
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.domain.Recipe

fun RecipeEntity.toModel() = Recipe(
    id = id,
    title = title,
    author = author,
    authorId = authorId,
    kitchenCategory = kitchenCategory,
    cookingTime = cookingTime,
    ingredientsList = ingredientsList,
    cookingInstructionList = cookingInstructionList,
    previewUri = previewUri,
    isFavorite = isFavorite
)
fun Recipe.toEntity() = RecipeEntity(
    id = id,
    title = title,
    author = author,
    authorId = authorId,
    kitchenCategory = kitchenCategory,
    cookingTime = cookingTime,
    ingredientsList = ingredientsList,
    cookingInstructionList = cookingInstructionList,
    previewUri = previewUri,
    isFavorite = isFavorite
)