import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
}

group = "com.andavin"
version = "3.0.0-SNAPSHOT"

val images = extensions.create<ImagesExtension>("images")
images.javaRelease.convention(25)

base {
    archivesName = "images-${project.name}"
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        jvmTarget = images.javaRelease.map { JvmTarget.fromTarget(if (it == 8) "1.8" else "$it") }
        freeCompilerArgs.add(images.javaRelease.map { "-Xjdk-release=$it" })
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = images.javaRelease
}
