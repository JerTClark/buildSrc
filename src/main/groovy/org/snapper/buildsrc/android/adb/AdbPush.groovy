package org.snapper.buildsrc.android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.annotations.DslMethod
import org.snapper.buildsrc.annotations.MockTaskAction

/**
 * Tasks of this type encapsulate the 'adb push' command.
 */
class AdbPush extends DefaultTask {

    /**The source file being pushed to the device.*/
    String source

    /**The target location of the 'adb push' command (on the device).*/
    String target = "/sdcard/"

    AdbPush() {
        group = "snapper-android-adb"
        description = "Executes an adb push operation against a connected device"
    }

    @Input
    String getSource() { this.source }

    void setSource(String source) { this.source = source }

    void setSource(File source) { this.source = source.absolutePath }

    @Input
    @Optional
    getTarget() { this.target }

    void setTarget(String target) { this.target = target }

    @DslMethod
    void config(Closure closure) {
        closure.delegate = this
        closure()
    }

    @TaskAction
    void start() {
        println "Pushing ${this.source} to ${AdbCommands.getConnectedDeviceName()} ${this.target}"
        final def output = "${AdbCommands.adbPullCommand} ${this.source} ${this.target}".execute().text
        println output
    }

    /**
     * This method mocks the tack action by simply returning the command that the real method
     * will execute to perform the actual 'adb push' operation.
     */
    @MockTaskAction
    String mockStart() {
        "${AdbCommands.adbPullCommand} ${this.source} ${this.target}"
    }

}
