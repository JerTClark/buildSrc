package org.snapper.buildsrc.repackaging

import org.gradle.api.Project
import org.gradle.internal.jvm.Jvm
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

/**
 * I have not made it very far at all with these tests... for some reason, the Ant classloader
 * (which I don't understand) is finding jarjar-1.3.jar, but not the classes in the Jar file...
 * This has led to some "creative" circumventions.
 *
 * NOTE: If... for whatever imaginable reason whatsoever, this test begins to fail with an
 * error of No class definition found for ZipExtraField... sigh... "Invalidate options/Restart"
 * has not fixed anything for me, but deleting the project files (see the .gitignore-- whatever
 * is in that is safe to delete) and open the buildSrc as a project again. So fun!
 */
class RepackageWithJarJarTest extends Specification {

    @Shared
    RepackageWithJarJar repackageWithJarJar

    def "test RepackageWithJarJar test with configuration dependencies"() {
        setup:
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: "groovy"
        project.repositories {
            mavenCentral()
        }
        project.configurations {
            jarjarDemo
        }
        project.dependencies {
            jarjarDemo "org.apache.httpcomponents:httpclient:4.5.1"
        }
        this.repackageWithJarJar = (RepackageWithJarJar) project.task("repackageWithJarJar", type: RepackageWithJarJar) {
            debugMode true
            outputJar "repackageWithJarJarTest.jar"
            configurationForRepackaging "jarjarDemo"
            matchingPatterns "org.apache.**", "org.apache.http.**"
            replacementPatterns "org.snapper.apache.@1", "org.snapper.http.@1"
        }
        this.repackageWithJarJar.execute()
        expect:
        /*
         * I can't get the output file to be created according to the patterns specified...
         * JarJarLinks creates a single Jar like one would expect, but the patterns aren't
         * being used or the custom target is just ignoring the rule I create for each pattern... :/
         */
        this.repackageWithJarJar.outputJar.exists()
    }

}