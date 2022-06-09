package com.shante.nerecipe.presentation.ui

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.FeedFragmentBinding
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipeListAdapter(viewModel)
        binding.recipeRecyclerView.adapter = adapter

        viewModel.recipeList.observe(viewLifecycleOwner) { recipeList ->
            adapter.submitList(recipeList)
        }

    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val toolBarEditText = activity?.findViewById(R.id.toolBarEditText) as EditText
        with(menu) {
            findItem(R.id.add_button).isVisible = false
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
            findItem(R.id.search_button).isVisible = true
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
            R.id.cancel_button -> {
                toolBarEditText.text.clear()
                toolBarEditText.visibility = View.GONE
                viewModel.onCancelClicked()
                requireActivity().invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}