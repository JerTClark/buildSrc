package troubleshoot

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class JarJarExperiments extends DefaultTask {
    @OutputFile File outputJar

    String dependencyConfig
    String jarJarConfig
    String byteCodeClasses
    String tempFolder
    String copyTo
    String[] replacePatterns
    String[] resultPatterns

    JarJarExperiments() {
        group = 'verification'
        description = 'Try experimental things while repackaging ' +
                'problematic dependencies with JarJarLinks'
    }

    @TaskAction
    void start() {
        File dependencyJar
        String zipGroupFileset
        //Multiple jar files on the project configuration path
        String input = project.configurations."${dependencyConfig}".asPath
        logger.info "input: ${input}"
        String[] inputs = input.split(";")
        if (inputs.size() == 1) {
            dependencyJar = new File(input)
            logger.info "dependencyJar: ${dependencyJar}"
        } else if (inputs.size() > 1) {
            inputs.each {String jarFile ->
                project.copy {
                    from jarFile
                    into tempFolder
                }
            }
            zipGroupFileset = tempFolder
        }
        String jarJarClasspath = project.configurations."${jarJarConfig}".asPath
        project.ant {
            taskdef name: "jarjar", className: "com.tonicsystems.jarjar.JarJarTask",
                    classpath: "${new File(jarJarClasspath).absolutePath}"
            /**
             * fileset The bytecode classes or source files?
             * zipfileset The jar file containing the pattern to replace via the rule
             * rule The pattern to match and what to replace it with
             * @param outputJar The String name of the jar file resulting from this task
             */
            jarjar(jarfile: outputJar) {
                if (byteCodeClasses != null) {
                    logger.info "JarJarring ${byteCodeClasses}"
                    println "byteCodeClasses: ${byteCodeClasses}"
                    fileset(dir: byteCodeClasses)
                }
                if( dependencyJar != null) {
                    logger.info "JarJarring ${dependencyJar}"
                    println "dependencyJar: ${dependencyJar}"
                    zipfileset(src: dependencyJar)
                }
                if (zipGroupFileset != null) {
                    logger.info "JarJarring contents of ${tempFolder}"
                    println "tempFolder contents: ${new File(tempFolder).list()}"
                    zipgroupfileset(dir:tempFolder)
                }
                replacePatterns.eachWithIndex { String pattern, int i ->
                    println "Converting ${pattern} to ${resultPatterns[i]}"
                    rule pattern: pattern, result: resultPatterns[i]
                }
            }
        }
        project.file(tempFolder).deleteDir()
    }
}