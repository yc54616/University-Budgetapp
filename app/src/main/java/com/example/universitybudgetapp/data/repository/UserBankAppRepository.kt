// com/example/universitybudgetapp/data/repository/UserBankAppRepository.kt
package com.example.universitybudgetapp.data.repository

import com.example.universitybudgetapp.data.db.UserBankAppDao
import com.example.universitybudgetapp.data.db.UserBankAppEntity
import kotlinx.coroutines.flow.Flow

class UserBankAppRepository(private val dao: UserBankAppDao) {

    /** Flow를 리턴해서 화면에서 실시간으로 선택 상태를 관찰할 수 있도록 함 */
    fun getAllSelectedBankApps(): Flow<List<UserBankAppEntity>> =
        dao.getAllSelectedBankApps()

    /** 특정 앱 선택 */
    suspend fun selectBankApp(packageName: String, appLabel: String) {
        dao.insert(UserBankAppEntity(packageName = packageName, appLabel = appLabel))
    }

    /** 특정 앱 선택 해제 */
    suspend fun unselectBankApp(packageName: String) {
        val entity = dao.findByPackageName(packageName)
        if (entity != null) {
            dao.delete(entity)
        }
    }

    /** 이미 선택되었는지 체크 (예: 토글 상태 초기화 시 사용) */
    suspend fun isBankAppSelected(packageName: String): Boolean =
        dao.isSelected(packageName)
}
