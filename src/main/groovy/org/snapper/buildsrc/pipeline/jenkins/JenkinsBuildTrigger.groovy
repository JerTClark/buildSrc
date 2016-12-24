package org.snapper.buildsrc.pipeline.jenkins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class JenkinsBuildTrigger extends DefaultTask {

    /**The api token associated with this project in the Jenkins configuration.*/
    String apiToken

    /**The url to the Jenkins server.*/
    private String url

    @Input
    String getApiToken() {
        this.apiToken
    }

    void setApiToken(String apiToken) {
        this.apiToken = apiToken
    }

    @Input
    String getUrl() {
        this.url
    }

    void setUrl(String url) {
        this.url = url
    }

    JenkinsBuildTrigger() {
        group = "snapper-build-jenkins"
        description = "Trigger a build on the Jenkins instance"
    }

    @TaskAction
    void start() {
        /*i.e., http://10.34.177.109:9090/job/AndroidStrings/build?token=${this.apiToken}*/
        final def urlString  = "${this.url}${this.apiToken}"
        final HttpURLConnection connection
        try {
            final URL url =
                    new URL(url)
            connection = (HttpURLConnection) url.openConnection()
            connection.setRequestMethod("POST")
            connection.setUseCaches(false)
            connection.setDoOutput(true)
            final int code = connection.getResponseCode()
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
        } catch (final Exception e) {
            logger.info(e.getMessage());
        }
    }
}
