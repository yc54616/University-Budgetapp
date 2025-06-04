// com/example/universitybudgetapp/data/db/UserBankAppDao.kt
package com.example.universitybudgetapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBankAppDao {

    /** 사용자가 선택한 모든 패키지명을 Flow로 방출 */
    @Query("SELECT * FROM user_bank_apps")
    fun getAllSelectedBankApps(): Flow<List<UserBankAppEntity>>

    /** 특정 패키지를 추가(선택) */
    @Insert
    suspend fun insert(app: UserBankAppEntity)

    /** 특정 패키지를 삭제(선택 해제) */
    @Delete
    suspend fun delete(app: UserBankAppEntity)

    /** packageName으로 사용자가 이미 선택해두었는지 여부 체크 */
    @Query("SELECT EXISTS(SELECT 1 FROM user_bank_apps WHERE packageName = :pkg)")
    suspend fun isSelected(pkg: String): Boolean

    /** 특정 패키지를 키로 UserBankAppEntity를 가져오는 메서드 */
    @Query("SELECT * FROM user_bank_apps WHERE packageName = :pkg LIMIT 1")
    suspend fun findByPackageName(pkg: String): UserBankAppEntity?
}
