package org.snapper.buildsrc.web.css

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class FontfaceTest extends Specification {

    @Shared
    Fontface fontface
    Project project = ProjectBuilder.builder().build()

    def setup() {

//        def fontsFolder = new File(this.getClass().classLoader.getResource("fonts").file)
        def fontsFolder = project.file(project.projectDir.absolutePath + "\\src\\test\\resources\\fonts")
        String fontFile = "fontfaces.css"
        String srcUrlString = "../css"

        this.fontface = (Fontface) project.task("fontface", type: Fontface) {
            fonts (dir: fontsFolder, excludes: ["DONTINCLUDEME.txt", "git**"])
            fontfacesFile fontFile
            srcUrl srcUrlString
        }
        this.fontface.execute()
    }

    def "test Fontface test"() {
        expect:
        this.fontface.fontfacesFile.exists()
    }

    void cleanup() {
        expect:
        project.file("fontfaces.css").deleteOnExit()
    }
}