package com.shante.nerecipe.utils

object CookingTimeConverter {

    fun convertToString(hours: String?, minutes: String?): String? {
        if (hours != null) {
            if (minutes != null) {
                val _hours = hours.replace(" ","")
                val _minutes = minutes.replace(" ","")
                return if (_hours.isNotBlank() && _minutes.isBlank()) {
                    "${_hours}h"
                } else if (_hours.isBlank() && _minutes.isNotBlank()) {
                    "${_minutes}min"
                } else if (_hours.isNotBlank() && _minutes.isNotBlank()) {
                    "${_hours}h\n${_minutes}min"
                } else null
            }
        }
        return null
    }

    fun convertToHours(value: String?): String? {
        val hours = value?.substringBefore("h")
        return if (hours !== value) hours else null
    }

    fun convertToMinutes(value: String?): String? {
        val minutes = value?.substringBefore("min")?.substringAfter("h")?.replace("\n","")
        return if (minutes !== value) minutes else null
    }

}