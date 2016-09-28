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
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.soraworld.soraclient.download.DownloadService;
import org.soraworld.soraclient.exception.NoJsonNet;
import org.soraworld.soraclient.minecraft.Assets;
import org.soraworld.soraclient.minecraft.Minecraft;
import org.soraworld.soraclient.minecraft.Mods;
import org.soraworld.soraclient.minecraft.Npcs;
import org.soraworld.soraclient.minecraft.gson.Index;
import org.soraworld.soraclient.system.Reference;
import org.soraworld.soraclient.system.Settings;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.soraworld.soraclient.SoraClient.*;
import static org.soraworld.soraclient.system.Network.hasNetwork;
import static org.soraworld.soraclient.util.UnzipNative.unzipNative;

public class MainFrame implements Initializable {
    public Label launchLabel;
    public ProgressBar launchProgress;
    public Button btn_launch;
    public TextField userbox;
    public Label userhint;
    public RadioButton left_game;
    public BorderPane mainFrame;
    public AnchorPane right_game;
    public AnchorPane right_setting;
    public AnchorPane right_system;
    public AnchorPane right_about_sora;
    public AnchorPane right_about_soft;
    public RadioButton left_setting;
    public ToggleGroup leftmenu;
    public RadioButton left_system;
    public RadioButton left_about_sora;
    public RadioButton left_about_soft;
    public TextField setting_mxm;
    public TextField setting_resourceUrl;
    public ColorPicker setting_colorPicker;
    public ComboBox<String> setting_theme;
    public Button setting_background;

    //private DownloadService service = new DownloadService();
    private Gson GSON = new Gson();
    private Settings settingJson = new Settings();

    private void CheckSettings() {
        try {
            File settings = new File("settings.json");
            if (settings.exists()) {
                settingJson = GSON.fromJson(FileUtils.readFileToString(settings, Charset.defaultCharset()), Settings.class);
                if (settingJson != null && settingJson.isValid()) {
                    userbox.setText(settingJson.userToken.name);
                    setting_mxm.setText(settingJson.jvmMxm);
                    setting_resourceUrl.setText(settingJson.resourceUrl);
                    setting_theme.setValue(settingJson.theme.id);
                    setting_colorPicker.setValue(Color.valueOf(settingJson.theme.color));
                    setting_background.setText(settingJson.theme.url);
                    mainFrame.getStylesheets().clear();
                    mainFrame.getStylesheets().add(getClass().getResource("/assets/css/" + settingJson.theme.id + ".css").toExternalForm());
                    if ("Custom".equals(settingJson.theme.id)) {
                        setting_colorPicker.setDisable(false);
                        setting_background.setDisable(false);
                        Color themeColor = Color.valueOf(settingJson.theme.color);
                        int red = (int) (themeColor.getRed() * 255);
                        int green = (int) (themeColor.getGreen() * 255);
                        int blue = (int) (themeColor.getBlue() * 255);
                        double opacity = themeColor.getOpacity();
                        mainFrame.setStyle(mainFrame.getStyle() +
                                "theme-color: rgba("
                                + red + "," + green + "," + blue + "," + opacity
                                + ");theme-color-dark:rgba("
                                + red + "," + green + "," + blue + "," + (opacity + 1) / 2
                                + ");");
                        changeCustomBackground();
                    } else {
                        setting_colorPicker.setDisable(true);
                        setting_background.setDisable(true);
                    }

                } else {
                    System.out.println("invalid json");
                    settingJson = new Settings();
                    String jsonString = GSON.toJson(settingJson);
                    settings.createNewFile();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
                    bw.write(jsonString);
                    bw.close();
                }
            } else {
                String jsonString = GSON.toJson(settingJson);
                settings.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
                bw.write(jsonString);
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CloseClicked() {
        //service.shutdownNow();
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
        SaveSettings();
        userhint.setText("");
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateTitle("正在检查启动参数....");
                try {
                    System.out.println("start checking json");
                    File json = new File(".minecraft/versions/client/client.json");
                    if (hasNetwork()) {
                        System.out.println("has network");
                        json.delete();
                        System.out.println("downloaded");
                        WGet wGet = new WGet(new URL(Reference.ResourcesURL + ".minecraft/versions/client/client.json"), json);
                        System.out.println("downloaded");
                        wGet.download();
                        System.out.println("downloaded");
                    } else {
                        System.out.println("no nectwork");
                        if (!json.exists()) {
                            throw new NoJsonNet();
                        }
                    }
                    System.out.println("start checking username");
                    if (isValidId()) {
                        System.out.println("valid start fetch");
                        List<Index> indices = new ArrayList<>();
                        updateTitle("正在获取版本信息...");
                        indices.clear();
                        Minecraft minecraft = GSON.fromJson(FileUtils.readFileToString(json, Charset.defaultCharset()), Minecraft.class);
                        DownloadService jsonService = new DownloadService();
                        minecraft.fetchJson(indices);
                        if (indices.size() > 0 && !hasNetwork())
                            throw new NoJsonNet();
                        indices.forEach(jsonService::addIndex);
                        jsonService.shutdown();
                        while (!jsonService.isTerminated()) {
                            Thread.sleep(50);
                            updateProgress(jsonService.getProgress(), 1);
                        }
                        updateTitle("正在更新库文件...");
                        indices.clear();
                        DownloadService libraryService = new DownloadService();
                        List<String> command = minecraft.getLaunchCmd(userbox.getText(), settingJson.userToken.uuid, setting_mxm.getText(), indices);
                        if (indices.size() > 0 && !hasNetwork())
                            throw new NoJsonNet();
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
                        if (indices.size() > 0 && !hasNetwork())
                            throw new NoJsonNet();
                        indices.forEach(nativeService::addIndex);
                        nativeService.shutdown();
                        while (!nativeService.isTerminated()) {
                            Thread.sleep(50);
                            updateProgress(nativeService.getProgress(), 1);
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
                        updateTitle("正在更新模组文件...");
                        indices.clear();
                        File modJson = new File(".minecraft/mods/mods.json");
                        Mods mods = GSON.fromJson(FileUtils.readFileToString(modJson, Charset.defaultCharset()), Mods.class);
                        DownloadService modService = new DownloadService();
                        mods.download(indices);
                        if (indices.size() > 0 && !hasNetwork())
                            throw new NoJsonNet();
                        indices.forEach(modService::addIndex);
                        modService.shutdown();
                        while (!modService.isTerminated()) {
                            Thread.sleep(50);
                            updateProgress(modService.getProgress(), 1);
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
                        updateProgress(1, 1);
                        new Thread(() -> {
                            try {
                                ProcessBuilder processBuilder = new ProcessBuilder(command);
                                processBuilder.redirectErrorStream(true);
                                Process process = processBuilder.start();
                                BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                while ((stdout.readLine()) != null) {
                                }
                                stdout.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                        updateTitle("游戏已经启动,祝您游戏愉快*_^");
                        indices.clear();
                        File npcJson = new File(".minecraft/customnpcs/npcs.json");
                        Npcs npcs = GSON.fromJson(FileUtils.readFileToString(npcJson, Charset.defaultCharset()), Npcs.class);
                        DownloadService npcService = new DownloadService();
                        npcs.download(indices);
                        indices.forEach(npcService::addIndex);
                        npcService.shutdown();
                        System.out.println("===shut down=====");
                        //Thread.sleep(1000);
                        Platform.runLater(() -> CloseClicked());
                        System.out.println("Task Finished!");
                    }
                } catch (NoJsonNet e) {
                    Platform.runLater(e::dispose);
                } catch (MalformedURLException e) {
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

    private void SaveSettings() {
        try {
            settingJson.jvmMxm = setting_mxm.getText();
            settingJson.userToken.name = userbox.getText();
            settingJson.resourceUrl = setting_resourceUrl.getText();
            File settings = new File("settings.json");
            settings.createNewFile();
            String jsonString = GSON.toJson(settingJson);
            BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
            bw.write(jsonString);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    }

    public void LeftGameClicked(MouseEvent mouseEvent) {
        right_game.setVisible(true);
        right_setting.setVisible(false);
        right_system.setVisible(false);
        right_about_sora.setVisible(false);
        right_about_soft.setVisible(false);

    }

    public void LeftSettingClicked(MouseEvent mouseEvent) {
        right_game.setVisible(false);
        right_setting.setVisible(true);
        right_system.setVisible(false);
        right_about_sora.setVisible(false);
        right_about_soft.setVisible(false);
    }

    public void LeftSystemClicked(MouseEvent mouseEvent) {
        right_game.setVisible(false);
        right_setting.setVisible(false);
        right_system.setVisible(true);
        right_about_sora.setVisible(false);
        right_about_soft.setVisible(false);
    }

    public void LeftAboutSoraClicked(MouseEvent mouseEvent) {
        right_game.setVisible(false);
        right_setting.setVisible(false);
        right_system.setVisible(false);
        right_about_sora.setVisible(true);
        right_about_soft.setVisible(false);
    }

    public void LeftAboutSoftClicked(MouseEvent mouseEvent) {
        right_game.setVisible(false);
        right_setting.setVisible(false);
        right_system.setVisible(false);
        right_about_sora.setVisible(false);
        right_about_soft.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CheckSettings();
    }

    public void SwitchTheme() {

    }

    public void SwitchCustomColor() {
        settingJson.theme.color = setting_colorPicker.getValue().toString();
        changeCustomColor();
        SaveSettings();
    }

    public void ThemeHidden(Event event) {
        settingJson.theme.id = setting_theme.getValue();
        if ("Custom".equals(settingJson.theme.id)) {
            setting_colorPicker.setDisable(false);
            setting_background.setDisable(false);
            changeCustomColor();
            changeCustomBackground();
        } else {
            setting_colorPicker.setDisable(true);
            setting_background.setDisable(true);
            mainFrame.setStyle("");
        }
        mainFrame.getStylesheets().clear();
        mainFrame.getStylesheets().add(getClass().getResource("/assets/css/" + settingJson.theme.id + ".css").toExternalForm());
        System.out.println(mainFrame.getStylesheets());
        SaveSettings();
    }

    private void changeCustomColor() {
        Color themeColor = Color.valueOf(settingJson.theme.color);
        int red = (int) (themeColor.getRed() * 255);
        int green = (int) (themeColor.getGreen() * 255);
        int blue = (int) (themeColor.getBlue() * 255);
        double opacity = themeColor.getOpacity();
        mainFrame.setStyle(mainFrame.getStyle() +
                "theme-color: rgba("
                + red + "," + green + "," + blue + "," + opacity
                + ");theme-color-dark:rgba("
                + red + "," + green + "," + blue + "," + (opacity + 1) / 2
                + ");");
    }

    private void changeCustomBackground() {
        try {
            File background = new File(settingJson.theme.url);
            if (background.exists()) {
                System.out.println("custom background exist! style:" + mainFrame.getStyle());
                String url = background.toURI().toURL().toExternalForm();
                mainFrame.setStyle(mainFrame.getStyle() + "-fx-background-image: url(\"" + url + "\");");
            }
        } catch (Exception ignored) {
            System.out.println("ignored load settings");
        }
    }

    public void ThemeBackgroundClicked(MouseEvent mouseEvent) {

        FileChooser fileChooser = new FileChooser();
        File background;
        try {
            background = new File(setting_background.getText());
            if (background.exists()) {
                fileChooser.setInitialDirectory(background.getParentFile());
            }
        } catch (Exception ignored) {
            System.out.println("ignored theme click");
        }

        fileChooser.setTitle("选择一张背景图片");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("图片", "*.jpg", "*.jpeg", "*.png", "*.bmp"));
        background = fileChooser.showOpenDialog(mainStage);
        if (background != null) {
            setting_background.setText(background.getPath());
        }
        settingJson.theme.url = setting_background.getText();
        changeCustomBackground();
        SaveSettings();
    }
}
