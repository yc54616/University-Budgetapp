// com/example/universitybudgetapp/viewmodel/UserBankAppViewModel.kt

package com.example.universitybudgetapp.viewmodel

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.universitybudgetapp.data.repository.UserBankAppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserBankAppViewModel(
    application: Application,
    private val repository: UserBankAppRepository
) : AndroidViewModel(application) {

    data class SelectableAppInfo(
        val packageName: String,
        val appLabel: String,
        val icon: android.graphics.drawable.Drawable,
        val isSelected: Boolean
    )

    // 1) DB에 이미 선택된 패키지명을 Flow로 제공
    private val selectedPackagesFlow: Flow<Set<String>> =
        repository.getAllSelectedBankApps()
            .map { list -> list.map { it.packageName }.toSet() }

    // 2) getInstalledApplications()을 사용하여 모든 설치된 앱 목록을 Flow로 제공
    private val allAppsFlow: Flow<List<ApplicationInfo>> = flow {
        val pm: PackageManager = getApplication<Application>().packageManager

        // 2-A) getInstalledApplications으로 모든 앱(ApplicationInfo) 가져오기
        //     GET_META_DATA를 주면 meta-data까지 로드하지만, 단순히 아이콘/라벨만 필요하면 0으로 해도 무방합니다.
        val installed: List<ApplicationInfo> =
            pm.getInstalledApplications(PackageManager.GET_META_DATA)

        // 2-B) (선택) 로그로 전체 개수와 몇 개 예시를 출력해보기
        Log.d("AllAppsDebug", "앱 전체 개수(getInstalledApplications): ${installed.size}")
        installed.forEach { appInfo ->
            Log.d(
                "AllAppsDebug",
                "  • ${pm.getApplicationLabel(appInfo)} (${appInfo.packageName})"
            )
        }

        // 2-C) 앱 이름(라벨) 기준으로 오름차순 정렬
        val sorted: List<ApplicationInfo> = installed.sortedBy { appInfo ->
            pm.getApplicationLabel(appInfo).toString()
        }

        emit(sorted)
    }

    // 3) Installed(모든 앱) + Selected(DB에 저장된 패키지명) 을 합쳐서 UI 상태로 제공
    val uiState: StateFlow<List<SelectableAppInfo>> =
        allAppsFlow.combine(selectedPackagesFlow) { installedList, selectedSet ->
            val pm = getApplication<Application>().packageManager
            installedList.map { appInfo ->
                SelectableAppInfo(
                    packageName = appInfo.packageName,
                    appLabel = pm.getApplicationLabel(appInfo).toString(),
                    icon = pm.getApplicationIcon(appInfo),
                    isSelected = selectedSet.contains(appInfo.packageName)
                )
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // 4) 다이얼로그용 “설치 앱 전체”를 StateFlow로 노출
    val installedApps: StateFlow<List<ApplicationInfo>> =
        allAppsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // 5) 토글 변경 시 DB에 저장/삭제
    fun onAppToggled(appInfo: SelectableAppInfo, isNowSelected: Boolean) {
        viewModelScope.launch {
            if (isNowSelected) {
                repository.selectBankApp(appInfo.packageName, appInfo.appLabel)
            } else {
                repository.unselectBankApp(appInfo.packageName)
            }
        }
    }
}
