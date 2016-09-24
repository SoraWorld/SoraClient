/*******************************************************************************
 * Created by Himmelt on 2016/9/23.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.system;

public class CONFIG {
    public static String DLHEAD = "https://dn-stc.qbox.me/";//https://dn-stc.qbox.me/.minecraft/client/client.json
    public static String SYSTEM = "win32";

    public CONFIG() {
        SYSTEM = getOS();
    }

    private String getOS() {
        String name = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        if (name.contains("win"))
            return name.contains("64") ? "win64" : "win32";
        if (name.contains("mac")) return "osx";
        return "linux";
    }
}
