package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.FeedFragmentBinding
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.RecipeListAdapter
import com.shante.nerecipe.presentation.ui.contract.HasCustomTitle
import com.shante.nerecipe.presentation.viewModel.RecipeViewModel

class RecipeListFragment : Fragment(), HasCustomTitle {

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeDetailsScreen.observe(this) { recipe ->
            val direction = RecipeListFragmentDirections.toRecipeDetailsFragment(recipe.id)
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipeListAdapter(viewModel)

        binding.recipeRecyclerView.adapter = adapter

        viewModel.recipeList.observe(viewLifecycleOwner) { recipe ->
            adapter.submitList(recipe)
        }


    }.root

    override fun getTitleRes() = R.string.recipe_list

}