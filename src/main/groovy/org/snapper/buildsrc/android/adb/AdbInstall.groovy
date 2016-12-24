package org.snapper.buildsrc.android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.annotations.DslMethod
import org.snapper.buildsrc.annotations.MockTaskAction

/**
 * This class defines a task type that encapsulates the 'adb install'
 * command. It allows the consumer to specify one or more apk files to install
 * and assumes the default connected device.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class AdbInstall extends DefaultTask {

    /**A collection of paths to apk files*/
    List<Object> apkFiles = []

    AdbInstall() {
        group = "snapper-android-adb"
        description = "Installs one or more provided apk files on a connected device via adb"
    }

    @Input
    getApkFiles() { this.apkFiles }

    void setPathsToApkFiles(Object... apkFiles) {
        this.apkFiles = apkFiles as List
    }

    @DslMethod
    void setConfig(Closure closure) {
        closure.delegate = this
        closure()
    }

    /**Execute the 'adb install' command for the provided path to an apk file.*/
    private void install(String path) {
        println "Installing ${it}"
        final def output = "${AdbCommands.adbInstallCommand} ${it}".execute().text
        println output
    }

    @TaskAction
    void start() {
        this.apkFiles.each { apkFile ->
            if (apkFile instanceof String) {
                if (apkFile.endsWith(".apk")) {
                    this.install apkFile
                } else {
                    this.install apkFile + ".apk"
                }
            } else if(apkFile instanceof File) {
                this.install apkFile.absolutePath
            } else if (apkFile instanceof FileCollection) {
                apkFile.each { File file ->
                    this.install file.absolutePath
                }
            }
        }
    }

    /**
     * Test for various supplied inputs of strings, files, and file collections.
     * This method mocks the task action by adding valid apk files to a list where
     * the real method would attempt to perform the 'adb install.'
     * Fringe cases should test for objects of types other than these. A list will
     * be returned with a size matching the number of validly configured apk files.
     * All items in the list will be strings and should end with '.apk.'
     */
    @MockTaskAction
    List<String> mockStart() {
        def list = []
        this.apkFiles.each { apkFile ->
            if (apkFile instanceof String) {
                if (apkFile.endsWith(".apk")) {
                    list.add apkFile
                } else {
                    list.add apkFile + ".apk"
                }
            } else if(apkFile instanceof File) {
                list.add apkFile.absolutePath
            } else if (apkFile instanceof FileCollection) {
                apkFile.each { File file ->
                    list.add file.absolutePath
                }
            }
        }
        list
    }

}
