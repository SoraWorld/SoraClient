/*******************************************************************************
 * Created by Himmelt on 2016/9/25.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipNative {

    public static void unzipNative(File file, String outPath) {
        try {
            File outFile = null;
            ZipFile zipFile = new ZipFile(file);
            ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;
            InputStream input = null;
            OutputStream output = null;
            while ((entry = zipInput.getNextEntry()) != null) {
                if (entry.getName().endsWith(".dll")) {
                    outFile = new File(outPath + File.separator + entry.getName());
                    outFile.getParentFile().mkdirs();
                    outFile.createNewFile();
                    input = zipFile.getInputStream(entry);
                    output = new FileOutputStream(outFile);
                    byte[] temp = new byte[512];
                    int len = 0;
                    while ((len = input.read(temp)) != -1) {
                        output.write(temp, 0, len);
                    }
                    input.close();
                    output.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
