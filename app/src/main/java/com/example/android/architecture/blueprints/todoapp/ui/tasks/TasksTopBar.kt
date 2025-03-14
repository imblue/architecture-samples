package com.example.android.architecture.blueprints.todoapp.ui.tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ui.AppTheme

@Composable
fun TasksTopAppBar(
    openDrawer: () -> Unit,
    onFilterSelected: (TasksFilterType) -> Unit,
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Filled.Menu, stringResource(id = R.string.open_drawer))
            }
        },
        actions = {
            FilterTasksMenu(onFilterSelected)
            MoreTasksMenu(onClearCompletedTasks, onRefresh)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FilterTasksMenu(
    onFilterSelected: (TasksFilterType) -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painterResource(id = R.drawable.ic_filter_list),
                stringResource(id = R.string.menu_filter)
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(onClick = { onFilterSelected(TasksFilterType.ALL_TASKS); closeMenu() },
            text = { Text(text = stringResource(id = R.string.nav_all)) }
        )
        DropdownMenuItem(onClick = { onFilterSelected(TasksFilterType.ACTIVE_TASKS); closeMenu() },
            text = { Text(text = stringResource(id = R.string.nav_active)) }
        )
        DropdownMenuItem(onClick = { onFilterSelected(TasksFilterType.COMPLETED_TASKS); closeMenu() },
            text = { Text(text = stringResource(id = R.string.nav_completed)) }
        )
    }
}

@Composable
private fun MoreTasksMenu(
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(Icons.Filled.MoreVert, stringResource(id = R.string.menu_more))
        }
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.menu_clear)) },
            onClick = { onClearCompletedTasks(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.refresh)) },
            onClick = { onRefresh(); closeMenu() }
        )
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}

@Preview
@Composable
private fun TasksTopAppBarPreview() {
    AppTheme {
        Surface {
            TasksTopAppBar({}, {}, {}, {})
        }
    }
}
