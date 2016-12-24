package org.snapper.buildsrc.announcement

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

class AnnouncementTest extends Specification {

    def messageText = "This is a message"
    @Shared Announcement announcement

    def setup() {
        Project project = ProjectBuilder.builder().build()
        this.announcement = (Announcement) project.task("announce", type: Announcement) {
            message messageText
            type "local"
        }
        this.announcement.execute()
    }

    def "test Announce task"() {
        expect:
        this.announcement.mockStart() == this.messageText
        this.announcement.group == "snapper-announcement"
        this.announcement.description == "Applies the 'announce' plugin to the project and announces a message of a specified type"
    }

}
