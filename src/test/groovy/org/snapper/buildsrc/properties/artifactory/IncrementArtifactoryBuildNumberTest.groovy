package org.snapper.buildsrc.properties.artifactory

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class IncrementArtifactoryBuildNumberTest extends Specification {

    @Shared IncrementArtifactoryBuildNumber incrementArtifactoryBuildNumber
    @Shared int buildNumber

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.incrementArtifactoryBuildNumber =
                (IncrementArtifactoryBuildNumber) project.task("iABN", type: IncrementArtifactoryBuildNumber) {
                    propertiesFile = "gradle.demo.properties"
                }
        this.incrementArtifactoryBuildNumber.execute()
        project.ant.loadproperties srcFile: this.incrementArtifactoryBuildNumber.propertiesFile
        this.buildNumber = Integer.valueOf(project.ant.getProperty(this.incrementArtifactoryBuildNumber.buildNumberKey) as String)
    }

    def "test IncrementArtifactoryBuildNumber test"() {
        expect:
        this.buildNumber == 1
    }

    def cleanup() {
        assert this.incrementArtifactoryBuildNumber.propertiesFile.delete()
    }
}
