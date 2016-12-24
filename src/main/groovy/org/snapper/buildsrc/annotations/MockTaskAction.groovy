package org.snapper.buildsrc.annotations

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Use this annotation to mark a mock implementation of a custom Gradle
 * task's task action method used for testing.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface MockTaskAction {

}