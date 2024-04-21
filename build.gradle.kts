import com.google.protobuf.gradle.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.5"
    id("io.micronaut.aot") version "4.3.5"
    id("com.google.protobuf") version "0.9.2"
}

version = "0.1"
group = "org.veegres"

val kotlinVersion=project.properties.get("kotlinVersion")
graalvmNative.toolchainDetection.set(false)

repositories {
    mavenCentral()
}

dependencies {
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.openapi:micronaut-openapi")
    ksp("io.micronaut.serde:micronaut-serde-processor")

    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.sql:micronaut-jdbi")
    implementation("io.micronaut.flyway:micronaut-flyway")
    runtimeOnly("com.h2database:h2")

    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.grpc:grpc-netty-shaded")
    implementation("io.grpc:grpc-stub")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.micronaut.grpc:micronaut-protobuff-support")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.telegram:telegrambots-longpolling:7.2.1")
    implementation("org.telegram:telegrambots-client:7.2.1")
}

application {
    mainClass.set("org.veegres.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}


sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.61.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("org.veegres.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

tasks.named<io.micronaut.gradle.docker.MicronautDockerfile>("dockerfile") {
    baseImage("eclipse-temurin:21-jre-jammy")
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion.set("21")
}