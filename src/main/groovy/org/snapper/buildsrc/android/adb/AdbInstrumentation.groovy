package org.snapper.buildsrc.android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.annotations.DslMethod
import org.snapper.buildsrc.annotations.MockTaskAction

/**
 * Defines a task that encapsulates the manual running of instrumentation
 * tests on a connected device via 'adb instrumentation.'
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class AdbInstrumentation extends DefaultTask {

    /**Whether or not to run the instrumentation tests with coverage.*/
    boolean withCoverage = false

    /**The package name of the test application.*/
    String testPackageName

    /**The fully qualified name of the instrumentation test runner to use.*/
    String testInstrumentationRunner

    /**/
    String deviceCoverageFile

    AdbInstrumentation() {
        group = "snapper-android-adb"
        description = "Run Android instrumentation tests on connected device via adb"
    }

    @Input
    getWithCoverage() { this.withCoverage }

    @Input
    getTestPackageName() { this.testPackageName }

    @Input
    getTestInstrumentationRunner() { this.testInstrumentationRunner }

    @Input
    @Optional
    getDeviceCoverageFile() { this.deviceCoverageFile }

    void setWithCoverage(boolean withCoverage) {
        this.withCoverage = withCoverage
    }

    void setTestPackageName(String testPackageName) {
        this.testPackageName = testPackageName
    }

    void setTestInstrumentationRunner(String testInstrumentationRunner) {
        this.testInstrumentationRunner = testInstrumentationRunner
    }

    void setDeviceCoverageFile(String deviceCoverageFile) {
        this.deviceCoverageFile = deviceCoverageFile
    }

    @TaskAction
    void start() {
        final String useCoverage = "-w -e coverage true -e coverageFile ${this.deviceCoverageFile}"
        final String packageTestRunner = "${this.testPackageName}/${this.testInstrumentationRunner}"
        println "Running Android instrumentation tests on ${AdbCommands.getConnectedDeviceName()}"
        final def command = this.withCoverage ?
                "${AdbCommands.adbInstrumentationCommand} ${useCoverage} ${packageTestRunner}" :
                "${AdbCommands.adbInstrumentationCommand} ${packageTestRunner}"
        final Scanner scanner = new Scanner(command.execute().inputStream)
        def line
        while(scanner.hasNext()) {
            line = scanner.nextLine()
            if(!line.equals("")) println line
        }
    }

    /**
     * This mock implementation of the {@link #start} method returns a string
     * representing the command that would be executed in the actual running
     * of a real task of this type.
     */
    @MockTaskAction
    String mockStart() {
        final String useCoverage = "-w -e coverage true -e coverageFile ${this.deviceCoverageFile}"
        final String packageTestRunner = "${this.testPackageName}/${this.testInstrumentationRunner}"
        String command = this.withCoverage ?
                "${AdbCommands.adbInstrumentationCommand} ${useCoverage} ${packageTestRunner}" :
                "${AdbCommands.adbInstrumentationCommand} ${packageTestRunner}"
        command
    }

}
