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

        private String _zipFile;
        private String _location;

        public DecompressFast(String zipFile, String location) {
            _zipFile = zipFile;
            _location = location;

            _dirChecker("");
        }

    public static String unzip(File zipFile, File targetDirectory) throws IOException {

        String retPath = "";

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {

                String fileName = ze.getName();
                //skip CSS files,folder and MAC_OSX files
                if (fileName.contains("CSS") || fileName.contains("__MACOSX/") || fileName.contains(".DS_Store")) {
                    ze = zis.getNextEntry();
                    continue;
                }

                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
                /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }

        return retPath;
    }

        private void _dirChecker(String dir) {
            File f = new File(_location + dir);

            if(!f.isDirectory()) {
                f.mkdirs();
            }
        }
}
