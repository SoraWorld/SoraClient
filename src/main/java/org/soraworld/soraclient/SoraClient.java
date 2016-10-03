/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient;

import com.github.axet.wget.WGet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.soraworld.soraclient.system.Reference;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.github.axet.wget.WGet.getHtml;
import static org.soraworld.soraclient.system.Network.hasNetwork;

public class SoraClient extends Application {

    public static Stage mainStage;
    public static Double PosX, PosY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("assets.lang.Language", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/MainFrame.fxml"), resourceBundle);
        primaryStage.setTitle("SoraClient");
        primaryStage.setScene(new Scene(root, 750, 540));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/images/logo.png")));
        primaryStage.show();
        BorderPane mainFrame = (BorderPane) root.lookup("#mainFrame");
        mainFrame.requestFocus();
    }

    private void CheckUpdate() {
        if (hasNetwork()) {
            try {
                File fetch = new File("fetch.exe").getAbsoluteFile();
                System.out.println(fetch);
                if (!fetch.exists()) {
                    WGet wGet = new WGet(new URL(Reference.ResourcesURL + "fetch.exe"), fetch);
                    wGet.download();
                    System.out.println("fetch downloaded!");
                }
                String updateSha1 = getHtml(new URL(Reference.ResourcesURL + "update.sha1"));
                updateSha1 = updateSha1.substring(updateSha1.indexOf("[") + 1, updateSha1.indexOf("]"));
                System.out.println("[" + updateSha1 + "]");
                String filePath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
                System.out.println(filePath);
                if (filePath.endsWith(".exe")) {
                    File exeFile = new File(filePath);
                    System.out.println(exeFile.getAbsoluteFile().getPath());
                    if (exeFile.exists()) {
                        System.out.println("find myself");
                        String sha1 = DigestUtils.sha1Hex(FileUtils.readFileToByteArray(exeFile));
                        if (!updateSha1.equals(sha1)) {
                            if (fetch.exists()) {
                                System.out.println("fetch exist,fetching...");
                                System.out.println(fetch.getAbsolutePath() + " \"" + exeFile.getAbsolutePath() + "\"");
                                Runtime.getRuntime().exec(fetch.getAbsolutePath() + " \"" + exeFile.getAbsolutePath() + "\"");
                            }
                            System.out.println("exit");
                            System.exit(0);
                        }
                        System.out.println("Sha1 is the same!");
                    } else {
                        System.out.println("didn't find myself, maybe something wrong!");
                    }
                } else {
                    System.out.println("not end with .exe, maybe something wrong!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
