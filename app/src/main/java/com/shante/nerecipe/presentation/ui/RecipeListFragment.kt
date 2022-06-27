package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.RecipeListFragmentBinding
import com.shante.nerecipe.domain.Kitchen
import com.shante.nerecipe.domain.Recipe
import com.shante.nerecipe.presentation.adapters.recipeListScreen.RecipeListAdapter
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

        //        Callback for backPressed
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }


        setFragmentResultListener(requestKey = RecipeEditorFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey !== RecipeEditorFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe =
                bundle.getParcelable<Recipe>(RecipeEditorFragment.RESULT_KEY_FOR_ADD_NEW_RECIPE)
            if (newRecipe != null)
                viewModel.addRecipe(newRecipe)
        }

        val textView = activity?.findViewById(R.id.toolBarEditText) as AutoCompleteTextView
        val adapter = RecipeListAdapter(viewModel)
        SettingsFragment.initSettings()

        binding.recipeRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipeList ->


            adapter.submitList(
                getRecipeListWithFilters(
                    recipeList = recipeList,
                    filteredByKitchen = true,
                    filteredByRequest = true,
                    selectedBottomMenuItemId = selectedItemId
                )
            )

            textView.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
                override fun beforeTextChanged(s: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
                override fun afterTextChanged(arg0: Editable) {
                    adapter.submitList(
                        getRecipeListWithFilters(
                            recipeList = recipeList,
                            filteredByKitchen = true,
                            filteredByRequest = true,
                            selectedBottomMenuItemId = selectedItemId
                        )
                    )
                }
            })
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val recipeList = viewModel.data.value
            when (item.itemId) {
                R.id.all_recipes -> {
                    selectedItemId = R.id.all_recipes
                    adapter.submitList(
                        getRecipeListWithFilters(
                            recipeList = recipeList ?: emptyList(),
                            filteredByKitchen = true,
                            filteredByRequest = true,
                            selectedBottomMenuItemId = selectedItemId
                        )
                    )
                    requireActivity().invalidateOptionsMenu()
                    return@setOnItemSelectedListener true
                }
                R.id.my_recipes -> {
                    selectedItemId = R.id.my_recipes
                    adapter.submitList(
                        getRecipeListWithFilters(
                            recipeList = recipeList ?: emptyList(),
                            filteredByKitchen = true,
                            filteredByRequest = true,
                            selectedBottomMenuItemId = selectedItemId
                        )
                    )
                    requireActivity().invalidateOptionsMenu()
                    return@setOnItemSelectedListener true
                }
                R.id.favorite_recipes -> {
                    selectedItemId = R.id.favorite_recipes
                    adapter.submitList(
                        getRecipeListWithFilters(
                            recipeList = recipeList ?: emptyList(),
                            filteredByKitchen = true,
                            filteredByRequest = true,
                            selectedBottomMenuItemId = selectedItemId
                        )
                    )
                    requireActivity().invalidateOptionsMenu()
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
        val bottomNavigationView =
            activity?.findViewById(R.id.bottomNavigation) as BottomNavigationView
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
                    findItem(R.id.cancel_button).isVisible = true
                }
                View.GONE, View.INVISIBLE -> {
                    findItem(R.id.filter_button).isVisible = true
                    findItem(R.id.cancel_button).isVisible = false
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toolBarEditText = activity?.findViewById(R.id.toolBarEditText) as AutoCompleteTextView
        return when (item.itemId) {
            R.id.search_button -> {
                if (toolBarEditText.visibility == View.GONE) {
                    toolBarEditText.visibility = View.VISIBLE
                    requireActivity().invalidateOptionsMenu()
                }
                true
            }
            R.id.cancel_button -> {
                toolBarEditText.text.clear()
                toolBarEditText.visibility = View.GONE
                requireActivity().invalidateOptionsMenu()
                true
            }
            R.id.add_button -> {
                viewModel.onAddClicked()
                true
            }
            R.id.filter_button -> {
                val direction = RecipeListFragmentDirections.toSettingsFragment()
                findNavController().navigate(direction)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setNoItemImageVisibility(
        recipeList: List<Recipe>?
    ) {
        val noResultsImageView = activity?.findViewById(R.id.no_results) as ImageView
        if (recipeList?.isEmpty() == true) noResultsImageView.visibility = View.VISIBLE
        else noResultsImageView.visibility = View.GONE
    }

    fun getRecipeListWithFilters(
        recipeList: List<Recipe>,
        filteredByKitchen: Boolean,
        filteredByRequest: Boolean,
        selectedBottomMenuItemId: Int
    ): List<Recipe> {
        val myId = 2 //todo get account id
        val selectedKitchenCategory =
            Kitchen.selectedKitchenList.filter { it.isChecked }.map { it.title }
        val textView = activity?.findViewById(R.id.toolBarEditText) as AutoCompleteTextView
        var newRecipeList: List<Recipe> = recipeList
        when (selectedBottomMenuItemId) {
            R.id.all_recipes -> {
                newRecipeList = recipeList
            }
            R.id.my_recipes -> {
                newRecipeList = recipeList.filter { it.authorId == myId }
            }
            R.id.favorite_recipes -> {
                newRecipeList = recipeList.filter { it.isFavorite }
            }
        }
        if (filteredByKitchen) newRecipeList =
            newRecipeList.filter { it.kitchenCategory in selectedKitchenCategory }
        if (filteredByRequest) newRecipeList = newRecipeList.filter {
            it.title.contains(textView.text, ignoreCase = true) || it.author.contains(
                textView.text,
                ignoreCase = true
            )
        }
        setNoItemImageVisibility(newRecipeList)
        return newRecipeList
    }

    companion object {
        var selectedItemId: Int = R.id.all_recipes
    }

}
