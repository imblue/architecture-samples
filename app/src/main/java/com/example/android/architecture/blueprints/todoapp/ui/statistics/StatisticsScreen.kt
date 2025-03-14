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

package com.example.android.architecture.blueprints.todoapp.ui.statistics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ui.AppTheme

@Composable
fun StatisticsScreen(
    openDrawer: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { StatisticsTopAppBar(openDrawer) },
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            StatisticsContent(
                loading = uiState.isLoading,
                empty = uiState.isEmpty,
                activeTasksPercent = uiState.activeTasksPercent,
                completedTasksPercent = uiState.completedTasksPercent,
            )
        }
    }
}

@Composable
private fun StatisticsContent(
    loading: Boolean,
    empty: Boolean,
    activeTasksPercent: Float,
    completedTasksPercent: Float,
) {
    when {
        loading -> CircularProgressIndicator()
        empty -> Text(stringResource(id = R.string.statistics_no_tasks))
        else -> {
            ElevatedCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(all = 16.dp)
                ) {
                    Text(stringResource(id = R.string.statistics_active_tasks, activeTasksPercent))
                    Text(
                        stringResource(
                            id = R.string.statistics_completed_tasks,
                            completedTasksPercent
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticsTopAppBar(openDrawer: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.statistics_title)) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Filled.Menu, stringResource(id = R.string.open_drawer))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun StatisticsTopAppBarPreview() {
    AppTheme {
        Surface {
            StatisticsTopAppBar { }
        }
    }
}

@Preview
@Composable
fun StatisticsContentPreview() {
    Surface {
        StatisticsContent(
            loading = false,
            empty = false,
            activeTasksPercent = 80f,
            completedTasksPercent = 20f
        )
    }
}

@Preview
@Composable
fun StatisticsContentEmptyPreview() {
    Surface {
        StatisticsScreen({})
    }
}
