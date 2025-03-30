plugins {
    kotlin("jvm") version "2.1.20"
    id("io.ktor.plugin") version "3.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
    id("com.diffplug.spotless") version "7.0.2"
    id("ch.acanda.gradle.fabrikt") version "1.13.0"
}

group = "todolist"

version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sourceSets {
    main {
        kotlin {
            srcDir("build/generated/sources/fabrikt/src/main/kotlin")
        }
    }
}

repositories { mavenCentral() }

dependencies {
    implementation("io.insert-koin:koin-ktor3:4.1.0-Beta5")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("org.hibernate.validator:hibernate-validator:8.0.2.Final")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.20")
}

spotless {
    kotlin {
        ktfmt("0.53").kotlinlangStyle().configure {}
    }
}

fabrikt {
    // Fabrikt generates Kotlin code from your OpenAPI specification
    // https://github.com/cjbooms/fabrikt https://github.com/acanda/fabrikt-gradle-plugin
    generate("todos") {
        apiFile = file("openapi.yaml")
        basePackage = "todolist.api"
        controller {
            generate = enabled
            target = Ktor
        }
        model { serializationLibrary = Kotlin }
    }
}
