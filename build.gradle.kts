plugins {
    java
    `maven-publish`
}

group = "scm.notification.services"
version = if (project.hasProperty("version")) {
    project.property("version") as String
} else {
    System.getenv("VERSION") ?: "0.0.1"
}


java {
    withSourcesJar()
    withJavadocJar()
}

tasks.jar {
    enabled = true
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
             from(components["java"])
            groupId = "scm.notification.services"
            artifactId = "scm-notification-service"
            version = project.version.toString()
        }
    }
}
