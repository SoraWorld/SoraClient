/*******************************************************************************
 * Created by Himmelt on 2016/9/21.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.download;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadManger {
    private static long PID = 0;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private List<DownloadTask> taskList = new ArrayList<>();

    public void addTask(DownloadTask task) {
        task.setPID(PID++);
        taskList.add(task);
        executorService.execute(task);
    }

    public List<DownloadTask> getTaskList() {
        return taskList;
    }

    public DownloadTask getTask(int i) {
        return taskList.get(i);
    }

    public float getProgress() {
        float progress = 0;
        int i, length = taskList.size();
        for (i = 0; i < length; i++) {
            progress += taskList.get(i).getProgress() / length;
        }
        return progress;
    }

    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    public void awaitTermination() throws InterruptedException {
        executorService.awaitTermination(1, TimeUnit.HOURS);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void shutdownNow() {
        executorService.shutdownNow();
    }
}
