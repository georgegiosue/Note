package com.nmrc.note.data.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.room.dao.NotesDao
import com.nmrc.note.data.model.room.dao.TasksDao
import com.nmrc.note.data.model.room.converter.Converters

@Database(entities = [Note::class,Task::class],
          version = 1,
          exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            INSTANCE?.let { return it }

            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "APP_DATABASE"
                ).build()

                return INSTANCE as AppDatabase
            }
        }

    }

    abstract fun notesDao(): NotesDao

    abstract fun tasksDao(): TasksDao
}