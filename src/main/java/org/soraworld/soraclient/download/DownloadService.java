/*******************************************************************************
 * Created by Himmelt on 2016/9/23.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.download;

import org.soraworld.soraclient.minecraft.gson.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService {

    private ExecutorService service = Executors.newCachedThreadPool();
    private List<DownloadTask> tasks = new ArrayList<>();

    public DownloadService() {
    }

    public void addTask(DownloadTask task) {
        tasks.add(task);
        service.execute(task);
    }

    public void addIndex(Index index) {
        DownloadTask task = new DownloadTask(index);
        tasks.add(task);
        service.execute(task);
    }


    public double getProgress() {
        float progress = 0;
        int i, length = tasks.size();
        for (i = 0; i < length; i++) {
            progress += tasks.get(i).getProgress() / length;
        }
        return progress;
    }

    public boolean isTerminated() {
        return service.isTerminated();
    }

    public void shutdown() {
        service.shutdown();
    }

    public void shutdownNow() {
        service.shutdownNow();
    }

    public boolean isShutdown() {
        return service.isShutdown();
    }

}
