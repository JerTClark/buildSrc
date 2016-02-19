package android.adb
/**
 * Utility class for various static methods and fields related
 * to executing adb commands for Android development.
 */
class AdbCommands {
    /**
     * The syntax to execute an adb uninstall command:
     * <code>cmd.exe /c adb uninstall</code>
     */
    static final String adbUninstallCommand = "cmd.exe /c adb uninstall"
    /**
     * The syntax to execute an adb install command:
     * <code>cmd.exe /c adb install</code>
     */
    static final String adbInstallCommand = "cmd.exe /c adb install"
    /**
     * The syntax to execute an adb shell getprops command
     * <code>cmd.exe /c adb shell getprop</code>
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
     * <code>cmd.exe /c adb shell am instrument</code>
     */
    static final String adbInstrumentationCommand = "cmd.exe /c adb shell am instrument"
    /**
     * The syntax for executing an adb pull operation against a connected device
     * <code>cmd.exe /c adb pull</code>
     */
    static final String adbPullCommand = "cmd.exe /c adb pull"
    /**
     * The syntax for executing an adb push operation against a connected device
     * <code>cmd.exe /c adb push</code>
     */
    static final String adbPushCommand = "cmd.exe /c adb push"
}
