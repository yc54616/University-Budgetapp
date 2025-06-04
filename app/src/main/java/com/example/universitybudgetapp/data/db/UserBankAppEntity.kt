// com/example/universitybudgetapp/data/db/UserBankAppEntity.kt
package com.example.universitybudgetapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 사용자가 알림을 받고자 선택한 은행/금융 앱의 패키지명을 저장하는 Entity.
 * - packageName: 앱을 구분하는 고유 키 (Primary Key)
 * - appLabel: 화면에 보여줄 앱 이름(선택 사항)
 */
@Entity(tableName = "user_bank_apps")
data class UserBankAppEntity(
    @PrimaryKey val packageName: String,
    val appLabel: String
)
