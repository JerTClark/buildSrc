package org.snapper.buildsrc.web.css

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class SassCleanTest extends Specification {

    @Shared SassClean sassClean
    @Shared File sassFolder
    @Shared File cssFile, sassFile

    def setup() {
        Project project = ProjectBuilder.builder().build()

        this.sassFolder = new File(this.getClass().classLoader.getResource("tempSass").file)
        this.cssFile = new File(this.getClass().classLoader.getResource("tempSass/temp.css").file)
        this.sassFile = new File(this.sassFolder.absolutePath, "temp.sass")

        this.sassClean = (SassClean) project.task("sassClean", type: SassClean) {
            cssDir this.sassFolder
        }
        this.sassClean.execute()
    }

    def "test Css2Sass test"() {
        expect:
        this.sassFolder.exists()
        this.cssFile.exists()
        !this.sassFile.exists()
    }

    def cleanup() {
        this.sassFile.createNewFile()
        this.sassFile.withWriter {
            it.write "html \n  color: black\n  display: inline-block\n"
        }
    }

}