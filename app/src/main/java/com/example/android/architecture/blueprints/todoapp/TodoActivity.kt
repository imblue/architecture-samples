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

package com.example.android.architecture.blueprints.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.view.WindowCompat
import com.example.android.architecture.blueprints.todoapp.ui.TodoNavGraph
import com.example.android.architecture.blueprints.todoapp.ui.AppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activities are entry points into the App, and
 * have to be referenced in the AndroidManifest.xml file.
 *
 * Activities are either created using intent filters
 * (e.g. as Main/Launcher Activity, or via Deep-Links)
 * or within the App.
 *
 * Compose Apps usually follow a "Single Activity" Approach.
 */
@AndroidEntryPoint
class TodoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Color status bar analog to app theme
        enableEdgeToEdge()

        // Allows easier handling of insets if components
        // are aligned at the bottom - e.g. FloatingActionButton
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            AppTheme {
                TodoNavGraph()
            }
        }
    }
}

/**
 * Previews can and should be used to showcase components
 * without having to run the App.
 *
 * This helps during development (implemented screens can
 * be checked instantly), and during maintenance (changes
 * can be seen instantly).
 *
 * [Preview] can be used for customized previews (screen size, background colors, theming).
 * [PreviewLightDark] provides a default light and dark theme preview.
 *
 * Previews can be stacked using the [Preview] annotation.
 *
 * @see https://developer.android.com/develop/ui/compose/tooling/previews
 */
@Composable
@PreviewLightDark
private fun Playground_Preview() {
    AppTheme {
        Scaffold { padding ->
            Column(Modifier.padding(padding)) {
                // Note:
                // Themed values like colors or typography should never be
                // accessed directly, always use the MaterialTheme instead.
                // Otherwise, changes in the Theme might not be applied here,
                // or unexpected behaviour with light/dark mode might occur (e.g. contrast issues).
                Text(
                    "Hello World!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}
