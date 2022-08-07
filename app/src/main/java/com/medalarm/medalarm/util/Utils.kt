package com.medalarm.medalarm.util

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

// Extension functions are wonderful! https://stackoverflow.com/a/68423182
tailrec fun Context.getActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}