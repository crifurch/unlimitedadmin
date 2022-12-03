package fenix.product.unlimitedadmin.api.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

/**
 * File-utilities.
 */
public class FileUtils {
    protected FileUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Used to delete a folder.
     *
     * @param file The folder to delete.
     * @return true if the folder was successfully deleted.
     */
    public static boolean deleteFolder(File file) {
        try (Stream<Path> files = Files.walk(file.toPath())) {
            files.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Used to delete the contents of a folder, without deleting the folder itself.
     *
     * @param file The folder whose contents to delete.
     * @return true if the contents were successfully deleted
     */
    public static boolean deleteFolderContents(File file) {
        try (Stream<Path> files = Files.walk(file.toPath())) {
            files.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .filter(f -> !f.equals(file))
                    .forEach(File::delete);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Helper method to copy the world-folder.
     *
     * @param source Source-File
     * @param target Target-File
     * @return true if it had success
     */
    public static boolean copyFolder(File source, File target) {
        return copyFolder(source, target, null);
    }

    /**
     * Helper method to copy the world-folder.
     *
     * @param source       Source-File
     * @param target       Target-File
     * @param excludeFiles files to ignore and not copy over to Target-File
     * @return true if it had success
     */
    public static boolean copyFolder(File source, File target, List<String> excludeFiles) {
        Path sourceDir = source.toPath();
        Path targetDir = target.toPath();

        try {
            Files.walkFileTree(sourceDir, new CopyDirFileVisitor(sourceDir, targetDir, excludeFiles));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getFolderFromList(File parent, boolean create, String... children) {
        File result = parent;
        for (String i : children) {
            result = new File(result, i);
        }
        if (create && !result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    public static File getFolderFromList(File parent, String... children) {
        return getFolderFromList(parent, true, children);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getFileFromList(File parent, boolean create, String... children) {
        File result = parent;
        for (String i : children) {
            result = new File(result, i);
        }
        if (create && !result.exists()) {
            result.getParentFile().mkdirs();
            try {
                result.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static File getFileFromList(File parent, String... children) {
        return getFileFromList(parent, true, children);
    }


    private static class CopyDirFileVisitor extends SimpleFileVisitor<Path> {

        private final Path sourceDir;
        private final Path targetDir;
        private final List<String> excludeFiles;

        private CopyDirFileVisitor(Path sourceDir, Path targetDir, List<String> excludeFiles) {
            this.sourceDir = sourceDir;
            this.targetDir = targetDir;
            this.excludeFiles = excludeFiles;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path newDir = targetDir.resolve(sourceDir.relativize(dir));
            if (!Files.isDirectory(newDir)) {
                Files.createDirectory(newDir);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            // Pass files that are set to ignore
            if (excludeFiles != null && excludeFiles.contains(file.getFileName().toString()))
                return FileVisitResult.CONTINUE;
            // Copy the files
            Path targetFile = targetDir.resolve(sourceDir.relativize(file));
            Files.copy(file, targetFile, COPY_ATTRIBUTES);
            return FileVisitResult.CONTINUE;
        }
    }


}
