package com.nmrc.note.repository.model.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

const val DATE_ONLY: Int = 0
const val DATE_ONLY_MONTH_DAY = 1
const val DATE_ONLY_HOURS = 2
const val DATE_COMPLETED = 3
const val ONE_SECONDS = 1000
const val TWO_SECONDS = 2000

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    DATE_ONLY,
    DATE_ONLY_MONTH_DAY,
    DATE_ONLY_HOURS,
    DATE_COMPLETED
)
annotation class DateConstraint

fun newToast(@StringRes text: Int, context: Context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, duration).show()
}

fun newSnackB(@StringRes text: Int, view: View, duration: Int = TWO_SECONDS) {
    Snackbar.make(view, text,duration.absoluteValue).show()
}

fun navigate(view: View, @IdRes to: Int) = Navigation.findNavController(view).navigate(to)

fun Fragment.alertDialog(@StringRes title: Int, @StringRes msg: Int, @DrawableRes ic: Int) {
    MaterialAlertDialogBuilder(this.requireContext()).apply {
        setTitle(title)
        setMessage(msg)
        setIcon(ic)
        show()
    }
}

fun Fragment.navigate(@IdRes to: Int) = this.findNavController().navigate(to)

fun Fragment.loadAnim(@AnimRes anim: Int): Animation = AnimationUtils.loadAnimation(this.context,anim)

@SuppressLint("WeekBasedYear")
infix fun LocalDateTime.asFormat(@DateConstraint constraint: Int): String {
    val dateFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY")
    val monthDayFormat = DateTimeFormatter.ofPattern("MMMM dd")
    val hourFormat = DateTimeFormatter.ofPattern("h:mm a")
    val dateCFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY - h:mm a")

    return when (constraint) {
        0 -> dateFormat.format(this)
        1 -> monthDayFormat.format(this)
        2 -> hourFormat.format(this)
        3 -> dateCFormat.format(this)
        else -> dateFormat.format(this)
    }

}

fun ImageView.setImg(@DrawableRes img: Int) = this.setImageResource(img)



