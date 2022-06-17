package com.shante.nerecipe.utils

import com.shante.nerecipe.domain.KitchenCategory

object RecipeFilling {

    fun getRandomKitchenCategory(): String {
        return KitchenCategory.kitchenCategory.random()
    }

}