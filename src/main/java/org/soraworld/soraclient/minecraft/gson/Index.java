/*******************************************************************************
 * Created by Himmelt on 2016/9/22.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.gson;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Index {
    @SerializedName("sha1")
    public String sha1;
    @SerializedName("path")
    public String path;

    public boolean needUpdate() throws IOException {
        File file = new File(path);
        return !(file.exists() && Objects.equals(sha1, DigestUtils.sha1Hex(FileUtils.readFileToByteArray(file))));
    }
}
