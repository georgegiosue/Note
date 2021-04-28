package com.nmrc.note.repository.model

import com.nmrc.note.R

enum class Priority(drawable: Int) {

    HIGHT(R.drawable.shape_priority_high_task),
    MEDIUM(R.drawable.shape_priority_medium_task),
    LOW(R.drawable.shape_priority_low_task);

    var drawable: Int? = null

    init {
        this.drawable = drawable
    }
}