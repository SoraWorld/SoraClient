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

    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void shutdownNow() {
        executorService.shutdownNow();
    }
}
