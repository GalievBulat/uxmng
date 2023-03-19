import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id ("application")
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.intellij") version "1.5.2"
    id ("org.openjfx.javafxplugin") version "0.0.10"
    id ("org.beryx.jlink") version "2.24.1"
}

repositories {
    mavenCentral()
}


java {
    sourceCompatibility = JavaVersion.VERSION_11
}
javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

group = "com.bgaliev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    localPath.set("C:\\Program Files\\Android\\Android Studio")
    //version.set("2021.1.2")
    type.set("IC") // Target IDE Platform
    plugins.set(listOf("android", /*"com.intellij.java"*/))
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
dependencies {
    implementation("org.tensorflow:tensorflow-core-platform:0.5.0")

    implementation(platform("io.projectreactor:reactor-bom:2020.0.20"))
    implementation("io.rsocket:rsocket-core:1.1.2")
    implementation("io.rsocket:rsocket-transport-netty:1.1.2")
    implementation("io.rsocket.broker:rsocket-broker-frames:0.3.0")


    implementation ("org.jooq:joor-java-8:0.9.7")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation ("com.google.code.gson:gson:2.8.6")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.3.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")


    testImplementation ("junit:junit:4.12")
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = "11"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}