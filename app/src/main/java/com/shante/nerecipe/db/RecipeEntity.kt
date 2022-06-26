package com.shante.nerecipe.db

import android.net.Uri
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.utils.CookingStepListTypeConverter
import com.shante.nerecipe.utils.ImageUriConverter
import com.shante.nerecipe.utils.IngredientListTypeConverter
import org.jetbrains.annotations.NotNull

@Entity(tableName = "recipes")
@TypeConverters(IngredientListTypeConverter::class,CookingStepListTypeConverter::class,ImageUriConverter::class)
class RecipeEntity(
    @ColumnInfo(name = "title")
    @NotNull
    val title: String,
    @ColumnInfo(name = "author")
    @NotNull
    val author: String,
    @ColumnInfo(name = "authorId")
    @NotNull
    val authorId: Int,
    @ColumnInfo(name = "kitchenCategory")
    @Nullable
    val kitchenCategory: String,
    @ColumnInfo(name = "cookingTime")
    @Nullable
    val cookingTime: String?,
    @ColumnInfo(name = "ingredientsList")
    @NotNull
    @TypeConverters(IngredientListTypeConverter::class)
    val ingredientsList: List<Ingredient>,
    @ColumnInfo(name = "cookingInstructionList")
    @NotNull
    @TypeConverters(CookingStepListTypeConverter::class)
    val cookingInstructionList: List<CookingStep>,
    @ColumnInfo(name = "previewUri")
    @Nullable
    @TypeConverters(ImageUriConverter::class)
    val previewUri: Uri?,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NotNull
    val id: Int,
)