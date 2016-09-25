/*******************************************************************************
 * Created by Himmelt on 2016/9/25.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.download;

import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import org.soraworld.soraclient.minecraft.gson.Index;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.soraworld.soraclient.system.CONFIG.DLHEAD;

public class DownloadRun implements Runnable {
    private File target;
    private DownloadInfo info;
    private long PID;
    private double progress = 0;
    private long last = 0;

    public DownloadRun(String source, String path) {
        target = new File(path);
        try {
            info = new DownloadInfo(new URL(source));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    public DownloadRun(String source, File file) {
        target = file;
        try {
            info = new DownloadInfo(new URL(source));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    public DownloadRun(Index library) {
        target = new File("./" + library.path);
        try {
            info = new DownloadInfo(new URL(DLHEAD + library.path));
            System.out.println(info.getSource());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
        System.out.println(info.getState());
    }

    @Override
    public void run() {
        target.delete();
        WGet wGet = new WGet(info, target);
        wGet.download(new AtomicBoolean(false), () -> {
            System.out.println("downloading:" + info.getState());
            long now = System.currentTimeMillis();
            if (now - last > 1000) {
                progress = info.getCount() / (double) info.getLength();
                last = now;
            }
        });
    }
}
