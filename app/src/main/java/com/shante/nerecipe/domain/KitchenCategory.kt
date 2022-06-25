package com.shante.nerecipe.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kitchen(
    var title: String,
    var isChecked: Boolean = true
) : Parcelable {

    companion object {
        var selectedKitchenList = mutableListOf<Kitchen>(
            Kitchen("European", true),
            Kitchen("Asian", true),
            Kitchen("Pan-Asian", true),
            Kitchen("Eastern", true),
            Kitchen("American", true),
            Kitchen("Mediterranean", true),
            Kitchen("Undefined category", true)
        )
    }
}



