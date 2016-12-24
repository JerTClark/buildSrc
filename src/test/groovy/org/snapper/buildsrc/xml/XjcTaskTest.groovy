package org.snapper.buildsrc.xml

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.snapper.buildsrc.file.ListFiles
import spock.lang.Shared
import spock.lang.Specification

class XjcTaskTest extends Specification {

    @Shared XjcTask xjcTask
    @Shared File outputFolder

    def setup() {
        Project project = ProjectBuilder.builder().build()

        File xsdFile = new File(this.getClass().classLoader.getResource("xml/bible.xsd").file)
        this.outputFolder = new File(this.getClass().classLoader.getResource("xml").file, "javaClasses")

        this.xjcTask = (XjcTask) project.task("xjcTask", type: XjcTask) {
            schemaFile xsdFile
            outputDir this.outputFolder
            options "-extension -Xinject-code"
        }
        this.xjcTask.execute()
    }

    def "test XjcTask test"() {
        expect:
        this.outputFolder.exists()
        ListFiles.listFiles(this.outputFolder).collect().size() > 0
    }

    def cleanup() {
        this.outputFolder.deleteDir()
    }

}