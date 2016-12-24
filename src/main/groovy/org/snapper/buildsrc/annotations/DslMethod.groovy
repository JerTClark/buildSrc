package org.snapper.buildsrc.annotations

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Use this annotation to mark methods intended to be a part of
 * the configuration DSL of a custom task.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
@Retention(RetentionPolicy.CLASS)
@Target([ElementType.CONSTRUCTOR, ElementType.METHOD])
@interface DslMethod {

}