package com.example.i2lc.edi.backend;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by vlad on 29/05/2017.
 */

public class DecompressFast {

    public DecompressFast() {

    }

    /**
     *
     * @param zipFile, which represents the path of the zipFile
     * @param targetDirectory, which represents the path were the zipFile is unarchived
     * @throws IOException
     */
    public static void unZip(File zipFile, File targetDirectory) throws IOException {

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];

            //go through each entry in the zip file
            while ((ze = zis.getNextEntry()) != null) {

                String fileName = ze.getName();
                //skip CSS files,folder and MAC_OSX files
                if (fileName.contains("CSS") || fileName.contains("__MACOSX/") || fileName.contains(".DS_Store")) {
                    //ze = zis.getNextEntry();
                    continue;
                }

                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());

                if (ze.isDirectory())
                    continue;

                //write the data on a file
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }
}
