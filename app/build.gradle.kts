import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("jacoco")
}

android {
    namespace = "com.dshovhenia.mvvm.compose.template"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dshovhenia.mvvm.compose.template"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_BASE_URL", "\"https://api.coingecko.com/api/v3/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeVersion = "1.3.0"
    val hiltVersion = "2.50"
    val coreKtxVersion = "1.9.0"
    val junitVersion = "4.13.2"
    val androidxJunitVersion = "1.1.3"
    val espressoCoreVersion = "3.4.0"
    val lifecycleKtxVersion = "2.5.1"
    val mockkVersion = "1.12.3"
    val coroutinesTestVersion = "1.6.0"
    val archCoreTestingVersion = "2.1.0"
    val retrofitVersion = "2.9.0"
    val timberVersion = "5.0.1"
    val coroutinesVersion = "1.6.4"
    val desugarVersion = "1.1.5"

    // Flow
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarVersion")

    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleKtxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:$timberVersion")

    // Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-util:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleKtxVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("dev.olshevski.navigation:reimagined-hilt:1.5.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1") {
        exclude(group = "androidx.test")
    }

    // Chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")

    // Test
    testImplementation("junit:junit:$junitVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")
    testImplementation("androidx.arch.core:core-testing:$archCoreTestingVersion")
    testImplementation("org.amshove.kluent:kluent-android:1.68")
    testImplementation("app.cash.turbine:turbine:0.12.0") // For Flow testing
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}

// DETEKT

detekt {
    config.setFrom(project.file("$rootDir/config/detekt/config.yml"))
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    basePath = projectDir.absolutePath
}

// KTLINT

ktlint {
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.PLAIN)
    }
    filter {
        exclude("**/generated/**")
        include("**/java/**")
        include("**/kotlin/**")
        include("**/test/**")
    }
}

// RUN ALL CHECKS

val runAllChecks by tasks.registering {
    dependsOn("detekt")
    dependsOn("ktlintCheck")
    dependsOn("test")
    dependsOn("lint")

    description = "Runs tests, detekt, ktlint and lint checks as single task"
    group = "Verification"
}

// JACOCO

jacoco {
    toolVersion = "0.8.7"
}

val coverageExcludes =
    listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/App*.*",
        "**/*Activity*.*",
        "**/*Fragment*.*",
        "jdk.internal.*",
    )

tasks {
    create<JacocoReport>("jacocoDevDebugTestReport") {
        dependsOn("testDevDebugUnitTest")

        reports {
            html.required.set(true)
            xml.required.set(true)
        }

        classDirectories.setFrom(
            project.fileTree("$buildDir/tmp/kotlin-classes/devDebug") {
                exclude(coverageExcludes)
            },
        )
        executionData.setFrom(files("$buildDir/jacoco/testDevDebugUnitTest.exec"))
    }
}

tasks {
    create<JacocoCoverageVerification>("jacocoDevDebugCoverageVerification") {
        group = "verification"
        description = "Checks for the minimum code coverage level for devDebug variant"

        // Run `jacocoTestReport` before so we will have nice html and xml reports too
        dependsOn("jacocoDevDebugTestReport")
        getByName("check").dependsOn(this)

        violationRules {
            // Please set to true on a real project
            isFailOnViolation = false
            rule {
                limit {
                    minimum = 0.8.toBigDecimal()
                }
            }
        }

        classDirectories.setFrom(
            project.fileTree("$buildDir/tmp/kotlin-classes/devDebug") {
                exclude(coverageExcludes)
            },
        )
        executionData.setFrom(files("$buildDir/jacoco/testDevDebugUnitTest.exec"))
    }
}
