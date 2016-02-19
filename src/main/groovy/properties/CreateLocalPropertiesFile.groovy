package properties

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class CreateLocalPropertiesFile extends DefaultTask {

    @OutputFile
    File localPropertiesFile = project.file("../local.properties")

    CreateLocalPropertiesFile() {
        group = "build"
        description = "Creates a local.properties file with the sdk.dir property set to this system's " +
                "ANDROID_HOME environment variable"
    }

    @TaskAction
    void start() {
        //In case different OS also entails a different value of the env variable
        def sdkDirectory = System.getenv("OS").equals("Windows_NT") ? System.getenv("ANDROID_HOME") : System.getenv("\$ANDROID_HOME") + "/platforms"
        ant.propertyfile(file: localPropertiesFile) {
            println "Creating local.properties file at ${localPropertiesFile.absolutePath}"
            entry(key: "sdk.dir", type: "string", operation: "=", value: sdkDirectory)
        }
    }

}