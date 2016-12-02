package android.adb

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import sun.java2d.pipe.AAShapePipe
import sun.nio.cs.UTF_8

import java.nio.charset.StandardCharsets

class AdbInstrumentation extends DefaultTask {

    @Input
    boolean withCoverage = false

    @Input
    String testPackageName

    @Input
    String testInstrumentationRunner

    @Input
    @Optional
    String deviceCoverageFile

    AdbInstrumentation() {
        group = "snapper"
        description = "Run Android instrumentation tests on connected device via adb"
    }

    @TaskAction
    void start() {
        final String useCoverage = "-w -e coverage true -e coverageFile ${deviceCoverageFile}"
        final String packageTestRunner = "${testPackageName}/${testInstrumentationRunner}"
        println "Running Android instrumentation tests on ${AdbCommands.getConnectedDeviceName()}"
        final def command = withCoverage ?
                "${AdbCommands.adbInstrumentationCommand} ${useCoverage} ${packageTestRunner}" :
                "${AdbCommands.adbInstrumentationCommand} ${packageTestRunner}"
        final Scanner scanner = new Scanner(command.execute().inputStream)
        def line
        while(scanner.hasNext()) {
            line = scanner.nextLine()
            if(!line.equals("")) println line
        }
    }
}
