plugins {
    id("io.micronaut.library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.allopen")
}

val micronautVersion=project.properties.get("micronautVersion")

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("micronaut.camunda.bpm.feature.test.*")
    }
}

dependencies {
    testImplementation(project(":micronaut-camunda-bpm-feature"))
    testImplementation(project(":micronaut-camunda-bpm-feature", "testArtifacts"))
    testRuntimeOnly("com.h2database:h2")

    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest("io.micronaut.data:micronaut-data-processor")
    kaptTest("io.micronaut:micronaut-inject-java:$micronautVersion")

    // Integration of Transaction Management
    testAnnotationProcessor("io.micronaut.data:micronaut-data-processor")
    testImplementation("io.micronaut.data:micronaut-data-jdbc")
}

java {
    sourceCompatibility = JavaVersion.toVersion("1.8")
}

tasks {
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}