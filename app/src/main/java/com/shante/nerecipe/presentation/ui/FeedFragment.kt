package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shante.nerecipe.databinding.FeedFragmentBinding
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.viewModel.RecipeViewModel

class FeedFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        viewModel.recipeList.observe(viewLifecycleOwner) {
            Log.d("TAG", it.toString())
        }
        viewModel.addRecipe(Recipe("123", "me", "RU"))
        viewModel.deleteRecipe(Recipe("Recipe №0","Me","Russian food", 0))
        viewModel.editRecipe(Recipe("Recipe №1","Me","BLABLABLA", 1))
    }.root


}