package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipeDetailsFragmentBinding
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.detailsScreen.RecipeDetailsCookingInstructionAdapter
import com.shante.nerecipe.presentation.adapters.detailsScreen.RecipeDetailsIngredientsAdapter
import com.shante.nerecipe.presentation.viewModel.RecipeDetailsViewModel

class RecipeDetailsFragment : Fragment() {

    private val viewModel: RecipeDetailsViewModel by viewModels()
    private val args by navArgs<RecipeDetailsFragmentArgs>()  // recipe id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setFragmentResultListener(requestKey = RecipeEditorFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey !== RecipeEditorFragment.REQUEST_KEY) return@setFragmentResultListener
            val recipe =
                bundle.getParcelable<Recipe>(RecipeEditorFragment.RESULT_KEY_FOR_ADD_NEW_RECIPE)
            if (recipe != null)
                viewModel.editRecipe(recipe)
        }

        viewModel.navigateToRecipeEditorScreen.observe(this) { recipe ->
            val direction = RecipeDetailsFragmentDirections.toRecipeConstructorFragment(recipe)
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeDetailsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val toolBarEditText = activity?.findViewById(R.id.toolBarEditText) as EditText

//        Callback for backPressed
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (toolBarEditText.text.isNotEmpty()) toolBarEditText.visibility = View.VISIBLE
            findNavController().popBackStack()
        }

        viewModel.recipeList.observe(viewLifecycleOwner) { recipeList ->
            val recipe = recipeList.find { it.id == args.recipeId } ?: return@observe
            with(binding) {
                recipeItemPreview.author.text = recipe.author
                recipeItemPreview.title.text = recipe.title
                if (recipe.previewURL !== null) {
                    Glide.with(this@RecipeDetailsFragment)
                        .asDrawable()
                        .load(recipe.previewURL)
                        .error(R.drawable.ic_no_image)
                        .into(recipeItemPreview.recipePreview)
                } else {
                    recipeItemPreview.recipePreview.setImageResource(R.drawable.ic_no_image)
                }


                if (recipe.kitchenCategory == "Undefined category") { //todo придумать что то с категорией
                    recipeItemPreview.kitchenCategory.visibility = View.GONE
                } else recipeItemPreview.kitchenCategory.text = recipe.kitchenCategory
                if (recipe.cookingTime == null) {
                    recipeItemPreview.cookingTime.visibility = View.GONE
                } else recipeItemPreview.cookingTime.text = recipe.cookingTime
                recipeItemPreview.favoriteButton.setImageResource(
                    when (recipe.isFavorite) {
                        true -> R.drawable.ic_star_24
                        false -> R.drawable.ic_star_border_24
                    }
                )

                // region Ingredients List
                val ingredientsAdapter = RecipeDetailsIngredientsAdapter()
                binding.ingredientsList.adapter = ingredientsAdapter
                ingredientsAdapter.submitList(recipe.ingredientsList)
                // endregion Ingredients List

                // region Cooking Steps List
                val cookingStepsAdapter = RecipeDetailsCookingInstructionAdapter()
                binding.cookingInstructionList.adapter = cookingStepsAdapter
                cookingStepsAdapter.submitList(recipe.cookingInstructionList)
                // endregion Cooking Steps List

                when (recipe.isIngredientsShowed) {
                    true -> {
                        ingredientsList.visibility = View.VISIBLE
                        showIngredientsButton.setIconResource(R.drawable.ic_arrow_drop_up)
                    }
                    false -> {
                        ingredientsList.visibility = View.GONE
                        showIngredientsButton.setIconResource(R.drawable.ic_arrow_drop_down)
                    }
                }

                when (recipe.isCookingStepsShowed) {
                    true -> {
                        cookingInstructionList.visibility = View.VISIBLE
                        showCookingInstructionButton.setIconResource(R.drawable.ic_arrow_drop_up)
                    }
                    false -> {
                        cookingInstructionList.visibility = View.GONE
                        showCookingInstructionButton.setIconResource(R.drawable.ic_arrow_drop_down)
                    }
                }


                showCookingInstructionButton.setOnClickListener {
                    viewModel.onCookStepsShowClicked(recipe)
                }

                showIngredientsButton.setOnClickListener {
                    viewModel.onIngredientsShowClicked(recipe)
                }

                recipeItemPreview.favoriteButton.setOnClickListener {
                    viewModel.onFavoriteClicked(recipe)
                }
            }
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
            findItem(R.id.filter_button).isVisible = false
            findItem(R.id.preview_clear_button).isVisible = false
            findItem(R.id.add_button).isVisible = false
            findItem(R.id.ok_button).isVisible = false
            val myId = 2 //TODO get my id to show corrected list
            val recipe = viewModel.recipeList.value?.find { it.id == args.recipeId }
                when (recipe?.authorId) {
                    myId -> {
                        findItem(R.id.edit_button).isVisible = true
                        findItem(R.id.delete_button).isVisible = true
                    }
                    else -> {
                        findItem(R.id.edit_button).isVisible = false
                        findItem(R.id.delete_button).isVisible = false
                    }
                }
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_button -> {
                val recipe = viewModel.recipeList.value?.find { it.id == args.recipeId } ?: return true
                viewModel.onDeleteClicked(recipe)
                findNavController().popBackStack()
                true
            }
            R.id.edit_button -> {
                val recipe = viewModel.recipeList.value?.find { it.id == args.recipeId } ?: return true
                viewModel.onEditClicked(recipe)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}


