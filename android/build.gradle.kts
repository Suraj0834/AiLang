// Top-level build file for AiLang Android SDK

plugins {
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.dokka") version "1.9.10" apply false
    id("com.vanniktech.maven.publish") version "0.27.0" apply false
}

allprojects {
    group = property("GROUP").toString()
    version = property("VERSION_NAME").toString()
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
