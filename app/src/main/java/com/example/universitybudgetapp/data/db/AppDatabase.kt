package com.example.universitybudgetapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.universitybudgetapp.data.model.CategoryConverter
import com.example.universitybudgetapp.data.model.DateConverter
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.data.model.NotificationItem
import com.example.universitybudgetapp.data.model.UserCategoryEntity
import com.example.universitybudgetapp.data.db.UserBankAppEntity  // 🔥 추가

@Database(
    entities = [
        Entry::class,
        UserCategoryEntity::class,
        NotificationItem::class,
        UserBankAppEntity::class     // 🔥 추가: 사용자 선택 은행/금융 앱 엔티티
    ],
    version = 8,    // 🔥 버전 업: 기존 7 → 8
    exportSchema = false
)
@TypeConverters(
    CategoryConverter::class,
    DateConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun entryDao(): EntryDao
    abstract fun userCategoryDao(): UserCategoryDao
    abstract fun notificationDao(): NotificationDao

    // 🔥 새로 추가된 DAO
    abstract fun userBankAppDao(): UserBankAppDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_db"
                )
                    .fallbackToDestructiveMigration() // 개발 중이라면 스키마가 변경되면 DB를 초기화
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
