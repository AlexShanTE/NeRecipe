package com.shante.nerecipe.presentation.adapters.interactionListeners

import com.shante.nerecipe.domain.CookingStep

interface  CookingStepsInteractionListener {

    fun onCookingStepUp(cookingStep: CookingStep, moveBy: Int)

    fun onCookingStepDown(cookingStep: CookingStep, moveBy: Int)

    fun onCookingStepEdit(cookingStep: CookingStep)

    fun onCookingStepDelete(cookingStep: CookingStep)

}