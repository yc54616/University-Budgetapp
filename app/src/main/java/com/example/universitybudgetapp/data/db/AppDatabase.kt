package com.example.universitybudgetapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.universitybudgetapp.data.model.*

@Database(
    entities = [
        Entry::class,
        UserCategoryEntity::class,
        NotificationItem::class  // 🔥 새로 추가!
    ],
    version = 7, // 🔥 DB 스키마 버전 업그레이드!
    exportSchema = false
)
@TypeConverters(
    CategoryConverter::class,
    DateConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun userCategoryDao(): UserCategoryDao
    abstract fun notificationDao(): NotificationDao  // 🔥 추가!

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_db"
                )
                    .fallbackToDestructiveMigration() // 개발 중이라면 안전하게 데이터 삭제 후 재생성
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
