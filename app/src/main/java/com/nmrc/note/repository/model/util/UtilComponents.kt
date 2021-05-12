package com.nmrc.note.repository.model.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nmrc.note.R

fun createToast(
    text: String,
    duration: Int = Toast.LENGTH_SHORT,
    context: () -> Context
) {

    Toast.makeText(context(), text, duration).show()
}

fun Fragment.alertDialog(@StringRes title: Int, @StringRes msg: Int, @DrawableRes ic: Int) {
    MaterialAlertDialogBuilder(this.requireContext()).apply {
        setTitle(title)
        setMessage(msg)
        setIcon(ic)
        show()
    }
}