package css

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static file.ListFiles.listFiles

class Css2Sass extends DefaultTask {
    File cssDir

    Css2Sass() {
        group = "snapper"
        description = "Create a Sass ('.sass') file from a folder of '.css' stylesheets"
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
            it.name.endsWith ".css"
        }.each { css ->
            String text = css.text
            text = "${text.replaceAll(/[{};]/, "").replaceAll(/    /, "  ")}"
            File sass = new File("${css.parent}", "${css.name - ".css" + ".sass"}")
            if(!sass.exists()) { sass.createNewFile() }
            sass.withWriter { writer ->
                writer.write text
            }
        }
    }

}
