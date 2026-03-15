package com.example.android.architecture.blueprints.todoapp.data.source.network

import com.example.android.architecture.blueprints.todoapp.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.prepareGet
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Inject
import kotlinx.serialization.json.Json

/**
 * Sample implementation for a "prod" remote data source using KTOR.
 * Note: the server URL does not exist and therefor does not work.
 * This is just a demonstration on how KTOR could be used for API requests.
 */
class NetworkDataSourceImpl @Inject constructor() : NetworkDataSource {
    private val httpClient = HttpClient {
        install(ContentNegotiation) { json(Json) }

        defaultRequest {
            url {
                host = BuildConfig.SERVER_URL
            }
        }
        expectSuccess = true
    }

    override suspend fun loadTasks(): List<TaskNetwork> {
        return httpClient.prepareGet {
            contentType(ContentType.Application.Json)
        }.execute().body()
    }

    override suspend fun saveTasks(tasks: List<TaskNetwork>) {
        httpClient.preparePost {
            contentType(ContentType.Application.Json)
            setBody(tasks)
        }
    }
}
