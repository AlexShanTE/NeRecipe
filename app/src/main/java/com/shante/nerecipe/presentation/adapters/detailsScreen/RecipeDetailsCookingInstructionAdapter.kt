package com.shante.nerecipe.presentation.adapters.detailsScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.CookingStepItemBinding
import com.shante.nerecipe.domain.CookingStep

class RecipeDetailsCookingInstructionAdapter :
    ListAdapter<CookingStep, RecipeDetailsCookingInstructionAdapter.CookingStepsViewHolder>(
        DiffCallBackCookingSteps
    ) {

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
                step.text = "Step ${adapterPosition + 1}"
                if (cookingStep.stepImageURL == null) {
                    stepPreview.visibility = View.GONE
                } else {
                    stepPreview.visibility = View.VISIBLE
                    Glide.with(binding.stepPreview)
                            .asDrawable()
                            .load(cookingStep.stepImageURL)
                            .error(R.mipmap.ic_launcher)
                            .into(binding.stepPreview)
                    }
                cookingStepOptionsButton.visibility = View.GONE
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
