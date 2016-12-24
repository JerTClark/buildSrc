package org.snapper.buildsrc.xml

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * Tasks of this type encapsulate Ant's Xslt target.
 */
class XjcTask extends DefaultTask {
    
    /**The directory into which the Java content classes will be generated.*/
    String outputDir

    /**The full path to the schema file from which to generate the Java content classes.*/
    String schemaFile

    /**An optional string of options to pass to the Xjc command (i.e. "-extension -Xinject-code -Xschemanator").*/
    String options = ""
    
    @Input
    getOutputDir() {
        this.outputDir
    }

    void setOutputDir(String outputDir) {
        this.outputDir = outputDir
    }

    void setOutputDir(File outputDir) {
        this.outputDir = outputDir.absolutePath
    }
    
    @Input
    getSchemaFile() {
        this.schemaFile
    }

    void setSchemaFile(String schemaFile) {
        this.schemaFile = schemaFile
    }

    void setSchemaFile(File schemaFile) {
        this.schemaFile = schemaFile.absolutePath
    }

    @Input
    @Optional
    getOptions() {
        this.options
    }

    void setOptions(String options) {
        this.options = options
    }

    void setOptions(String... options) {
        this.options = options.join(" ")
    }

    XjcTask() {
        group = "snapper-xml"
        description = "Generate Java content classes from an Xml Schema"
        onlyIf {
            this.schemaFile != null || this.outputDir != null
        }
    }

    @TaskAction
    void start() {
        File outputDirFile = new File(this.outputDir)
        if(!outputDirFile.exists()) {
            outputDirFile.mkdirs()
        }
        final String xjcCommand = "cmd /c, xjc -d ${this.outputDir} ${this.options} ${this.schemaFile}"
        println xjcCommand.execute().text
    }

}