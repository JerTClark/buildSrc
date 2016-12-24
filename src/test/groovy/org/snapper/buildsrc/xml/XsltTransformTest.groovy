package org.snapper.buildsrc.xml

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class XsltTransformTest extends Specification {

    @Shared XsltTransform xsltTransform
    @Shared File xmlResult

    def setup() {
        Project project = ProjectBuilder.builder().build()

        File xmlOrig = new File(this.getClass().getClassLoader().getResource("xml/style.xml").file)
        File xslt = new File(this.getClass().getClassLoader().getResource("xml/style.xsl").file)
        this.xmlResult = new File(this.getClass().getClassLoader().getResource("xml").file, "restyled.xml")

        this.xsltTransform = (XsltTransform) project.task("xsltTransform", type: XsltTransform) {
            originalXml xmlOrig
            xsltFile xslt
            outputFile this.xmlResult
        }
        this.xsltTransform.execute()
    }

    def "test XsltTransform test"() {
        expect:
        this.xmlResult.exists()
    }

    def cleanup() {
        this.xmlResult.delete()
    }

}