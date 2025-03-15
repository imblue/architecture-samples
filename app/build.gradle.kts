/*
 * Copyright 2020 The Android Open Source Project
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

/*
 * build.gradle is the main configuration file for compiling
 * info and dependency management.
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    // Namespace is used for the AndroidManifest.xml
    namespace = "com.example.android.architecture.blueprints.todoapp"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.android.architecture.blueprints.main"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        // Version-Code will be used for the App Store, and checked by
        // the OS during installation and updates (e.g. you can not install an
        // App with version 2 if an App with version code 3 is already installed).
        versionCode = 1
        // Version-Name is a string representation, which can be shown to users.
        versionName = "1.0"

        // Necessary to setup the database library "room"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += "room.incremental" to "true"
            }
        }
    }

    // build types can be used to create different versions of your App.
    // e.g. having a debuggable version available for development,
    // and a release version with minification and other optimizations
    // for playstore uploads.
    buildTypes {
        // "debug" and "release" are added by default
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            // Adapt the application ID to be able to install both versions simultaneously
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        // Additional build types can be added using `create("myBuildType")`
    }

    flavorDimensions += "mode"

    // Additional flavors can be added depending on the projects needs.
    // e.g. having a mock/demo version with a faked backend implementation,
    // ready to be used if the backend is not available.
    productFlavors {
        create("demo") {
            dimension = "mode"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
        }
        create("prod") {
            dimension = "mode"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        excludes += "META-INF/AL2.0"
        excludes += "META-INF/LGPL2.1"
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
            freeCompilerArgs += "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
            freeCompilerArgs += "-opt-in=kotlin.Experimental"
        }
    }
}

/*
 Dependency versions are defined in the top level build.gradle file. This helps keeping track of
 all versions in a single place. This improves readability and helps managing project complexity.
 */
dependencies {

    // App dependencies
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)

    // Architecture Components
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    // Hilt
    implementation(libs.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.core)

    // Jetpack Compose
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(libs.androidx.activity.compose)
    implementation(composeBom)
    implementation(libs.androidx.compose.foundation.core)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.accompanist.appcompat.theme)

    debugImplementation(composeBom)
    debugImplementation(libs.androidx.compose.ui.tooling.core)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
