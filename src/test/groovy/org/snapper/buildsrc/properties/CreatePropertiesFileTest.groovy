package org.snapper.buildsrc.properties

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class CreatePropertiesFileTest extends Specification {

    @Shared CreatePropertiesFile createPropertiesFile
    @Shared Map propMap

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.propMap = ["name":"Jeremy", "age": 1]
        this.createPropertiesFile =
                (CreatePropertiesFile) project.task("createPropertiesFile", type: CreatePropertiesFile) {
                    propertiesMap propMap
                    propertiesFile project.file("test.properties")
                }
        this.createPropertiesFile.execute()
    }

    def "test CreatePropertiesFile task"() {
        expect:
        this.createPropertiesFile.propertiesMap == this.propMap
        this.createPropertiesFile.propertiesFile.exists()
    }

}
