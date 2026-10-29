import java.io.File

plugins {
    id("com.android.application") version "8.5.2"
    id("org.jetbrains.kotlin.android") version "1.9.24"
}

val copySrc = tasks.register<Copy>("copySrc") {
    from(layout.projectDirectory) { include("*.kt") }
    into(layout.buildDirectory.dir("flatsrc"))
}

val genRes = tasks.register("genRes") {
    val outDir = layout.buildDirectory.dir("genres")
    outputs.dir(outDir)
    doLast {
        val d = outDir.get().asFile.resolve("drawable")
        d.mkdirs()
        File(d, "ic_launcher.xml").writeText(
            """<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp" android:height="108dp"
    android:viewportWidth="108" android:viewportHeight="108">
    <path android:fillColor="#FF3D5AFE" android:pathData="M0,0h108v108h-108z"/>
    <path android:fillColor="#FF000000" android:pathData="M38,38h32a16,16 0 0 1 0,32h-32a16,16 0 0 1 0,-32z"/>
    <path android:fillColor="#FF4CAF50" android:pathData="M36,54a5,5 0 1 0 10,0a5,5 0 1 0 -10,0z"/>
    <path android:fillColor="#FFFFFFFF" android:pathData="M52,51h16v6h-16z"/>
</vector>"""
        )
    }
}

android {
    namespace = "com.example.island"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.island"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDir(copySrc)
            res.srcDir(genRes)
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
