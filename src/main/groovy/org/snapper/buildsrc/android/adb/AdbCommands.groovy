package org.snapper.buildsrc.android.adb
/**
 * Utility class for various static methods and fields related
 * to executing various 'adb' commands.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class AdbCommands {
    /**
     * The syntax to execute an adb uninstall command:
     * {@code cmd.exe /c adb uninstall}
     */
    static final String adbUninstallCommand = "cmd.exe /c adb uninstall"
    /**
     * The syntax to execute an adb install command:
     * {@code cmd.exe /c adb install}
     */
    static final String adbInstallCommand = "cmd.exe /c adb install"
    /**
     * The syntax to execute an adb shell getprops command
     * {@code cmd.exe /c adb shell getprop}
     */
    private static final String adbGetPropsCommand = "cmd.exe /c adb shell getprop"
    /**
     * Returns the model name of the connected device
     */
    static final String getConnectedDeviceName() {
        return "${adbGetPropsCommand} ro.product.model".execute().text
    }
    /**
     * The syntax for running instrumentation tests on a connected device
     * {@code cmd.exe /c adb shell am instrument}
     */
    static final String adbInstrumentationCommand = "cmd.exe /c adb shell am instrument"
    /**
     * The syntax for executing an adb pull operation against a connected device
     * {@code cmd.exe /c adb pull}
     */
    static final String adbPullCommand = "cmd.exe /c adb pull"
    /**
     * The syntax for executing an adb push operation against a connected device
     * {@code cmd.exe /c adb push}
     */
    static final String adbPushCommand = "cmd.exe /c adb push"
}
