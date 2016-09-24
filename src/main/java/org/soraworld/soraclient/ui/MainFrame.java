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
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.commons.io.FileUtils;
import org.soraworld.soraclient.download.DownloadService;
import org.soraworld.soraclient.exception.NoJsonNet;
import org.soraworld.soraclient.minecraft.Minecraft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.soraworld.soraclient.SoraClient.*;
import static org.soraworld.soraclient.system.CONFIG.DLHEAD;
import static org.soraworld.soraclient.system.net.hasNetwork;

public class MainFrame {
    @FXML
    public Label launchLabel;
    @FXML
    public ProgressBar launchProgress;
    public Button btn_launch;
    public TextField userbox;
    public Label userhint;
    @FXML
    private RadioButton left_game;
    @FXML
    private BorderPane mainFrame;
    @FXML
    private Button launch;

    private DownloadService service = new DownloadService();

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

    public void StartPressed(MouseEvent mouseEvent) {
    }

    public void StartReleased(MouseEvent mouseEvent) {
    }

    public void ReleaseFocus() {
        mainFrame.requestFocus();
    }

    public void LeftMenuClicked(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if (source instanceof RadioButton) {
            String id = ((RadioButton) source).getId();
            switch (id) {
                case "left_game":
                    left_game.setVisible(false);
            }
        }
    }

    public void LaunchGame() {
        userhint.setText("");
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateTitle("正在检查启动参数....");
                try {
                    File json = new File("./.minecraft/client/client.json");
                    if (hasNetwork()) {
//                        json.delete();
                        System.out.println(json.delete() + DLHEAD + ".minecraft/client/client.json");
                        WGet wGet = new WGet(new URL(DLHEAD + ".minecraft/client/client.json"), json);
                        wGet.download();
                    } else {
                        if (!json.exists()) {
                            throw new NoJsonNet();
                        }
                    }
                    if (isValidId()) {
                        System.out.println("=============valid============");
                        DownloadService service = new DownloadService();
                        launch(userbox.getText(), "1224", "1024", service);
                        service.shutdown();
                        while (service.isTerminated()) {
                            Thread.sleep(200);
                            updateProgress(service.getProgress(), 1);
                        }
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

    public void launch(String username, String uuid, String xmx, DownloadService service) {
        System.out.println("========launch==========");
        File client = new File("./.minecraft/client/client.json");
        Gson GSON = new Gson();
        try {
            Minecraft minecraft = GSON.fromJson(FileUtils.readFileToString(client, Charset.defaultCharset()), Minecraft.class);
            System.out.println("========minecraft==========");
            ProcessBuilder process = new ProcessBuilder(minecraft.getLaunchCmd(username, uuid, xmx, service));
            System.out.println(process.command());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void TrayClicked(MouseEvent mouseEvent) {
        DialogStage dialog = new DialogStage("hhhhh");
        dialog.show();
    }
}
