package org.snapper.buildsrc.file.download

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.snapper.buildsrc.file.download.Download
import spock.lang.Shared
import spock.lang.Specification

class DownloadTest extends Specification {

    @Shared Download download

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.download = (Download) project.task("download", type: Download) {
            baseUrl "https://github.com"
            desiredResource "fluidicon.png"
            destinationDir "download"
        }
        this.download.execute()
    }

    def "test Download test"() {
        expect:
        this.download.outputFile.exists()
    }

}