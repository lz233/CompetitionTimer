import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
plugins {
    java
    kotlin("jvm") version "1.4.20"
    id("org.jetbrains.compose") version "0.2.0-build131"
    //id("org.jetbrains.compose.desktop.application")
}

group = "cn.cn.lz233.competitiontimer"
version = "1.0"

repositories {
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenCentral()
}

dependencies {
    //implementation("in.xiandan","count-down-timer","1.0.3")
    //implementation ("org.jetbrains.kotlinx","kotlinx-serialization-runtime-common","0.20.0")
    implementation(compose.desktop.currentOs)
    //implementation(compose.desktop.currentOs)
    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            javaHome = "C:\\Users\\24843\\.jdks\\openjdk-15.0.1"
            //outputBaseDir.set(project.buildDir.resolve("%USERPROFILE%\\desktop"))
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
        }
    }
}