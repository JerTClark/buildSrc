package util

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * POGO for setting the Project version
 */
class ProjectVersion {
    Integer major
    Integer minor
    Boolean release

    ProjectVersion(final Integer major, final Integer minor) {
        this.major = major
        this.minor = minor
        this.release = Boolean.FALSE
    }

    ProjectVersion(final Integer major, final Integer minor, final Boolean release) {
        this(major, minor)
        this.release = release
    }

    @Override
    String toString() {
        return "$major.$minor${this.release ? "" : "-SNAPSHOT"}"
    }
}

/**
 * Task for making a project a release version
 */
class ReleaseVersionTask extends DefaultTask {
    @Input Boolean release
    @OutputFile File destFile

    ReleaseVersionTask() {
        group = "snapper"
        description = "Makes project a release version"
    }

    @TaskAction
    void start() {
        project.version.release = true
        ant.propertyfile(file: destFile) {
            entry(key: 'release', type: 'string', operation: '=', value: 'true')
        }
    }
}

/**
 * Build lifecycle listener
 */
class ReleaseVersionListener implements TaskExecutionGraphListener {

    final static String releaseTaskPath = ":release"

    @Override
    void graphPopulated(final TaskExecutionGraph taskGraph) {
        if(taskGraph.hasTask(releaseTaskPath)) {
            final List<Task> allTasks = taskGraph.allTasks
            final Task releaseTask = allTasks.find {it.path == releaseTaskPath}
            final Project project = releaseTask.project
            if (!project.version.release) {
                project.version.release = true
                project.ant.propertyfile(file: project.versionFile) {
                    entry(key: 'release', type: 'string', operation: '=', value: 'true')
                }
            }
        }
    }
}