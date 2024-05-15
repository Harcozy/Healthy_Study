// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    // Define the compose_version as an extra property
    val composeVersion by extra("1.0.5")

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    }
}



plugins {
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
}