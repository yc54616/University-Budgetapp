package com.example.universitybudgetapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.universitybudgetapp.data.model.Entry

@Database(entities = [Entry::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
}
