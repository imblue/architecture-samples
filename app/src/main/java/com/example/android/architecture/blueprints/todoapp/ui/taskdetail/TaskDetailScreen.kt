/*
 * Copyright 2022 The Android Open Source Project
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.ui.AppTheme

@Composable
fun TaskDetailScreen(
    onEditTask: (String) -> Unit,
    onBack: () -> Unit,
    onDeleteTask: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TaskDetailTopAppBar(onBack = onBack, onDelete = viewModel::deleteTask) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEditTask(viewModel.taskId) }) {
                Icon(Icons.Filled.Edit, stringResource(id = R.string.edit_task))
            }
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        EditTaskContent(
            loading = uiState.isLoading,
            task = uiState.task,
            onTaskCheck = viewModel::setCompleted,
            modifier = Modifier.padding(paddingValues)
        )

        // Check for user messages to display on the screen
        uiState.userMessage?.let { userMessage ->
            if (uiState.isTaskDeleted) {
                return@let
            }

            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(
                    message = snackbarText,
                    withDismissAction = true,
                    duration = SnackbarDuration.Indefinite
                )
                viewModel.snackbarMessageShown()
                onBack()
            }
        }

        // Check if the task is deleted and call onDeleteTask
        LaunchedEffect(uiState.isTaskDeleted) {
            if (uiState.isTaskDeleted) {
                onDeleteTask()
            }
        }
    }
}

@Composable
private fun EditTaskContent(
    loading: Boolean,
    task: Task?,
    onTaskCheck: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (loading) {
        LinearProgressIndicator(
            modifier = modifier.fillMaxWidth()
        )
    }

    if (task != null) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(all = 16.dp),

            ) {
            Checkbox(task.isCompleted, onTaskCheck)
            Column {
                Text(text = task.title, style = MaterialTheme.typography.headlineSmall)
                Text(text = task.description, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun TaskDetailTopAppBar(onBack: () -> Unit, onDelete: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.task_details))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.menu_back))
            }
        },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, stringResource(id = R.string.menu_delete_task))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun EditTaskContentPreview() {
    Surface {
        EditTaskContent(
            loading = false,
            Task(
                title = "Title",
                description = "Description",
                isCompleted = false,
                id = "ID"
            ),
            onTaskCheck = { }
        )
    }

}

@Preview
@Composable
private fun EditTaskContentTaskCompletedPreview() {
    Surface {
        EditTaskContent(
            loading = false,
            Task(
                title = "Title",
                description = "Description",
                isCompleted = false,
                id = "ID"
            ),
            onTaskCheck = { }
        )
    }
}

@Preview
@Composable
private fun EditTaskContentEmptyPreview() {
    Surface {
        EditTaskContent(
            loading = false,
            Task(
                title = "Title",
                description = "Description",
                isCompleted = false,
                id = "ID"
            ),
            onTaskCheck = { }
        )
    }
}

@Preview
@Composable
private fun TaskDetailTopAppBarPreview() {
    AppTheme {
        Surface {
            TaskDetailTopAppBar({ }, { })
        }
    }
}
