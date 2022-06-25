package com.shante.nerecipe.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CookingStep(
    val description: String,
    val stepImageURL: String? = null,
    val id: Int = UNDEFINED_ID
):Parcelable {

    companion object {
        const val UNDEFINED_ID = -1
    }

}