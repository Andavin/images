plugins {
    id("images.kotlin-conventions")
}

images {
    javaRelease = 8
}

dependencies {
    implementation("org.tinylog:tinylog-api-kotlin:2.8.1")
    testImplementation(kotlin("test"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.params)
}

tasks.test {
    useJUnitPlatform()
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = images.javaRelease.map { JavaLanguageVersion.of(it) }
    }
}
