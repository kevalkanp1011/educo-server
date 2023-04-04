val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koinVersion: String by project
val kmongoVersion: String by project
val AWS_VERSION: String by project

plugins {
    application
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
}



group = "live.kevalkanpariya"
version = "0.0.1"
application {

    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks {
    create("stage").dependsOn("installDist")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-resources:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-server-call-logging:$ktor_version")

    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")


    // Auth
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")

    // Content Negotiation
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")

    // KMongo
    implementation("org.litote.kmongo:kmongo-async:$kmongoVersion")
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:$kmongoVersion")

    // Koin core features
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")


    //mongoDB JAVA ReactiveStream Driver
    implementation("org.mongodb:mongodb-driver-reactivestreams:4.8.0")
    


    // AWS
    implementation(platform("software.amazon.awssdk:bom:$AWS_VERSION"))
    implementation("software.amazon.awssdk:s3")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")


    // Koin
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    // Ktor Test
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    // Kotlin Test
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    // Truth
    testImplementation("com.google.truth:truth:1.1.3")



}

