/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.version;

import com.google.gson.annotations.SerializedName;

public class Natives {
    @SerializedName("windows")
    public String windows;
    @SerializedName("osx")
    public String osx;
    @SerializedName("linux")
    public String linux;
}
