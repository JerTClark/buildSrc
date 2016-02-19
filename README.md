#About the buildSrc folder
Hi, I'm **buildSrc**

This is an alternative location for placing our build script code where
we can organize our code into classes, packages, and gain access to the
entire Gradle API. For more information, see this [link][buildSrc].

##Location
It must be located in the **project root** directory. It won't work anywhere
else.

No other configuration is required for this to work.
- Do not declare it as a project or subproject
- Do not declare it as a dependency (it will compile into the Gradle API
and be available to build scripts thusly)
- You can add anything you'd add to any other build script to **buildSrc's**
build.gradle file

##Example
See the example **multiProjectBuildSrc** in the local Gradle installation's
**samples** folder for additional capabilities of using buildSrc like creating
plugins

[buildSrc]:https://docs.gradle.org/current/userguide/organizing_build_logic.html
