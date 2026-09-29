package com.skillx.features.payments.presentation.purchase
import com.skillx.core.result.AppResult
import com.skillx.features.payments.domain.usecase.LoadPointPackagesUseCase
import com.skillx.features.payments.domain.usecase.PurchasePointPackageUseCase
import com.skillx.features.points.domain.usecase.LoadPointBalanceUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PurchasePointsViewModel(private val loadPackages: LoadPointPackagesUseCase, private val purchasePackage: PurchasePointPackageUseCase, private val loadBalance: LoadPointBalanceUseCase, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(PurchasePointsUiState())
    val uiState: StateFlow<PurchasePointsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { _uiState.update { it.copy(isLoading = true) }; scope.launch {
        when (val b = loadBalance()) { is AppResult.Success -> _uiState.update { it.copy(currentBalance = b.data.points) }; is AppResult.Error -> {} }
        when (val p = loadPackages()) { is AppResult.Success -> _uiState.update { it.copy(packages = p.data, isLoading = false) }; is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = p.error.message) } }
    } }
    fun purchase(productId: String, transactionId: String, receipt: String) { _uiState.update { it.copy(isPurchasing = true) }; scope.launch { when (val r = purchasePackage(productId, transactionId, receipt)) { is AppResult.Success -> _uiState.update { it.copy(isPurchasing = false, purchaseSuccess = true) }; is AppResult.Error -> _uiState.update { it.copy(isPurchasing = false, error = r.error.message) } }; load() } }
}
