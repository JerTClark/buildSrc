package org.snapper.buildsrc.assets.typeface

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Tasks of this type handle the generation of a general "foundry" factory class
 * where a static method for each font asset in an Android application is created,
 * exposing an object of type 'Typeface' for easy acquirement within the application.
 * @author <a href="https://github.com/JerTClark">JerTClark</a>
 */
class TypefaceFactory extends DefaultTask {

    /**The path to the fonts folder (will be assumed to be either a path containing the 'assets' folder or relative to it).*/
    String fontsDir

    /**The name of the java file to create.*/
    String outputJavaFile

    /**We need to know the package name into which the {@link #outputJavaFile} will be created.*/
    String outputPackageName

    /**The path to where the packages for the factory should be created.*/
    String destination

    /**The name of the application class singleton (or class on which we can call getContext()). The IDE will fix the resulting error of not providing the fqdn.*/
    String applicationClassName

    /**A name for the preferred typeface to use as a default*/
    String defaultTypeface

    TypefaceFactory() {
        group = "snapper-android-assets"
        description = "Create a factory class of getters for the Android project's current font assets."
    }

    @Input
    getFontsDir() {
        this.fontsDir
    }

    void setFontsDir(String fontsDir) {
        if (fontsDir.contains("assets/")) {
            this.fontsDir = fontsDir
        } else {
            /*Attempt to assume the fonts directory is in the assets folder (where it must be).*/
            this.fontsDir = "assets/" + fontsDir
        }
    }

    void setFontsDir(File fontsDir) {
        this.fontsDir = fontsDir.absolutePath
    }

    @Input
    getOutputJavaFile() {
        this.outputJavaFile
    }

    void setOutputJavaFile(String outputJavaFile) {
        this.outputJavaFile = outputJavaFile
    }

    void setOutputJavaFile(File outputJavaFile) {
        this.outputJavaFile = outputJavaFile.absolutePath
    }

    @Input
    getOutputPackageName() {
        this.outputPackageName
    }

    void setOutputPackageName(String outputPackageName) {
        this.outputPackageName = outputPackageName
    }

    @Input
    getDestination() {
        this.destination
    }

    void setDestination(String destination) {
        this.destination = destination
    }

    void setDestination(File destination) {
        this.destination = destination.absolutePath
    }

    @Input
    String getApplicationClassName() {
        this.applicationClassName
    }

    void setApplicationClassName(String applicationClassName) {
        this.applicationClassName = applicationClassName
    }

    @Input
    String getDefaultTypeface() {
        return defaultTypeface
    }

    void setDefaultTypeface(String defaultTypeface) {
        this.defaultTypeface = defaultTypeface
    }

    @TaskAction
    void start() {
        /**Obtain the relative path from the assets folder*/
        final String sub = this.fontsDir.substring(0, this.fontsDir.indexOf("assets/"))
        final String relativePath = "${(this.fontsDir - sub) - "assets/"}"

        /**
         * Initially, strip leading and trailing whitespace, eliminating all non-alphanumeric
         * characters as these are not typically allowed in element attributes.
         */
        final String whitespace = "\\s+\\n*\\t*";
        final String nonAlphaNumeric = "([^a-zA-Z0-9_])";
        this.defaultTypeface = this.defaultTypeface.trim()
                .capitalize()
                .substring(0, this.defaultTypeface.indexOf("."))
                .replaceAll(whitespace, "")
                .replaceAll(nonAlphaNumeric, "")

        /**A map of subdirectories within the 'pathToFontsDir' and their contents.*/
        final HashMap<String, ArrayList<String>> fontAssets = new HashMap<>()

        final File fonts = new File(this.fontsDir)
        for (final File subfolderFile : fonts.listFiles()) {
            if (subfolderFile.isDirectory()) {
                fontAssets.put(subfolderFile.name, new ArrayList<String>(Arrays.asList(subfolderFile.list())))
            }
        }

        /**Create the package folders (if needed)*/
        final File packageRootFolder = new File(destination, this.outputPackageName.replace(".", "/"))
        packageRootFolder.mkdirs()

        /**Create the Java factory class file*/
        if (!this.outputJavaFile.endsWith(".java")) { this.outputJavaFile += ".java" }
        println "${this.outputJavaFile}"

        final File javaFactoryClassFile = new File(packageRootFolder, this.outputJavaFile)
        javaFactoryClassFile.createNewFile()

        javaFactoryClassFile.withWriter {
            /**Package, imports, and class declaration*/
            it.write "package ${this.outputPackageName};\n"
            it.append "\n"
            it.append "import android.content.Context;\n" +
                    "import android.graphics.Typeface;\n\n"
            it.append "/**\n" +
                    " * This class contains a single factory method for every font present\n" +
                    " * in it's project's assets/fonts folder that returns a {@link Typeface}\n" +
                    " * corresponding to that particular font.\n" +
                    " */\n"
            it.append "@SuppressWarnings({\"unused\", \"OverlyComplexClass\", \"ConstantNamingConvention\", \"StaticNonFinalField\"})\n"
            it.append "public final class TypefaceFoundry {\n\n"

            /**A context field*/
            it.append "    /**A context is required for accessing the font assets.*/\n" +
                    "    private static final Context context;\n\n"

            /**The static block, private constructor*/
            it.append "    static {\n" +
                    "        context = ${this.applicationClassName}.getContext();\n" +
                    "    }\n" +
                    "\n" +
                    "    /**Prevent accidental instantiation of this factory class.*/\n" +
                    "    private TypefaceFoundry() {\n" +
                    "        throw new InstantiationError(TypefaceFoundry.class.getSimpleName() + \" is not intended for instantiation, \" +\n" +
                    "                \"but only for the use of its public static methods.\");\n" +
                    "    }\n\n"

            /**The default typeface getter and the "by name" getter*/
            it.append "    /**\n" +
                    "     * Obtain the application's default {@link Typeface}.\n" +
                    "     * @return the application's default {@link Typeface}\n" +
                    "     */\n" +
                    "    public static Typeface getDefaultTypeface() {\n" +
                    "        return get${this.defaultTypeface}();\n" +
                    "    }\n\n" +

                    "    /**\n" +
                    "     * A generic factory method for obtaining a present typeface by name.\n" +
                    "     * <p>Note that the extension of the typeface file <b>is required</b> (i.e.\n" +
                    "     * {@code .ttf} for true type font files).\n" +
                    "     * @param typefaceName the name of the typeface as it occurs in the font assets\n" +
                    "     * @return the {@link Typeface} specified by name if found; otherwise a\n" +
                    "     * default typeface will be provided\n" +
                    "     */\n" +
                    "    public static Typeface getTypeface(final String typefaceName) {\n" +
                    "        Typeface typeface;\n" +
                    "        try {\n" +
                    "            typeface = Typeface.createFromAsset(context.getAssets(), \"fonts/\" + typefaceName);\n" +
                    "        } catch (final RuntimeException e) {\n" +
                    "            new AppLogger(TypefaceFoundry.class).e(AppLogger.Tag.EXCEPTION,\n" +
                    "                \"getTypeface\",\n" +
                    "                e.getMessage());\n" +
                    "            typeface = getDefaultTypeface();\n" +
                    "        }\n" +
                    "        return typeface;\n" +
                    "    }\n\n"

            /**Create an instance field for each of the subfolder's files*/
            for (final Map.Entry<String, ArrayList<String>> entry : fontAssets.entrySet()) {
                String category = entry.key
                it.append "    /**Typefaces from the ${category} family.*/\n\n"

                for (final String typefaceFileName : entry.value) {
                    String typefaceName
                    /**Remove the file name's extension*/
                    String ext
                    if (typefaceFileName.endsWith(".TTF")) { ext = ".TTF" }
                    else if (typefaceFileName.endsWith(".otf")) { ext = ".otf" }
                    else if (typefaceFileName.endsWith(".OTF")) { ext = ".OTF" }
                    else { ext = ".ttf" }
                    typefaceName = typefaceFileName.replace(ext, "")

                    /**Determine how to create the camel case variable name*/
                    if (typefaceName.contains("_") || typefaceName.contains("-")) {
                        String splitter
                        if (typefaceName.contains("_")) {
                            splitter = "_"
                        } else {
                            splitter = "-"
                        }
                        /**Formatting the variable name*/
                        String[] parts = typefaceName.split(splitter)
                        for (int i; i < parts.length; i++) {
                            if (i == 0) {
                                String[] firstPart = parts[i].split("")
                                firstPart[0] = firstPart[0].toLowerCase()
                                typefaceName = firstPart.join("")
                            } else {
                                typefaceName += (parts[i].capitalize())
                            }
                        }
                    }
                    it.append "    /**The {@code ${typefaceFileName}} typeface*/\n"
                    it.append "    private static Typeface ${typefaceName};\n\n"
                    it.append "    /**\n" +
                            "     * Obtain the {@link #${typefaceName}} typeface.\n" +
                            "     * @return the {@code ${typefaceFileName}} typeface\n" +
                            "     */\n" +
                            "    public static Typeface get${typefaceName.capitalize()}() {\n" +
                            "        if (${typefaceName} == null) {\n" +
                            "            ${typefaceName} = Typeface.createFromAsset(context.getAssets(), \"${relativePath}/${category}/${typefaceFileName}\");\n" +
                            "        }\n" +
                            "        return ${typefaceName};\n" +
                            "    }\n\n"
                }
                it.append "\n"
            }
            /**Closing bracket*/
            it.append("\n}\n")
        }

    }

}
