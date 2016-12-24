package org.snapper.buildsrc.properties

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class ReleaseVersionTaskTest extends Specification {

    @Shared ReleaseVersionTask releaseVersionTask

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.releaseVersionTask =
                (ReleaseVersionTask) project.task("releaseVersionTask", type: ReleaseVersionTask) {
                    release false
                    propertiesFile project.file("test.properties")
                }
        this.releaseVersionTask.execute()
    }

    def "test ReleaseVersion task"() {
        expect:
        !this.releaseVersionTask.release
        this.releaseVersionTask.propertiesFile.exists()
    }

}
