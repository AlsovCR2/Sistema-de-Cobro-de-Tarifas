import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.jetbrains.kotlin.jvm") version "1.8.0"
	id("org.jetbrains.kotlin.plugin.spring") version "1.8.0"
}

group = "com.capsulecorporation"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	testImplementation("org.jetbrains.kotlin:kotlin-test") // Agregado para soporte de pruebas en Kotlin
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "17"
		freeCompilerArgs = listOf("-Xjsr305=strict")
	}
}

tasks.named<JavaExec>("bootRun") {
	environment("spring.amqp.deserialization.trust.all", "true")
}


