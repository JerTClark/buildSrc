package org.snapper.buildsrc.web.css

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static org.snapper.buildsrc.file.ListFiles.listFiles

/**
 * Got a poorly formatted '.sass' file? Who doesn't?!... (yikes)...
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class SassFormat extends DefaultTask {
    File cssDir

    SassFormat() {
        group = "snapper-web"
        description = "Fix formatting errors in '.sass' files remaining after renaming a '.css' stylesheet to '.sass'"
    }

    @Input
    getCssDir() { this.cssDir }

    void setCssDir(File cssDir) {
        this.cssDir = cssDir
    }

    static formatSassFile(File sass) {
        String text = sass.text
        text = "${text.replaceAll(/[{};]/, "").replaceAll(/    /, "  ")}"
        sass.withWriter { writer ->
            writer.write text
        }
    }

    @TaskAction
    void start() {
        if(this.cssDir.isDirectory()) {
            def cssFiles = []
            this.cssDir.listFiles().each {
                cssFiles.addAll listFiles(it)
            }
            cssFiles.findAll {
                it.name.endsWith ".sass"
            }.each { File sass ->
                formatSassFile(sass)
            }
        } else {
            formatSassFile(this.cssDir)
        }
    }

}
