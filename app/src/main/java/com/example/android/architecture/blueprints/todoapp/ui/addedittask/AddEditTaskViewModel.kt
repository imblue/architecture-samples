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

package com.example.android.architecture.blueprints.todoapp.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.TaskRepository
import com.example.android.architecture.blueprints.todoapp.ui.TodoDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UiState for the Add/Edit screen
 */
data class AddEditTaskUiState(
    val title: String = "",
    val description: String = "",
    val isTaskCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskSaved: Boolean = false
)

/**
 * ViewModel for the Add/Edit screen.
 */
@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route: TodoDestinations.AddEditTask = savedStateHandle.toRoute()
    private val taskId: String? = route.taskId

    // A MutableStateFlow needs to be created in this ViewModel. The source of truth of the current
    // editable Task is the ViewModel, we need to mutate the UI state directly in methods such as
    // `updateTitle` or `updateDescription`
    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    init {
        if (taskId != null) {
            loadTask(taskId)
        }
    }

    // Called when clicking on fab.
    fun saveTask() {
        if (uiState.value.title.isEmpty() || uiState.value.description.isEmpty()) {
            _uiState.update {
                it.copy(userMessage = R.string.empty_task_message)
            }
            return
        }

        if (taskId == null) {
            createNewTask()
        } else {
            updateTask()
        }
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    fun updateTitle(newTitle: String) {
        _uiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateDescription(newDescription: String) {
        _uiState.update {
            it.copy(description = newDescription)
        }
    }

    private fun createNewTask() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            runCatching {
                taskRepository.createTask(
                    title = uiState.value.title,
                    description = uiState.value.description
                )
            }
                .onSuccess { _uiState.update { it.copy(isTaskSaved = true) } }
                .onFailure { _uiState.update { it.copy(isLoading = false, userMessage = R.string.saving_task_error) } }
        }
    }

    private fun updateTask() {
        _uiState.update { it.copy(isLoading = true) }

        if (taskId == null) {
            _uiState.update { it.copy(isLoading = false, userMessage = R.string.saving_task_error) }
            return
        }

        viewModelScope.launch {
            runCatching {
                taskRepository.updateTask(
                    taskId = taskId,
                    title = uiState.value.title,
                    description = uiState.value.description,
                )
            }
                .onSuccess { _uiState.update { it.copy(isTaskSaved = true) } }
                .onFailure { _uiState.update { it.copy(isLoading = false, userMessage = R.string.saving_task_error) } }
        }
    }

    private fun loadTask(taskId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            runCatching { taskRepository.getTask(taskId) }
                .onSuccess { task ->
                    if (task != null) {
                        _uiState.update {
                            it.copy(
                                title = task.title,
                                description = task.description,
                                isTaskCompleted = task.isCompleted,
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userMessage = R.string.loading_task_error
                            )
                        }
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = R.string.loading_task_error
                        )
                    }
                }
        }
    }
}
