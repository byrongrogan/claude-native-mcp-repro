plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}

application {
    mainClass.set("io.modelcontextprotocol.sample.server.MainKt")
}

repositories {
    mavenCentral()
}

group = "io.modelcontextprotocol.sample"
version = "1.0.0"

dependencies {
    implementation(libs.mcp.kotlin)
    implementation(libs.slf4j.simple)
}

kotlin {
    jvmToolchain(17)
}