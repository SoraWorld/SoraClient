/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.soraworld.soraclient.minecraft.version.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Launch {

    public static List<String> launch(String username, String gameDir, String version, String uuid, String xmx) throws IOException {
        List<String> jvmArgs = new ArrayList<String>();
        String versionPath = gameDir + "/versions/" + version + "/" + version;
        File jsonFile = new File(versionPath + ".json");
        String json = FileUtils.readFileToString(jsonFile, Charset.defaultCharset());
        Gson GSON = new Gson();
        Minecraft minecraft = GSON.fromJson(json, Minecraft.class);
        jvmArgs.add("-Xmn128m");
        jvmArgs.add("-Xmx" + xmx);
        jvmArgs.add("-Djava.library.path=" + versionPath + "-natives");
        jvmArgs.add("-cp");
        jvmArgs.add("classpath");
        jvmArgs.add(minecraft.mainClass);
        jvmArgs.add("--uuid");
        jvmArgs.add(uuid);
        jvmArgs.add("--width");
        jvmArgs.add("854");
        jvmArgs.add("--height");
        jvmArgs.add("480");
        jvmArgs.add("--version");
        jvmArgs.add(version);
        jvmArgs.add("--gameDir");
        jvmArgs.add(gameDir);
        jvmArgs.add("--username");
        jvmArgs.add(username);
        jvmArgs.add("--assetsDir");
        jvmArgs.add(gameDir + "/assets");
        jvmArgs.add("--userType");
        jvmArgs.add("Legacy");
        jvmArgs.add("--assetIndex");
        jvmArgs.add(minecraft.assetIndex.id);
        jvmArgs.add("--accessToken");
        jvmArgs.add(uuid);
        jvmArgs.add("--versionType");
        jvmArgs.add("SoraClient");

        return jvmArgs;
    }

}
