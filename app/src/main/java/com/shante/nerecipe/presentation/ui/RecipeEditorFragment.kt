package com.shante.nerecipe.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipeEditorFragmentBinding
import com.shante.nerecipe.domain.CookingStep
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.constructorScreen.*
import com.shante.nerecipe.utils.CookingTimeConverter


class RecipeEditorFragment : Fragment() {

    private val args by navArgs<RecipeEditorFragmentArgs>()  // recipe or null

    private val ingredientService: IngredientService = IngredientService
    private val cookingStepsService: CookingStepSevice = CookingStepSevice

    private var selectedRecipePreviewImageUri: Uri? = null

    private val pickImage: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri ->
            selectedRecipePreviewImageUri = imageUri
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

        val recipe = args.recipe

        val ingredientsAdapter = IngredientsAdapter(object : IngredientActionListener {
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
                CookingInstructionStepsAdapter(it, object : CookingInstructionStepsActionListener {
                    override fun onCookingStepUp(cookingStep: CookingStep, moveBy: Int) {
                        cookingStepsService.moveCookingStep(cookingStep, moveBy)
                    }

                    override fun onCookingStepDown(cookingStep: CookingStep, moveBy: Int) {
                        cookingStepsService.moveCookingStep(cookingStep, moveBy)
                    }

                    override fun onCookingStepEdit(cookingStep: CookingStep) {
                        val direction =
                            RecipeEditorFragmentDirections.toCookingStepEditorFragment(cookingStep)
                        findNavController().navigate(direction)
                    }

                    override fun onCookingStepDelete(cookingStep: CookingStep) {
                        cookingStepsService.deleteCookingStep(cookingStep)
                    }

                })
            }

        if (recipe !== null) {
            ingredientService.setIngredientsList(recipe.ingredientsList)
            cookingStepsService.setCookingStepsList(recipe.cookingInstructionList)
            binding.title.setText(recipe.title)
            binding.cookingTimeHours.setText(CookingTimeConverter.convertToHours(recipe.cookingTime))
            binding.cookingTimeMinutes.setText(CookingTimeConverter.convertToMinutes(recipe.cookingTime))
            binding.kitchenCategoryTitle.text = recipe.kitchenCategory
            if (recipe.previewURL !== null) {
                binding.previewAddButton.visibility = View.GONE
                binding.previewClearButton.visibility = View.VISIBLE
                Glide.with(this)
                    .asDrawable()
                    .load(recipe.previewURL)
                    .error(R.drawable.ic_no_image)
                    .into(binding.recipePreview)
            } else {
                binding.recipePreview.setImageResource(R.drawable.ic_no_image)
                binding.previewAddButton.visibility = View.VISIBLE
                binding.previewClearButton.visibility = View.GONE
            }
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

        ingredientService.addListener(ingredientsListener)  //TODO разобраться со слушателем
        cookingStepsService.addListener(cookingStepsListener)  //TODO разобраться со слушателем

        binding.previewClearButton.setOnClickListener {
            binding.previewAddButton.visibility = View.VISIBLE
            binding.previewClearButton.visibility = View.GONE
            binding.recipePreview.setImageResource(R.drawable.ic_no_image)
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
                    value = binding.newIngredientValueEditText.text.toString()
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
            findItem(R.id.preview_clear_button).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ok_button -> {
                val resultBundle = Bundle(1)
                val newRecipe = Recipe(
                    id = if (args.recipe !== null) args.recipe!!.id else -1,
                    title = view?.findViewById<EditText>(R.id.title)?.text.toString(),
                    author = "Me", //todo get account name
                    authorId = 2, //todo get account id
                    kitchenCategory = view?.findViewById<TextView>(R.id.kitchen_category_title)?.text.toString(),
                    cookingTime = CookingTimeConverter.convertToString(
                        view?.findViewById<EditText>(R.id.cooking_time_hours)?.text.toString(),
                        view?.findViewById<EditText>(R.id.cooking_time_minutes)?.text.toString()
                    ),
                    ingredientsList = IngredientService.getIngredients(),
                    cookingInstructionList = CookingStepSevice.getCookingSteps(),
                    /* todo если стоит заглушка, то передать  в previewURL Null,
                         ecли selectedRecipePreviewImageUri не null то загрузить на сервер и передать ссылку
                         selectedRecipePreviewImageUri обнулить
                    */
                    previewURL = null,
                    isIngredientsShowed = false,
                    isCookingStepsShowed = false,
                    isFavorite = false

                )
                if (checkRecipeForEmptyFields(newRecipe)) {
//                    ingredientService.targetIngredient = null //TODO если эта хрень есть то чистится вьюмодель
//                    ingredientService.clearIngredients() //TODO если эта хрень есть то чистится вьюмодель
//                    cookingStepsService.clearCookingSteps() //TODO если эта хрень есть то чистится вьюмодель
                    resultBundle.putParcelable(RESULT_KEY_FOR_ADD_NEW_RECIPE, newRecipe)
                    setFragmentResult(REQUEST_KEY, resultBundle)
                    findNavController().popBackStack()
                }
                true
            }
            R.id.preview_clear_button -> {
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

    companion object {
        const val RESULT_KEY_FOR_ADD_NEW_RECIPE = "add new recipe"
        const val REQUEST_KEY = "requestKey"
        const val MIMETYPE_IMAGES = "image/*"

    }

}