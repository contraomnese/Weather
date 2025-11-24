plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `java-test-fixtures`
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    api(libs.bundles.core.common)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testFixturesImplementation(libs.bundles.test)
    testFixturesImplementation(libs.bundles.core.common)
    testFixturesImplementation("org.jetbrains.kotlin:kotlin-reflect")
}