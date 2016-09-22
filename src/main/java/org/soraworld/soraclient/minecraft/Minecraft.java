/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
import org.soraworld.soraclient.minecraft.gson.Index;
import org.soraworld.soraclient.minecraft.gson.OSIndex;

import java.util.List;

public class Minecraft {
    @SerializedName("assets")
    public String assets;
    @SerializedName("version")
    public String version;
    @SerializedName("server-ip")
    public String serverIP;
    @SerializedName("mainClass")
    public String mainClass;
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
}
