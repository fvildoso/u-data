import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "me.pipe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.1.2")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-nop
    implementation("org.slf4j:slf4j-nop:1.7.36")

    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

    // https://mvnrepository.com/artifact/io.github.evanrupert/excelkt
    implementation("io.github.evanrupert:excelkt:1.0.2")

}



tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}