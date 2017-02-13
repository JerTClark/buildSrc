package org.snapper.buildsrc.web.css

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import static org.snapper.buildsrc.file.ListFiles.listFilesWithRelativePaths

/**
 * This class defines a type of task that can process a folder of font files
 * (such as ".ttf" files) and generates a single "fontfaces.css" file. The
 * generated file will feature an "@fontface" block exposing each font file
 * asset in the specified folder.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class Fontface extends DefaultTask {

    /**
     * A file tree specifying a folder or folders that contains font assets (such as .ttf files).
     */
    FileTree fonts

    /**The output file that will contain the generated '@fontface' blocks.*/
    File fontfacesFile

    /**A string that will specify the relative 'src: url' for all '@fontface' blocks (i.e., "../css").*/
    String srcUrl

    Fontface() {
        group = "snapper-web"
        description = "Create a '.css' stylesheet containing an '@font-face' block for " +
                "each font asset found in a specified folder"
    }

    @Input
    getFonts() { this.fonts }

    void setFonts(String fontDir) {
        if (this.fonts != null) {
            this.fonts += project.fileTree(dir: fontDir)
        } else {
            this.fonts = project.fileTree(dir: fontDir)
        }
    }

    void setFonts(File fontDir) {
        if (this.fonts != null) {
            this.fonts += project.fileTree(fontDir)
        } else {
            this.fonts = project.fileTree(fontDir)
        }
    }

    void setFonts(FileTree fonts) {
        this.fonts = fonts
    }

    void setFonts(Map map) {
        this.fonts = project.fileTree(map)
    }

    @Input
    getFontfacesFile() { this.fontfacesFile }

    void setFontfacesFile(File fontfacesFile) {
        this.fontfacesFile = fontfacesFile
    }

    void setFontfacesFile(String fontfacesFile) {
        this.fontfacesFile = fontfacesFile.endsWith(".css") ?
                new File(fontfacesFile) : new File(fontfacesFile + ".css")
    }

    @Input
    getSrcUrl() { this.srcUrl }

    void setSrcUrl(String srcUrl) { this.srcUrl = srcUrl }


    @TaskAction
    void start() {
        this.srcUrl = this.srcUrl.replaceAll("\\\\", "/")

        if (this.fontfacesFile.exists()) {
            this.fontfacesFile.delete()
            this.fontfacesFile.createNewFile()
        }

        this.fontfacesFile.withWriter { writer ->
            this.fonts.visit {
                if (!it.isDirectory()) {
                    writer.append """@font-face {
    font-family: "${it.name}";
    src: url("${this.srcUrl.endsWith("/") ? this.srcUrl : this.srcUrl + "/"}${it.relativePath}");
}

"""
                }
            }
        }

    }

}
