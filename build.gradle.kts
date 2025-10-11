plugins {
    id("java")
    kotlin("jvm")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

// 跳过编译的文件
val willNotCompile = listOf(
    "**/ch13_functional/Closure3.java",
    "**/ch13_functional/Closure5.java",
    "**/ch13_functional/Closure7.java",
)

sourceSets {
    named("main") {
        java {
            exclude(willNotCompile)
        }
    }
}
