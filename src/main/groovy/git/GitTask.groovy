package git

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class GitTask extends DefaultTask {

    @Input
    String commitMessage
    /**
     * The path to the user's public key
     */
    private String idPub = "-i /c/Users/Clark/.ssh/idFAM_rsa.pub",
            status = "cmd /c git status",
            add = "cmd /c git add -A .",
            commit = "cmd /c git commit -m \"${commitMessage}\"",
            push = "cmd /c git push ${idPub}"

    GitTask() {
        group = "pipeline"
        description = "Add all files and commit changes with the git command"
    }

    @TaskAction
    void start() {
        println status.execute().text
        println add.execute().text
        println status.execute().text
        println commit.execute().text
    }
}
