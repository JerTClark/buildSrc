package org.snapper.buildsrc.repackaging

import org.apache.tools.ant.Task
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.snapper.buildsrc.logging.BuildSrcLogger

/**
 * Tasks of this type encapsulate the running of JarJarLinks. There are two modes of use for this
 * task: (1) specify the current project's folder of class files or (2) specify a project configuration
 * whose dependencies will be used to populate a classpath from which a single or multiple Jar file(s)
 * will be obtained and processed. It is not intended that both of these be specified in a single task
 * (class files will take priority if this occurs and the project configuration will be ignored).
 * <p>This task will create a Jar file as its output that will contain the repackaged work.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class RepackageWithJarJar extends DefaultTask {

    /**The Jar file that is a result of repackaging with this task (it's output file).*/
    File outputJar

    /**
     * A configuration specified on the project that holds all the dependencies whose packages will be
     * analyzed and repackaged with JarJarLinks. It is this dependency's classpath from which Jar files
     * will be pulled and inspected by the task. Do not specify this property if using JarJarLinks on
     * the current project's class files.
     */
    String configurationForRepackaging = null

    /**
     * The directory containing the project's '.class' files. With Android, it is
     * difficult to specify a sensible default due to build variants. I've used {@code null}
     * for all of my projects for this field. I believe that the class files are only
     * used if it is our own project on which we're using JarJarLinks. Don't use this field
     * if intending to repackage using the alternative mode, specifying a project configuration
     * whose classpath will be used to pull in as (a) Jar file(s).
     */
    String classesDir = null/*project.pluginManager.hasPlugin("com.android.application") ||
            project.pluginManager.hasPlugin("com.android.library") ?
            "${project.buildDir}/intermediates/classes/debug" :
            "${project.buildDir}/classes/main"*/

    /**
     * When necessary, this task will create its own temporary folder into which it
     * will copy Jar files on the classpath of the configuration intended for repackaging.
     * It will not be deleted if this task is run in debug mode.
     */
    File tempFolder = project.file("temp")

    /**
     * This is a list of patterns used to do the matching. Each pattern will be
     * sought for in the classes (if a class directory has been specified) or the
     * Jar files of the configuration intended for repackaging. For each occurrence,
     * the matched pattern will be replaced by the string specified in the replacement
     * pattern list at the same index.
     */
    List matchingPatterns

    /**
     * This is a list of patterns used to replace matched patterns. Each item will be used to
     * replace a matched pattern according to its index in the list (i.e., the string at index
     * zero in this list will be used to replace a matching pattern in index zero of that list).
     */
    List replacementPatterns

    /**Whether or not to run in 'debug mode.' Useful if getting unexpected results. Lord knows I have before!*/
    boolean debugMode = false

    /**This property indicates whether or not the task is dealing with one or more Jar files to repackage.*/
    boolean multipleJars

    BuildSrcLogger log

    @OutputFile
    getOutputJar() {
        this.outputJar
    }

    void setOutputJar(File outputJar) {
        this.outputJar = outputJar
    }

    void setOutputJar(String outputJar) {
        this.outputJar = project.file(outputJar)
    }

    @Input
    @Optional
    getConfigurationForRepackaging() {
        this.configurationForRepackaging
    }

    void setConfigurationForRepackaging(String dependencyConfig) {
        this.configurationForRepackaging = dependencyConfig
    }

    void setConfigurationForRepackaging(Configuration dependencyConfig) {
        this.configurationForRepackaging = dependencyConfig
    }

    @Input
    @Optional
    getClassesDir() {
        this.classesDir
    }

    void setClassesDir(String classesDir) {
        this.classesDir = classesDir
    }

    void setClassesDir(File classesDir) {
        this.classesDir = classesDir.absolutePath
    }

    @Input
    @Optional
    getTempFolder() {
        this.tempFolder
    }

    void setTempFolder(File tempFolder) {
        this.tempFolder = tempFolder
    }

    void setTempFolder(String tempFolder) {
        this.tempFolder = project.file(tempFolder)
    }

    @Input
    getMatchingPatterns() {
        this.matchingPatterns
    }

    void setMatchingPatterns(List matchPatterns) {
        this.matchingPatterns = matchPatterns
    }

    void setMatchingPatterns(String... matchPatterns) {
        this.matchingPatterns = matchPatterns as List
    }

    @Input
    getReplacementPatterns() {
        this.replacementPatterns
    }

    void setReplacementPatterns(List replacePatterns) {
        this.replacementPatterns = replacePatterns
    }

    void setReplacementPatterns(String... replacePatterns) {
        this.replacementPatterns = replacePatterns as List
    }

    @Input
    @Optional
    getDebugMode() {
        return debugMode
    }

    void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode
    }

    RepackageWithJarJar() {
        group = 'snapper-build'
        description = 'Repackage classes and/or dependencies with JarJarLinks'
        onlyIf {
            this.classesDir != null || this.configurationForRepackaging != null
        }
        log = new BuildSrcLogger()
        log.init {
            logFile = new File("${System.getenv("USERPROFILE")}\\Desktop\\RepackageWithJarJarLog.txt")
        }
    }

    /**
     * This method adds a "jarJarConfiguration" and an "antConfiguration" to the project.
     * The "jarjarConfiguration declares a dependency on "com.googlecode.jarjar:jarjar:1.3"
     * and the "antConfiguration" declares one on "org.apache.ant:ant:1.9.6."
     * 
     * The reason I've brought in another version of Ant when Gradle is already exposing one
     * via the Groovy AntBuilder (I think that's how it works) is because the original Ant
     * jar has a package namespace structure expected by the JarJarLinks library. Eliminating
     * this version of Ant, and the AntClassLoader will not be able to "find org.apache.tools.zip.ZipExtraField"
     * because under Gradle it is "org.gradle.internal.impldep.org.apache.tools.zip.ZipExtraField."
     * Probably a better way to do what I want to do with JarJarLinks, but I've still got a lot
     * to learn.
     */
    def addProjectConfigDependency() {
        project.repositories {
            mavenCentral()
        }
        /*This configuration will bring in the JarJarLinks library.*/
        Configuration config = project.configurations.create("jarjarConfiguration")
        config.defaultDependencies {
            it.add project.dependencies.create("com.googlecode.jarjar:jarjar:1.3")
        }
        /*
         * This configuration will bring in a fit version of the Ant jar (one using the 
         * package namespaces expected by the JarJarLinks library).
         */
        Configuration antConfig = project.configurations.create("antConfiguration")
        antConfig.extendsFrom project.configurations.getByName("compile")
        antConfig.defaultDependencies {
            it.add project.dependencies.create("org.apache.ant:ant:1.9.6")
        }
    }

    /**
     * This method examines the items on the classpath of the consuming project's
     * configuration since it holds a reference to the dependencies intended for repackaging.
     */
    private void processConfigurationForRepackaging() {
        /*
         * Here we determine the quantity of Jar files being fed to this task (a.k.a., what's on the
         * classpath of the project configuration intended for repackaging).
         */
        final String input = this.configurationForRepackaging != null ?
                        project.configurations."${configurationForRepackaging}".asPath :
                        null
        final String[] inputs = input.split(";")

        /*Set multipleJars boolean accordingly*/
        this.multipleJars = input != null && input.split(";").size() > 1

        /*If the task is concerned with multiple Jars to process, we copy them into the temporary folder.*/
        if (this.multipleJars && inputs != null) {
            inputs.each { final String jarFile ->
                project.copy {
                    from jarFile
                    into this.tempFolder
                }
            }
        }
    }

    @TaskAction
    void start() {
        /*Adds a configuration and dependency to the project required by this task.*/
        this.addProjectConfigDependency()

        /*
         * Since a null classes directory indicates that we're processing a project configuration,
         * a null check is used to determine if we need to bother with looking for Jar file dependencies.
         */
        if (this.classesDir == null) {
            this.processConfigurationForRepackaging()
        }

        Project gradleProject = project
        project.ant {
            /*Here, we're setting up a new Ant "jarjar" task using the configuration we've added to the project via this task.*/
            taskdef(name: "jarjar", className: "com.tonicsystems.jarjar.JarJarTask", description: "Repackages with JarJarLinks") {
                classpath {
                    pathelement path: gradleProject.configurations."jarjarConfiguration".asPath
                    pathelement path: gradleProject.configurations."antConfiguration".asPath
                }
            }

            jarjar(jarfile: this.outputJar) {
                /*Using a null check to set the appropriate configuration (...is sort of hacky)*/
                if (this.classesDir != null) {
                    if (this.debugMode) {
                        log.record "JarJarLinks will repackage the classes directory", true
                    }
                    /*Set the fileset property on the Ant task*/
                    fileset dir: this.classesDir
                } else if (!this.multipleJars) {
                    if (this.debugMode) {
                        log.record "JarJarLinks will repackage a single Jar", true
                    }
                    /*Grab the single Jar file to repackage*/
                    File dependencyJar = new File(gradleProject.configurations."${configurationForRepackaging}".asPath as String)
                    /*Set the zipfileset property on the Ant task (we're using Jar files at this point)*/
                    zipfileset src: dependencyJar
                } else {
                    if (this.debugMode) {
                        log.record "JarJarLinks will repackage multiple Jars", true
                    }
                    /*Set the zipgroupfileset property on the Ant task (we're using multiple Jar files at this point)*/
                    zipgroupfileset dir: this.tempFolder
                }

                /*For each match pattern, we add a rule using the replace pattern at the same index.*/
                this.matchingPatterns.eachWithIndex { final matchingPattern, final int i ->
                    if (this.debugMode) {
                        log.record "${matchingPattern} --will be--> ${this.replacementPatterns[i]}", true
                    }
                    rule pattern: matchingPattern, result: this.replacementPatterns[i]
                }

            }

        }

        if (!this.debugMode) {
            /*Delete the temporary folder used by this task.*/
            project.file(this.tempFolder).deleteDir()
        }

    }

}