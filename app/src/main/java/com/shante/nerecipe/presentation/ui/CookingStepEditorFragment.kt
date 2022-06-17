package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shante.nerecipe.databinding.CookingStepEditorBinding
import com.shante.nerecipe.databinding.RecipeEditorFragmentBinding
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.presentation.adapters.constructorScreen.CookingStepSevice

class CookingStepEditorFragment : Fragment() {

    private val args = navArgs<CookingStepEditorFragmentArgs>()

    private val cookingStepService = CookingStepSevice


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CookingStepEditorBinding.inflate(layoutInflater, container, false).also { binding ->

        if (args.value !== null) {
            val step = args.value.cookingStep
            binding.descriptionStepEditText.setText(step?.description)
            if (step?.uri !== null) {
                binding.addPreview.visibility = View.GONE
                binding.stepPreview //todo load image
            } else {
                binding.clearPreview.visibility = View.GONE
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cancelEditStepButton.setOnClickListener {
            binding.descriptionStepEditText.text?.clear()
        }

        binding.addPreview.setOnClickListener {
            // todo load image from gallery
        }

        binding.okButton.setOnClickListener {
            val newDescription = binding.descriptionStepEditText.text
            if (!newDescription.isNullOrBlank()) {
                val step = CookingStep(
                    description = newDescription.toString(),
                    uri = null, //todo если стоит заглушка, то передать Null else img
                    id = args.value.cookingStep?.id ?: -1
                )
                cookingStepService.addCookingStep(step)
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    context,
                    "Description should not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }.root

    companion object {
        const val RESULT_KEY_NEW_STEP = "add new step"
        const val REQUEST_KEY = "request"
    }

}