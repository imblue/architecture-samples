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

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ui.addedittask.AddEditTaskScreen
import com.example.android.architecture.blueprints.todoapp.ui.statistics.StatisticsScreen
import com.example.android.architecture.blueprints.todoapp.ui.taskdetail.TaskDetailScreen
import com.example.android.architecture.blueprints.todoapp.ui.tasks.TasksScreen
import com.example.android.architecture.blueprints.todoapp.util.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TodoNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navActions: TodoNavigationActions = remember(navController) {
        TodoNavigationActions(navController)
    }
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navActions = remember(navController) { TodoNavigationActions(navController) }

    NavHost(
        navController = navController,
        startDestination = TodoDestinations.TaskList()
    ) {
        composable<TodoDestinations.TaskList> { entry ->
            val route: TodoDestinations.TaskList = entry.toRoute()
            AppModalDrawer(drawerState, route, navActions) {
                TasksScreen(
                    onAddTask = { navActions.navigateToAddEditTask(R.string.add_task, null) },
                    onTaskClick = { task -> navActions.navigateToTaskDetail(task.id) },
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }

        composable<TodoDestinations.Statistics> { entry ->
            AppModalDrawer(drawerState, entry.toRoute<TodoDestinations.Statistics>(), navActions) {
                StatisticsScreen(openDrawer = { scope.launch { drawerState.open() } })
            }
        }

        composable<TodoDestinations.AddEditTask> { entry ->
            val route: TodoDestinations.AddEditTask = entry.toRoute()
            AddEditTaskScreen(
                topBarTitle = route.title,
                onFinish = {
                    navActions.navigateToTasks(
                        if (route.taskId == null) {
                            R.string.successfully_added_task_message
                        } else {
                            R.string.successfully_saved_task_message
                        }
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<TodoDestinations.TaskDetail> { entry ->
            TaskDetailScreen(
                onEditTask = { taskId ->
                    navActions.navigateToAddEditTask(R.string.edit_task, taskId)
                },
                onBack = { navController.popBackStack() },
                onDeleteTask = { navActions.navigateToTasks(R.string.successfully_deleted_task_message) }
            )
        }
    }
}
