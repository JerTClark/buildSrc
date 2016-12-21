package css

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static file.ListFiles.listFiles

class SassFormat extends DefaultTask {
    File cssDir

    SassFormat() {
        group = "snapper"
        description = "Fix formatting errors in '.sass' files remaining after renaming a '.css' stylesheet to '.sass'"
    }

    @Input
    getCssDir() { this.cssDir }

    void setCssDir(File cssDir) {
        this.cssDir = cssDir
    }

    @TaskAction
    void start() {
        def cssFiles = []

        cssDir.listFiles().each {
            cssFiles.addAll listFiles(it)
        }

        cssFiles.findAll {
            it.name.endsWith ".sass"
        }.each { sass ->
            String text = sass.text
            text = "${text.replaceAll(/[{};]/, "").replaceAll(/    /, "  ")}"
            sass.withWriter { writer ->
                writer.write text
            }
        }
    }

}
