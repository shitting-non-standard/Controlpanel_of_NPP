plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("npp.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    val springVersion = "6.1.5"

    implementation("org.springframework:spring-core:${springVersion}")
    implementation("org.springframework:spring-beans:${springVersion}")
    implementation("org.springframework:spring-context-support:${springVersion}")
    implementation("org.springframework:spring-aop:${springVersion}")

    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")

    implementation("org.aspectj:aspectjrt:1.9.22")
    implementation("org.aspectj:aspectjweaver:1.9.22")
}

tasks.compileJava {
    options.encoding = "UTF-8"
}


tasks.named<JavaExec>("run") {
    jvmArgs("-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8")
}
