package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipeDetailsFragmentBinding
import com.shante.nerecipe.presentation.adapters.RecipeCookingStepsAdapter
import com.shante.nerecipe.presentation.adapters.RecipeIngredientsAdapter
import com.shante.nerecipe.presentation.ui.contract.HasCustomTitle
import com.shante.nerecipe.presentation.viewModel.RecipeDetailsViewModel

class RecipeDetailsFragment : Fragment(), HasCustomTitle {

    private val viewModel: RecipeDetailsViewModel by viewModels()
    private val args by navArgs<RecipeDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeDetailsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        //TODO обработать OnBackPress , чтоб сворачивало инфо об ингредиентах и инструкцию по приготовлению

        viewModel.recipeList.observe(viewLifecycleOwner) { recipeList ->
            val recipe = recipeList.find { it.id == args.recipeId } ?: return@observe
            with(binding) {
//                recipeItemPreview.recipePreview = recipe.
                recipeItemPreview.author.text = recipe.author
                recipeItemPreview.title.text = recipe.title
                recipeItemPreview.category.text = recipe.category
                recipeItemPreview.cookingTime.text = recipe.cookingTime
                recipeItemPreview.favoriteButton.setImageResource(
                    when (recipe.isFavorite) {
                        true -> R.drawable.ic_star_24
                        false -> R.drawable.ic_star_border_24
                    }
                )

                // region Ingredients List
                val ingredientsAdapter = RecipeIngredientsAdapter()
                binding.ingredientsList.adapter = ingredientsAdapter
                ingredientsAdapter.submitList(recipe.ingredientsList)
                // endregion Ingredients List

                // region Cooking Steps List
                val cookingStepsAdapter = RecipeCookingStepsAdapter()
                binding.cookingInstructionList.adapter = cookingStepsAdapter
                cookingStepsAdapter.submitList(recipe.cookingStepsList)
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

    override fun getTitleRes() = R.string.recipe_details

}