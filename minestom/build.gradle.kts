plugins {
    id("images.shadow-conventions")
    application
}

dependencies {
    implementation(project(":core"))
    implementation(libs.minestom)
}

application {
    mainClass = "com.andavin.images.minestom.MainKt"
}
