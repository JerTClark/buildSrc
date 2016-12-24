package org.snapper.buildsrc.file

/**
 * Exposes static method to list all non-directory files in
 * a file object including those in subdirectories.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class ListFiles {

    /**List the files in a directory and its subdirectories.*/
    static listFiles = { File file ->
        if (file.isDirectory()) {
            file.listFiles().each {
                if (it.isDirectory()) {
                    trampoline(it)
                } else {
                    it
                }
            }
        } else { file }
    }

}
