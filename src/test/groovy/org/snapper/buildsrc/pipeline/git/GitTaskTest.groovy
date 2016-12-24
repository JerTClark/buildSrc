package org.snapper.buildsrc.pipeline.git

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class GitTaskTest extends Specification {

    @Shared GitTask gitTask

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.gitTask = (GitTask) project.task("gitTask", type: GitTask) {
            commitMessage "Testing the GitTask task"
            statusOnly true
        }
        this.gitTask.execute()
    }

    def "test GitTask task"() {
        expect:
        this.gitTask.mockStart() == this.gitTask.gitBash + " -c " + this.gitTask.status
    }

}
