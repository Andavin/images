plugins {
    id("images.shadow-conventions")
}

images {
    javaRelease = 8
}

java {
    disableAutoTargetJvm()
}

dependencies {
    implementation(project(":core"))
    implementation("org.tinylog:tinylog-api-kotlin:2.8.1")
    implementation("org.tinylog:slf4j-tinylog:2.8.1")
    compileOnly(libs.paper.api)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filesMatching("plugin.yml") {
        expand(props)
    }
}
