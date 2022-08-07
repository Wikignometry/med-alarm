package com.medalarm.medalarm.util

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.medalarm.medalarm.BuildConfig

// Extension functions are wonderful! https://stackoverflow.com/a/68423182
tailrec fun Context.getActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun logDebug(msg: String) {
    if (BuildConfig.DEBUG) {
        Log.d("medalarm", msg)
    }
}