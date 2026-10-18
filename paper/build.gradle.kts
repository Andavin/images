plugins {
    id("images.shadow-conventions")
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
