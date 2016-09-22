/*******************************************************************************
 * Created by Himmelt on 2016/9/21.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.soraworld.soraclient.download.DownloadManger;
import org.soraworld.soraclient.download.DownloadTask;

import static org.soraworld.soraclient.SoraClient.*;

public class MainFrame {
    private DownloadManger manger = new DownloadManger();
    @FXML
    private RadioButton left_game;
    @FXML
    private BorderPane mainFrame;
    @FXML
    private Button launch;

    public void CloseClicked() {
        manger.shutdownNow();
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
        Thread task = new Thread(() -> {
            manger.addTask(new DownloadTask("https://dn-stc.qbox.me/setup-1.1.exe", "./task-1.exe"));
            manger.addTask(new DownloadTask("https://dn-stc.qbox.me/setup-1.1.exe", "./task-2.exe"));
            manger.addTask(new DownloadTask("https://dn-stc.qbox.me/setup-1.1.exe", "./task-3.exe"));
            manger.addTask(new DownloadTask("https://dn-stc.qbox.me/setup-1.1.exe", "./task-4.exe"));
            manger.addTask(new DownloadTask("https://dn-stc.qbox.me/setup-1.1.exe", "./task-5.exe"));
            manger.shutdown();
            System.out.print("Task Add Finish");
            while (!manger.isTerminated()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> left_game.setText(String.valueOf(manger.getTask(1).getProgress())));
            }
        });

        task.start();

        System.out.println("Thread Started");

    }
}
