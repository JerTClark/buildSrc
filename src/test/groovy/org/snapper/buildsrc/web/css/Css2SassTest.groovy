package org.snapper.buildsrc.web.css

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class Css2SassTest extends Specification {

    @Shared Css2Sass css2Sass
    @Shared File cssFolder
    @Shared File cssFile, sassFile

    def setup() {
        Project project = ProjectBuilder.builder().build()

        this.cssFolder = new File(this.getClass().classLoader.getResource("tempCss").file)
        this.cssFile = new File(this.getClass().classLoader.getResource("tempCss/temp.css").file)
        this.sassFile = new File(this.cssFolder.absolutePath, "temp.sass")

        this.css2Sass = (Css2Sass) project.task("css2Sass", type: Css2Sass) {
            cssDir this.cssFolder
        }
        this.css2Sass.execute()
    }

    def "test Css2Sass test"() {
        expect:
        this.cssFolder.exists()
        this.cssFile.exists()
        this.sassFile.exists()
        this.cssFile.text != this.sassFile.text
        this.sassFile.text == "html \n  color: black\n  display: inline-block\n"
    }

    def cleanup() {
        this.sassFile.delete()
    }

}
