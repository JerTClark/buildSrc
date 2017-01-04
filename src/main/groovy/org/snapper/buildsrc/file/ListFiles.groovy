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

    /**
     * Processes a folder, obtaining the relative paths of its
     * files. The default parameter for parent indicates it is
     * okay to provide an empty string and let this method take
     * care of recursive calls in which it will be provided.
     */
    static listFilesWithRelativePaths(File folder, String parent = "", Closure block) {
        folder.listFiles().each {
            String name = (parent) ? parent + "/" + it.name : it.name
            if(it.isDirectory()) {
                it.listFiles().each {
                    if(it.isDirectory()) {
                        listFilesWithRelativePaths(it, name + "/" + it.name, block)
                    } else {
                        block "${name}/${it.name}"
                    }
                }
            } else {
                block "${name}"
            }
        }
    }

}
