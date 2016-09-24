/*******************************************************************************
 * Created by Himmelt on 2016/9/21.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.download;

import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import javafx.concurrent.Task;
import org.soraworld.soraclient.minecraft.gson.Index;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.soraworld.soraclient.system.CONFIG.DLHEAD;

public class DownloadTask extends Task<Void> {

    private File target;
    private DownloadInfo info;
    private long PID;
    private double progress = 0;

    public DownloadTask(String source, String path) {
        target = new File(path);
        try {
            info = new DownloadInfo(new URL(source));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    public DownloadTask(String source, File file) {
        target = file;
        try {
            info = new DownloadInfo(new URL(source));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    public DownloadTask(Index library) {
        System.out.println("+++++++ new task +++++++");
        target = new File("./" + library.path);
        try {
            info = new DownloadInfo(new URL(DLHEAD + library.path));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        info.extract();
    }

    @Override
    protected Void call() throws Exception {
        target.delete();
        WGet wGet = new WGet(info, target);
        wGet.download(new AtomicBoolean(false), () -> {
            progress = info.getCount() / (double) info.getLength();
            updateProgress(info.getCount(), info.getLength());
        });
        return null;
    }

    public double getProgressVal() {
        return progress;
    }

    public long getPID() {
        return PID;
    }

    void setPID(long _pid) {
        PID = _pid;
    }
}
