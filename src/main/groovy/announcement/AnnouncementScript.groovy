package announcement

import org.gradle.api.Project

class SuccessfulBuildAnnouncement {
    private Project project
    SuccessfulBuildAnnouncement(final Project project) {
        this.project = project
    }
    def show() {
        this.project.announce.announce "${this.project.rootProject.name}:${this.project.name} BUILD SUCCESSFUL", 'local'
    }
}

class CustomAnnouncement {
    private Project project
    private String message
    CustomAnnouncement(final Project project, final String message) {
        this.project = project
        this.message = message
    }
    def show() {
        this.project.announce.announce "${this.message}", 'local'
    }
}