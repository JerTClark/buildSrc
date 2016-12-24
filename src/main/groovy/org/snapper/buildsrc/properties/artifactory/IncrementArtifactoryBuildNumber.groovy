package org.snapper.buildsrc.properties.artifactory

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * This task type handles manual incrementation of the 'buildInfo.build.number'
 * property in the project's 'gradle.properties' file. This file and the associated
 * properties will all be created if not already present.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class IncrementArtifactoryBuildNumber extends DefaultTask {

    /**The key expected by the Artifactory plugin for a project's build number.*/
    final def buildNumberKey = "buildInfo.build.number"

    /**A reference to the 'gradle.properties' file for the project.*/
    File propertiesFile = project.file("gradle.properties")

    IncrementArtifactoryBuildNumber() {
        group = "snapper-properties"
        description = "Increments the Artifactory Build-Info object's build number in the project's gradle.properties file"
    }

    @Input
    @Optional
    getPropertiesFiles() { this.propertiesFile }

    void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = project.file(propertiesFile)
    }

    void setPropertiesFile(File propertiesFile) {
        this.propertiesFile = propertiesFile
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
            final def buildNumber = Integer.valueOf(ant.getProperty(this.buildNumberKey) as String) + 1
            ant.propertyfile(file: this.propertiesFile) {
                entry(key: "${this.buildNumberKey}", type: "string", operation: "=", value: "${buildNumber}")
            }
        }
    }

}