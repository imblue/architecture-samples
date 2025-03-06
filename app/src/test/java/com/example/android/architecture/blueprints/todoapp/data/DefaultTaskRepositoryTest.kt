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

import com.example.android.architecture.blueprints.todoapp.data.source.local.FakeTaskDao
import com.example.android.architecture.blueprints.todoapp.data.source.network.FakeNetworkDataSource
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
@ExperimentalCoroutinesApi
class DefaultTaskRepositoryTest {

    private val task1 = Task(id = "1", title = "Title1", description = "Description1")
    private val task2 = Task(id = "2", title = "Title2", description = "Description2")
    private val task3 = Task(id = "3", title = "Title3", description = "Description3")

    private val newTaskTitle = "Title new"
    private val newTaskDescription = "Description new"
    private val newTask = Task(id = "new", title = newTaskTitle, description = newTaskDescription)
    private val newTasks = listOf(newTask)

    private val networkTasks = listOf(task1, task2).toNetwork()
    private val localTasks = listOf(task3.toLocal())

    // Test dependencies
    private lateinit var networkDataSource: FakeNetworkDataSource
    private lateinit var localDataSource: FakeTaskDao

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    // Class under test
    private lateinit var taskRepository: TaskRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        networkDataSource = FakeNetworkDataSource(networkTasks.toMutableList())
        localDataSource = FakeTaskDao(localTasks)
        // Get a reference to the class under test
        taskRepository = TaskRepositoryImpl(
            networkDataSource = networkDataSource,
            localDataSource = localDataSource,
            dispatcher = testDispatcher,
            dispatcherIO = testDispatcher,
        )
    }

    @Test
    fun saveTask_savesToLocalAndRemote() = testScope.runTest {
        // When a task is saved to the tasks repository
        val newTaskId = taskRepository.createTask(newTask.title, newTask.description)

        // Then the remote and local sources contain the new task
        assertThat(networkDataSource.tasks?.map { it.id }?.contains(newTaskId)).isTrue()
        assertThat(localDataSource.tasks?.map { it.id }?.contains(newTaskId)).isTrue()
    }

    @Test
    fun completeTask_completesTaskToServiceAPIUpdatesCache() = testScope.runTest {
        // Save a task
        val newTaskId = taskRepository.createTask(newTask.title, newTask.description)

        // Make sure it's active
        assertThat(taskRepository.getTask(newTaskId)?.isCompleted).isFalse()

        // Mark is as complete
        taskRepository.completeTask(newTaskId)

        // Verify it's now completed
        assertThat(taskRepository.getTask(newTaskId)?.isCompleted).isTrue()
    }

    @Test
    fun completeTask_activeTaskToServiceAPIUpdatesCache() = testScope.runTest {
        // Save a task
        val newTaskId = taskRepository.createTask(newTask.title, newTask.description)
        taskRepository.completeTask(newTaskId)

        // Make sure it's completed
        assertThat(taskRepository.getTask(newTaskId)?.isActive).isFalse()

        // Mark is as active
        taskRepository.activateTask(newTaskId)

        // Verify it's now activated
        assertThat(taskRepository.getTask(newTaskId)?.isActive).isTrue()
    }

    @Test
    fun getTask_repositoryCachesAfterFirstApiCall() = testScope.runTest {
        // Obtain a task from the local data source
        localDataSource = FakeTaskDao(mutableListOf(task1.toLocal()))
        val initial = taskRepository.getTask(task1.id)

        // Change the tasks on the remote
        networkDataSource.tasks = newTasks.toNetwork().toMutableList()

        // Obtain the same task again
        val second = taskRepository.getTask(task1.id)

        // Initial and second tasks should match because we didn't force a refresh
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun getTask_forceRefresh() = testScope.runTest {
        // Trigger the repository to load data, which loads from remote and caches
        networkDataSource.tasks = mutableListOf(task1.toNetwork())
        val task1FirstTime = taskRepository.getTask(task1.id, forceUpdate = true)
        assertThat(task1FirstTime?.id).isEqualTo(task1.id)

        // Configure the remote data source to return a different task
        networkDataSource.tasks = mutableListOf(task2.toNetwork())

        // Force refresh
        val task1SecondTime = taskRepository.getTask(task1.id, true)
        val task2SecondTime = taskRepository.getTask(task2.id, true)

        // Only task2 works because task1 does not exist on the remote
        assertThat(task1SecondTime).isNull()
        assertThat(task2SecondTime?.id).isEqualTo(task2.id)
    }
}
