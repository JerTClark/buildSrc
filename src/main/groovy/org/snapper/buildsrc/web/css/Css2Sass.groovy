package org.snapper.buildsrc.web.css

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static org.snapper.buildsrc.file.ListFiles.listFiles

/**
 * This task creates a properly formatted '.sass' file for each '.css' stylesheet
 * in a specified folder (or simply a single file).
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class Css2Sass extends DefaultTask {

    /**The stylesheets' parent folder.*/
    File cssDir

    Css2Sass() {
        group = "snapper-web"
        description = "Create sass ('.sass') files from a folder of '.css' stylesheets"
    }

    @Input
    getCssDir() { this.cssDir }

    void setCssDir(File cssDir) {
        this.cssDir = cssDir
    }

    void setCssDir(String cssDir) {
        this.cssDir = project.file(cssDir)
    }

    /**
     * Performs the generation of the '.sass' file from a given '.css' file.
     */
    static def generateSass(File css) {
        String text = css.text
        text = "${text.replaceAll(/[{};]/, "").replaceAll(/    /, "  ")}"
        File sass = new File("${css.parent}", "${css.name - ".css" + ".sass"}")
        if (!sass.exists()) {
            sass.createNewFile()
        }
        sass.withWriter { writer ->
            writer.write text
        }
    }

    @TaskAction
    void start() {
        if (this.cssDir.isDirectory()) {
            def cssFiles = []
            this.cssDir.listFiles().each {
                cssFiles.addAll listFiles(it)
            }
            cssFiles.findAll {
                it.name.endsWith ".css"
            }.each { File css ->
                generateSass(css)
            }
        } else {
            generateSass(this.cssDir)
        }
    }

}
