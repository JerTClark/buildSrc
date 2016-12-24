package org.snapper.buildsrc.android.adb

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class AdbPullTest extends Specification {

    @Shared AdbPull adbPull

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.adbPull = (AdbPull) project.task("adbPull", type: AdbPull) {
            source "/sdcard/file.txt"
            destination "/c/users/home/file.txt"
        }
    }

    def "test AdbPull task"() {
        expect:
        this.adbPull.group == "snapper-android-adb"
        this.adbPull.description == "Executes an adb pull operation against a connected device"
        this.adbPull.source == "/sdcard/file.txt"
        this.adbPull.destination == "/c/users/home/file.txt"
        this.adbPull.mockStart() == AdbCommands.adbPullCommand + " /sdcard/file.txt /c/users/home/file.txt"
    }

}
