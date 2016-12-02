package xml

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class XsltTransform extends DefaultTask {
    @InputFile
    @Optional
    File inputFile

    @InputFile
    File xsltStyleFile

    @Input
    @Optional
    Map<String, String> params = [:]

    @OutputFile
    File outputFile

    XsltTransform() {
        group = "snapper"
        description = "Transform an Xml input file using an Xslt stylesheet input file."
        onlyIf {
            inputFile.exists()
        }
    }

    @TaskAction
    void start() {
        project.ant.xslt(in: inputFile, style: xsltStyleFile, out: outputFile) {
            params.each { final key, final value ->
                ant.param(name: key, expression: value)
            }
        }
    }

}