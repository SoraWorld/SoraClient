/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft;

import com.google.gson.annotations.SerializedName;
import org.soraworld.soraclient.minecraft.gson.Index;

import java.util.List;
import java.util.Map;

public class Assets {
    @SerializedName("objects")
    public Map<String, AssetsObject> objects;

    public void download(List<Index> indexes) {
        for (AssetsObject object : objects.values()) {
            Index index = new Index(object.hash, ".minecraft/assets/objects/" + object.hash.substring(0, 2) + "/" + object.hash);
            if (index.needUpdate()) {
                indexes.add(index);
            }
        }
    }

    private class AssetsObject {
        @SerializedName("hash")
        public String hash;
        @SerializedName("size")
        public long size;
    }
}
