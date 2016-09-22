/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OSIndex extends Index {
    @SerializedName("os")
    public List<String> os;
}
