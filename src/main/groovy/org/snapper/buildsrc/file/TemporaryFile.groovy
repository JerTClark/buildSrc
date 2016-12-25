package org.snapper.buildsrc.file

/**
 * A basic extension of a file object that encapsulates a temporary
 * file that will reside in the current system's temporary directory
 */
class TemporaryFile {

    private final File file
    private final File parent

    private final String temp = System.getenv("TEMP")

    /**
     * Creates a temporary file with the provided name.
     * @param filename the name of the temporary file to create
     */
    TemporaryFile(String filename) {
        this.file = new File(this.temp, filename)
        this.parent = null
    }

    TemporaryFile(String parent, String child) {
        this.file = new File(this.temp + "/" + parent, child)
        this.parent = new File(this.temp + "/" + parent)
    }

    TemporaryFile(File parent, String child) {
        this.file = new File(this.temp + "/" + parent.getName(), child)
        this.parent = new File(this.temp + "/" + parent.getName())
    }

    /**Create the file and its parent (if necessary) as a directory.*/
    def mkdir() {
        if(this.parent != null) {
            this.mkdirs()
        } else {
            this.file.mkdir()
        }
    }

    String getAbsolutePath() {
        this.file.absolutePath
    }

    String getName() {
        this.file.name
    }

    /**Creates the directories required by the file.*/
    def mkdirs() {
        this.file.mkdirs()
    }

    /**Whether or not the file currently exists.*/
    def exists() {
        this.file.exists()
    }

    /**
     * This method encapsulates the work of deleting the temporary file
     * whether it is a directory or a file of some other kind.
     */
    def finished() {
        /*Delete the parent directory or the file, itself*/
        this.parent != null ? this.parent.deleteDir() :
        this.file.isDirectory() ? this.file.deleteDir() : this.file.delete()
    }

}
