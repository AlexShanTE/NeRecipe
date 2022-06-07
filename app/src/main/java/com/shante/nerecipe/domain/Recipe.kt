package com.shante.nerecipe.domain

data class Recipe(
    val title: String,
    val author: String,
    val category: String,
    val cookingTime:String,
    val isFavorite:Boolean = false,
    val id: Int = UNDEFINED_ID
    ) {

    companion object {
        const val UNDEFINED_ID = -1
    }

}


