package org.snapper.buildsrc.properties

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * POGO for setting the Project version.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class ProjectVersion {

    /**The major number (semantic versioning).*/
    int major

    /**The minor number (semantic versioning).*/
    int minor

    /**The patch number (semantic versioning).*/
    int patch

    /**Whether or not the project is currently a 'release' version (otherwise, 'SNAPSHOT' will be used).*/
    boolean release

    /**
     * Obtain an instance of this class.
     */
    ProjectVersion(final int major, final int minor, final int patch) {
        this.major = major
        this.minor = minor
        this.release = false
        this.patch = patch
    }

    /**
     * Obtain an instance of this class.
     */
    ProjectVersion(final int major, final int minor, int patch, final boolean release) {
        this(major, minor, patch)
        this.release = release
    }

    @Override
    String toString() {
        return "${this.major}.${this.minor}.${this.patch}${this.release ? "" : "-SNAPSHOT"}"
    }
}

/**
 * Task for making a project a release version.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class ReleaseVersionTask extends DefaultTask {

    /**Whether or not this version represents a release.*/
    boolean release

    /**The output file for this task (a properties file).*/
    File propertiesFile

    @Input
    getRelease() {
        this.release
    }

    void setRelease(boolean release) {
        this.release = release
    }

    @OutputFile
    getPropertiesFile() {
        this.propertiesFile
    }

    void setPropertiesFile(File destFile) {
        this.propertiesFile = destFile
    }

    ReleaseVersionTask() {
        group = "snapper-properties"
        description = "Record whether or not the project is a release version in a properties file"
    }

    @TaskAction
    void start() {
        ant.propertyfile(file: this.propertiesFile) {
            entry(key: "release", type: "string", operation: "=", value: this.release)
        }
    }
}

/**
 * This class was built using an example while learning how to hook into the build lifecycle
 * using a task execution graph listener. Nevertheless, it's kept for its usefulness in any
 * coordination needed or useful regarding the "release" property of a particular project.
 * <p>This listener will look for a property on the project object named {@code versionFile}
 * that points to a properties file.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class ReleaseVersionListener implements TaskExecutionGraphListener {

    final String releaseTaskPath = ":release"

    @Override
    void graphPopulated(final TaskExecutionGraph taskGraph) {
        if(taskGraph.hasTask(this.releaseTaskPath)) {
            final List<Task> allTasks = taskGraph.allTasks
            final Task releaseTask = allTasks.find {it.path == this.releaseTaskPath}
            final Project project = releaseTask.project
            project.ant.loadproperties srcFile: project.versionFile
            def release = project.ant.getProperty("release")
            if (!release) {
                project.ant.propertyfile(file: project.versionFile) {
                    entry(key: "release", type: "string", operation: "=", value: release)
                }
            }
        }
    }

}