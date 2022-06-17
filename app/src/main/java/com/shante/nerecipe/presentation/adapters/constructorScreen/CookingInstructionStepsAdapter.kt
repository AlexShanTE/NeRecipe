package com.shante.nerecipe.presentation.adapters.constructorScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.CookingStepItemBinding
import com.shante.nerecipe.domain.CookingStep

interface  CookingInstructionStepsActionListener {

    fun onCookingStepUp(cookingStep: CookingStep, moveBy: Int)

    fun onCookingStepDown(cookingStep: CookingStep, moveBy: Int)

    fun onCookingStepEdit(cookingStep: CookingStep)

    fun onCookingStepDelete(cookingStep: CookingStep)

}

class CookingInstructionStepsAdapter(
    val context: Context,
    private val cookingInstructionStepsActionListener: CookingInstructionStepsActionListener
) : RecyclerView.Adapter<CookingInstructionStepsAdapter.CookingStepViewHolder>(), View.OnClickListener {


    var cookingSteps: List<CookingStep> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cooking_step_options_button -> {
                showPopUpMenu(v)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingStepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CookingStepItemBinding.inflate(inflater, parent, false)

        binding.cookingStepOptionsButton.setOnClickListener(this)

        return CookingStepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CookingStepViewHolder, position: Int) {
        val cookingStep = cookingSteps[position]
        with(holder.binding) {
            cookingStepOptionsButton.tag = cookingStep
            step.text = context.getString(R.string.step, position + 1)
            cookingStepDescription.text = cookingStep.description
            if (cookingStep.stepImageURL == null) {
                stepPreview.visibility = View.GONE
            } else {
                stepPreview.visibility = View.VISIBLE
                Glide.with(holder.binding.stepPreview)
                    .asDrawable()
                    .load(cookingStep.stepImageURL)
                    .error(R.drawable.ic_no_image)
                    .into(holder.binding.stepPreview)
            }
            cookingStepOptionsButton.visibility = View.VISIBLE
        }
    }

    private fun showPopUpMenu(view: View) {
        val context = view.context
        val cookingStep = view.tag as CookingStep
        val position = cookingSteps.indexOfFirst { it.id == cookingStep.id }
        PopupMenu(context, view).apply {

            inflate(R.menu.liist_element_menu)

            menu.findItem(R.id.move_up).isEnabled = position > 0

            menu.findItem(R.id.move_down).isEnabled = position < cookingSteps.size -1

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.move_up -> {
                        cookingInstructionStepsActionListener.onCookingStepUp(cookingStep, -1)
                        true
                    }
                    R.id.move_down -> {
                        cookingInstructionStepsActionListener.onCookingStepDown(cookingStep,1)
                        true
                    }
                    R.id.edit -> {
                        cookingInstructionStepsActionListener.onCookingStepEdit(cookingStep)
                        true
                    }
                    R.id.delete -> {
                        cookingInstructionStepsActionListener.onCookingStepDelete(cookingStep)
                        true
                    }
                    else -> false
                }
            }

        }.show()
    }

    override fun getItemCount(): Int = cookingSteps.size

    class CookingStepViewHolder(
        val binding: CookingStepItemBinding
    ) : RecyclerView.ViewHolder(binding.root)


}