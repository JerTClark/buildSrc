package org.snapper.buildsrc.file.distribution

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class DistributionTest extends Specification {

    @Shared Distribution distribution
    @Shared Project project

    def setup() {
        this.project = ProjectBuilder.builder().build()
        String distFolder = this.getClass().classLoader.getResource("dist").file
        this.distribution = (Distribution) this.project.task("distribution", type: Distribution) {
            distContents "${distFolder}/README.md", "${distFolder}/build.gradle"
            distName "demoDist"
            destinationDir "distribution"
        }
        this.distribution.execute()
    }

    def "test Distribution test"() {
        expect:
        this.distribution.outputFile.exists()
        this.project.zipTree(this.distribution.outputFile).files.findAll {
            it.name == "README.md" || it.name == "build.gradle"
        }.size() == 2
    }

}