package com.github.vasniktel.snooper.util

import android.view.View

fun changeVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
