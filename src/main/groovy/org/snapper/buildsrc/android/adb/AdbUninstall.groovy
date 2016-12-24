package org.snapper.buildsrc.android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.annotations.DslMethod
import org.snapper.buildsrc.annotations.MockTaskAction

/**
 * Defines a type of task that encapsulates 'adb uninstall' of specific packages.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
@Newify
class AdbUninstall extends DefaultTask {

    /**A list of application package names to uninstall.*/
    List<String> packages = []

    AdbUninstall() {
        group = "snapper-android-adb"
        description = "Uninstall package from connected device via adb"
    }

    @Input
    getPackages() { this.packages }

    void setPackages(List<String> packages) {
        this.packages.addAll packages
    }

    void setPackages(String... packages) {
        this.packages.addAll packages as List
    }

    void addPackage(String packageName) {
        this.packages.add packageName
    }

    @DslMethod
    void config(Closure closure) {
        closure.delegate = this
        closure()
    }

    @TaskAction
    void start() {
        this.packages.each {
            println "Uninstalling ${it} from ${AdbCommands.getConnectedDeviceName()}"
            final def output = "${AdbCommands.adbUninstallCommand} ${it}".execute().text
            println output
        }
    }

    /**
     * This method mocks the tack action by simply returning the commands that the real method
     * will execute to perform the actual 'adb push' operation as a list.
     */
    @MockTaskAction
    List<String> mockStart() {
        def list = []
        this.packages.each {
            list.add "${AdbCommands.adbUninstallCommand} ${it}"
        }
        list
    }

}
