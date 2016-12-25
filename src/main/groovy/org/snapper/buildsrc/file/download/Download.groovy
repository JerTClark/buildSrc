package org.snapper.buildsrc.file.download

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * This task encapsulates a download operation of a specified resource.
 * It is inspired by "Gradle Goodness" article "Distribute Custom Gradle
 * in Our Company" by Hubert Klein Ikkink
 * (<a href="http://mrhaki.blogspot.com/2012/10/gradle-goodness-distribute-custom.html"></a>).
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class Download extends DefaultTask {

    /**The base url of the resource's host site. The resource will be appended to this url.*/
    String baseUrl

    /**The name of the resource to download. It will be appended to the base url.*/
    String desiredResource

    /**A folder into which the resource will be downloaded.*/
    File destinationDir

    Download() {
        group = "snapper-resources"
        description = "Downloads a specified resource to a target location on the local machine"
    }

    @Input
    getBaseUrl() {
        return baseUrl
    }

    void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl
    }

    @Input
    getDesiredResource() {
        return desiredResource
    }

    void setDesiredResource(String resourceName) {
        this.desiredResource = resourceName
    }

    @Input
    getDestinationDir() {
        return destinationDir
    }

    void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir
    }

    void setDestinationDir(String destinationDir) {
        this.destinationDir = project.file destinationDir
    }

    String getResourceToDownload() {
        "${this.baseUrl}/${this.desiredResource}"
    }

    @OutputFile
    File getOutputFile() {
        new File(this.destinationDir, this.desiredResource)
    }

    @TaskAction
    void start() {
        if(!this.destinationDir.exists()) {
            this.destinationDir.mkdirs()
        }
        this.outputFile.bytes = new URL(this.resourceToDownload).bytes
    }

}
