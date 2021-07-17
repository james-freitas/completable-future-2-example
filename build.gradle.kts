
plugins {
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.springframework.boot") version "2.5.2"
	id("io.gitlab.arturbosch.detekt") version "1.9.1"
	id("java")
	id("jacoco")
	jacoco
}

group = "com.gsuitesafe"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

jacoco {
	toolVersion = "0.8.5"
}

val excludesPaths: Iterable<String> = listOf(
	"com/gsuitesafe/gmailbackup/dto/*",
	"com/gsuitesafe/gmailbackup/configuration/*",
	"com/gsuitesafe/gmailbackup/GmailbackupApplication*",
)


tasks.jacocoTestReport {
	reports {
		xml.isEnabled = false
		csv.isEnabled = false
		html.destination = file("$buildDir/jacocoHtml")
	}
	classDirectories.setFrom(
			sourceSets.main.get().output.asFileTree.matching {
				exclude(excludesPaths)
			}
	)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = 0.95.toBigDecimal()
			}
		}
	}
	classDirectories.setFrom(
			sourceSets.main.get().output.asFileTree.matching {
				exclude(excludesPaths)
			}
	)
	mustRunAfter(tasks["jacocoTestReport"])
}

repositories {
	mavenCentral()
}

dependencies {
	implementation ("org.springframework.boot:spring-boot-starter-web")

	// Swagger
	implementation ("io.springfox:springfox-swagger2:2.9.2")
	implementation ("io.springfox:springfox-swagger-ui:2.9.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

}

springBoot {
	buildInfo()
}