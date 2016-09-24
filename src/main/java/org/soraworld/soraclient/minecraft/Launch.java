/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.soraworld.soraclient.download.DownloadService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Launch {
    public void launch(String username, String uuid, String xmx, DownloadService service) {
        File client = new File("./.minecraft/client/client.json");
        Gson GSON = new Gson();
        try {
            Minecraft minecraft = GSON.fromJson(FileUtils.readFileToString(client, Charset.defaultCharset()), Minecraft.class);
            ProcessBuilder process = new ProcessBuilder(minecraft.getLaunchCmd(username, uuid, xmx, service));
            process.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
