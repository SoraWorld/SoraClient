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
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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

    public BorderPane mainFrame;
    public Text title_version;
    public TextField userbox;
    public Button btn_launch;
    public Label launchLabel;
    public Label userhint;
    public ProgressBar launchProgress;

    public ToggleGroup leftmenu;
    public RadioButton left_game;
    public RadioButton left_setting;
    public RadioButton left_system;
    public RadioButton left_about_sora;
    public RadioButton left_about_soft;
    public RadioButton left_sponsor;

    public AnchorPane right_game;
    public AnchorPane right_setting;
    public AnchorPane right_system;
    public AnchorPane right_about_sora;
    public AnchorPane right_about_soft;
    public AnchorPane right_sponsor;

    public TextField setting_mxm;
    public TextField setting_resourceUrl;
    public ComboBox<String> setting_theme;
    public ColorPicker setting_colorPicker;
    public Button setting_background;
    public Button game_clean_all;
    public Button game_clean_mods;
    public Button game_clean_version;
    public Button game_clean_config;

    private Gson GSON = new Gson();
    private Settings settingJson = new Settings();

    private void LoadSettings() {
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
                        loadCustomBackground();
                    } else {
                        setting_colorPicker.setDisable(true);
                        setting_background.setDisable(true);
                    }

                } else {
                    settingJson = new Settings();
                    String jsonString = GSON.toJson(settingJson);
                    settings.createNewFile();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
                    bw.write(jsonString);
                    bw.close();
                    GameClean(".minecraft/versions/client");
                }
            } else {
                String jsonString = GSON.toJson(settingJson);
                settings.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
                bw.write(jsonString);
                bw.close();
                GameClean(".minecraft/versions/client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void loadCustomColor() {
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

    private void loadCustomBackground() {
        try {
            File background = new File(settingJson.theme.url);
            if (background.exists()) {
                String url = background.toURI().toURL().toExternalForm();
                mainFrame.setStyle(mainFrame.getStyle() + "-fx-background-image: url(\"" + url + "\");-fx-background-size: cover;-fx-background-position: center;");
            }
        } catch (Exception ignored) {
            System.out.println("ignored load settings");
        }
    }

    private boolean checkInput() {
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

    private void GameClean(String path) {
        File modsDir = new File(path);
        if (modsDir.exists() && modsDir.isDirectory()) {
            try {
                System.out.println("deleting game dir");
                FileUtils.deleteDirectory(modsDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SelectTheme() {
        settingJson.theme.id = setting_theme.getValue();
        if ("Custom".equals(settingJson.theme.id)) {
            setting_colorPicker.setDisable(false);
            setting_background.setDisable(false);
            loadCustomColor();
            loadCustomBackground();
        } else {
            setting_colorPicker.setDisable(true);
            setting_background.setDisable(true);
            mainFrame.setStyle("");
        }
        mainFrame.getStylesheets().clear();
        mainFrame.getStylesheets().add(getClass().getResource("/assets/css/" + settingJson.theme.id + ".css").toExternalForm());
        System.out.println("no style ?" + mainFrame.getStyle());
        System.out.println("stylesheet:" + mainFrame.getStylesheets());
        SaveSettings();
    }

    public void SelectColor() {
        settingJson.theme.color = setting_colorPicker.getValue().toString();
        loadCustomColor();
        SaveSettings();
    }

    public void SelectBackground() {

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
        loadCustomBackground();
        SaveSettings();
    }

    public void WindowClose() {
        mainStage.close();
    }

    public void WindowMinimize() {
        mainStage.setIconified(true);
    }

    public void WindowPressed(MouseEvent mouseEvent) {
        PosX = mouseEvent.getScreenX() - mainStage.getX();
        PosY = mouseEvent.getScreenY() - mainStage.getY();
    }

    public void WindowDragged(MouseEvent mouseEvent) {
        mainStage.setX(mouseEvent.getScreenX() - PosX);
        mainStage.setY(mouseEvent.getScreenY() - PosY);
    }

    public void LeftMenuClicked(MouseEvent mouseEvent) {
        right_game.setVisible(false);
        right_setting.setVisible(false);
        right_system.setVisible(false);
        right_about_sora.setVisible(false);
        right_about_soft.setVisible(false);
        right_sponsor.setVisible(false);
        Object source = mouseEvent.getSource();
        if (source.equals(left_game))
            right_game.setVisible(true);
        if (source.equals(left_setting))
            right_setting.setVisible(true);
        if (source.equals(left_system))
            right_system.setVisible(true);
        if (source.equals(left_about_sora))
            right_about_sora.setVisible(true);
        if (source.equals(left_about_soft))
            right_about_soft.setVisible(true);
        if (source.equals(left_sponsor))
            right_sponsor.setVisible(true);
    }

    public void GameCleanClicked(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if (source.equals(game_clean_all))
            GameClean(".minecraft");
        if (source.equals(game_clean_mods))
            GameClean(".minecraft/mods");
        if (source.equals(game_clean_version))
            GameClean(".minecraft/versions");
        if (source.equals(game_clean_config)) {
            GameClean(".minecraft/config");
            FileUtils.deleteQuietly(new File(".minecraft/options.txt"));
            FileUtils.deleteQuietly(new File(".minecraft/optionsof.txt"));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadSettings();
        title_version.setText(settingJson.version);
    }

    public void LaunchGame() {
        SaveSettings();
        userhint.setText("");
        Task task = LaunchTask();
        btn_launch.disableProperty().bind(task.runningProperty());
        launchProgress.visibleProperty().bind(task.runningProperty());
        launchProgress.progressProperty().bind(task.progressProperty());
        launchLabel.visibleProperty().bind(task.runningProperty());
        launchLabel.textProperty().bind(task.titleProperty());
        new Thread(task).start();
    }

    private Task LaunchTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                updateTitle("正在检查启动参数....");
                try {
                    /// 检查更新JSON文件
                    File json = new File(".minecraft/versions/client/client.json");
                    if (hasNetwork()) {
                        json.delete();
                        WGet wGet = new WGet(new URL(Reference.ResourcesURL + ".minecraft/versions/client/client.json"), json);
                        wGet.download();
                    } else {
                        if (!json.exists()) {
                            throw new NoJsonNet();
                        }
                    }
                    /// 检查用户ID并启动
                    if (checkInput()) {
                        Minecraft minecraft = GSON.fromJson(FileUtils.readFileToString(json, Charset.defaultCharset()), Minecraft.class);
                        List<Index> indices = new ArrayList<>();
                        updateTitle("正在获取版本信息...");
                        indices.clear();
                        /// 检查版本是否更新
                        if (!settingJson.version.equals(minecraft.version)) {
                            GameClean(".minecraft/mods");
                            settingJson.version = minecraft.version;
                            Platform.runLater(() -> title_version.setText(settingJson.version));
                            SaveSettings();
                        }
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
                        Platform.runLater(() -> WindowClose());
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
    }


}
