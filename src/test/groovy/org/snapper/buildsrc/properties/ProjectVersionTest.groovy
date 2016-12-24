package org.snapper.buildsrc.properties

import spock.lang.Shared
import spock.lang.Specification

class ProjectVersionTest extends Specification {

    @Shared ProjectVersion projectVersion

    def setup() {
        this.projectVersion = new ProjectVersion(1, 2, 3, false)
    }

    def "test ProjectVersion"() {
        expect:
        this.projectVersion.major == 1
        this.projectVersion.minor == 2
        this.projectVersion.patch == 3
        !this.projectVersion.release
        this.projectVersion.toString() == "1.2.3-SNAPSHOT"
    }

}
