import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class IncrementArtifactoryBuildNumber extends DefaultTask {

    final def buildNumberKey = "buildInfo.build.number"
    final def propertiesFile = project.file("gradle.properties")

    IncrementArtifactoryBuildNumber() {
        group = "snapper"
        description = "Increments the Artifactory Build-Info object's build number in the project's gradle.properties file"
    }

    @TaskAction
    void start() {
        if (!this.propertiesFile.exists()) {
            this.propertiesFile.createNewFile()
            ant.propertyfile(file: this.propertiesFile) {
                entry(key: "buildInfo.build.name", type: "string", operation: "=", value: "${project.name}")
                entry(key: "${this.buildNumberKey}", type: "string", operation: "=", value: "1")
            }
        } else {
            ant.loadproperties srcFile: this.propertiesFile
            final def buildNumber = Integer.valueOf(ant.getProperty(this.buildNumberKey)) + 1
            ant.propertyfile(file: this.propertiesFile) {
                entry(key: "${this.buildNumberKey}", type: "string", operation: "=", value: "${buildNumber}")
            }
        }
    }

}