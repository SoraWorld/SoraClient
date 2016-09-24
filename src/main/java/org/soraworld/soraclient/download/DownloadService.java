/*******************************************************************************
 * Created by Himmelt on 2016/9/23.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.download;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService {

    private ExecutorService service = Executors.newCachedThreadPool();
    private List<DownloadTask> tasks = new ArrayList<>();

    public void addTask(DownloadTask task) {
        tasks.add(task);
        System.out.println("received task");
        service.submit(task);
        System.out.println("super add task");
    }

    public double getProgress() {
        float progress = 0;
        int i, length = tasks.size();
        for (i = 0; i < length; i++) {
            progress += tasks.get(i).getProgressVal() / length;
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
}
