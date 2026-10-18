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
    compileOnly(libs.paper.api)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filesMatching("plugin.yml") {
        expand(props)
    }
}
