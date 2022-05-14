import com.bmuschko.gradle.tomcat.extension.TomcatPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    id("com.bmuschko.tomcat") version "2.7.0"
    application
}

//apply(plugin = "")
//
//buildscript {
//    repositories {
//        gradlePluginPortal()
//        mavenCentral()
//    }
//    dependencies {
//        val tomcatVersion = "9.0.1"
//        classpath {
//
//        }
//        classpath("com.bmuschko:gradle-tomcat-plugin:2.7.0")
//    }
//}

//tasks.register<Tomcat>("tomcat") {
//
//}

group = "me.yangjiahao"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    val tomcatVersion = "9.0.1"
    tomcat("org.apache.tomcat.embed:tomcat-embed-core:${tomcatVersion}")
    tomcat("org.apache.tomcat.embed:tomcat-embed-logging-juli:9.0.0.M6")
    tomcat("org.apache.tomcat.embed:tomcat-embed-jasper:${tomcatVersion}")
    implementation("com.bmuschko:gradle-tomcat-plugin:2.7.0")
    implementation("org.springframework:spring-context:5.3.19")
    implementation("org.springframework:spring-webmvc:5.3.19")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register<Jar>("uberJar") {
    archiveClassifier.set("uber")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(
            "Main-Class" to "MainKt"
        )
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

//tasks.jar {
//    manifest {
//        attributes(
//            "Main-Class" to "MainKt"
//        )
//        from({
//            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
//        })
//    }
//}

application {
    mainClass.set("MainKt")
}

configure<TomcatPluginExtension> {
    httpProtocol = "org.apache.coyote.http11.Http11Nio2Protocol"
    ajpProtocol = "org.apache.coyote.ajp.AjpNio2Protocol"
}