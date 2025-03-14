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

package com.example.android.architecture.blueprints.todoapp.ui.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.TaskRepository
import com.example.android.architecture.blueprints.todoapp.ui.TodoDestinations
import com.example.android.architecture.blueprints.todoapp.ui.tasks.TasksFilterType.ACTIVE_TASKS
import com.example.android.architecture.blueprints.todoapp.ui.tasks.TasksFilterType.ALL_TASKS
import com.example.android.architecture.blueprints.todoapp.ui.tasks.TasksFilterType.COMPLETED_TASKS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UiState for the task list screen.
 */
data class TasksUiState(
    val items: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val selectedFilter: TasksFilterType = ALL_TASKS,
    val userMessage: Int? = null
)

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val route: TodoDestinations.TaskList = savedStateHandle.toRoute()

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)

    private val _selectedFilter = MutableStateFlow<TasksFilterType>(ALL_TASKS)
    private val _tasks = taskRepository.getTasksStream()
        .catch {
            _userMessage.emit(R.string.loading_tasks_error)
            emit(emptyList())
        }
        .combine(_selectedFilter) { tasks, filter -> filter.filterTasks(tasks) }

    val uiState: StateFlow<TasksUiState> = combine(
        _selectedFilter, _isLoading, _userMessage, _tasks
    ) { selectedFilter, isLoading, userMessage, tasks ->
        TasksUiState(
            items = tasks,
            selectedFilter = selectedFilter,
            isLoading = isLoading,
            userMessage = userMessage,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = TasksUiState(isLoading = true)
        )

    init {
        route.userMessage?.let { showSnackbarMessage(it) }
        refresh()
    }

    fun setFiltering(requestType: TasksFilterType) {
        _selectedFilter.value = requestType
    }

    fun clearCompletedTasks() {
        _isLoading.value = true
        viewModelScope.launch {
            taskRepository.clearCompletedTasks()
            showSnackbarMessage(R.string.completed_tasks_cleared)

            _isLoading.value = false
        }
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        _isLoading.value = true

        if (completed) {
            taskRepository.completeTask(task.id)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            taskRepository.activateTask(task.id)
            showSnackbarMessage(R.string.task_marked_active)
        }

        _isLoading.value = false
    }

    fun resetUserMessage() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    fun refresh() {
        _isLoading.value = true

        viewModelScope.launch {
            taskRepository.refresh()
            _isLoading.value = false
        }
    }

    private fun TasksFilterType.filterTasks(tasks: List<Task>): List<Task> {
        return tasks.filter {
            when (this) {
                ALL_TASKS -> true
                ACTIVE_TASKS -> it.isActive
                COMPLETED_TASKS -> it.isCompleted
            }
        }
    }
}
