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

package com.example.android.architecture.blueprints.todoapp.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UiState for the statistics screen.
 */
data class StatisticsUiState(
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val activeTasksPercent: Float = 0f,
    val completedTasksPercent: Float = 0f,
    val throwable: Throwable? = null
)

/**
 * ViewModel for the statistics screen.
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<Throwable?>(null)
    private val _tasks: Flow<Result<List<Task>>> = taskRepository.getTasksStream()
        .map { Result.success(it) }
        .catch { t -> emit(Result.failure(t)) }

    val uiState: StateFlow<StatisticsUiState> =
        combine(_isLoading, _error, _tasks) { loading, error, tasks ->
            produceStatisticsUiState(loading, error, tasks)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = StatisticsUiState(isLoading = true)
            )

    init {
        _isLoading.value = true

        viewModelScope.launch {
            runCatching { taskRepository.refresh() }
                .onFailure { _error.value = it }
                .let { _isLoading.value = false }
        }
    }

    private fun produceStatisticsUiState(
        loading: Boolean,
        error: Throwable?,
        taskLoad: Result<List<Task>>
    ): StatisticsUiState {
        val stats = taskLoad.getOrNull()?.let { data -> getActiveAndCompletedStats(data) }

        return StatisticsUiState(
            isLoading = loading,
            isEmpty = taskLoad.getOrNull().isNullOrEmpty(),
            activeTasksPercent = stats?.activeTasksPercent ?: 0f,
            completedTasksPercent = stats?.completedTasksPercent ?: 0f,
            throwable = error ?: taskLoad.exceptionOrNull(),
        )
    }
}
