package com.shante.nerecipe.utils

import android.app.Application
import android.content.Context


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context? = null
        val appContext: Context?
            get() = context
    }
}