package jenkins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class JenkinsBuildTrigger extends DefaultTask {
    @Input
    String apiToken
    private String urlString

    JenkinsBuildTrigger() {
        group = "pipeline"
        description = "Trigger a Jenkins build"
    }

    @TaskAction
    void start() {
        urlString  = "http://10.34.177.109:9090/job/AndroidStrings/build?token=${this.apiToken}"
        HttpURLConnection connection = null
        try {
            URL url =
                    new URL(urlString)
            connection = (HttpURLConnection) url.openConnection()
            connection.setRequestMethod("POST")
            connection.setUseCaches(false)
            connection.setDoOutput(true)
            int code = connection.getResponseCode()
            println "Connection to ${url.toString()}"
            switch (code) {
                case 201:
                    project.logger.info(code + " OK")
                    break;
                case 403:
                    project.logger.info(code + " Forbidden")
                    break;
                default:
                    project.logger.info(code + " unknown code")
                    break;
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
