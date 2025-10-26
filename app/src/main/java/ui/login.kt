package com.example.goldnet.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldnet.controllers.UserController
import com.example.goldnet.data.repository.DataManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(dataManager: DataManager) : ViewModel() {
    private val userController = UserController(dataManager)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val res = userController.login(email, password)
            _uiState.value = if (res.isSuccess) LoginUiState.Success(res.getOrNull()!!) else LoginUiState.Error(res.exceptionOrNull()?.message ?: "Error")
        }
    }

    sealed class LoginUiState {
        object Idle: LoginUiState()
        object Loading: LoginUiState()
        data class Success(val user: com.example.goldnet.data.model.User) : LoginUiState()
        data class Error(val message: String): LoginUiState()
    }
}
