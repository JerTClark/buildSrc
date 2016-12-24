package org.snapper.buildsrc.logging

class BuildSrcLogger {

    /**This is the text file to which this class writes or appends logging messages. Change to suit.*/
    private File logFile

    def setDebugLog(File logFile) {
        this.logFile = logFile
    }

    /**
     * This method logs a message to a text file (specified by a field in this class).
     * @param message the message to log to the text file
     * @param append whether or not to append to the existing file, otherwise it will be overwritten
     */
    def record(final String message, final boolean append) {
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
        if (append) {
            logFile.withWriterAppend {
                it.newPrintWriter().println message
            }
        } else {
            logFile.withPrintWriter {
                it.println message + "\n"
            }
        }
    }

    def init(Closure closure) {
        closure.delegate = this
        closure()
    }

    static void main(String[] args) {
        log "Line 1", false
        log "Line 2", true
        log "Line 3", true
        log "Line 4", true
    }

}
