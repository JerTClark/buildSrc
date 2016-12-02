package properties

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class CreateLocalPropertiesFile extends DefaultTask {

    @Input
    def propertiesMap = [:]

    @OutputFile
    File propertiesFile

    CreateLocalPropertiesFile() {
        group = "snapper"
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