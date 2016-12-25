package org.snapper.buildsrc.file.distribution

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.ant.AntHelper
import org.snapper.buildsrc.file.TemporaryFile
import org.snapper.buildsrc.timestamp.Today

/**
 * This task encapsulates the bundling of one or more specified
 * resources into a single zip file ready for distribution.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class Distribution extends DefaultTask {

    /**The content to bundle in the created distribution.*/
    List<File> distContents = []

    /**The name of the resulting distribution.*/
    String distName

    /**The destination folder... in case I needed to clarify. I am just addicted to comments...*/
    File destinationDir

    /**A temporary file for storing the contents to be bundled as a distribution.*/
    TemporaryFile tempDir

    Distribution() {
        group = "snapper-distribution"
        description = "Creates a distributable from a collection of specified resources"
    }

    @InputFiles
    getDistContents() {
        this.distContents
    }

    void setDistContents(List<File> distContents) {
        this.distContents = distContents
    }

    void setDistContents(File... distContents) {
        this.distContents = distContents as List
    }

    void setDistContents(String... distContents) {
        distContents.each {
            this.distContents.add project.file(it)
        }
    }

    void setDistContents(FileCollection distContents) {
        this.distContents = distContents.asList()
    }

    @Input
    getDistName() {
        this.distName
    }

    void setDistName(String distributionName) {
        this.distName = distributionName
    }

    @Input
    getDestinationDir() {
        this.destinationDir
    }

    void setDestinationDir(String destinationDir) {
        this.destinationDir = project.file destinationDir
    }

    void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir
    }

    @OutputFile
    getOutputFile() {
        new File(this.destinationDir, "${this.distName}.zip")
    }

    /**Create the */
    def mkdirs() {
        this.tempDir = new TemporaryFile("_gradle${project.name.capitalize()}${new Today()}")
        if(!this.tempDir.exists()) {
            this.tempDir.mkdir()
        }
        if(!this.destinationDir.exists()) {
            this.destinationDir.mkdirs()
        }
    }

    @TaskAction
    void start() {
        AntHelper.clearDefaultExcludes(project)

        this.mkdirs()

        /**Copy all of the intended contents to the task's temporary folder.*/
        this.distContents.each { item ->
            project.ant.copy(file: item, tofile: "${this.tempDir.absolutePath}/${item.name}")
        }
        project.ant.zip(destfile: this.outputFile, basedir: this.tempDir.absolutePath)

        /**Delete the temporary folder. Not really necessary, but polite.*/
        this.tempDir.finished()

        AntHelper.restoreDefaultExcludes(project)
    }

}
