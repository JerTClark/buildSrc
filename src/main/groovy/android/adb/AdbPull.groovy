package android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class AdbPull extends DefaultTask {

    @Input
    String pullFrom

    @Input
    @Optional
    String pullTo = ""

    AdbPull() {
        group = "snapper"
        description = "Executes an adb pull operation against a connected device"
    }

    @TaskAction
    void start() {
        println "Pulling ${pullFrom} from ${AdbCommands.getConnectedDeviceName()}"
        final def output = "${AdbCommands.adbPullCommand} ${pullFrom} ${pullTo}".execute().text
        println output
    }

}
