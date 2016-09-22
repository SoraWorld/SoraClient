/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.version;

import com.google.gson.annotations.SerializedName;

public class NativeDownload {
    @SerializedName("natives-linux")
    public LibDownload nativeLinux;
    @SerializedName("natives-osx")
    public LibDownload nativesOsx;
    @SerializedName("natives-windows")
    public LibDownload nativesWin;
    @SerializedName("natives-windows-32")
    public LibDownload nativesWin32;
    @SerializedName("natives-windows-64")
    public LibDownload nativesWin64;
}
