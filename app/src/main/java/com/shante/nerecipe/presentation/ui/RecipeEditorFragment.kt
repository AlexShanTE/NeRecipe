package com.shante.nerecipe.presentation.ui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipeEditorFragmentBinding
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.editorScreen.*
import com.shante.nerecipe.presentation.adapters.interactionListeners.CookingStepsInteractionListener
import com.shante.nerecipe.presentation.adapters.interactionListeners.IngredientInteractionListener
import com.shante.nerecipe.presentation.viewModel.RecipeListViewModel
import com.shante.nerecipe.utils.CookingTimeConverter


class RecipeEditorFragment : Fragment() {

    private val args by navArgs<RecipeEditorFragmentArgs>()  // recipe or null
    private val viewModel: RecipeListViewModel by viewModels()

    private val ingredientService: IngredientService = IngredientService
    private val cookingStepsService: CookingStepService = CookingStepService

    private var selectedRecipePreviewImageUri: Uri? = null

    private val pickImage: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri ->
            selectedRecipePreviewImageUri = imageUri
            imageRecipePreviewIsEmpty = false
            view?.findViewById<ImageView>(R.id.recipe_preview)?.setImageURI(imageUri)
            view?.findViewById<ImageButton>(R.id.preview_add_button)?.visibility = View.GONE
            view?.findViewById<ImageButton>(R.id.preview_clear_button)?.visibility = View.VISIBLE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeEditorFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        //        Callback for backPressed
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            clearServicesData()
            imageRecipePreviewIsEmpty = false
            selectedRecipePreviewImageUri = null
            findNavController().popBackStack()
        }

        val recipe = args.recipe?.id?.let { viewModel.getRecipeById(it) }

        binding.previewAddButton.visibility = View.VISIBLE
        binding.previewClearButton.visibility = View.GONE

        if (recipe !== null) {
            val editedCookingStepList = cookingStepsService.getCookingSteps()
            val editedIngredientsList = ingredientService.getIngredients()
            when (editedCookingStepList) {
                emptyList<CookingStep>() -> cookingStepsService.setCookingStepsList(recipe.cookingInstructionList as MutableList<CookingStep>)
                else -> cookingStepsService.setCookingStepsList(editedCookingStepList as MutableList<CookingStep>)
            }
            when (editedIngredientsList) {
                emptyList<Ingredient>() -> ingredientService.setIngredientsList(recipe.ingredientsList as MutableList<Ingredient>)
                else -> ingredientService.setIngredientsList(editedIngredientsList as MutableList<Ingredient>)
            }

            if (savedInstanceState == null) {
                binding.title.setText(recipe.title)
                binding.cookingTimeHours.setText(CookingTimeConverter.convertToHours(recipe.cookingTime))
                binding.cookingTimeMinutes.setText(CookingTimeConverter.convertToMinutes(recipe.cookingTime))
                binding.kitchenCategoryTitle.text = recipe.kitchenCategory
                if (recipe.previewUri !== null) {
                    imageRecipePreviewIsEmpty = false
                    binding.previewAddButton.visibility = View.GONE
                    binding.previewClearButton.visibility = View.VISIBLE
                    Glide.with(this)
                        .asDrawable()
                        .load(recipe.previewUri)
                        .error(R.drawable.ic_no_image)
                        .into(binding.recipePreview)
                }
            }
        } else binding.recipePreview.setImageResource(R.drawable.ic_no_image)

        if (selectedRecipePreviewImageUri !== null) {
            Glide.with(this)
                .asDrawable()
                .load(selectedRecipePreviewImageUri)
                .error(R.drawable.ic_no_image)
                .into(binding.recipePreview)
            binding.previewAddButton.visibility = View.GONE
            binding.previewClearButton.visibility = View.VISIBLE
        }

        if (selectedKitchen !== null) binding.kitchenCategoryTitle.text = selectedKitchen

        val ingredientsAdapter = IngredientsAdapter(object : IngredientInteractionListener {
            override fun onIngredientUp(ingredient: Ingredient, moveBy: Int) {
                ingredientService.moveIngredient(ingredient, moveBy)
            }

            override fun onIngredientDown(ingredient: Ingredient, moveBy: Int) {
                ingredientService.moveIngredient(ingredient, moveBy)
            }

            override fun onIngredientEdit(ingredient: Ingredient) {
                ingredientService.targetIngredient = ingredient
                binding.ingredientEditGroup.visibility = View.VISIBLE
                binding.addIngredientButton.text = getString(R.string.save_ingredient)
                binding.newIngredientNameEditText.setText(ingredient.title)
                binding.newIngredientValueEditText.setText(ingredient.value)
            }

            override fun onIngredientDelete(ingredient: Ingredient) {
                ingredientService.deleteIngredient(ingredient)
            }
        }
        )

        val cookingInstructionStepsAdapter =
            this.context?.let {
                CookingInstructionStepsAdapter(it, object : CookingStepsInteractionListener {
                    override fun onCookingStepUp(cookingStep: CookingStep, moveBy: Int) {
                        cookingStepsService.moveCookingStep(cookingStep, moveBy)
                    }

                    override fun onCookingStepDown(cookingStep: CookingStep, moveBy: Int) {
                        cookingStepsService.moveCookingStep(cookingStep, moveBy)
                    }

                    override fun onCookingStepEdit(cookingStep: CookingStep) {
                        cookingStepsService.targetCookingStep = cookingStep
                        val direction =
                            RecipeEditorFragmentDirections.toCookingStepEditorFragment(cookingStep)
                        findNavController().navigate(direction)
                    }

                    override fun onCookingStepDelete(cookingStep: CookingStep) {
                        cookingStepsService.deleteCookingStep(cookingStep)
                    }
                })
            }

        binding.ingredientEditGroup.visibility = View.GONE

        binding.ingredientsList.adapter = ingredientsAdapter
        binding.cookingInstructionList.adapter = cookingInstructionStepsAdapter

        val ingredientsListener: IngredientListener = {
            ingredientsAdapter.ingredients = ingredientService.getIngredients()
        }

        val cookingStepsListener: CookingStepListener = {
            cookingInstructionStepsAdapter?.cookingSteps = cookingStepsService.getCookingSteps()
        }

        ingredientService.addListener(ingredientsListener)
        cookingStepsService.addListener(cookingStepsListener)

        binding.previewClearButton.setOnClickListener {
            binding.previewAddButton.visibility = View.VISIBLE
            binding.previewClearButton.visibility = View.GONE
            binding.recipePreview.setImageResource(R.drawable.ic_no_image)
            selectedRecipePreviewImageUri = null
            imageRecipePreviewIsEmpty = true
        }

        binding.previewAddButton.setOnClickListener {
            pickImage.launch(MIMETYPE_IMAGES)
        }

        binding.cancelEditIngredientButton.setOnClickListener {
            binding.newIngredientNameEditText.text.clear()
            binding.newIngredientValueEditText.text.clear()
            binding.addIngredientButton.text = getString(R.string.add_ingredient)
            binding.ingredientEditGroup.visibility = View.GONE
        }

        binding.kitchenCategoryButton.setOnClickListener {
            PopupMenu(binding.root.context, binding.kitchenCategoryButton).apply {
                inflate(R.menu.kitchen_category_menu)
                setOnMenuItemClickListener {
                    binding.kitchenCategoryTitle.text = it.title
                    selectedKitchen = it.title.toString()
                    true
                }
            }.show()
        }

        binding.addIngredientButton.setOnClickListener {
            val ingredientEditGroupVisibility = binding.ingredientEditGroup.visibility
            val targetIngredient =
                ingredientService.targetIngredient // notnull means ingredient edit mode

            // too much if coz of hard logic
            if (ingredientEditGroupVisibility == View.GONE) {
                binding.ingredientEditGroup.visibility = View.VISIBLE
                binding.addIngredientButton.text = getString(R.string.save_ingredient)
            }
            if (ingredientEditGroupVisibility == View.VISIBLE && binding.newIngredientNameEditText.text.isNullOrBlank()) {
                toastMaker("Ingredient name field is empty")
                return@setOnClickListener
            }
            if (ingredientEditGroupVisibility == View.VISIBLE && targetIngredient !== null) {
                ingredientService.targetIngredient = targetIngredient.copy(
                    title = binding.newIngredientNameEditText.text.toString(),
                    value = binding.newIngredientValueEditText.text.toString()
                )
                ingredientService.targetIngredient?.let { ingredient ->
                    ingredientService.editIngredient(ingredient)
                }
                ingredientService.targetIngredient = null
                binding.newIngredientNameEditText.text.clear()
                binding.newIngredientValueEditText.text.clear()
                binding.ingredientEditGroup.visibility = View.GONE
                binding.addIngredientButton.text = getString(R.string.add_ingredient)
                return@setOnClickListener
            }
            if (ingredientEditGroupVisibility == View.VISIBLE && targetIngredient == null) {
                ingredientService.targetIngredient = Ingredient(
                    title = binding.newIngredientNameEditText.text.toString(),
                    value = binding.newIngredientValueEditText.text.toString(),
                    id = Ingredient.UNDEFINED_ID
                )
                ingredientService.targetIngredient?.let { ingredient ->
                    ingredientService.addIngredient(ingredient)
                }
                ingredientService.targetIngredient = null
                binding.newIngredientNameEditText.text.clear()
                binding.newIngredientValueEditText.text.clear()
                binding.ingredientEditGroup.visibility = View.GONE
                binding.addIngredientButton.text = getString(R.string.add_ingredient)
                return@setOnClickListener
            }

        }

        binding.addCookingStepButton.setOnClickListener {
            val direction = RecipeEditorFragmentDirections.toCookingStepEditorFragment(null)
            findNavController().navigate(direction)
        }

        Log.d("TAG", "step list editor fragment ${CookingStepService.getCookingSteps()}")


    }.root


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val toolBarEditText = activity?.findViewById(R.id.toolBarEditText) as EditText
        toolBarEditText.visibility = View.GONE
        super.onPrepareOptionsMenu(menu)
        with(menu) {
            findItem(R.id.search_button).isVisible = false
            findItem(R.id.add_button).isVisible = false
            findItem(R.id.filter_button).isVisible = false
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
            findItem(R.id.ok_button).isVisible = true
            findItem(R.id.cancel_button).isVisible = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ok_button -> {
                val resultBundle = Bundle(1)
                val newRecipe = Recipe(
                    id = args.recipe?.id ?: Recipe.UNDEFINED_ID,
                    title = view?.findViewById<EditText>(R.id.title)?.text.toString(),
                    author = "Me", //todo get account name
                    authorId = 2, //todo get account id
                    kitchenCategory = view?.findViewById<TextView>(R.id.kitchen_category_title)?.text.toString(),
                    cookingTime = CookingTimeConverter.convertToString(
                        view?.findViewById<EditText>(R.id.cooking_time_hours)?.text.toString(),
                        view?.findViewById<EditText>(R.id.cooking_time_minutes)?.text.toString()
                    ),
                    ingredientsList = IngredientService.getIngredients(),
                    cookingInstructionList = CookingStepService.getCookingSteps(),
                    previewUri = when {
                        imageRecipePreviewIsEmpty -> null
                        selectedRecipePreviewImageUri !== null -> selectedRecipePreviewImageUri
                        args.recipe?.previewUri !== null -> args.recipe?.previewUri
                        else -> null
                    },
                    isFavorite = false
                )
                if (checkRecipeForEmptyFields(newRecipe)) {
                    clearServicesData()
                    resultBundle.putParcelable(RESULT_KEY_FOR_ADD_NEW_RECIPE, newRecipe)
                    setFragmentResult(REQUEST_KEY, resultBundle)
                    findNavController().popBackStack()
                }
                true
            }
            R.id.cancel_button -> {
                clearServicesData()
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkRecipeForEmptyFields(recipe: Recipe): Boolean {
        return when {
            recipe.title.isBlank() -> {
                toastMaker("title shouldn't be empty")
                false
            }
            recipe.ingredientsList.isEmpty() -> {
                toastMaker("Please add at least one ingredient")
                false
            }
            recipe.cookingInstructionList.isEmpty() -> {
                toastMaker("Please add at least one cooking instruction step")
                false
            }
            else -> true
        }
    }

    private fun toastMaker(value: String) {
        Toast.makeText(
            context,
            value,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun clearServicesData() {
        selectedKitchen = null
        ingredientService.targetIngredient = null
        cookingStepsService.targetCookingStep = null
        ingredientService.setIngredientsList(mutableListOf())
        cookingStepsService.setCookingStepsList(mutableListOf())
    }

    companion object {
        const val RESULT_KEY_FOR_ADD_NEW_RECIPE = "add recipe"
        const val REQUEST_KEY = "requestKey"
        const val MIMETYPE_IMAGES = "image/*"
        const val IMAGE_RECIPE_PREVIEW_IS_EMPTY_TAG = "empty image preview"
        var imageRecipePreviewIsEmpty: Boolean = false
        var selectedKitchen: String? = null

    }

}