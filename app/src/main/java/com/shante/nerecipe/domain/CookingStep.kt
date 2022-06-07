package com.shante.nerecipe.domain

data class CookingStep(
    val description: String,
    val id: Int = UNDEFINED_ID
) {

    companion object {
        const val UNDEFINED_ID = -1
    }

}