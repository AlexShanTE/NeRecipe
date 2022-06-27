package com.shante.nerecipe.presentation.adapters.settingsScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shante.nerecipe.databinding.ItemSettingsBinding
import com.shante.nerecipe.domain.Kitchen


class SettingsAdapter : RecyclerView.Adapter<SettingsAdapter.SettingHolder>() {

    var selectedKitchenList: MutableList<Kitchen> = mutableListOf()
        get() = Kitchen.selectedKitchenList
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = selectedKitchenList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingsBinding.inflate(inflater, parent, false)
        return SettingHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingHolder, position: Int) {
        val selectedKitchen = selectedKitchenList[position]
            with(holder.binding) {
                settingCheckbox.text = selectedKitchen.title
                settingCheckbox.isChecked = selectedKitchen.isChecked
            }
        holder.binding.settingCheckbox.setOnClickListener {
            selectedKitchen.isChecked = !selectedKitchen.isChecked
        }
//        if (position == Kitchen.selectedKitchenList.lastIndex) holder.binding.settingCheckbox.isClickable = false
    }

    class SettingHolder(
        val binding: ItemSettingsBinding
    ) : RecyclerView.ViewHolder(binding.root)


}

