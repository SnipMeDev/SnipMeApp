import groovy.lang.Binding
import groovy.lang.GroovyShell
import org.gradle.internal.impldep.org.codehaus.plexus.interpolation.reflection.ReflectionValueExtractor.evaluate

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://storage.googleapis.com/download.flutter.io")
        }
    }
}

rootProject.name = "SnipMeApp"
include(":app")

apply { from("flutter_settings.gradle") }

 