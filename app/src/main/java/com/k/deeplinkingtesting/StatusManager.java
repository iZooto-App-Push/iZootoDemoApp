package com.k.deeplinkingtesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StatusManager {

    public static void saveStatusFiles(String sourceDirectory, String destinationDirectory) {
        File sourceDir = new File(sourceDirectory);
        File destinationDir = new File(destinationDirectory);

        // Ensure the source directory exists and is a directory
        if (sourceDir.exists() && sourceDir.isDirectory()) {
            // Ensure the destination directory exists or create it
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            // Iterate over status files in the source directory
            File[] statusFiles = sourceDir.listFiles();
            if (statusFiles != null) {
                for (File file : statusFiles) {
                    try {
                        // Copy each status file to the destination directory
                        FileInputStream fis = new FileInputStream(file);
                        FileOutputStream fos = new FileOutputStream(new File(destinationDir, file.getName()));
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        fos.close();
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}