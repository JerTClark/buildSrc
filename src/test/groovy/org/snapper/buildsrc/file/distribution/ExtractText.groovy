package org.snapper.buildsrc.file.distribution

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.snapper.buildsrc.file.TemporaryFile
import spock.lang.Shared
import spock.lang.Specification

class ExtractTest extends Specification {

    @Shared Extract extract

    def setup() {
        Project project = ProjectBuilder.builder().build()

        File archiveA = new File(this.getClass().classLoader.getResource("extract/ant-antlr.jar").file)
        File archiveB = new File(this.getClass().classLoader.getResource("extract/demoDist.zip").file)

        this.extract = (Extract) project.task("extract", type: Extract) {
            archiveFiles archiveA, archiveB
            outputDir "extractDir"
        }

        this.extract.execute()
    }

    def "test Extract test"() {
        expect:
        this.extract.outputDir.listFiles().size() > 0
        this.extract.getOutputFileByName(".gitignore").exists()
    }

}