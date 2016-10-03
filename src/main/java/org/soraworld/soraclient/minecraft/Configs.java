/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
import org.soraworld.soraclient.minecraft.gson.Index;
import org.soraworld.soraclient.minecraft.gson.KeepIndex;

import java.util.List;

public class Configs {
    @SerializedName("configs")
    public List<KeepIndex> configs;

    public void download(List<Index> indices) {
        for (KeepIndex index : configs) {
            if (index.needUpdate()) {
                //System.out.println(index.name+" need update");
                indices.add(index);
            }
        }
    }
}
