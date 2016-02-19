package jer.buildsrc.testing

import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

class NotificationTestListener implements TestListener {
    final Project project

    NotificationTestListener(Project project) {
        this.project = project
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        if(!suite.parent && result.getTestCount() > 0) {
            String testSingularOrPlural = result.getTestCount() > 1 ? "tests" : "test"
            long elapsedTime = result.getEndTime() - result.getStartTime()
            project.announce.announce("""Elapsed time for execution of ${result.getTestCount()} $testSingularOrPlural: ${elapsedTime} ms""", 'local')
        }
    }

    @Override
    void beforeSuite(TestDescriptor suite) {}
    @Override
    void beforeTest(TestDescriptor testDescriptor) {}
    @Override
    void afterTest(TestDescriptor testDescriptor, TestResult result) {}
}