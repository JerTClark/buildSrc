package environment

/**Determine which local machine is the current development machine*/
class Environment {

    /**@return {@code true} if the current local machine is the desktop; {@code false} otherwise*/
    private static boolean isDesktop() {
        System.getenv("USERPROFILE").equalsIgnoreCase("C:\\Users\\Clark")
    }

    /**Determine the appropriate pre-path leading to the current project's root*/
    static prePath = {
        isDesktop() ? "D:/Jeremy/Development/Projects" : "C:/Users/jerem/Development"
    }

}
