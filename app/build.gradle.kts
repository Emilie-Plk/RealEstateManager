plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1"
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.emplk.realestatemanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.emplk.realestatemanager"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.DelicateCoroutinesApi",
            "-opt-in=kotlin.ExperimentalStdlibApi"
        )
    }


    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true

        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("junit:junit:4.13.2")
    // mockk
    api("io.mockk:mockk:1.13.4")
    // coroutines tests
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("com.google.android.gms:play-services-awareness:19.0.1")


    // GOOGLE MAPS SDK
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:android-maps-utils:2.3.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")

    // DESUGARING
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // FLEXBOX
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // GLIDE
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    // CarouselRecyclerView
    implementation("com.github.sparrow007:carouselrecyclerview:1.2.6")

    // HILT
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-compiler:2.46.1")

    // ROOM
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    // RETROFIT
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // GSON
    implementation("com.google.code.gson:gson:2.10")

    // WORK
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.hilt:hilt-compiler:1.0.0")

    // DATASTORE
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Unit tests

    testImplementation("androidx.arch.core:core-testing:2.2.0") {
        exclude("org.mockito", "mockito-core") // excludes redundant mockito dependency bundled with arch core
    }
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")  // weird but testImplementation doesn't work
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.slf4j:slf4j-nop:2.0.9") // to avoid warning about missing logger
    testImplementation("app.cash.turbine:turbine:1.0.0")


    // Android tests

    androidTestImplementation("androidx.arch.core:core-testing:2.2.0") {
        exclude("org.mockito", "mockito-core") // excludes redundant mockito dependency bundled with arch core
    }
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
}