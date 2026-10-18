plugins {
    id("images.kotlin-conventions")
    id("com.gradleup.shadow")
}

tasks.shadowJar {
    filesMatching("META-INF/*.kotlin_module") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}
