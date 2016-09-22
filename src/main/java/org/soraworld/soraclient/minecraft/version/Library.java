/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.version;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Library {
    @SerializedName("name")
    public String name;
    @SerializedName("rules")
    public ArrayList<Rules> rules;
    @SerializedName("natives")
    public Natives natives;
    @SerializedName("extract")
    public Extract extract;
    @SerializedName("downloads")
    public LibDownloads downloads;
}