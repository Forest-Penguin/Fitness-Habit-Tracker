plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.example.fitness_habit_tracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fitness_habit_tracker"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room schema configuration
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Gson for JSON serialization
    implementation(libs.gson)

    // Navigation Components
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    implementation("androidx.room:room-common:2.5.2")


    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Coroutines, Recycler View
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.google.android.material:material:1.11.0")


    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("androidx.room:room-testing:2.5.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.runner)

    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.14.0")

    // Local Broadcast Manager
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
}