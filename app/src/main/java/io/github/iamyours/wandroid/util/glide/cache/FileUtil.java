package io.github.iamyours.wandroid.util.glide.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {
    public static void copyFileToDirectory(File src, File destDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        } else {
            if (src.isFile()) {
                copyFileToDirectory(src, destDir,true);
            } else {
                throw new IOException("The source " + src + "must be a file.");
            }

        }
    }

    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        } else {
            File destFile = new File(destDir, srcFile.getName());
            copyFile(srcFile, destFile, preserveFileDate);
        }
    }

    private static void checkFileRequirements(File src, File dest) throws FileNotFoundException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        } else if (dest == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!src.exists()) {
            throw new FileNotFoundException("Source '" + src + "' does not exist");
        }
    }


    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        checkFileRequirements(srcFile, destFile);
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        } else if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        } else {
            File parentFile = destFile.getParentFile();
            if (parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            } else if (destFile.exists() && !destFile.canWrite()) {
                throw new IOException("Destination '" + destFile + "' exists but is read-only");
            } else {
                doCopyFile(srcFile, destFile, preserveFileDate);
            }
        }
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        } else {
            FileInputStream fis = new FileInputStream(srcFile);
            Throwable var4 = null;

            try {
                FileChannel input = fis.getChannel();
                Throwable var6 = null;

                try {
                    FileOutputStream fos = new FileOutputStream(destFile);
                    Throwable var8 = null;

                    try {
                        FileChannel output = fos.getChannel();
                        Throwable var10 = null;

                        try {
                            long size = input.size();
                            long pos = 0L;

                            long bytesCopied;
                            for(long count = 0L; pos < size; pos += bytesCopied) {
                                long remain = size - pos;
                                count = remain > 31457280L ? 31457280L : remain;
                                bytesCopied = output.transferFrom(input, pos, count);
                                if (bytesCopied == 0L) {
                                    break;
                                }
                            }
                        } catch (Throwable var91) {
                            var10 = var91;
                            throw var91;
                        } finally {
                            if (output != null) {
                                if (var10 != null) {
                                    try {
                                        output.close();
                                    } catch (Throwable var90) {
                                        var10.addSuppressed(var90);
                                    }
                                } else {
                                    output.close();
                                }
                            }

                        }
                    } catch (Throwable var93) {
                        var8 = var93;
                        throw var93;
                    } finally {
                        if (fos != null) {
                            if (var8 != null) {
                                try {
                                    fos.close();
                                } catch (Throwable var89) {
                                    var8.addSuppressed(var89);
                                }
                            } else {
                                fos.close();
                            }
                        }

                    }
                } catch (Throwable var95) {
                    var6 = var95;
                    throw var95;
                } finally {
                    if (input != null) {
                        if (var6 != null) {
                            try {
                                input.close();
                            } catch (Throwable var88) {
                                var6.addSuppressed(var88);
                            }
                        } else {
                            input.close();
                        }
                    }

                }
            } catch (Throwable var97) {
                var4 = var97;
                throw var97;
            } finally {
                if (fis != null) {
                    if (var4 != null) {
                        try {
                            fis.close();
                        } catch (Throwable var87) {
                            var4.addSuppressed(var87);
                        }
                    } else {
                        fis.close();
                    }
                }

            }

            long srcLen = srcFile.length();
            long dstLen = destFile.length();
            if (srcLen != dstLen) {
                throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
            } else {
                if (preserveFileDate) {
                    destFile.setLastModified(srcFile.lastModified());
                }

            }
        }
    }

}
