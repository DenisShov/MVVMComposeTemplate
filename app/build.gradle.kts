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
    // Flow
    implementation(libs.kotlinCoroutinesCoreLib)

    // Hilt
    implementation(libs.hiltAndroidLib)
    ksp(libs.hiltAndroidCompilerLib)

    coreLibraryDesugaring(libs.tooldDesugarJdkLib)

    implementation(libs.coreKtxLib)
    implementation(libs.appcompatLib)
    implementation(libs.lifecycleLivedataKtxLib)
    implementation(libs.lifecycleViewmodelKtxLib)
    implementation(libs.kotlinxCoroutinesAndroidLib)
    implementation(libs.kotlinCoroutinesCoreLib)

    // Timber
    implementation(libs.timberLib)

    // Compose
    implementation(libs.composeUiLib)
    implementation(libs.composeUiUtilLib)
    implementation(libs.composeUiToolingPreviewLib)
    implementation(libs.composeFoundationLib)
    implementation(libs.composeMaterial3Lib)
    implementation(libs.composeMaterialIconsExtendedLib)
    implementation(libs.lifecycleRuntimeKtxLib)
    implementation(libs.lifecycleViewmodelComposeLib)
    implementation(libs.composeRuntimeLivedataLib)
    implementation(libs.composeActivityLib)
    implementation(libs.navigationHiltLib)
    implementation(libs.coilComposeLib)

    // Chart
    implementation(libs.mpAndroidChartLib)

    // Retrofit
    implementation(libs.retrofitLib)
    implementation(libs.converterGsonLib)
    implementation(libs.loggingInterceptorLib)

    detektPlugins(libs.detektFormattingLib)

    // Test
    testImplementation(libs.junitLib)
    testImplementation(libs.mockkLib)
    testImplementation(libs.kotlinxCoroutinesTestLib)
    testImplementation(libs.coreTestingLib)
    testImplementation(libs.kluentAndroidLib)
    testImplementation(libs.turbineLib) // For Flow testing
    androidTestImplementation(libs.testExtLib)
    androidTestImplementation(libs.espressoCoreLib)
    androidTestImplementation(libs.uiTestJunit4Lib)
    debugImplementation(libs.uiToolkitLib)
    debugImplementation(libs.uiTestManifestLib)
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
