package com.shante.nerecipe.utils

import android.net.Uri
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shante.nerecipe.db.RecipeEntity
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.domain.Recipe
import java.lang.reflect.Type
import java.util.*


object IngredientListTypeConverter {
        private val gson = Gson()
        @TypeConverter
        fun stringToListIngredients(data: String?): List<Ingredient> {
            if (data == null)  return Collections.emptyList()
            val listType: Type = object : TypeToken<List<Ingredient>>() {}.type
            return gson.fromJson<List<Ingredient>>(data, listType)
        }

        @TypeConverter
        fun ingredientsListToString(someObjects: List<Ingredient>): String {
            if (someObjects == null) return ""
            return gson.toJson(someObjects)
        }
}

object CookingStepListTypeConverter {
     private val gson = Gson()

        @TypeConverter
        fun stringToCookingStepsList(data: String?): List<CookingStep> {
            if (data == null) return Collections.emptyList()
            val listType: Type = object : TypeToken<kotlin.collections.List<String>>() {}.type
            val listOfStringOfSteps = gson.fromJson<List<String>>(data, listType)
            val listOfCookingSteps : MutableList<CookingStep> = mutableListOf()
            for (step in listOfStringOfSteps) {
                listOfCookingSteps.add(
                    CookingStep(
                        description = step.substringAfter("description=").substringBefore(","),
                        stepImageUri = ImageUriConverter.fromString(step.substringAfter("stepImageUri=").substringBefore(",")),
                        id = step.substringAfter("id=").substringBefore(")").toInt()
                    )
                )
            }
            return listOfCookingSteps
        }

        @TypeConverter
        fun cookingStepsListToString(someObjects: List<CookingStep>): String {
            if (someObjects == null) return ""
            val listOfString : MutableList<String> = mutableListOf()
            for (step in someObjects){
                listOfString.add(step.toString())
            }
            return gson.toJson(listOfString)
        }
}

object ImageUriConverter {
        @TypeConverter
        fun fromString(value: String?): Uri? {
            return if (value == null) null else Uri.parse(value)
        }

        @TypeConverter
        fun toString(uri: Uri?): String? {
            return uri?.toString()
        }
}

object DeleteTypeConverter {
    @TypeConverter
    fun getIdFromRecipe(recipe: RecipeEntity): Int {
        return recipe.id
    }
}
