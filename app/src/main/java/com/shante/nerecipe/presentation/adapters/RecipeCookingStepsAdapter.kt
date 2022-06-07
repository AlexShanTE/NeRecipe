package com.shante.nerecipe.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.CookingStepItemBinding
import com.shante.nerecipe.databinding.IngredientsItemBinding
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient

class RecipeCookingStepsAdapter :
    ListAdapter<CookingStep, RecipeCookingStepsAdapter.CookingStepsViewHolder>(DiffCallBackCookingSteps) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingStepsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CookingStepItemBinding.inflate(inflater, parent, false)
        return CookingStepsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CookingStepsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CookingStepsViewHolder(
        private val binding: CookingStepItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var cookingStep: CookingStep

        fun bind(cookingStep: CookingStep) {
            this.cookingStep = cookingStep
            with(binding) {
                cookingStepDescription.text = cookingStep.description
                step.text = "Step ${cookingStep.id + 1}"
            }
        }
    }
}

private object DiffCallBackCookingSteps : DiffUtil.ItemCallback<CookingStep>() {
    override fun areItemsTheSame(oldItem: CookingStep, newItem: CookingStep): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CookingStep, newItem: CookingStep): Boolean {
        return oldItem == newItem
    }

}
