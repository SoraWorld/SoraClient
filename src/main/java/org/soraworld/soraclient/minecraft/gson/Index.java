/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.gson;

import com.google.gson.annotations.SerializedName;

public class Index {
    @SerializedName("sha1")
    public String sha1;
    @SerializedName("path")
    public String path;
}
