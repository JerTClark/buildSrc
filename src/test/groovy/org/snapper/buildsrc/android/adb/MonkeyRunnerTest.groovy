package org.snapper.buildsrc.android.adb

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class MonkeyRunnerTest extends Specification {

    @Shared
    MonkeyRunner monkeyRunner

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.monkeyRunner = (MonkeyRunner) project.task("monkeyRunner", type: MonkeyRunner) {
            applicationPackageName "dummy.package.name"
            report "report.txt"
        }
    }

    def "test MonkeyRunner test"() {
        expect:
        this.monkeyRunner.numberOfTests == 1000
        this.monkeyRunner.applicationPackageName == "dummy.package.name"
        this.monkeyRunner.report.name == "report.txt"
    }

}