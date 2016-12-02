package android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * A task to uninstall packages via adb
 */
class AdbUninstallTask extends DefaultTask {

    @Input
    List<String> packageNames = []

    AdbUninstallTask() {
        group = "snapper"
        description = "Uninstall package from connected device via adb"
    }

    @TaskAction
    void start() {
        packageNames.each {
            println "Uninstalling ${it} from ${AdbCommands.getConnectedDeviceName()}"
            final def output = "${AdbCommands.adbUninstallCommand} ${it}".execute().text
            println output
        }
    }

}
