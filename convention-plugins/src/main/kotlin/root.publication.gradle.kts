plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

val publicationGroup = providers.gradleProperty("publishedGroup").orNull
    ?: providers.gradleProperty("group").orNull
    ?: "com.mohamedrejeb.richeditor"

val publicationVersion = providers.gradleProperty("publishedVersion").orNull
    ?: providers.gradleProperty("version").orNull
    ?: System.getenv("VERSION")
    ?: "1.0.0-rc13"

allprojects {
    group = publicationGroup
    version = publicationVersion
}

nexusPublishing {
    // Configure maven central repository
    // https://github.com/gradle-nexus/publish-plugin#publishing-to-maven-central-via-sonatype-ossrh
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            stagingProfileId.set(System.getenv("OSSRH_STAGING_PROFILE_ID"))
            username.set(System.getenv("OSSRH_USERNAME"))
            password.set(System.getenv("OSSRH_PASSWORD"))
        }
    }
}
