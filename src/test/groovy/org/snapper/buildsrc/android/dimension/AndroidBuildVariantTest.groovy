package org.snapper.buildsrc.android.dimension

import spock.lang.Shared
import spock.lang.Specification

class AndroidBuildVariantTest extends Specification {

    @Shared AndroidBuildVariant androidBuildVariant

    def setup() {
        this.androidBuildVariant = new AndroidBuildVariant(productFlavor: "mock", buildType: "debug")
    }

    def "expect fields to be set properly"() {
        expect:
        this.androidBuildVariant.buildType == "debug"
        this.androidBuildVariant.productFlavor == "mock"
    }

}
