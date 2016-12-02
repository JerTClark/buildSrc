package temporary

import org.gradle.api.Project

class TemporaryFiles {

    static void tryToDelete(final Project project, final String startsWith = null, final String endsWith = null) {
        final def filter
        if (startsWith && endsWith)
            filter = { final File dir, final String name ->
                name.startsWith(startsWith) || name.endsWith(endsWith)}
        else if (startsWith && !endsWith)
            filter = { final File dir, final String name -> name.startsWith(startsWith)}
        else filter = { final File dir, final String name -> name.endsWith(endsWith)}

        project.file(System.getenv("Temp"))
                .listFiles(filter as FilenameFilter).each {
            project.logger.info "Deleting $it"
            it.isDirectory() ? it.deleteDir() : it.delete()
        }
    }

}
