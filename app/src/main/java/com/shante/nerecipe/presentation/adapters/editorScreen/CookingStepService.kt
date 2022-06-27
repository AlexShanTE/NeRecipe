package com.shante.nerecipe.presentation.adapters.editorScreen

import com.shante.nerecipe.domain.CookingStep
import java.util.*

typealias  CookingStepListener = (cookingSteps: List<CookingStep>) -> Unit

object CookingStepService {

    private var cookingSteps = mutableListOf<CookingStep>()

    var targetCookingStep: CookingStep? = null

    private val listeners = mutableSetOf<CookingStepListener>()

    fun getCookingSteps(): List<CookingStep> {
        return cookingSteps
    }

    fun setCookingStepsList(cookingStepsList: MutableList<CookingStep>) {
        cookingSteps = cookingStepsList
        notifyChanges()
    }

    fun deleteCookingStep(cookingStep: CookingStep) {
        val indexToDelete = cookingSteps.indexOfFirst { it.id == cookingStep.id }
        if (indexToDelete != -1) {
            cookingSteps.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun addCookingStep(cookingStep: CookingStep) {
        if (cookingStep.id == CookingStep.UNDEFINED_ID) {
            if (cookingSteps.isNotEmpty()) {
                val newId = cookingSteps.maxOf { it.id } + 1
                cookingSteps.add(cookingStep.copy(id = newId))
            } else cookingSteps.add(cookingStep.copy(id = CookingStep.UNDEFINED_ID + 1))
            notifyChanges()
        } else editCookingStep(cookingStep)
    }

    fun editCookingStep(cookingStep: CookingStep) {
        cookingSteps = cookingSteps.map {
            if (it.id == cookingStep.id) cookingStep else it
        } as MutableList<CookingStep>
        notifyChanges()
    }

    fun moveCookingStep(cookingStep: CookingStep, moveBy: Int) {
        val oldIndex = cookingSteps.indexOfFirst { it.id == cookingStep.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= cookingSteps.size) return
        Collections.swap(cookingSteps, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: CookingStepListener) {
        listeners.add(listener)
        listener.invoke(cookingSteps)
    }

    fun clearCookingSteps(){
        cookingSteps.clear()
    }

    fun removeLisener(listener: CookingStepListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(cookingSteps) }
    }

}