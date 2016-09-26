/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
import org.soraworld.soraclient.minecraft.gson.Index;
import org.soraworld.soraclient.minecraft.gson.ModIndex;

import java.util.List;

public class Mods {
    @SerializedName("mods")
    public List<ModIndex> mods;

    public void download(List<Index> indices) {
        for (ModIndex mod : mods) {
            if (mod.needUpdate()) {
                indices.add(mod);
            }
        }
    }
}
