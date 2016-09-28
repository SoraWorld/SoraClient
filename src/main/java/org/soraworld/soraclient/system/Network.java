/*******************************************************************************
 * Created by Himmelt on 2016/9/23.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.system;

import java.io.IOException;
import java.net.InetAddress;

public final class Network {

    public static boolean hasNetwork() {
        try {
            InetAddress address = InetAddress.getByName("www.baidu.com");
            return address.isReachable(5000);
        } catch (IOException e) {
            return false;
        }
    }
}
