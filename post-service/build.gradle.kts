import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin ("jvm") version "1.5.10"
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "solutions.desati.twatter"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.2.1"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "solutions.desati.twatter.post-service.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-validation")
  implementation("io.vertx:vertx-auth-jwt")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-openapi")
  implementation("io.vertx:vertx-service-discovery")
  implementation("io.vertx:vertx-json-schema")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-web-api-contract")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
  implementation("org.jetbrains.exposed:exposed-core:0.36.2")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.36.2")
  implementation("org.jetbrains.exposed:exposed-dao:0.36.2")
  implementation("com.zaxxer:HikariCP:5.0.0")
  implementation("com.impossibl.pgjdbc-ng:pgjdbc-ng:0.8.9")
  implementation("org.jetbrains.exposed:exposed-java-time:0.36.1")

  testImplementation("io.vertx:vertx-unit")
  testImplementation("junit:junit:4.13.1")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnit()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
//  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
  args = listOf("run", mainVerticleName, "--launcher-class=$launcherClassName")
}
