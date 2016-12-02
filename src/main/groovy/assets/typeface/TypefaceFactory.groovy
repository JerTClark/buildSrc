package assets.typeface

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class TypefaceFactory extends DefaultTask {

    /**The path to the fonts folder (i.e. {@code projectDir/src/assets/fonts}).*/
    @Input
    String pathToFontsDir

    /**The name of the java file to create.*/
    @Input
    String javaFileName

    /**The package name in which the {@link #javaFileName} will be created.*/
    @Input
    String packageName

    /**The path to where the packages for the factory should be created.*/
    @Input
    String destination

    /**The name of the application class singleton*/
    @Input
    String applicationClassName

    /**A name for the preferred typeface to use as a default*/
    @Input
    String defaultTypeface

    TypefaceFactory() {
        group = "snapper"
        description = "Create a factory class of getters for the Android project's current font assets."
    }

    @TaskAction
    void start() {

        /**Obtain the relative path from the assets folder*/
        final String sub = this.pathToFontsDir.substring(0, this.pathToFontsDir.indexOf("assets/"))
        final String relativePath = "${(this.pathToFontsDir - sub) - "assets/"}"

        /**
         * Initially, strip leading and trailing whitespace, eliminating all non-alphanumeric
         * characters as these are not typically allowed in element attributes
         */
        final String whitespace = "\\s+\\n*\\t*";
        final String nonAlphaNumeric = "([^a-zA-Z0-9_])";
        this.defaultTypeface = this.defaultTypeface.trim()
                .capitalize()
                .substring(0, this.defaultTypeface.indexOf("."))
                .replaceAll(whitespace, "")
                .replaceAll(nonAlphaNumeric, "")

        /**A map of subfolders within the {@link #pathToFontsDir} and their contents.*/
        final HashMap<String, ArrayList<String>> fontAssets = new HashMap<>()

        final File fonts = new File(this.pathToFontsDir)
        for (final File subfolderFile : fonts.listFiles()) {
            if (subfolderFile.isDirectory()) {
                fontAssets.put(subfolderFile.name, new ArrayList<String>(Arrays.asList(subfolderFile.list())))
            }
        }

        /**Create the package folders (if needed)*/
        final File packageRootFolder = new File(destination, this.packageName.replace(".", "/"))
        packageRootFolder.mkdirs()

        /**Create the Java factory class file*/
        if (!this.javaFileName.endsWith(".java")) { this.javaFileName += ".java" }
        println "${this.javaFileName}"

        final File javaFactoryClassFile = new File(packageRootFolder, this.javaFileName)
        javaFactoryClassFile.createNewFile()

        javaFactoryClassFile.withWriter {
            /**Package, imports, and class declaration*/
            it.write "package ${this.packageName};\n"
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
