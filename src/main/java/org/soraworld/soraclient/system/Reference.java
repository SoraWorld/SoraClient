/*******************************************************************************
 * Created by Himmelt on 2016/9/27.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.system;

public class Reference {

    public static String OS = getOS();
    public static String ResourcesURL = "https://dn-stc.qbox.me/soraclient/";

    private static String getOS() {
        String name = System.getProperty("os.name").toLowerCase();
        if (name.contains("win"))
            return name.contains("64") ? "win64" : "win32";
        if (name.contains("mac")) return "osx";
        return "linux";
    }
}
