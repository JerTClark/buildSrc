package org.snapper.buildsrc.web.css

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class SassFormatTest extends Specification {

    @Shared SassFormat sassFormat
    @Shared File sassFile

    def setup() {
        Project project = ProjectBuilder.builder().build()

        this.sassFile = new File(this.getClass().classLoader.getResource("tempSassFormat/temp.sass").file)

        this.sassFormat = (SassFormat) project.task("sassFormat", type: SassFormat) {
            cssDir this.sassFile
        }
        this.sassFormat.execute()
    }

    def "test SassFormat test"() {
        expect:
        this.sassFile.exists()
        this.sassFile.text == "html \r\n  color: black\r\n  display: inline-block\r\n"
    }

}