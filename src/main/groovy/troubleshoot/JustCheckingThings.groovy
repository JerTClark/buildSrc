package troubleshoot

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JustCheckingThings extends DefaultTask {

    String[] replacePattern
    String[] resultPattern

    @TaskAction
    void start() {
//        String[] replacements = replacePattern.split("-")
//        String[] results = resultPattern.split("-")
        replacePattern.eachWithIndex { String entry, int i ->
            println "rule pattern: ${entry}, result: ${resultPattern[i]}"
        }
    }

}
