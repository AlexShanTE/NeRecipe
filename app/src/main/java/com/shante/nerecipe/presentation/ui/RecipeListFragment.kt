package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipeListFragmentBinding
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.RecipeListAdapter
import com.shante.nerecipe.presentation.viewModel.RecipeListViewModel


class RecipeListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


        viewModel.navigateToRecipeDetailsScreen.observe(this) { recipe ->
            val direction = RecipeListFragmentDirections.toRecipeDetailsFragment(recipe.id)
            findNavController().navigate(direction)
        }

        viewModel.navigateToRecipeEditorScreen.observe(this) { recipe ->
            val direction = RecipeListFragmentDirections.toRecipeEditorFragment(recipe)
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeListFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        setFragmentResultListener(requestKey = RecipeEditorFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey !== RecipeEditorFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(RecipeEditorFragment.RESULT_KEY_FOR_ADD_NEW_RECIPE)
            if (newRecipe != null)
                viewModel.addRecipe(newRecipe)
        }

        val adapter = RecipeListAdapter(viewModel)

        binding.recipeRecyclerView.adapter = adapter

        viewModel.recipeList.observe(viewLifecycleOwner) { recipeList ->
            val myId = 2 //todo get real user id
            var recipes = recipeList

            //initial state
            if (recipeList.isEmpty()) binding.noResults.visibility = View.VISIBLE
            else binding.noResults.visibility = View.GONE

            when (binding.bottomNavigation.selectedItemId) {
                R.id.all_recipes -> {
                    adapter.submitList(recipes)
                    if (recipes.isEmpty()) binding.noResults.visibility = View.VISIBLE
                }
                R.id.my_recipes -> {
                    recipes = recipeList.filter { it.authorId == myId }
                    adapter.submitList(recipes)
                    if (recipes.isEmpty()) binding.noResults.visibility = View.VISIBLE
                }
                R.id.favorite_recipes -> {
                    recipes= recipeList.filter { it.isFavorite  }
                    adapter.submitList(recipes)
                    if (recipes.isEmpty()) binding.noResults.visibility = View.VISIBLE
                }
                else -> {
                    adapter.submitList(recipeList)
                    binding.noResults.visibility = View.GONE
                }
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            var recipeList = viewModel.recipeList.value
            when (item.itemId) {
                R.id.all_recipes -> {
                    requireActivity().invalidateOptionsMenu()
                    if (recipeList?.isEmpty() == true) binding.noResults.visibility = View.VISIBLE
                    else binding.noResults.visibility = View.GONE
                    adapter.submitList(viewModel.recipeList.value)
                    return@setOnItemSelectedListener true
                }
                R.id.my_recipes -> {
                    requireActivity().invalidateOptionsMenu()
                    val myId = 2 //TODO get my id to show corrected list
                    recipeList = viewModel.recipeList.value?.filter { it.authorId == myId }
                    adapter.submitList(recipeList)
                    if (recipeList?.isEmpty() == true) binding.noResults.visibility = View.VISIBLE
                    else binding.noResults.visibility = View.GONE
                    return@setOnItemSelectedListener true
                }
                R.id.favorite_recipes -> {
                    requireActivity().invalidateOptionsMenu()
                    recipeList = viewModel.recipeList.value?.filter { it.isFavorite }
                    adapter.submitList(recipeList)
                    if (recipeList?.isEmpty() == true) binding.noResults.visibility = View.VISIBLE
                    else binding.noResults.visibility = View.GONE
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val toolBarEditText = activity?.findViewById(R.id.toolBarEditText) as EditText
        val bottomNavigationView = activity?.findViewById(R.id.bottomNavigation) as BottomNavigationView
        with(menu) {
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
            findItem(R.id.ok_button).isVisible = false
            when (bottomNavigationView.selectedItemId) {
                R.id.my_recipes -> {
                    findItem(R.id.add_button).isVisible = true
                }
                R.id.all_recipes, R.id.favorite_recipes -> {
                    findItem(R.id.add_button).isVisible = false
                }
            }
            when (toolBarEditText.visibility) {
                View.VISIBLE -> {
                    findItem(R.id.filter_button).isVisible = false
                    findItem(R.id.preview_clear_button).isVisible = true
                }
                View.GONE, View.INVISIBLE -> {
                    findItem(R.id.filter_button).isVisible = true
                    findItem(R.id.preview_clear_button).isVisible = false
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toolBarEditText = activity?.findViewById(R.id.toolBarEditText) as EditText
        return when (item.itemId) {
            R.id.search_button -> {
                if (toolBarEditText.visibility == View.GONE) {
                    toolBarEditText.visibility = View.VISIBLE
                    requireActivity().invalidateOptionsMenu()
                } else {
                    if (toolBarEditText.text.isNullOrBlank()) {
                        Toast.makeText(
                            this.context,
                            "Searching field is empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.onSearchClicked(toolBarEditText.text.toString())
                    }
                }
                true
            }
            R.id.preview_clear_button -> {
                toolBarEditText.text.clear()
                toolBarEditText.visibility = View.GONE
                viewModel.onCancelClicked()
                requireActivity().invalidateOptionsMenu()
                true
            }
            R.id.add_button -> {
                viewModel.onAddClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}