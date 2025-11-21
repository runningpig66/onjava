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
    // Guava: Google Core Libraries For Java
    implementation("com.google.guava:guava:33.5.0-jre")
    // Logback Classic Module
    implementation("ch.qos.logback:logback-classic:1.5.21")
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
    "**/ch13_functional/Closure9.java",
    "**/ch15_exceptions/TryAnything.java",
    "**/ch15_exceptions/Human.java", // 异常捕获报错，临时排除
)

sourceSets {
    named("main") {
        java {
            exclude(willNotCompile)
        }
    }
}
