package org.snapper.buildsrc.xml

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Tasks of this type encapsulate Ant's Xslt target.
 */
class XsltTransform extends DefaultTask {

    /**This is the Xml document on which the Xslt transformation will be performed.*/
    File originalXml

    /**This is the Xslt stylesheet that will be used to perform the tranformation.*/
    File xsltFile

    /**This is an optional map of parameters that can be passed to the Xslt target.*/
    Map<String, String> params = [:]

    /**The file that will contain the result of the Xslt transformation operation.*/
    File outputFile

    @InputFile
    @Optional
    getOriginalXml() {
        this.originalXml
    }

    void setOriginalXml(File inputFile) {
        this.originalXml = inputFile
    }

    void setOriginalXml(String inputFile) {
        this.originalXml = project.file inputFile
    }

    @InputFile
    getXsltFile() {
        this.xsltFile
    }

    void setXsltFile(File xsltStyleFile) {
        this.xsltFile = xsltStyleFile
    }

    void setXsltFile(String xsltStyleFile) {
        this.xsltFile = project.file xsltStyleFile
    }

    @Input
    @Optional
    getParams() {
        this.params
    }

    void setParams(Map<String, String> params) {
        this.params = params
    }

    @OutputFile
    getOutputFile() {
        this.outputFile
    }

    void setOutputFile(File outputFile) {
        this.outputFile = outputFile
    }

    void setOutputFile(String outputFile) {
        this.outputFile = project.file outputFile
    }

    XsltTransform() {
        group = "snapper-xml"
        description = "Transform an Xml input file using an Xslt stylesheet input file."
        onlyIf {
            this.originalXml.exists()
        }
    }

    @TaskAction
    void start() {
        project.ant.xslt(in: this.originalXml, style: this.xsltFile, out: this.outputFile) {
            this.params.each { final key, final value ->
                ant.param(name: key, expression: value)
            }
        }
    }

}