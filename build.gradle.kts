plugins {
    java
    `java-library`
    id("org.springframework.boot") version "2.5.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

allprojects {
    repositories {
        mavenCentral()
    }

    ext {
        set("apacheHttpClientVersion", "4.5.13")
    }
}

configure(subprojects.filter { it.name !in listOf { "shard" } }) {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.2")
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        compileOnly("org.springframework.boot:spring-boot-devtools")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.projectreactor:reactor-test")
    }

    tasks {
        "test"(Test::class) {
            useJUnitPlatform()
        }
    }
}

configure(subprojects.filter { it.parent?.name in listOf{"shard"} }) {
    tasks.bootJar {
        enabled = false
    }
    tasks.jar {
        enabled = false
    }
}

configurations.all {
    // SNAPSHOT 버전의 dependencies는 캐싱하지 않는다.
    resolutionStrategy.cacheChangingModulesFor(1, "seconds")
}
