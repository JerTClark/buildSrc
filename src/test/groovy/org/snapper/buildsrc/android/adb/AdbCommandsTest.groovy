package org.snapper.buildsrc.android.adb

import spock.lang.Specification

class AdbCommandsTest extends Specification {

    def "check for command correctness"() {
        expect:
        AdbCommands.adbUninstallCommand == "cmd.exe /c adb uninstall"
        AdbCommands.adbInstallCommand == "cmd.exe /c adb install"
        AdbCommands.adbInstrumentationCommand == "cmd.exe /c adb shell am instrument"
        AdbCommands.adbPullCommand == "cmd.exe /c adb pull"
        AdbCommands.adbPushCommand == "cmd.exe /c adb push"
    }

}
