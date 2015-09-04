package com.thalesgroup.optet.securerepository.impl;
public class Path {
//TODO: work with StringBuilder where possible   

    private String dir, file, path;

    public Path(String path) {
        this.path = path.trim();
        separateAndAssignPath();
    }

    public Path(String dir, String file) {
        this.dir = normalizeDirPath(dir);
        this.file = file.trim();
        this.path = dir + file;
    }

    private void separateAndAssignPath() {
        int ind = path.lastIndexOf("/");
        this.dir = path.substring(0, ind + 1);
        this.file = path.substring(ind + 1, path.length());
    }

    public static boolean isRoot(String path) {
        return path.equals("/");
    }

    /**
     *
     * @param path path
     * @return null if the path is root, and parent folder with separator otherwise
     */
    public static String getParent(String path) {
        if(isRoot(path)) {
            return null;
        }
        String parent = Path.normalizeDirPath(path);
        parent = parent.substring(0, parent.length() - 1);
        return parent.substring(0, parent.lastIndexOf("/") + 1);
    }

    /**
     * @param path any path
     * @return Returns last entry of a path without any separators
     */
    public static String getChild(String path) {
        if(isRoot(path)) {
            return null;
        }
        String denormPath = removeSeparatorFromDirPath(path);
        int ind = denormPath.lastIndexOf('/');
        return denormPath.substring(ind + 1, denormPath.length());
    }

    public boolean containsFile() {
        return file.length() != 0;
    }

    public static boolean isRegExpPath(String path) {
        return path.contains("*") || path.contains("//") || path.contains("?");
    }

    public String getDir() {
        return dir;
    }

    public String getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public String getRegexpForDir() {
        return getRegexpForDirCommon(this.dir);
    }

    /**
     * Get regexp for dir to process it via XQuery.
     * @param dir directory path
     * @return regexp
     */
    public static String getRegexpForDirCommon(String dir) {

        String regexp = normalizeDirPath(dir);
        regexp = subsStandardRegexpSymbols(regexp);

        regexp = regexp.replace("*", "[\\w\\.-]*"); //represents * in a path
        regexp = regexp.replace("?", "[\\w\\.-]?");
        regexp = regexp.replace("//", "/([\\w\\.-]+/)*"); //represents // in a path

        return embraceRegexp(regexp);
    }

    public static String getRegexpForFilePath(String filePath) {
        String regexp = filePath;
        regexp = subsStandardRegexpSymbols(regexp);

        regexp = regexp.replace("*", "[\\w\\.-]*"); //represents * in a path
        regexp = regexp.replace("?", "[\\w\\.-]?");
        regexp = regexp.replace("//", "/([\\w\\.-]+/)*"); //represents // in a path

        return embraceRegexp(regexp);
    }

    public String getRegexpForFile() {
        String regexp = file;
        regexp = subsStandardRegexpSymbols(regexp);

        regexp = regexp.replace("*", "[\\w\\.-]*"); //represents * in a filename
        regexp = regexp.replace("?", "[\\w\\.-]?");     //represents ? in a filename
        return embraceRegexp(regexp);
    }

    /* Regexp for returning all subfolder names of the particular folder*/
    public static String getRegexpForSubfolders(String folder) {
        String regexp = isRoot(folder) ? "" : folder.trim();
        regexp += "(/[\\w\\.-]+)+";
        return embraceRegexp(regexp);
    }

    public static String extractFolderChild(String path, String folder) {
        if(!path.startsWith(folder)) {
            throw new IllegalArgumentException("'path' should start with 'folder'");
        }
        int startIndex = isRoot(folder) ? 1 : folder.length() + 1;
        String child = path.substring(startIndex, path.length());
        int ind = child.indexOf("/");
        if(ind != -1) {
            child = child.substring(0, ind);
        }
        return child;
    }

    private static String embraceRegexp(String regexp) {
        return "^" + regexp + "$"; //^ and $ are user to match the whole string
    }

    private static String subsStandardRegexpSymbols(String regexp) {
        String newReg = regexp;
        newReg = newReg.replace(".", "\\.");
        return newReg;
    }


    public static String normalizeDirPath(String path) {
        path = path.trim();
        if(path.length() == 0 || isRoot(path)) {
            return "/";
        }
        return (path.endsWith("/")) ? path : path + "/";
    }

    public static String removeSeparatorFromDirPath(String path) {
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }
}
