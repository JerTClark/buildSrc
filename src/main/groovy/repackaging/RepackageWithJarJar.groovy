package repackaging

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class RepackageWithJarJar extends DefaultTask {
    @OutputFile File outputJar

    String dependencyConfig
    String jarJarConfig
    String byteCodeClasses
    String tempFolder
    String[] replacePatterns
    String[] resultPatterns
    boolean debugMode

    RepackageWithJarJar() {
        group = 'snapper'
        description = 'Repackage problematic dependencies with JarJarLinks'
    }

    @TaskAction
    void start() {
        final File dependencyJar
        final String zipGroupFileset
        final String input = project.configurations."${dependencyConfig}".asPath
        logger.info "input: ${input}"
        final String[] inputs = input.split(";")
        if (inputs.size() == 1) {
            dependencyJar = new File(input)
            logger.info "dependencyJar: ${dependencyJar}"
        } else if (inputs.size() > 1) {
            inputs.each { final String jarFile ->
                project.copy {
                    from jarFile
                    into tempFolder
                }
            }
            zipGroupFileset = tempFolder
        }
        final String jarJarClasspath = project.configurations."${jarJarConfig}".asPath
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
                    logger.info "byteCodeClasses: ${byteCodeClasses}"
                    fileset(dir: byteCodeClasses)
                }
                if (dependencyJar != null) {
                    logger.info "dependencyJar: ${dependencyJar}"
                    zipfileset(src: dependencyJar)
                }
                if (zipGroupFileset != null) {
                    logger.info "JarJarring contents of ${tempFolder}"
                    logger.info "tempFolder contents: ${new File(tempFolder).list()}"
                    zipgroupfileset(dir:tempFolder)
                }
                replacePatterns.eachWithIndex { final String pattern, final int i ->
                    logger.info "Converting ${pattern} to ${resultPatterns[i]}"
                    rule pattern: pattern, result: resultPatterns[i]
                }
            }
        }
        if (!debugMode) project.file(tempFolder).deleteDir()
    }
}