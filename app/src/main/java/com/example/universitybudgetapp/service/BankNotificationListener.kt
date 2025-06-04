// com/example/universitybudgetapp/service/BankNotificationListener.kt

package com.example.universitybudgetapp.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.universitybudgetapp.data.db.AppDatabase
import com.example.universitybudgetapp.data.repository.UserBankAppRepository
import com.example.universitybudgetapp.util.NotificationBroadcaster
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class BankNotificationListener : NotificationListenerService() {

    // ① 선택된 은행 앱 패키지를 메모리 캐시로 보관할 Set
    private val selectedAppPackages = mutableSetOf<String>()

    // ② Service 전체에서 사용할 CoroutineScope
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // ③ 금액 파싱용 정규식
    private val amountRegex = Regex(
        """
        (?:₩\s*)?
        (\d{1,3}(?:,\d{3})*)
        (?:\s*원)?
        """.trimIndent()
    )

    override fun onCreate() {
        super.onCreate()

        // ④ 데이터베이스에서 UserBankAppDao 를 가져와 Repository 생성
        val bankAppDao = AppDatabase
            .getInstance(applicationContext)
            .userBankAppDao()
        val repository = UserBankAppRepository(bankAppDao)

        // ⑤ 서비스가 시작될 때, Flow로부터 “현재 선택된 은행 앱 패키지명 목록”을 수집하여 캐시에 저장
        serviceScope.launch {
            repository.getAllSelectedBankApps()
                .map { list -> list.map { it.packageName }.toSet() }
                .collectLatest { newSet ->
                    synchronized(selectedAppPackages) {
                        selectedAppPackages.clear()
                        selectedAppPackages.addAll(newSet)
                    }
                    Log.d("BankNotificationListener", "캐시된 선택앱 목록 업데이트 → $selectedAppPackages")
                }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // ⑥ 알림을 보낸 앱의 패키지명
        val originatingPackage = sbn.packageName

        // ⑦ 캐시에 없으면(=설정에서 ON하지 않은 앱이라면) 무시
        val isSelected: Boolean = synchronized(selectedAppPackages) {
            selectedAppPackages.contains(originatingPackage)
        }
        if (!isSelected) return

        // ⑧ 여기부터 “선택된 은행 앱의 알림”만 처리
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString().orEmpty()
        val text = extras.getCharSequence("android.text")?.toString().orEmpty()

        // ⑨ amountRegex 로 금액 매칭
        val match = amountRegex.find(text)
        val amount: Long = match
            ?.groups
            ?.get(1)         // 그룹 1: “2,200” 또는 “130,024” 같은 콤마 포함 숫자
            ?.value
            ?.replace(",", "") // 콤마 제거
            ?.toLongOrNull()  // Long 변환 시도
            ?: 0L              // 매칭 안되면 0

        // ⑩ 지출/수입 키워드 예시
        val expenseKeywords = listOf("출금", "이체", "결제", "송금")
        val incomeKeywords = listOf("입금", "수신", "적립", "급여")

        val isExpense = expenseKeywords.any { it in text || it in title }
        val isIncome = incomeKeywords.any  { it in text || it in title }

        val type = when {
            isExpense -> "지출"
            isIncome  -> "수입"
            else      -> "기타"
        }

        Log.d(
            "BankNotificationListener",
            "감지된 알림: 패키지=$originatingPackage, 제목='$title', 본문='$text', 금액=$amount, 타입=$type"
        )

        // ⑪ 브로드캐스트(또는 앱 내부 알림 표시) 호출
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

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel() // 서비스가 내려갈 때 코루틴 스코프 취소
    }
}
