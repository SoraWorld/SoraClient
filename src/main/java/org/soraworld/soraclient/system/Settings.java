/*******************************************************************************
 * Created by Himmelt on 2016/9/27.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.system;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Settings {
    @SerializedName("version")
    public String version;
    @SerializedName("jvmMxm")
    public String jvmMxm;
    @SerializedName("userToken")
    public UserToken userToken;
    @SerializedName("resourceUrl")
    public String resourceUrl;
    @SerializedName("theme")
    public Theme theme;

    public Settings() {
        version = "";
        jvmMxm = "1024";
        userToken = new UserToken();
        theme = new Theme();
        resourceUrl = Reference.ResourcesURL;
    }

    public boolean isValid() {
        return !(jvmMxm == null || userToken == null | userToken.uuid == null | resourceUrl == null | theme == null | theme.color == null | theme.id == null) && jvmMxm.matches("[0-9]{3,4}") && resourceUrl.startsWith("http") && userToken.uuid.matches("[0-9|a-f]{32}") && theme.color.matches("0x[0-9|a-f]{3,8}");
    }

    public class UserToken {
        @SerializedName("name")
        public String name;
        @SerializedName("uuid")
        public String uuid;

        public UserToken() {
            name = "";
            uuid = UUID.randomUUID().toString().toLowerCase().replace("-", "");
        }
    }

    public class Theme {
        @SerializedName("id")
        public String id;
        @SerializedName("color")
        public String color;
        @SerializedName("url")
        public String url;

        public Theme() {
            id = "Default";
            color = "0x00b6ff80";
        }
    }
}
