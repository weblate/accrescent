package app.accrescent.client.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.accrescent.client.data.RepoDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import java.io.FileNotFoundException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    private val repoDataRepository: RepoDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val appId = savedStateHandle.get<String>("appId")!!
    var uiState by mutableStateOf(AppDetailsUiState(appId = appId))
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(isFetchingData = true, error = null)

            val trustedInfo = repoDataRepository.getApp(appId)
            if (trustedInfo == null) {
                uiState = uiState.copy(appExists = false, isFetchingData = false)
                return@launch
            } else {
                uiState = uiState.copy(appName = trustedInfo.name)
            }

            uiState = try {
                val untrustedInfo = repoDataRepository.getAppRepoData(appId)
                uiState.copy(
                    versionName = untrustedInfo.version,
                    versionCode = untrustedInfo.versionCode,
                )
            } catch (e: ConnectException) {
                uiState.copy(error = "Network error: ${e.message}", appExists = false)
            } catch (e: FileNotFoundException) {
                uiState.copy(error = "Failed to download repodata", appExists = false)
            } catch (e: SerializationException) {
                uiState.copy(error = "Failed to decode repodata", appExists = false)
            } catch (e: UnknownHostException) {
                uiState.copy(error = "Unknown host error: ${e.message}", appExists = false)
            }

            uiState = uiState.copy(isFetchingData = false)
        }
    }
}
