package com.shante.nerecipe.domain

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CookingStep(
    val description: String,
    val stepImageUri: Uri? = null,
    val id: Int = UNDEFINED_ID
):Parcelable {

    companion object {
        const val UNDEFINED_ID = -1
    }

}