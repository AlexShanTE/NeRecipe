package com.shante.nerecipe.utils

import com.shante.nerecipe.domain.Kitchen
import kotlin.random.Random

object RecipeFilling {

    fun getRandomKitchenCategory(): String {
        return Kitchen.selectedKitchenList.random().title
    }

    fun setRandomImagePreview(): String? {
        val imgUrl = listOf<String?>(
            "https://wp-s.ru/wallpapers/5/0/458570834775326/blyudo-iz-lososya-s-kartofelem-i-svezhimi-ovoshhami.jpg",
            "https://podruzke.ru/wp-content/uploads/2021/08/0-28.jpg",
            "https://flytothesky.ru/wp-content/uploads/2018/11/636547.jpg",
            "https://s1.1zoom.ru/b4652/504/The_second_dishes_Vienna_sausage_Potato_Wood_520555_2048x1152.jpg",
            null
        )
        val urlIndex = Random.nextInt(0, imgUrl.size)
        return imgUrl[urlIndex]
    }

    fun setRandomCookingStepImage(): String? {
        val imgUrl = listOf<String?>(
            "https://www.trn-news.ru/Ru/Upload/Image/food_egg-dish-of-italia_152K.jpg",
            "https://www.kiy-v.ua/media/wysiwyg/720/GGM-Gastro-FTKY240_720.jpg",
            "https://vinand.ru/media/i/lekue/0205500R14U150-lozhka-silikonovaia-tsvet-krasnyi-lekue-73340-2.jpg",
            null
        )
        val urlIndex = Random.nextInt(0, imgUrl.size)
        return imgUrl[urlIndex]
    }

}