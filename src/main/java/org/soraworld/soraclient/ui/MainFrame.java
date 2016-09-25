/*******************************************************************************
 * Created by Himmelt on 2016/9/21.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.ui;

import com.github.axet.wget.WGet;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.commons.io.FileUtils;
import org.soraworld.soraclient.download.DownloadService;
import org.soraworld.soraclient.download.DownloadTask;
import org.soraworld.soraclient.exception.NoJsonNet;
import org.soraworld.soraclient.minecraft.Assets;
import org.soraworld.soraclient.minecraft.Minecraft;
import org.soraworld.soraclient.minecraft.gson.Index;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.soraworld.soraclient.SoraClient.*;
import static org.soraworld.soraclient.system.CONFIG.DLHEAD;
import static org.soraworld.soraclient.system.net.hasNetwork;
import static org.soraworld.soraclient.util.UnzipNative.unzipNative;

public class MainFrame {
    public Label launchLabel;
    public ProgressBar launchProgress;
    public Button btn_launch;
    public TextField userbox;
    public Label userhint;
    public RadioButton left_game;
    public BorderPane mainFrame;

    private DownloadService service = new DownloadService();
    private Gson GSON = new Gson();
    public void CloseClicked() {
        service.shutdownNow();
        mainStage.close();
    }

    public void MiniClicked() {
        mainStage.setIconified(true);
    }

    public void MouseDragged(MouseEvent mouseEvent) {
        mainStage.setX(mouseEvent.getScreenX() - PosX);
        mainStage.setY(mouseEvent.getScreenY() - PosY);
    }

    public void MousePressed(MouseEvent mouseEvent) {
        PosX = mouseEvent.getScreenX() - mainStage.getX();
        PosY = mouseEvent.getScreenY() - mainStage.getY();
    }

    public void LaunchGame() {
        userhint.setText("");
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateTitle("正在检查启动参数....");
                try {
                    File json = new File(".minecraft/client/client.json");
                    if (hasNetwork()) {
                        json.delete();
                        WGet wGet = new WGet(new URL(DLHEAD + ".minecraft/client/client.json"), json);
                        wGet.download();
                    } else {
                        if (!json.exists()) {
                            throw new NoJsonNet();
                        }
                    }
                    if (isValidId()) {
                        List<Index> indices = new ArrayList<>();
                        updateTitle("正在获取版本信息...");
                        indices.clear();
                        Minecraft minecraft = GSON.fromJson(FileUtils.readFileToString(json, Charset.defaultCharset()), Minecraft.class);
                        DownloadService jsonService = new DownloadService();
                        minecraft.fetchJson(indices);
                        indices.forEach(jsonService::addIndex);
                        jsonService.shutdown();
                        while (!jsonService.isTerminated()) {
                            Thread.sleep(50);
                            updateProgress(jsonService.getProgress(), 1);
                        }
                        updateTitle("正在更新资源文件...");
                        indices.clear();
                        File assetJson = new File(".minecraft/assets/indexes/" + minecraft.assets + ".json");
                        Assets assets = GSON.fromJson(FileUtils.readFileToString(assetJson, Charset.defaultCharset()), Assets.class);
                        DownloadService assetService = new DownloadService();
                        assets.download(indices);
                        int size = indices.size();
                        for (int i = 0; i < size; i++) {
                            assetService.addIndex(indices.get(i));
                            updateProgress(i, size);
                        }
                        assetService.shutdown();
                        while (!assetService.isTerminated()) {
                            Thread.sleep(50);
                            updateProgress(assetService.getProgress(), 1);
                        }
                        updateTitle("正在更新库文件...");
                        indices.clear();
                        DownloadService libraryService = new DownloadService();
                        List<String> command = minecraft.getLaunchCmd(userbox.getText(), "14233482b8dbad97617757a5c31d5872", "2048", indices);
                        indices.forEach(libraryService::addIndex);
                        libraryService.shutdown();
                        while (!libraryService.isTerminated()) {
                            Thread.sleep(50);
                            updateProgress(libraryService.getProgress(), 1);
                        }
                        updateTitle("正在更新本地库文件...");
                        indices.clear();
                        DownloadService nativeService = new DownloadService();
                        minecraft.fetchNative(indices);
                        indices.forEach(nativeService::addIndex);
                        nativeService.shutdown();
                        while (!nativeService.isTerminated()) {
                            Thread.sleep(50);
                            updateProgress(nativeService.getProgress(), 1);
                        }
                        updateTitle("正在解压本地库文件...");
                        ExecutorService unzipService = Executors.newCachedThreadPool();
                        File nativeDir = new File(".minecraft/libraries/natives");
                        File[] nativeFiles = nativeDir.listFiles();
                        size = nativeFiles.length;
                        for (int i = 0; i < size; i++) {
                            updateProgress(i, size);
                            File file = nativeFiles[i];
                            unzipService.execute(() -> unzipNative(file, ".minecraft/natives"));
                        }
                        unzipService.shutdown();
                        while (!unzipService.isTerminated()) {
                            Thread.sleep(50);
                        }
                        updateTitle("正在启动游戏本体...");
                        ProcessBuilder process = new ProcessBuilder(command);
                        process.start();
                        System.out.println("Task Finished!");
                    }
                } catch (NoJsonNet | MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        btn_launch.disableProperty().bind(task.runningProperty());
        launchProgress.visibleProperty().bind(task.runningProperty());
        launchProgress.progressProperty().bind(task.progressProperty());
        launchLabel.visibleProperty().bind(task.runningProperty());
        launchLabel.textProperty().bind(task.titleProperty());
        new Thread(task).start();
        System.out.println("Task Started!");
    }

    private boolean isValidId() {
        String username = userbox.getText();
        if (username.matches("^[a-zA-z][a-zA-Z0-9_]{2,9}$")) {
            return true;
        }
        Platform.runLater(() -> {
            userbox.requestFocus();
            userhint.setText("请输入游戏Id[以字母开头,3-10位字母,数字,下划线组合]");
        });
        return false;
    }

    public void TrayClicked(MouseEvent mouseEvent) {
        File file = new File("./test/fuck/you/fuck.txt");
        file.getParentFile().mkdirs();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                DownloadService service = new DownloadService();
                ExecutorService sss = Executors.newCachedThreadPool();
                service.addTask(new DownloadTask("https://dn-stc.qbox.me/.minecraft/libraries/forge-1.7.10-10.13.4.1614-1.7.10.jar",
                        "C:\\Users\\Himmelt\\Desktop\\SoraClient\\.minecraft\\libraries\\forge-1.7.10-10.13.4.1614-1.7.10.jar"));
                System.out.println("task executed !");
                service.shutdown();
                return null;
            }
        };
        new Thread(task).start();
    }
}
