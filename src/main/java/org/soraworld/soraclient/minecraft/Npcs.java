/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
import org.soraworld.soraclient.minecraft.gson.Index;

import java.util.List;

public class Npcs {
    @SerializedName("objects")
    private List<Index> objects;

    public void download(List<Index> indices) {
        for (Index index : objects) {
            if (index.needUpdate()) {
                indices.add(index);
            }
        }
    }
}
