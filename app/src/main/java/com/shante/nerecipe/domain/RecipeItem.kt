package com.shante.nerecipe.domain

data class RecipeItem(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val author: String,
    val category: String
) {

    companion object {
        const val UNDEFINED_ID = -1
    }

}


