/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName("libraries")
    public List<Index> libraries;
    @SerializedName("jsonIndex")
    public List<Index> jsonIndex;
    @SerializedName("natives")
    public List<OSIndex> natives;

    public List<String> getLaunchCmd(String username, String uuid, String xmx, List<Index> indices) {
        List<String> launchCmd = new ArrayList<>();
        System.out.println("*****command******");
        launchCmd.add("javaw");
        launchCmd.add("-Xmn128m");
        launchCmd.add("-Xmx" + xmx + "m");
        launchCmd.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
        launchCmd.add("-Dfml.ignorePatchDiscrepancies=true");//-Dfml.ignoreInvalidMinecraftCertificates=true -Dfml.ignorePatchDiscrepancies=true
        launchCmd.add("-Djava.library.path=.minecraft/natives");
        launchCmd.add("-cp");
        System.out.println("*****getclasspath******");
        launchCmd.add(getClasspath(indices) + ".minecraft/versions/client/client.jar");
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
        launchCmd.add("--userProperties");
        launchCmd.add("{}");
        launchCmd.add("--assetIndex");
        launchCmd.add(assets);
        launchCmd.add("--accessToken");
        launchCmd.add(uuid);
        launchCmd.add("--versionType");
        launchCmd.add("\"" + version + "\"");
        launchCmd.add("--tweakClass");
        launchCmd.add("cpw.mods.fml.common.launcher.FMLTweaker");
        launchCmd.add("--server");
        launchCmd.add(server);
        return launchCmd;
    }

    public void fetchJson(List<Index> indices) {
        for (Index json : jsonIndex) {
            if (json.needUpdate())
                indices.add(json);
        }
    }

    public void fetchNative(List<Index> indices) {
        for (OSIndex index : natives) {
            if (index.os.contains(SYSTEM) && index.needUpdate()) {
                indices.add(index);
            }
        }
    }

    private String getClasspath(List<Index> indices) {
        StringBuilder classpath = new StringBuilder();
        for (Index library : libraries) {
            if (library.needUpdate())
                indices.add(library);
            classpath.append(library.path).append(";");
        }
        return classpath.toString();
    }

}
