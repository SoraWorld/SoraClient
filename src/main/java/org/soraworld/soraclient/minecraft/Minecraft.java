/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.soraworld.soraclient.download.DownloadManger;
import org.soraworld.soraclient.download.DownloadTask;
import org.soraworld.soraclient.minecraft.gson.Index;
import org.soraworld.soraclient.minecraft.gson.OSIndex;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.soraworld.soraclient.system.CONFIG.DLHEAD;
import static org.soraworld.soraclient.system.CONFIG.SYSTEM;

public class Minecraft {
    @SerializedName("assets")
    public String assets;
    @SerializedName("version")
    public String version;
    @SerializedName("server-ip")
    public String serverIP;
    @SerializedName("mainClass")
    public String mainClass;

    @SerializedName("jsonIndex")
    public List<Index> jsonIndex;

    @SerializedName("modsIndex")
    public Index modsIndex;
    @SerializedName("npcsIndex")
    public Index npcsIndex;
    @SerializedName("packIndex")
    public Index packIndex;
    @SerializedName("assetsIndex")
    public Index assetsIndex;
    @SerializedName("configIndex")
    public Index configIndex;
    @SerializedName("clientIndex")
    public Index clientIndex;
    @SerializedName("natives")
    public List<OSIndex> natives;
    @SerializedName("libraries")
    public List<OSIndex> libraries;

    public void fetchJson() throws IOException, InterruptedException {
        DownloadManger manger = new DownloadManger();
        for (Index json : jsonIndex) {
            File jsonFile = new File(json.path);
            if (!jsonFile.exists() || !Objects.equals(json.sha1, DigestUtils.sha1Hex(FileUtils.readFileToByteArray(jsonFile)))) {
                manger.addTask(new DownloadTask(DLHEAD + json.path, json.path));
            }
        }
        manger.shutdown();
        manger.awaitTermination();
    }

    public String getClasspath(DownloadManger manger) throws IOException {
        StringBuilder classpath = new StringBuilder();
        for (OSIndex library : libraries) {
            if (library.os.contains(SYSTEM)) {
                if (library.needUpdate()) {
                    manger.addTask(new DownloadTask(library));
                }
                classpath.append(library.path).append(";");
            }
        }
        return classpath.toString();
    }
}
