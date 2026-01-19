plugins {
    java
    groovy
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

repositories {
    mavenCentral()
}
dependencies {
    implementation(libs.bundles.implementation)
    implementation("org.springframework.boot:spring-boot-starter-mail")
    testImplementation(libs.bundles.testImplementation)
    annotationProcessor(libs.bundles.annotationProcessor)
    compileOnly(libs.bundles.compileOnly)
    runtimeOnly(libs.bundles.runTimeOnly)
}

sourceSets {
    getByName("test") {
        java.srcDir("app/src/test/java")
        groovy.srcDir("app/src/test/groovy")
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}