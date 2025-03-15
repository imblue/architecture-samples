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

    /**
     * "data class" can be used to define routes
     * with parameters.
     */
    @Serializable
    data class TaskList(
        @StringRes val userMessage: Int? = null
    ) : TodoDestinations()

    /**
     * "data object" can be used to define routes
     * without parameters.
     */
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
 * Wrapper for the nav controller that handles the navigation actions within the app.
 */
class TodoNavigationActions(private val navController: NavHostController) {

    fun navigateToTasks(userMessage: Int? = null) {
        val navigatesFromDrawer = userMessage == null

        navController.navigate(TodoDestinations.TaskList(userMessage)) {
            // "popUpTo" pops all destinations off the back stack
            // -> the start destination (i.e. TodoDestinations.TaskList)
            // will be the only route left in the back stack.
            // If the user presses the "back button", while this screen is opened,
            // the App will be closed as there is no back stack to navigate to.
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }

            // Use "launchSingleTop" to avoid copies of the same destination in the backstack
            // if opened multiple times.
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToStatistics() {
        navController.navigate(TodoDestinations.Statistics) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items within the navigation drawer.
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
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
