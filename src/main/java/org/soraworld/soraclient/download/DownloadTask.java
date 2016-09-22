/*******************************************************************************
 * Created by Himmelt on 2016/9/21.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.download;

import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.URLInfo;
import com.github.axet.wget.info.ex.DownloadInterruptedError;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadTask implements Runnable {

    private String path;
    private float progress;
    private DownloadInfo info;
    private long PID;

    public DownloadTask(String source, String _path) {
        path = _path;
        progress = 0;
        try {
            info = new DownloadInfo(new URL(source));
            info.extract();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public float getProgress() {
        return progress;
    }

    public URLInfo.States getState() {
        return info.getState();
    }

    public void run() {
        try {
            File target = new File(path);
            WGet wGet = new WGet(info, target);
            wGet.download(new AtomicBoolean(false), () -> {
                progress = info.getCount() / (float) info.getLength();
            });
        } catch (DownloadInterruptedError e) {
            System.out.println("下载被中断");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getPID() {
        return PID;
    }

    void setPID(long _pid) {
        PID = _pid;
    }
}
