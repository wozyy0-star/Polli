plugins {
    id("com.android.application") version "8.5.2"
    id("org.jetbrains.kotlin.android") version "1.9.24"
}

val copySrc = tasks.register<Copy>("copySrc") {
    from(layout.projectDirectory) { include("*.kt") }
    into(layout.buildDirectory.dir("flatsrc"))
}

android {
    namespace = "com.example.island"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.island"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDir(copySrc)
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
}

dependencies {
    val bom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(bom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.3")
    implementation("androidx.savedstate:savedstate-ktx:1.2.1")
}
