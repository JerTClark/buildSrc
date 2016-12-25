package org.snapper.buildsrc.ant

import org.gradle.api.Project

/**
 * This class is used to expose some simple convenience methods
 * for working with Ant.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class AntHelper {

    /**Prevent accidental instantiation.*/
    private AntHelper() {
        throw new InstantiationError(AntHelper.class.getSimpleName() + " is not intended for instantiation");
    }

    /**Clear Ant's default excludes patterns.*/
    static clearDefaultExcludes(Project project) {
        File defExcludes =
                new File(project.getClass().classLoader
                        .getResource("defaultexcludes.txt").file)
        defExcludes.eachLine { line ->
            project.ant.defaultexcludes(remove: line)
        }
    }

    /**Restores Ant's default excludes to their original defaults.*/
    static restoreDefaultExcludes(Project project) {
        project.ant.defaultexcludes(default: true)
    }

}
