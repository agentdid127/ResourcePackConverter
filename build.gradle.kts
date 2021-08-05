plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    `java-library`
    `maven-publish`
}

group = "com.agentdid127"
version = project.version

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

java { toolchain { languageVersion.set(JavaLanguageVersion.of(8)) } }

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    implementation("net.lingala.zip4j:zip4j:2.9.0")
}

    tasks.withType<JavaCompile>().configureEach {
        options.isIncremental = true
        options.encoding = Charsets.UTF_8.name()
    }

    tasks.withType<Javadoc>().configureEach {
        options.encoding = Charsets.UTF_8.name()
    }

    tasks.withType<ProcessResources>().configureEach {
        filteringCharset = Charsets.UTF_8.name()
    }

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        }
    }
}