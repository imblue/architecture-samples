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

package com.example.android.architecture.blueprints.todoapp.data

import com.example.android.architecture.blueprints.todoapp.data.source.local.TaskDao
import com.example.android.architecture.blueprints.todoapp.data.source.network.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of [TaskRepository]. Single entry point for managing tasks' data.
 *
 * @param networkDataSource - The network data source
 * @param localDataSource - The local data source
 */
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: TaskDao,
) : TaskRepository {

    override fun getTasksStream(): Flow<List<Task>> {
        return localDataSource.observeAll()
            .map { tasks -> tasks.toModelList() }
    }

    override fun getTaskStream(taskId: String): Flow<Task?> {
        return localDataSource.observeById(taskId)
            .map { it?.toModel() }
    }

    override suspend fun createTask(title: String, description: String): String {
        val taskId = UUID.randomUUID().toString()
        val task = Task(
            title = title,
            description = description,
            id = taskId,
        )
        localDataSource.insertOrReplace(task.toDB())
        saveTasksToNetwork()
        return taskId
    }

    override suspend fun updateTask(taskId: String, title: String, description: String) {
        val task = getTask(taskId)?.copy(
            title = title,
            description = description
        ) ?: throw Exception("Task (id $taskId) not found")

        localDataSource.insertOrReplace(task.toDB())
        saveTasksToNetwork()
    }

    override suspend fun refreshTask(taskId: String) {
        refresh()
    }

    /**
     * Get a Task with the given ID. Will return null if the task cannot be found.
     *
     * @param taskId - The ID of the task
     * @param forceUpdate - true if the task should be updated from the network data source first.
     */
    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Task? {
        if (forceUpdate) {
            refresh()
        }
        return localDataSource.getById(taskId)?.toModel()
    }

    override suspend fun completeTask(taskId: String) {
        localDataSource.updateCompleted(taskId = taskId, completed = true)
        saveTasksToNetwork()
    }

    override suspend fun activateTask(taskId: String) {
        localDataSource.updateCompleted(taskId = taskId, completed = false)
        saveTasksToNetwork()
    }

    override suspend fun clearCompletedTasks() {
        localDataSource.deleteCompleted()
        saveTasksToNetwork()
    }

    override suspend fun deleteTask(taskId: String) {
        localDataSource.deleteById(taskId)
        saveTasksToNetwork()
    }

    /**
     * The following methods load tasks from (refresh), and save tasks to, the network.
     *
     * Real apps may want to do a proper sync, rather than the "one-way sync everything" approach
     * below. See https://developer.android.com/topic/architecture/data-layer/offline-first
     * for more efficient and robust synchronisation strategies.
     *
     * Note that the refresh operation is a suspend function (forces callers to wait) and the save
     * operation is not. It returns immediately so callers don't have to wait.
     */

    /**
     * Delete everything in the local data source and replace it with everything from the network
     * data source.
     *
     * `withContext` is used here in case the bulk `toLocal` mapping operation is complex.
     */
    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val remoteTasks = networkDataSource.loadTasks()
            localDataSource.deleteAll()
            localDataSource.insertOrReplaceAll(remoteTasks.toDBList())
        }
    }

    /**
     * Send the tasks from the local data source to the network data source
     *
     * Returns immediately after launching the job. Real apps may want to suspend here until the
     * operation is complete or (better) use WorkManager to schedule this work. Both approaches
     * should provide a mechanism for failures to be communicated back to the user so that
     * they are aware that their data isn't being backed up.
     */
    private suspend fun saveTasksToNetwork() {
        val localTasks = localDataSource.observeAll().first()
        withContext(Dispatchers.IO) {
            val networkTasks = localTasks.toNetworkList()
            networkDataSource.saveTasks(networkTasks)
        }
    }
}
