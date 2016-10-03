/*******************************************************************************
 * Created by Himmelt on 2016/9/23.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.minecraft.gson;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class KeepIndex extends NameIndex {
    @SerializedName("keep")
    public boolean keep;

    @Override
    public boolean needUpdate() {
        try {
            File file = new File(path);
            if (keep) {
                return !(file.exists() && sha1.equals(DigestUtils.sha1Hex(FileUtils.readFileToByteArray(file))));
            } else {
                return !file.exists();
            }
        } catch (IOException e) {
            return true;
        }
    }
}
