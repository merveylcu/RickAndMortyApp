package com.merveylcu.core

import androidx.lifecycle.ViewModel
import com.merveylcu.contract.INetworkError
import com.merveylcu.contract.RestResult
import com.merveylcu.contract.network.ErrorType
import com.merveylcu.contract.network.LoadingType
import com.merveylcu.navigation.NavigationCommand
import com.merveylcu.navigation.NavigationManager
import com.merveylcu.navigation.Screens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    private var loadingCounter = 0

    @Inject
    lateinit var navigationManager: NavigationManager

    protected fun <T : Any> request(
        errorType: ErrorType = ErrorType.Default,
        loadingType: LoadingType = LoadingType.Default,
        call: () -> Flow<RestResult<T>>
    ): Flow<RestResult<T>> {
        return try {
            call.invoke().transform { restResult ->
                when (restResult) {
                    is RestResult.Error -> {
                        if (errorType == ErrorType.Default) {
                            onServiceError(restResult.error)
                        }
                    }

                    is RestResult.Loading -> {
                        if (loadingType == LoadingType.Default) {
                            handleLoading(restResult)
                            if (loadingCounter == 0 && restResult.loading.not()) {
                                hideLoading()
                            } else if (loadingCounter == 1 && restResult.loading) {
                                showLoading()
                            }
                        }
                    }

                    is RestResult.Success -> Unit
                }
                emit(restResult)
            }
        } catch (exception: Exception) {
            onCatch(
                exception = exception
            )
        }.catch {
            onCatch<T>(
                exception = it
            )
        }
    }

    private fun <T> onCatch(exception: Throwable) =
        flow<RestResult<T>> {
            val error = object : INetworkError {
                override var message: String? = exception.message
                override var code: Int? = null
            }

            onServiceError(error)
            val errorResult = RestResult.Error(error)
            emit(errorResult)
            return@flow
        }

    private fun onServiceError(error: INetworkError) {
        navigationManager.navigate(NavigationCommand.OpenScreen(Screens.Error.route(error.message.orEmpty())))
    }

    private fun handleLoading(result: RestResult.Loading) {
        if (result.loading) {
            loadingCounter++
        } else {
            loadingCounter--
        }
    }

    open fun showLoading() {
        if (::navigationManager.isInitialized) {
            navigationManager.navigate(NavigationCommand.ShowLoading(true))
        }
    }

    open fun hideLoading() {
        navigationManager.navigate(NavigationCommand.ShowLoading(false))
    }
}
