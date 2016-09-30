/*******************************************************************************
 * Created by Himmelt on 2016/9/23.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.system;

import java.net.URL;
import java.net.URLConnection;

public class Network {
    public static boolean hasNetwork() {
        try {
            URLConnection connection = new URL("http://www.baidu.com/").openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.connect();
            return true;
        } catch (Exception e) {
            System.out.println("no network");
            return false;
        }
    }
}
