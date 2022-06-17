package com.shante.nerecipe.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    val title: String,
    val author: String,
    val authorId: Int,
    val kitchenCategory: String,
    val cookingTime: String?,
    val ingredientsList: List<Ingredient>,
    val cookingInstructionList: List<CookingStep>,
    val previewURL: String? = null,
    val isIngredientsShowed:Boolean = false,
    val isCookingStepsShowed:Boolean = false,
    val isFavorite: Boolean = false,
    val id: Int = UNDEFINED_ID
):Parcelable {

    companion object {
        const val UNDEFINED_ID = -1
    }

}


