import java.io.ByteArrayOutputStream

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.agentdid127.resourcepack"
description = "Console"

dependencies {
    implementation(project(":Forwards"))
    implementation(project(":Library"))
}

val gitHash: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-parse", "--short=7", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
}


val date: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "show", "-s", "--format=%ci", gitHash)
        standardOutput = stdout
    }
    stdout.toString().trim()
}


val gitBranch: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
}

tasks.shadowJar {
        archiveVersion.set("")
        archiveClassifier.set("")
        archiveBaseName.set("ResourcePackConverter-Console-${project.version}")
        mergeServiceFiles()
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }

tasks.jar {
    archiveVersion.set("")
    archiveBaseName.set("original")
    archiveClassifier.set("ResourcePackConverter-Console-${project.version}")

    manifest {
         attributes(
            "Main-Class" to "com.agentdid127.resourcepack.Main",
            "Implementation-Title" to "Console",
            "Implementation-Version" to "git-ResourcePackConverterConsole-\"$gitHash\"",
            "Implementation-Vendor" to "$date",
            "Specification-Title" to "ResourcePackConverter",
            "Specification-Version" to "${project.version}",
            "Specification-Vendor" to "HypixelDev",
            "Multi-Release" to "true",
            "Git-Branch" to "$gitBranch",
            "Git-Commit" to "$gitHash"
        )
    }
}
tasks {
    build {
        dependsOn(shadowJar)
    }
}
