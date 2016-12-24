package org.snapper.buildsrc.properties

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class CreatePropertiesFile extends DefaultTask {

    Map propertiesMap = [:]

    File propertiesFile

    @Input
    getPropertiesMap() {
        this.propertiesMap
    }

    void setPropertiesMap(Map propertiesMap) {
        this.propertiesMap = propertiesMap
    }

    @OutputFile
    File getPropertiesFile() {
        return propertiesFile
    }

    void setPropertiesFile(File propertiesFile) {
        this.propertiesFile = propertiesFile
    }

    CreatePropertiesFile() {
        group = "snapper-properties"
        description = "Create a properties file from a provided map"
    }

    @TaskAction
    void start() {
        ant.propertyfile(file: propertiesFile) {
            propertiesMap.entrySet().each {
                entry(key: "${it.key}", type: "string", operation: "=", value: "${it.value}")
            }
        }
    }

}