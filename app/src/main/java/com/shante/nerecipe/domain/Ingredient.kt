package com.shante.nerecipe.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val title: String,
    val value: String,
    val id: Int = UNDEFINED_ID
): Parcelable {
    companion object {
        const val UNDEFINED_ID = -1
    }
}