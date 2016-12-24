package org.snapper.buildsrc.android.adb

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class AdbPushTest extends Specification {

    @Shared AdbPush adbPush

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.adbPush = (AdbPush) project.task("adbPush", type: AdbPush) {
            source "/c/users/home/file.txt"
        }
    }

    def "test AdbPush task"() {
        expect:
        this.adbPush.target == "/sdcard/"
        this.adbPush.group == "snapper-android-adb"
        this.adbPush.description == "Executes an adb push operation against a connected device"
        this.adbPush.mockStart() == AdbCommands.adbPullCommand + " ${this.adbPush.source} /sdcard/"
    }

}
