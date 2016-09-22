/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.version;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Minecraft {
    @SerializedName("minecraftArguments")
    public String minecraftArguments;
    @SerializedName("mainClass")
    public String mainClass;
    @SerializedName("time")
    public String time;
    @SerializedName("id")
    public String id;
    @SerializedName("type")
    public String type;
    @SerializedName("processArguments")
    public String processArguments;
    @SerializedName("releaseTime")
    public String releaseTime;
    @SerializedName("jar")
    public String jar;
    @SerializedName("inheritsFrom")
    public String inheritsFrom;
    @SerializedName("runDir")
    public String runDir;
    @SerializedName("minimumLauncherVersion")
    public int minimumLauncherVersion;
    @SerializedName("hidden")
    public boolean hidden;
    @SerializedName("assetIndex")
    public AssetDownload assetIndex;
    @SerializedName("libraries")
    public ArrayList<Library> libraries;
    @SerializedName("assets")
    protected String assets;
    @SerializedName("download")
    private Download download;
}
