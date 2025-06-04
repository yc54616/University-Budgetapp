// BankNotificationListener.kt

package com.example.universitybudgetapp.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.universitybudgetapp.data.db.AppDatabase
import com.example.universitybudgetapp.data.repository.UserBankAppRepository
import com.example.universitybudgetapp.util.NotificationBroadcaster
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class BankNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // ① 알림을 보낸 앱의 패키지명
        val originatingPackage = sbn.packageName

        // ② AppDatabase를 통해 DAO 가져오기
        val dao = AppDatabase
            .getInstance(applicationContext)
            .userCategoryDao() // 항상 UserBankAppDao가 아니라 UserCategoryDao를 쓰고 싶은 경우만 사용

        // 위 코드는 잘못된 예시. 실제로는 UserBankAppDao를 가져와야 합니다:
        // val dao = AppDatabase.getInstance(applicationContext).userBankAppDao()

        // ③ 올바르게 UserBankAppDao를 가져오는 예시:
        val bankAppDao = AppDatabase
            .getInstance(applicationContext)
            .userBankAppDao()

        val repository = UserBankAppRepository(bankAppDao)

        // ④ Flow에서 “선택된 은행 앱 패키지명” 목록을 동기적으로 가져오기
        val selectedSet: Set<String> = runBlocking {
            repository.getAllSelectedBankApps()
                .firstOrNull()
                ?.map { it.packageName }
                ?.toSet()
                ?: emptySet()
        }

        // ⑤ 알림을 보낸 패키지명이 선택된 앱 목록에 없으면 무시
        if (!selectedSet.contains(originatingPackage)) {
            return
        }

        // ⑥ 여기부터는 “선택된 은행 앱의 알림”만 처리
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        val amountRegex = Regex("""(\d{1,3}(,\d{3})*(\.\d{1,2})?)\s?(원|₩|KRW)?""")
        val match = amountRegex.find(text)
        val amount = match?.groups?.get(1)?.value
            ?.replace(",", "")
            ?.toDoubleOrNull()
            ?.toLong() ?: 0L

        val expenseKeywords = listOf("출금", "이체", "결제", "송금")
        val incomeKeywords = listOf("입금", "수신", "적립", "급여")

        val isExpense = expenseKeywords.any { text.contains(it) || title.contains(it) }
        val isIncome = incomeKeywords.any { text.contains(it) || title.contains(it) }

        val type = when {
            isExpense -> "지출"
            isIncome  -> "수입"
            else      -> "기타"
        }

        Log.d(
            "BankNotificationListener",
            "선택된 앱 알림 감지: 패키지=$originatingPackage, 제목='$title', 본문='$text', 금액=$amount, 타입=$type"
        )

        NotificationBroadcaster.broadcastNotification(
            applicationContext,
            title,
            text,
            amount,
            type
        )
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("BankNotificationListener", "NotificationListenerService 연결됨")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("BankNotificationListener", "NotificationListenerService 연결 해제됨")
    }
}
