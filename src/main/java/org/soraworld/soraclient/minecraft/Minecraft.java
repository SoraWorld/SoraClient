/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
import org.soraworld.soraclient.download.DownloadService;
import org.soraworld.soraclient.download.DownloadTask;
import org.soraworld.soraclient.minecraft.gson.Index;
import org.soraworld.soraclient.minecraft.gson.OSIndex;

import java.util.ArrayList;
import java.util.List;

import static org.soraworld.soraclient.system.CONFIG.SYSTEM;

public class Minecraft {
    @SerializedName("assets")
    public String assets;
    @SerializedName("server")
    public String server;
    @SerializedName("version")
    public String version;
    @SerializedName("mainClass")
    public String mainClass;
    @SerializedName("jsonIndex")
    public List<Index> jsonIndex;
    @SerializedName("natives")
    public List<OSIndex> natives;
    @SerializedName("libraries")
    public List<OSIndex> libraries;

    private List<String> launchCmd = new ArrayList<>();

    public List<String> getLaunchCmd(String username, String uuid, String xmx, DownloadService service) {
        System.out.println("*****command******");
        launchCmd.add("javaw");
        launchCmd.add("-Xmn128m");
        launchCmd.add("-Xmx" + xmx + "m");
        launchCmd.add("-Djava.library.path=.minecraft/natives");
        launchCmd.add("-cp");
        System.out.println("*****getclasspath******");
        launchCmd.add(getClasspath(service) + ".minecraft/client/client.jar");
        launchCmd.add(mainClass);
        launchCmd.add("--uuid");
        launchCmd.add(uuid);
        launchCmd.add("--width");
        launchCmd.add("854");
        launchCmd.add("--height");
        launchCmd.add("480");
        launchCmd.add("--version");
        launchCmd.add("\"" + version + "\"");
        launchCmd.add("--gameDir");
        launchCmd.add(".minecraft");
        launchCmd.add("--username");
        launchCmd.add(username);
        launchCmd.add("--assetsDir");
        launchCmd.add(".minecraft/assets");
        launchCmd.add("--userType");
        launchCmd.add("Legacy");
        launchCmd.add("--assetIndex");
        launchCmd.add(assets);
        launchCmd.add("--accessToken");
        launchCmd.add(uuid);
        launchCmd.add("--versionType");
        launchCmd.add("\"" + version + "\"");
        launchCmd.add("--server");
        launchCmd.add(server);
        return launchCmd;
    }

    public void fetchJson(DownloadService service) {
        jsonIndex.stream().filter(Index::needUpdate).forEach(json -> service.addTask(new DownloadTask(json)));
    }

    public void fetchNative(DownloadService service) {
        natives.stream().filter(library -> library.os.contains(SYSTEM) && library.needUpdate()).forEach(library -> service.addTask(new DownloadTask(library)));
    }

    private String getClasspath(DownloadService service) {
        System.out.println(service.getProgress() + SYSTEM);
        StringBuilder classpath = new StringBuilder();
        libraries.stream().filter(library -> library.os.contains(SYSTEM)).forEach(library -> {
            System.out.println("need update?" + library.needUpdate());
            if (library.needUpdate()) {
                service.addTask(new DownloadTask(library));
            }
            classpath.append(library.path).append(";");
        });
        return classpath.toString();
    }
}
