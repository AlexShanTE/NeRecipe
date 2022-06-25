package com.shante.nerecipe.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.CookingStepEditorBinding
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.presentation.adapters.editorScreen.CookingStepService

class CookingStepEditorFragment : Fragment() {

    private val args = navArgs<CookingStepEditorFragmentArgs>()

    private val cookingStepService = CookingStepService

    private var selectedCookingStepImageUri: Uri? = null

    private val pickImage: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri ->
            selectedCookingStepImageUri = imageUri
            view?.findViewById<ImageView>(R.id.step_preview)?.setImageURI(imageUri)
            view?.findViewById<ImageButton>(R.id.preview_add_button)?.visibility = View.GONE
            view?.findViewById<ImageButton>(R.id.clear_preview_button)?.visibility = View.VISIBLE
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CookingStepEditorBinding.inflate(layoutInflater, container, false).also { binding ->


        if (args.value !== null) {
            val step = args.value.cookingStep
            binding.descriptionStepEditText.setText(step?.description)
            if (step?.stepImageURL == null) {
                binding.previewAddButton.visibility = View.VISIBLE
                binding.clearPreviewButton.visibility = View.GONE
            } else {
                binding.previewAddButton.visibility = View.GONE
                binding.clearPreviewButton.visibility = View.VISIBLE
                Glide.with(binding.stepPreview)
                    .asDrawable()
                    .load(step.stepImageURL)
                    .error(R.drawable.ic_no_image)
                    .into(binding.stepPreview)
            }
        } else {
            binding.clearPreviewButton.visibility = View.GONE
        }

        binding.clearPreviewButton.setOnClickListener {
            binding.stepPreview.setImageResource(R.drawable.ic_no_image)
            binding.previewAddButton.visibility = View.VISIBLE
            binding.clearPreviewButton.visibility = View.GONE
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cancelEditStepButton.setOnClickListener {
            binding.descriptionStepEditText.text?.clear()
        }

        binding.previewAddButton.setOnClickListener {
            pickImage.launch(MIMETYPE_IMAGES)
        }

        binding.okeyButton.setOnClickListener {
            val newDescription = binding.descriptionStepEditText.text
            if (!newDescription.isNullOrBlank()) {
                val step = CookingStep(
                    description = newDescription.toString(),
                    /* todo если стоит заглушка, то передать  в stepImageURL Null,
                         ecли selectedCookingStepImageUri не null то загрузить на сервер и передать ссылку
                         selectedImg обнулить
                    */
                    stepImageURL = args.value.cookingStep?.stepImageURL,
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
        const val MIMETYPE_IMAGES = "image/*"
    }


}