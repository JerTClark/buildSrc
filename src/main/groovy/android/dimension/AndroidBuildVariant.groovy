package android.dimension

import groovy.transform.Canonical

/**
 * Specify the target build variant across different build files
 */
@Canonical
class AndroidBuildVariant {
    String productFlavor
    String buildType
}
