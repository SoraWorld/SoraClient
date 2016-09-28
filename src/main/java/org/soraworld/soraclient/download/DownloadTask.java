/*******************************************************************************
 * Created by Himmelt on 2016/9/21.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.download;

import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import org.soraworld.soraclient.minecraft.gson.Index;
import org.soraworld.soraclient.system.Reference;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

class DownloadTask implements Runnable {

    private File target;
    private DownloadInfo info;
    private long PID;
    private double progress = 0;
    private long last = 0;

    public DownloadTask(String source, String path) {
        target = new File(path);
        target.delete();
        target.getParentFile().mkdirs();
        try {
            info = new DownloadInfo(new URL(source));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    public DownloadTask(String source, File file) {
        target = file;
        target.delete();
        target.getParentFile().mkdirs();
        try {
            info = new DownloadInfo(new URL(source));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    DownloadTask(Index library) {
        ///////////////////////////////////////////
        target = new File(library.path);
        target.delete();
        target.getParentFile().mkdirs();
        try {
            info = new DownloadInfo(new URL(Reference.ResourcesURL + library.path));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    @Override
    public void run() {
        WGet wGet = new WGet(info, target);
        wGet.download(new AtomicBoolean(false), () -> progress = info.getCount() / (double) info.getLength());
    }

    double getProgress() {
        return progress;
    }

    public String getPath() {
        return target.getAbsolutePath();
    }

    public long getPID() {
        return PID;
    }

    void setPID(long _pid) {
        PID = _pid;
    }


}
