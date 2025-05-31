package com.example.universitybudgetapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.universitybudgetapp.data.model.CategoryConverter
import com.example.universitybudgetapp.data.model.DateConverter
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.data.model.UserCategoryEntity

@Database(
    entities = [Entry::class, UserCategoryEntity::class],
    version = 5,               // ← 스키마 변경시 반드시 올려야 합니다!
    exportSchema = false
)
@TypeConverters(
    CategoryConverter::class,  // 기존
    DateConverter::class       // 추가
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun userCategoryDao(): UserCategoryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_db"
                )
                    // .fallbackToDestructiveMigration() // 개발 중 필요시
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
