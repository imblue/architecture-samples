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

package com.example.android.architecture.blueprints.todoapp.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ui.AppTheme
import com.example.android.architecture.blueprints.todoapp.ui.TodoDestinations
import com.example.android.architecture.blueprints.todoapp.ui.TodoNavigationActions
import kotlinx.coroutines.launch

/**
 * Uses the material3 navigation drawer.
 * This should be wrapped around any screen that provides the navigation drawer.
 */
@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: TodoDestinations,
    navigationActions: TodoNavigationActions,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToTasks = { navigationActions.navigateToTasks() },
                navigateToStatistics = { navigationActions.navigateToStatistics() },
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: TodoDestinations,
    navigateToTasks: () -> Unit,
    navigateToStatistics: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet {
        DrawerHeader()

        // "NavigationDrawerItem" is the default material3 implementation
        // of a clickable action inside the navigation drawer.
        // It supports adding icons, labels and selection markers by default.
        NavigationDrawerItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_list),
                    contentDescription = null
                )
            },
            label = { Text(stringResource(id = R.string.list_title)) },
            selected = currentRoute is TodoDestinations.TaskList,
            onClick = {
                navigateToTasks()
                closeDrawer()
            },
            // The default shape is with a round corner on all sides.
            // To "beautify" this slighty, we use a custom shape
            // to only round the corners on the right ("end") side
            // with a small space to the end of the drawer.
            // The left ("start") in this case is not round.
            shape = RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50),
            modifier = modifier.padding(end = 16.dp)
        )

        NavigationDrawerItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_statistics),
                    contentDescription = null
                )
            },
            label = { Text(stringResource(id = R.string.statistics_title)) },
            selected = currentRoute is TodoDestinations.Statistics,
            onClick = {
                navigateToStatistics()
                closeDrawer()
            },
            shape = RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50),
            modifier = modifier.padding(end = 16.dp)
        )
    }
}

/**
 * While "action" items come with a default styling,
 * a custom layout can be added for styling purposes.
 */
@Composable
private fun DrawerHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .height(192.dp)
            .padding(all = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_no_fill),
            contentDescription =
                stringResource(id = R.string.tasks_header_image_content_description),
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = stringResource(id = R.string.navigation_view_header_title),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {
    AppTheme {
        Surface {
            AppDrawer(
                currentRoute = TodoDestinations.TaskList(),
                navigateToTasks = {},
                navigateToStatistics = {},
                closeDrawer = {}
            )
        }
    }
}
