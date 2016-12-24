package org.snapper.buildsrc.announcement

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.annotations.MockTaskAction

/**
 * Tasks of this type apply the "announce" plugin to the project and
 * fire an announcement as specified by a simple message parameter.
 */
class Announcement extends DefaultTask {

    /**The message to display via the announce plugin.*/
    String message

    /**
     * The type of the announcement.
     * Options are:
     * <ul>
     *     <li>twitter</li>
     *     <li>snarl (Windows)</li>
     *     <li>growl (Mac OS X)</li>
     *     <li>notify-send (Ubuntu)</li>
     *     <li>local (the default)</li>
     * </ul>
     * Some of these options (not the 'local' default) might require the
     * {@code announce} closure (i.e., to specify username and password).
     */
    String type = "local"

    Announcement() {
        group = "snapper-announcement"
        description = "Applies the 'announce' plugin to the project and announces a message of a specified type"
    }

    @Input
    getMessage() { this.message }

    void setMessage(String message) {
        this.message = message
    }

    @Input
    @Optional
    getType() { this.type }

    void setType(String type) {
        switch(type) {
            case "twitter":
            case "snarl":
            case "growl":
            case "notify-send":
            case "local":
                this.type = type
                break
            default:
                this.type = "local"
                break
        }
    }

    @TaskAction
    void start() {
        project.apply plugin: "announce"
        project.announce.announce this.message, "local"
    }

    /**
     * Mocks the actual task execution by returning the message that would otherwise be shown.
     */
    @MockTaskAction
    String mockStart() {
        this.message
    }

}
