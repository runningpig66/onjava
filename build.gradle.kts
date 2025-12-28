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
    // JMH Core
    testImplementation("org.openjdk.jmh:jmh-core:1.37")
    // JMH Generators: Annotation Processors
    testAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
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
    "**/ch20_generics/Manipulation.java",
)

sourceSets {
    named("main") {
        java {
            exclude(willNotCompile)
        }
    }
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs(
        "-Dsun.stdout.encoding=UTF-8",
        "-Dsun.stderr.encoding=UTF-8",
        "-Dfile.encoding=UTF-8"
    )
}
