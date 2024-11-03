plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("kapt") version "1.9.25"
	id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
	id("org.jlleitschuh.gradle.ktlint-idea") version "11.5.1"
	id("org.flywaydb.flyway") version "7.7.3"
}

group = "com.cjthemarket"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Json
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Flyway
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")

	// QueryDSL
	implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
	implementation("com.querydsl:querydsl-core:5.1.0")
	kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
	kapt("com.querydsl:querydsl-kotlin-codegen:5.1.0")

	// Redisson
	implementation("org.redisson:redisson:3.37.0")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("io.springfox:springfox-swagger-ui:3.0.0")


	// MySQL
	runtimeOnly("com.mysql:mysql-connector-j")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Mock
	testImplementation("io.mockk:mockk:1.13.4")

	// KoTest
	testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
	testImplementation("io.kotest:kotest-assertions-core:5.5.4")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

ktlint {
	version.set("0.50.0")
	verbose.set(true)
	android.set(false)
	outputToConsole.set(true)
	coloredOutput.set(true)
	ignoreFailures.set(false)

	filter {
		exclude("*.kts")
		exclude("**/generated/**")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

noArg {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

flyway {
	url = "jdbc:mysql://localhost:3306/stock?serverTimezone=UTC&characterEncoding=UTF-8"
	user = "stock_admin"
	password = "stock"
	baselineVersion = "1"
	baselineOnMigrate = true
	schemas = listOf("stock").toTypedArray()
	locations = listOf("filesystem:src/main/resources/db/migration", "filesystem:src/main/resources/db/seed").toTypedArray()
}

task<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrateTestDB") {
	url = "jdbc:mysql://localhost:3306/stock_test?serverTimezone=UTC&characterEncoding=UTF-8"
	user = "stock_admin"
	password = "stock"
	baselineVersion = "1"
	baselineOnMigrate = true
	schemas = listOf("stock_test").toTypedArray()
	locations = listOf("filesystem:src/main/resources/db/migration").toTypedArray()
}

task<org.flywaydb.gradle.task.FlywayCleanTask>("flywayCleanTestDB") {
	url = "jdbc:mysql://localhost:3306/stock_test?serverTimezone=UTC&characterEncoding=UTF-8"
	user = "stock_admin"
	password = "stock"
	baselineVersion = "1"
	baselineOnMigrate = true
	schemas = listOf("stock_test").toTypedArray()
	locations = listOf("filesystem:src/main/resources/db/migration").toTypedArray()
}

tasks.withType<Test> {
	useJUnitPlatform()
}
