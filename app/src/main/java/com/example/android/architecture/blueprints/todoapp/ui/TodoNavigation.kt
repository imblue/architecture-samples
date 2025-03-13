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

package com.example.android.architecture.blueprints.todoapp.ui

import androidx.annotation.StringRes
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

/**
 * Destinations used in the nav graph.
 */
sealed class TodoDestinations {

    @Serializable
    data class TaskList(
        @StringRes val userMessage: Int? = null
    ) : TodoDestinations()

    @Serializable
    data object Statistics : TodoDestinations()

    @Serializable
    data class TaskDetail(
        val id: String
    ) : TodoDestinations()

    @Serializable
    data class AddEditTask(
        @StringRes val title: Int,
        val taskId: String?
    ) : TodoDestinations()
}

/**
 * Models the navigation actions in the app.
 */
class TodoNavigationActions(private val navController: NavHostController) {

    fun navigateToTasks(userMessage: Int? = null) {
        val navigatesFromDrawer = userMessage == null

        navController.navigate(TodoDestinations.TaskList(userMessage)) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToStatistics() {
        navController.navigate(TodoDestinations.Statistics) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToTaskDetail(taskId: String) {
        navController.navigate(TodoDestinations.TaskDetail(taskId))
    }

    fun navigateToAddEditTask(title: Int, taskId: String?) {
        navController.navigate(TodoDestinations.AddEditTask(title, taskId))
    }
}
