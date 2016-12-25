package org.snapper.buildsrc.file.distribution

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.execution.commandline.TaskConfigurationException
import org.snapper.buildsrc.ant.AntHelper

/**
 * This task encapsulates the extraction of one or more archives
 * into a specified folder.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class Extract extends DefaultTask {

    /**The archive to extract.*/
    String archiveFile

    /**
     * The archives to extract.
     * When extracting multiple archives, they will be processed
     * in their list-index order.
     */
    List<String> archiveFiles

    /**The folder into which the archive should be extracted.*/
    File outputDir

    @Input
    @Optional
    getArchiveFile() {
        this.archiveFile
    }

    void setArchiveFile(Object... archiveFile) {
        throw new TaskConfigurationException(
                this.path,
                "Use the 'archive[s]' configuration option when extracting multiple archives.",
                new Exception("Provided multiple archives to " + this.getClass() + " when specifying 'archive' instead of 'archive[s]'."))
    }

    void setArchiveFile(List<Object> archiveFile) {
        throw new TaskConfigurationException(
                this.path,
                "Use the 'archive[s]' configuration option when extracting multiple archives.",
                new Exception("Provided multiple archives to " + this.getClass() + " when specifying 'archive' instead of 'archive[s]'."))
    }

    void setArchiveFile(String archiveFile) {
        this.archiveFile = archiveFile
    }

    void setArchiveFile(File archiveFile) {
        this.archiveFile = archiveFile.absolutePath
    }

    @Input
    @Optional
    getArchiveFiles() {
        this.archiveFiles
    }

    void setArchiveFiles(List<String> archiveFiles) {
        this.archiveFiles = archiveFiles
    }

    void setArchiveFiles(String... archiveFiles) {
        this.archiveFiles = archiveFiles as List
    }

    void setArchiveFiles(File... archiveFiles) {
        this.archiveFiles = []
        archiveFiles.each {
            this.archiveFiles.add(it.absolutePath)
        }
    }

    @Input
    getOutputDir() {
        this.outputDir
    }

    void setOutputDir(String outputDir) {
        this.outputDir = project.file outputDir
    }

    void setOutputDir(File outputDir) {
        this.outputDir = outputDir
    }

    File getOutputFileByName(String name) {
        this.outputDir.listFiles().find {
            it.name == name
        }
    }

    @TaskAction
    void start() {
        if(!this.outputDir.exists()) {
            this.outputDir.mkdirs()
        }

        AntHelper.clearDefaultExcludes(project)

        project.copy {
            if (this.archiveFile != null) {
                from project.zipTree(project.file(this.archiveFile))
            } else {
                this.archiveFiles.each {
                    from project.zipTree(project.file(it))
                }
            }
            into this.outputDir
        }

        AntHelper.restoreDefaultExcludes(project)
    }

}
