package temporary

import org.gradle.api.Project

class TemporaryFiles {

    static void tryToDelete(Project project, String startsWith = null, String endsWith = null) {
        def filter
        if (startsWith && endsWith)
            filter = {File dir, String name ->
                name.startsWith(startsWith) || name.endsWith(endsWith)}
        else if (startsWith && !endsWith)
            filter = {File dir, String name -> name.startsWith(startsWith)}
        else filter = {File dir, String name -> name.endsWith(endsWith)}

        project.file(System.getenv("Temp"))
                .listFiles(filter as FilenameFilter).each {
            project.logger.info "Deleting $it"
            it.isDirectory() ? it.deleteDir() : it.delete()
        }
    }

}
