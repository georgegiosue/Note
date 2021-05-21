package com.nmrc.note.data.model.room.converter

import androidx.room.TypeConverter
import com.nmrc.note.data.model.Priority
import com.nmrc.note.data.model.Topic
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class Converters {

    @TypeConverter
    fun toDateTime(time: Long): LocalDateTime {
        val zdt = Date(time).toInstant().atZone(ZoneId.systemDefault())
        return zdt.toLocalDateTime()
    }

    @TypeConverter
    fun fromDateTime(ldt: LocalDateTime): Long {
        val instant = ldt.atZone(ZoneId.systemDefault()).toInstant()
        val date = Date.from(instant)
        return date.time
    }

    @TypeConverter
    fun toPriority(prt: String): Priority {
        return when(prt) {
            "HIGH" -> Priority.HIGH
            "MEDIUM" -> Priority.MEDIUM
            "LOW" -> Priority.LOW
            else -> throw IllegalArgumentException("Priority no found")
        }
    }

    @TypeConverter
    fun fromPriority(prt: Priority): String {
        return when(prt) {
            Priority.HIGH -> "HIGH"
            Priority.MEDIUM -> "MEDIUM"
            Priority.LOW -> "LOW"
        }
    }

    @TypeConverter
    fun toTopic(tp: String): Topic {
        return when(tp) {
            "HOME" -> Topic.HOME
            "WORK" -> Topic.WORK
            "OTHER" -> Topic.OTHER
            else -> throw IllegalArgumentException("Topic no found")
        }
    }

    @TypeConverter
    fun fromTopic(tp: Topic): String {
        return when(tp) {
            Topic.HOME -> "HOME"
            Topic.WORK -> "WORK"
            Topic.OTHER -> "OTHER"
        }
    }
}