/*******************************************************************************
 * Created by Himmelt on 2016/9/23.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.system;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.axet.wget.WGet.getHtml;

public final class Network {

    public static boolean hasNetwork() {
        try {
            return getHtml(new URL("http://example.com")).startsWith("<!doctype html>");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
