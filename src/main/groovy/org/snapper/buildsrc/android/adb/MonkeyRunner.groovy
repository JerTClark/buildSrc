package org.snapper.buildsrc.android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.timestamp.Today

/**
 * This task encapsulates running Monkey runner on an installed Android application package.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class MonkeyRunner extends DefaultTask {

    /**The number of tests to run.*/
    int numberOfTests = 1000

    /**
     * The package name of the installed application against which to run the monkey runner tests.
     * This could be something like {@code project.android.defaultConfig.applicationId + ".debug"}.
     */
    String applicationPackageName

    /**The report containing the results of the monkey runner tests.*/
    File report = project.file("${project.buildDir}/reports/${project.name}-Monkey-${new Today()}.txt")

    MonkeyRunner() {
        group = "${project.hasProperty("androidGroup") ? androidGroup : "android"}"
        description = "Runs monkey runner tool on the project over adb"
    }

    @Input
    @Optional
    getNumberOfTests() {
        this.numberOfTests
    }

    void setNumberOfTests(int numberOfTests) {
        this.numberOfTests = numberOfTests
    }

    @Input
    getApplicationPackageName() {
        this.applicationPackageName
    }

    void setApplicationPackageName(String packageName) {
        this.applicationPackageName = packageName
    }

    @Input
    @Optional
    getReport() {
        this.report
    }

    void setReport(File report) {
        this.report = report
    }

    void setReport(String report) {
        this.report = project.file(report)
    }

    @TaskAction
    void start() {
        if(!this.report.exists()) {
            this.report.mkdirs()
            this.report.createNewFile()
        }
        this.report.withWriter {
            it.write "adb shell monkey -p ${this.applicationPackageName} -v ${this.numberOfTests}".execute().text
        }
    }

}
