package org.snapper.buildsrc.android.adb

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.testfixtures.ProjectBuilder

class AdbInstallTest extends DefaultTask {

    def "test AdbInstall task"() {
        def list
        when:
        Project project = ProjectBuilder.builder().build()
        AdbInstall adbInstall = (AdbInstall) project.task("adbInstall", type: AdbInstall)
        adbInstall.config {
            apkFiles "some.apk", project.file("path/to/apkFile0.apk"), project.files([
                    "path/to/apk1.apk",
                    "path/to/apk2.apk",
                    "path/to/apk3.apk",
                    "path/to/apk4.apk"
            ])
        }
        list = adbInstall.mockStart()
        expect:
        adbInstall.group == "snapper-android-adb"
        adbInstall.description == "Installs one or more provided apk files on a connected device via adb"
        adbInstall.apkFiles.size() == 3
        !list.empty
        list.size() == 6
    }

}
