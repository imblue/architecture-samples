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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.android.architecture.blueprints.todoapp.R

/**
 * Used with the filter spinner in the tasks list.
 */
enum class TasksFilterType(
    @StringRes val title: Int,
    @StringRes val emptyLabel: Int,
    @DrawableRes val emptyIcon: Int
) {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS(
        R.string.label_all,
        R.string.no_tasks_all,
        R.drawable.logo_no_fill
    ),

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS(
        R.string.label_active,
        R.string.no_tasks_active,
        R.drawable.ic_check_circle_96dp
    ),

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS(
        R.string.label_completed,
        R.string.no_tasks_completed,
        R.drawable.ic_verified_user_96dp
    )
}
