package android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class AdbPush extends DefaultTask {

    @Input
    String pushFrom

    @Input
    @Optional
    String pushTo = "/sdcard/"

    AdbPush() {
        group = "snapper"
        description = "Executes an adb push operation against a connected device"
    }

    @TaskAction
    void start() {
        println "Pushing ${pushFrom} to ${AdbCommands.getConnectedDeviceName()} ${pushTo}"
        final def output = "${AdbCommands.adbPullCommand} ${pushFrom} ${pushTo}".execute().text
        println output
    }
}
