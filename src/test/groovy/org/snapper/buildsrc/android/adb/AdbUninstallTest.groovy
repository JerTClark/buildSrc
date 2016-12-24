package org.snapper.buildsrc.android.adb

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class AdbUninstallTest extends Specification {

    @Shared AdbUninstall adbUninstall
    @Shared def list

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.adbUninstall = (AdbUninstall) project.task("adbUninstall", type: AdbUninstall) {
            packages "org.snapper.android.testing", "org.snapper.groovy.testing",
                    "org.snapper.java.testing"
        }
        this.list = this.adbUninstall.mockStart()
    }

    def "test AdbUninstall task"() {
        expect:
        this.adbUninstall.group == "snapper-android-adb"
        this.adbUninstall.description == "Uninstall package from connected device via adb"
        this.adbUninstall.packages.size() == 3
        this.adbUninstall.packages.get(0) == "org.snapper.android.testing"
        this.adbUninstall.packages.get(1) == "org.snapper.groovy.testing"
        this.adbUninstall.packages.get(2) == "org.snapper.java.testing"
        this.list.get(0) == AdbCommands.adbUninstallCommand + " org.snapper.android.testing"
        this.list.get(1) == AdbCommands.adbUninstallCommand + " org.snapper.groovy.testing"
        this.list.get(2) == AdbCommands.adbUninstallCommand + " org.snapper.java.testing"
    }
}
