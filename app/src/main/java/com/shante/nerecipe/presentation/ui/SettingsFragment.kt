package com.shante.nerecipe.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shante.nerecipe.R
import com.shante.nerecipe.databinding.SettingsFragmentBinding
import com.shante.nerecipe.domain.Kitchen
import com.shante.nerecipe.presentation.adapters.settingsScreen.SettingsAdapter
import com.shante.nerecipe.utils.MyApplication
import java.lang.reflect.Type


class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SettingsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val selectedKitchenList = initSettings()

        val adapter = SettingsAdapter()

        binding.settingsList.adapter = adapter


        adapter.selectedKitchenList = Kitchen.selectedKitchenList

        //Callback for backPressed
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveSettings(selectedKitchenList)
            findNavController().popBackStack()
        }


    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        with(menu) {
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
            findItem(R.id.search_button).isVisible = false
            findItem(R.id.filter_button).isVisible = false
            findItem(R.id.add_button).isVisible = false
            findItem(R.id.preview_clear_button).isVisible = false
            findItem(R.id.ok_button).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ok_button -> {
                saveSettings(Kitchen.selectedKitchenList)
                findNavController().popBackStack()
                true
            }
            else -> false
        }
    }

    private fun saveSettings(kitchenList: List<Kitchen>) {
        val prefs = context?.getSharedPreferences(SAVED_SETTINGS, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(kitchenList)
        prefs?.edit {
            putString(SAVED_SETTINGS, json)
        }
        Kitchen.selectedKitchenList = kitchenList as MutableList<Kitchen>
    }




    companion object {
        const val SAVED_SETTINGS = "savedContent"
        private const val DEFAULT_VALUE_FOR_SAVED_SETTINGS = ""

        fun initSettings() : List<Kitchen> {
            val gson = Gson()
            val prefs = MyApplication.appContext?.getSharedPreferences(SAVED_SETTINGS, Context.MODE_PRIVATE)
            val type: Type = object : TypeToken<List<Kitchen>?>() {}.type
            val list = prefs?.getString(SAVED_SETTINGS, DEFAULT_VALUE_FOR_SAVED_SETTINGS)
            val selectedKitchenList = gson.fromJson<List<Kitchen>>(list, type) ?: Kitchen.selectedKitchenList.toList()
            Kitchen.selectedKitchenList = selectedKitchenList as MutableList<Kitchen>
            return selectedKitchenList
        }
    }

}