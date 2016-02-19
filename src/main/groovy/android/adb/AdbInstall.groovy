package android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class AdbInstall extends DefaultTask {

    @Input
    List<String> pathsToApkFiles = []

    AdbInstall() {
        group = "adb"
        description = "Installs one or more provided apk files on a connected device via adb"
    }

    @TaskAction
    void start() {
        pathsToApkFiles.each {
            println "Installing ${it}"
            def output = "${AdbCommands.adbInstallCommand} ${it}".execute().text
            println output
        }
    }

}
