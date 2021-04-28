package com.nmrc.note.repository.model

import android.graphics.drawable.Drawable
import com.nmrc.note.R

enum class TopicTaks(drawable: Int) {

    HOME(R.drawable.ic_home_statistisc_task),
    WORK(R.drawable.ic_work_statistisc_task),
    OTHER(R.drawable.ic_other_statistisc_task);

    var drawable: Int? = null

    init {
        this.drawable = drawable
    }
}