package org.snapper.buildsrc.pipeline.git

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.annotations.DslMethod
import org.snapper.buildsrc.annotations.MockTaskAction

/**
 * A work in progress that should read a commit message from a simple changelog file
 * and performs a simple commit operation. A bit questionable in its current state.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class GitTask extends DefaultTask {

    /**A commit message (it's recommended that it be read from a text file where changes have been written in a commit message format.*/
    String commitMessage

    /**Whether or not to run the push command following the commit command.*/
    boolean pushing = false

    /**Whether or not to merely run the status command.*/
    boolean statusOnly = false

    /**Command to open GitBash (must be on the path).*/
    def String gitBash = "git bash",
               /**Git status command.*/
               status = "git status",
               /**Git add all command.*/
               add = "git add -A .",
               /**Git commit -m command.*/
               commit = "git commit -m \"${this.commitMessage}\"",
               /**Git push command.*/
               push = "git push"

    GitTask() {
        group = "snapper-build-git"
        description = "Add all files and commit changes with the git command"
    }

    @DslMethod
    void config(Closure closure) {
        closure.delegate = this
        closure()
    }

    @Input
    String getCommitMessage() {
        this.commitMessage
    }

    void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage
    }

    @Input
    @Optional
    boolean getPushing() {
        this.pushing
    }

    void setPushing(boolean pushing) {
        this.pushing = pushing
    }

    @Input
    @Optional
    boolean getStatusOnly() {
        this.statusOnly
    }

    void setStatusOnly(boolean statusOnly) {
        this.statusOnly = statusOnly
    }

    def statusTask = project.task("_statusTask", type: Exec) {
        workingDir "${project.projectDir}"
        commandLine "git-bash", "-c", this.status
    }

    def pushingTask = project.task("_pushingTask", type: Exec) {
        workingDir "${project.projectDir}"
        commandLine "git-bash", "-c", "${this.add} && ${this.commit} && ${this.push}"
    }

    def commitTask = project.task("_commitTask", type: Exec) {
        workingDir "${project.projectDir}"
        commandLine "git-bash", "-c", "${this.add} && ${this.commit}"
    }

    @TaskAction
    void start() {
        if (this.statusOnly) {
            this.statusTask.execute()
        } else if (this.pushing) {
            this.pushingTask.execute()
        } else {
            this.commitTask.execute()
        }
    }

    /**
     * The mock execution of this task simply returns a string representing the command
     * that would execute in the actual task.
     */
    @MockTaskAction
    String mockStart() {
        if (this.statusOnly) {
            "${this.gitBash} -c ${this.status}"
        } else if (this.pushing) {
            "${this.gitBash} -c ${this.add} && ${this.commit} && ${this.push}"
        } else {
            "${this.gitBash} -c ${this.add} && ${this.commit}"
        }
    }

}
