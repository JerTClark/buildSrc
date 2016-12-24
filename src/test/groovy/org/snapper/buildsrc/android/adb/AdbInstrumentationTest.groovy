package org.snapper.buildsrc.android.adb

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class AdbInstrumentationTest extends Specification {

    @Shared AdbInstrumentation adbInstrumentation
    @Shared String packageTestRunner
    @Shared String useCoverage

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.adbInstrumentation = (AdbInstrumentation) project.task("adbInstrumentation", type: AdbInstrumentation) {
            withCoverage true
            testPackageName "org.snapper.android.test"
            testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
            deviceCoverageFile = "/sdcard/org.snapper.app.ec"
        }
        this.packageTestRunner = "${this.adbInstrumentation.testPackageName}/${this.adbInstrumentation.testInstrumentationRunner}"
        this.useCoverage = "-w -e coverage true -e coverageFile ${this.adbInstrumentation.deviceCoverageFile}"
    }

    def "test AdbInstrumentation task"() {
        expect:
        this.adbInstrumentation.group == "snapper-android-adb"
        this.adbInstrumentation.description == "Run Android instrumentation tests on connected device via adb"
        this.adbInstrumentation.mockStart() == AdbCommands.adbInstrumentationCommand + " ${useCoverage} " + "${packageTestRunner}"
    }

}
