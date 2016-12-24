package org.snapper.buildsrc.web.css

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static org.snapper.buildsrc.file.ListFiles.listFiles

/**
 * A task type that cleans (deletes) all '.sass,' '.scss,' and '.css.map' files in a specified folder,
 * intended for leaving only '.css' files.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class SassClean extends DefaultTask {

    /**The stylesheets' parent folder.*/
    File cssDir

    SassClean() {
        group = "snapper-web"
        description = "Cleans (deletes) all '.sass,' '.scss,' and '.css.map' files in a specified folder"
    }

    @Input
    getCssDir() { this.cssDir }

    void setCssDir(File cssDir) {
        this.cssDir = cssDir
    }

    @TaskAction
    void start() {
        def cssFiles = []

        this.cssDir.listFiles().each {
            cssFiles.addAll listFiles(it)
        }

        cssFiles.findAll {
            it.name.endsWith(".sass") || it.name.endsWith(".scss") || it.name.endsWith(".css.map")
        }.each { sass ->
            sass.delete()
        }
    }

}
