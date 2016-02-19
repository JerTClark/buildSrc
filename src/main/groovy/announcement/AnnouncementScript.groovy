package jer.buildsrc.announcement

import org.gradle.api.Project

class SuccessfulBuildAnnouncement {
    private Project project
    SuccessfulBuildAnnouncement(Project project) {
        this.project = project
    }
    def show() {
        this.project.announce.announce "${this.project.rootProject.name}:${this.project.name} build successfully", 'local'
    }
}

class CustomAnnouncement {
    private Project project
    private String message
    CustomAnnouncement(Project project, String message) {
        this.project = project
        this.message = message
    }
    def show() {
        this.project.announce.announce "${this.message}", 'local'
    }
}