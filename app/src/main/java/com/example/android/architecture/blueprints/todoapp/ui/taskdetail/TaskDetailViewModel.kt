/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.TaskRepository
import com.example.android.architecture.blueprints.todoapp.ui.TodoDestinations
import com.example.android.architecture.blueprints.todoapp.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * UiState for the Details screen.
 */
data class TaskDetailUiState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskDeleted: Boolean = false
)

/**
 * ViewModel for the Details screen.
 */
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route: TodoDestinations.TaskDetail = savedStateHandle.toRoute()
    val taskId = route.id

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isTaskDeleted = MutableStateFlow(false)

    private val _taskAsync = taskRepository.getTaskStream(taskId)
        .onEach {
            if (it == null) {
                _userMessage.value = R.string.task_not_found
            }
        }
        .asResult()

    val uiState: StateFlow<TaskDetailUiState> = combine(
        _userMessage, _isLoading, _isTaskDeleted, _taskAsync
    ) { userMessage, isLoading, isTaskDeleted, taskAsync ->
        if (taskAsync.isSuccess) {
            TaskDetailUiState(
                task = taskAsync.getOrNull(),
                isLoading = isLoading,
                userMessage = userMessage,
                isTaskDeleted = isTaskDeleted
            )
        } else {
            TaskDetailUiState(
                isLoading = isLoading,
                userMessage = R.string.loading_task_error,
                isTaskDeleted = isTaskDeleted
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = TaskDetailUiState(isLoading = true)
        )

    fun deleteTask() = viewModelScope.launch {
        runCatching { taskRepository.deleteTask(taskId) }
            .onSuccess { _isTaskDeleted.value = true }
            .onFailure { Timber.e(it) }
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = uiState.value.task ?: return@launch

        if (completed) {
            runCatching { taskRepository.completeTask(task.id) }
                .onSuccess { showSnackbarMessage(R.string.task_marked_complete) }
                .onFailure { Timber.e(it) }
        } else {
            runCatching { taskRepository.activateTask(task.id) }
                .onSuccess { showSnackbarMessage(R.string.task_marked_active) }
                .onFailure { Timber.e(it) }
        }
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            taskRepository.refreshTask(taskId)
            _isLoading.value = false
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }
}
