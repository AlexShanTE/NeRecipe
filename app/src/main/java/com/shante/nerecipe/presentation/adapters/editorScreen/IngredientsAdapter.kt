package com.shante.nerecipe.presentation.adapters.editorScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.IngredientsItemBinding
import com.shante.nerecipe.domain.Ingredient
import com.shante.nerecipe.presentation.adapters.interactionListeners.IngredientInteractionListener


class IngredientsAdapter(
    private val ingredientInteractionListener: IngredientInteractionListener
) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>(), View.OnClickListener {

    var ingredients: List<Ingredient> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ingredient_options_button -> {
                showPopUpMenu(v)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IngredientsItemBinding.inflate(inflater, parent, false)

        binding.ingredientOptionsButton.setOnClickListener(this)

        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        with(holder.binding) {
            ingredientOptionsButton.tag = ingredient
            ingredientTitle.text = ingredient.title
            ingredientValue.text = ingredient.value
            ingredientOptionsButton.visibility = View.VISIBLE
        }
    }

    private fun showPopUpMenu(view: View) {
        val context = view.context
        val ingredient = view.tag as Ingredient
        val position = ingredients.indexOfFirst { it.id == ingredient.id }
        PopupMenu(context, view).apply {

            inflate(R.menu.liist_element_menu)

            menu.findItem(R.id.move_up).isEnabled = position > 0

            menu.findItem(R.id.move_down).isEnabled = position < ingredients.size -1

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.move_up -> {
                        ingredientInteractionListener.onIngredientUp(ingredient, -1)
                        true
                    }
                    R.id.move_down -> {
                        ingredientInteractionListener.onIngredientDown(ingredient,1)
                        true
                    }
                    R.id.edit -> {
                        ingredientInteractionListener.onIngredientEdit(ingredient)
                        true
                    }
                    R.id.delete -> {
                        ingredientInteractionListener.onIngredientDelete(ingredient)
                        true
                    }
                    else -> false
                }
            }

        }.show()
    }

    override fun getItemCount(): Int = ingredients.size

    class IngredientViewHolder(
        val binding: IngredientsItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

}