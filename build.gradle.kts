plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    `java`
    `maven-publish`
}

group = "com.agentdid127"
version = project.version

allprojects {
    apply(plugin = "java")

java { toolchain { languageVersion.set(JavaLanguageVersion.of(8)) } }

repositories {
    mavenLocal()
    mavenCentral()
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
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}