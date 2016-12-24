package org.snapper.buildsrc.android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.annotations.DslMethod
import org.snapper.buildsrc.annotations.MockTaskAction

class AdbPull extends DefaultTask {

    /**The source to be pulled from the connected device.*/
    String source

    /**The destination for the pulled resource where it will be written on the local machine.*/
    String destination = ""

    AdbPull() {
        group = "snapper-android-adb"
        description = "Executes an adb pull operation against a connected device"
    }

    @Input
    @Optional
    getDestination() { this.destination }

    @Input
    getSource() { this.source }

    void setSource(String source) {
        this.source = source
    }

    void setDestination(String destination) {
        this.destination = destination
    }

    void setDestination(File destination) {
        this.destination = destination.absolutePath
    }

    @DslMethod
    void setConfig(Closure closure) {
        closure.delegate = this
        closure()
    }

    @TaskAction
    void start() {
        println "Pulling ${this.source} from ${AdbCommands.getConnectedDeviceName()}"
        final def output = "${AdbCommands.adbPullCommand} ${this.source} ${this.destination}".execute().text
        println output
    }

    /**
     * This method mocks the tack action by simply returning the command that the real method
     * will execute to perform the actual 'adb pull' operation.
     */
    @MockTaskAction
    String mockStart() {
        "${AdbCommands.adbPullCommand} ${this.source} ${this.destination}"
    }

}
