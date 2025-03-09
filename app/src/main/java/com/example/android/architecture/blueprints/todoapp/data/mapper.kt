/*
 * Copyright 2023 The Android Open Source Project
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

package com.example.android.architecture.blueprints.todoapp.data

import com.example.android.architecture.blueprints.todoapp.data.source.local.TaskDB
import com.example.android.architecture.blueprints.todoapp.data.source.network.TaskNetwork
import com.example.android.architecture.blueprints.todoapp.data.source.network.TaskStatus

fun Task.toDB() = TaskDB(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
)

fun TaskDB.toModel() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
)

fun List<TaskDB>.toModelList() = map(TaskDB::toModel)

fun TaskNetwork.toDB() = TaskDB(
    id = id,
    title = title,
    description = shortDescription,
    isCompleted = (status == TaskStatus.COMPLETE),
)

fun List<TaskNetwork>.toDBList() = map(TaskNetwork::toDB)

// Local to Network
fun TaskDB.toNetwork() = TaskNetwork(
    id = id,
    title = title,
    shortDescription = description,
    status = if (isCompleted) { TaskStatus.COMPLETE } else { TaskStatus.ACTIVE }
)

fun List<TaskDB>.toNetworkList() = map(TaskDB::toNetwork)
